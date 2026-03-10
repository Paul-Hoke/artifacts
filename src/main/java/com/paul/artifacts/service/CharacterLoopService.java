package com.paul.artifacts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.CharacterLoopState;
import com.paul.artifacts.model.CharacterPhase;
import com.paul.artifacts.model.common.SimpleItem;
import com.paul.artifacts.model.map.MapTile;
import com.paul.artifacts.model.request.DestinationRequest;
import com.paul.artifacts.model.request.GoldRequest;
import com.paul.artifacts.model.ws.CharacterPositionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterLoopService {

  private static final int GATHER_TARGET = 20;

  private final ArtifactsApiClient client;
  private final OverworldMapCache mapCache;
  private final PathfindingService pathfinding;
  private final SimpMessagingTemplate messagingTemplate;
  private final BankService bankService;

  private final Map<String, CharacterLoopState> states = new ConcurrentHashMap<>();

  // ── Public entry point ────────────────────────────────────────────────────

  public boolean isAnyCharacterReady() {
    if (states.isEmpty()) return true; // First run — let it through to discover characters
    Instant now = Instant.now();
    return states.values().stream().anyMatch(s -> !now.isBefore(s.getCooldownExpiration()));
  }

  public void tick(JsonNode character) {
    String name = character.path("name").asText();
    CharacterLoopState state = states.computeIfAbsent(name, k -> new CharacterLoopState());

    // Use locally-tracked cooldown for precise timing
    if (Instant.now().isBefore(state.getCooldownExpiration())) return;

    log.debug("{} phase={}", name, state.getPhase());

    switch (state.getPhase()) {
      case IDLE              -> planResourceCollection(name, character, state);
      case MOVING_TO_RESOURCE,
           MOVING_TO_BANK   -> continueMoving(name, character, state);
      case GATHERING         -> doGather(name, character, state);
      case BANKING           -> doBanking(name, character, state);
    }
  }

  // ── Phase handlers ────────────────────────────────────────────────────────

  private void planResourceCollection(String name, JsonNode character, CharacterLoopState state) {
    int cx = character.path("x").asInt();
    int cy = character.path("y").asInt();

    Collection<MapTile> resources = mapCache.getResourceTiles();
    List<MapTile> unvisited = resources.stream()
        .filter(t -> !state.getVisitedResourceTiles().contains(t.getX() + "," + t.getY()))
        .toList();

    if (unvisited.isEmpty()) {
      log.info("{} has visited all resource tiles — resetting visited set", name);
      state.getVisitedResourceTiles().clear();
      unvisited = resources.stream().toList();
    }

    if (unvisited.isEmpty()) {
      log.warn("No resource tiles found in map cache for {}", name);
      return;
    }

    // Pick closest unvisited resource by Manhattan distance
    MapTile target = unvisited.stream()
        .min(Comparator.comparingInt(t -> Math.abs(t.getX() - cx) + Math.abs(t.getY() - cy)))
        .orElseThrow();

    String targetKey = target.getX() + "," + target.getY();
    Deque<int[]> path = pathfinding.findPath(cx, cy, target.getX(), target.getY());

    if (path.isEmpty() && (cx != target.getX() || cy != target.getY())) {
      log.warn("{} cannot reach resource tile {} — skipping it", name, targetKey);
      state.getVisitedResourceTiles().add(targetKey);
      return;
    }

    log.info("{} targeting resource {} at {} ({} steps)", name, target.getContentCode(), targetKey, path.size());

    state.setTargetResourceKey(targetKey);
    state.setTargetResourceCode(target.getContentCode());
    state.setGatherCount(0);
    state.getMovePath().clear();
    state.getMovePath().addAll(path);
    state.setPhase(path.isEmpty() ? CharacterPhase.GATHERING : CharacterPhase.MOVING_TO_RESOURCE);
  }

  private void continueMoving(String name, JsonNode character, CharacterLoopState state) {
    Deque<int[]> path = state.getMovePath();

    if (path.isEmpty()) {
      arriveAtDestination(name, state);
      return;
    }

    int[] next = path.peek();
    MapTile tile = mapCache.getTile(next[0], next[1]);
    if (tile == null) {
      log.warn("{} path step ({},{}) is not a valid tile — replanning", name, next[0], next[1]);
      state.setPhase(CharacterPhase.IDLE);
      return;
    }

    DestinationRequest req = new DestinationRequest();
    req.setX(tile.getX());
    req.setY(tile.getY());
    req.setMapId(tile.getMapId());

    try {
      JsonNode response = client.actionMove(name, req);
      path.poll(); // consume step only after successful move
      storeCooldown(state, response);
      broadcastCharacterState(response);

      if (path.isEmpty()) {
        arriveAtDestination(name, state);
      }
    } catch (Exception e) {
      log.warn("{} move to ({},{}) failed: {} — replanning", name, next[0], next[1], e.getMessage());
      state.setPhase(CharacterPhase.IDLE);
    }
  }

  private void arriveAtDestination(String name, CharacterLoopState state) {
    if (state.getPhase() == CharacterPhase.MOVING_TO_RESOURCE) {
      log.info("{} arrived at resource tile {}", name, state.getTargetResourceKey());
      state.setPhase(CharacterPhase.GATHERING);
    } else {
      log.info("{} arrived at bank", name);
      state.setPhase(CharacterPhase.BANKING);
    }
  }

  private void doGather(String name, JsonNode character, CharacterLoopState state) {
    try {
      JsonNode response = client.actionGathering(name);
      storeCooldown(state, response);
      broadcastCharacterState(response);
      int gained = countItems(response.path("data").path("details").path("items"),
          state.getTargetResourceCode());
      state.setGatherCount(state.getGatherCount() + Math.max(gained, 1));

      log.debug("{} gathered {}/{} {}", name, state.getGatherCount(), GATHER_TARGET, state.getTargetResourceCode());

      if (state.getGatherCount() >= GATHER_TARGET) {
        planRouteToBank(name, character, state, true);
      }
    } catch (RestClientResponseException e) {
      if (e.getStatusCode().value() == 497) {
        log.info("{} inventory full — routing to bank without marking tile visited", name);
        planRouteToBank(name, character, state, false);
      } else {
        log.warn("{} gathering failed ({}): {} — skipping tile {}", name, e.getStatusCode().value(), e.getMessage(), state.getTargetResourceKey());
        state.getVisitedResourceTiles().add(state.getTargetResourceKey());
        state.setPhase(CharacterPhase.IDLE);
      }
    } catch (Exception e) {
      log.warn("{} gathering failed: {} — skipping tile {} and moving on", name, e.getMessage(), state.getTargetResourceKey());
      state.getVisitedResourceTiles().add(state.getTargetResourceKey());
      state.setPhase(CharacterPhase.IDLE);
    }
  }

  private void planRouteToBank(String name, JsonNode character, CharacterLoopState state, boolean markTileVisited) {
    if (markTileVisited) {
      state.getVisitedResourceTiles().add(state.getTargetResourceKey());
    }

    int cx = character.path("x").asInt();
    int cy = character.path("y").asInt();
    MapTile bank = mapCache.getClosestBankTile(cx, cy);

    if (bank == null) {
      log.warn("No bank tile found for {} — going idle", name);
      state.setPhase(CharacterPhase.IDLE);
      return;
    }

    Deque<int[]> path = pathfinding.findPath(cx, cy, bank.getX(), bank.getY());
    log.info("{} heading to bank at ({},{}) ({} steps)", name, bank.getX(), bank.getY(), path.size());

    state.getMovePath().clear();
    state.getMovePath().addAll(path);
    state.setPhase(path.isEmpty() ? CharacterPhase.BANKING : CharacterPhase.MOVING_TO_BANK);
  }

  private void doBanking(String name, JsonNode character, CharacterLoopState state) {
    // Deposit items one at a time, staying in BANKING until inventory is clear
    JsonNode inventory = character.path("inventory");
    if (inventory.isArray()) {
      for (JsonNode slot : inventory) {
        String code = slot.path("code").asText(null);
        int qty = slot.path("quantity").asInt(0);
        if (code != null && !code.isBlank() && qty > 0) {
          SimpleItem item = new SimpleItem();
          item.setCode(code);
          item.setQuantity(qty);
          try {
            JsonNode response = client.actionDepositBankItem(name, List.of(item));
            storeCooldown(state, response);
            broadcastCharacterState(response);
            bankService.broadcastBankState();
            log.info("{} deposited {} x {}", name, qty, code);
          } catch (Exception e) {
            log.warn("{} deposit of {} failed: {} — skipping", name, code, e.getMessage());
          }
          return; // one deposit per tick; cooldown tracked locally
        }
      }
    }

    // All items deposited — now deposit gold
    int gold = character.path("gold").asInt(0);
    if (gold > 0) {
      GoldRequest req = new GoldRequest();
      req.setQuantity(gold);
      try {
        JsonNode response = client.actionDepositBankGold(name, req);
        storeCooldown(state, response);
        broadcastCharacterState(response);
        bankService.broadcastBankState();
        log.info("{} deposited {} gold", name, gold);
      } catch (Exception e) {
        log.warn("{} gold deposit failed: {}", name, e.getMessage());
      }
    }

    log.info("{} banking complete — going idle", name);
    state.setPhase(CharacterPhase.IDLE);
  }

  // ── Helpers ───────────────────────────────────────────────────────────────

  /**
   * Reads {@code data.cooldown.total_seconds} from an action response and stores the
   * expiration time in the character's local state so the scheduler can skip API calls
   * while the character is on cooldown.
   */
  private void storeCooldown(CharacterLoopState state, JsonNode response) {
    double totalSeconds = response.path("data").path("cooldown").path("total_seconds").asDouble(0);
    if (totalSeconds > 0) {
      state.setCooldownExpiration(Instant.now().plusMillis((long) (totalSeconds * 1000)));
    }
  }

  private void broadcastCharacterState(JsonNode actionResponse) {
    JsonNode data = actionResponse.path("data");
    JsonNode charData = data.path("character");
    if (charData.isMissingNode()) return;
    double cooldownSeconds = data.path("cooldown").path("total_seconds").asDouble(0);
    log.debug("Broadcasting state: name={} x={} y={} cooldown={}",
        charData.path("name").asText(), charData.path("x").asInt(), charData.path("y").asInt(), cooldownSeconds);
    messagingTemplate.convertAndSend("/topic/characters",
        CharacterPositionMessage.builder()
            .name(charData.path("name").asText())
            .skin(charData.path("skin").asText())
            .x(charData.path("x").asInt())
            .y(charData.path("y").asInt())
            .cooldownSeconds(cooldownSeconds)
            .inventory(extractInventory(charData))
            .build());
  }

  private List<SimpleItem> extractInventory(JsonNode charData) {
    JsonNode invNode = charData.path("inventory");
    if (!invNode.isArray()) return List.of();
    List<SimpleItem> result = new ArrayList<>();
    for (JsonNode slot : invNode) {
      String code = slot.path("code").asText(null);
      if (code != null && !code.isBlank()) {
        SimpleItem item = new SimpleItem();
        item.setCode(code);
        item.setQuantity(slot.path("quantity").asInt(0));
        result.add(item);
      }
    }
    return result;
  }

  /** Counts items matching {@code code} in the gather-details items array. */
  private int countItems(JsonNode items, String code) {
    if (!items.isArray()) return 0;
    int total = 0;
    for (JsonNode item : items) {
      if (code.equals(item.path("code").asText())) {
        total += item.path("quantity").asInt(0);
      }
    }
    return total;
  }

}
