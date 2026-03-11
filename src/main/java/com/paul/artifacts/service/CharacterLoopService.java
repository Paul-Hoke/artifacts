package com.paul.artifacts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.CharacterLoopState;
import com.paul.artifacts.model.CharacterPhase;
import com.paul.artifacts.model.CharacterRole;
import com.paul.artifacts.model.common.SimpleItem;
import com.paul.artifacts.model.crafting.CraftableItem;
import com.paul.artifacts.model.crafting.CraftSkill;
import com.paul.artifacts.model.map.MapTile;
import com.paul.artifacts.model.request.CraftRequest;
import com.paul.artifacts.model.request.DeleteItemRequest;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterLoopService {

  private static final int GATHER_TARGET      = 20;
  private static final int MAX_CRAFT_DEPTH    = 1;
  private static final int MAX_ITEM_QUANTITY  = 20;

  private final ArtifactsApiClient client;
  private final OverworldMapCache mapCache;
  private final PathfindingService pathfinding;
  private final SimpMessagingTemplate messagingTemplate;
  private final BankService bankService;

  private final Map<String, CharacterLoopState> states = new ConcurrentHashMap<>();

  /** Resource tile keys already gathered this cycle — shared across all characters. */
  private final Set<String> visitedResourceTiles = ConcurrentHashMap.newKeySet();

  // ── Public entry points ───────────────────────────────────────────────────

  public boolean hasCharacters() {
    return !states.isEmpty();
  }

  public boolean isAnyCharacterReady() {
    Instant now = Instant.now();
    return states.values().stream().anyMatch(s -> !now.isBefore(s.getCooldownExpiration()));
  }

  /** Called once per character on startup to register it and assign its role. */
  public void bootstrapCharacter(JsonNode character, int characterIndex) {
    String name = character.path("name").asText();
    CharacterLoopState state = new CharacterLoopState();
    state.setRole(CharacterRole.fromIndex(characterIndex));
    state.setCachedCharacter(character);
    states.put(name, state);
    log.info("{} bootstrapped with role {}", name, state.getRole());

    // Gatherers and crafters deposit any held items before starting their loop
    if (state.getRole() != CharacterRole.HUNTER && hasItemsToDeposit(character)) {
      int cx = character.path("x").asInt();
      int cy = character.path("y").asInt();
      MapTile bank = mapCache.getClosestBankTile(cx, cy);
      if (bank != null) {
        Deque<int[]> path = pathfinding.findPath(cx, cy, bank.getX(), bank.getY());
        state.getMovePath().addAll(path);
        state.setPhase(path.isEmpty() ? CharacterPhase.BANKING : CharacterPhase.MOVING_TO_BANK);
        log.info("{} has items on startup — routing to bank before starting loop", name);
      }
    }
  }

  /**
   * After a withdrawal, checks if the withdrawn item now exceeds {@link #MAX_ITEM_QUANTITY}.
   * If so, schedules a delete for the next tick so the withdrawal cooldown can expire first.
   */
  private void trimInventoryExcess(String name, String code, JsonNode actionResponse, CharacterLoopState state) {
    JsonNode inventory = actionResponse.path("data").path("character").path("inventory");
    if (!inventory.isArray()) return;
    for (JsonNode slot : inventory) {
      if (code.equals(slot.path("code").asText(null))) {
        int qty = slot.path("quantity").asInt(0);
        int excess = qty - MAX_ITEM_QUANTITY;
        if (excess > 0) {
          state.setPendingDeleteCode(code);
          state.setPendingDeleteQuantity(excess);
          log.info("{} will delete {} excess {} next tick (had {}, cap {})", name, excess, code, qty, MAX_ITEM_QUANTITY);
        }
        break;
      }
    }
  }

  private boolean hasItemsToDeposit(JsonNode character) {
    if (character.path("gold").asInt(0) > 0) return true;
    JsonNode inventory = character.path("inventory");
    if (!inventory.isArray()) return false;
    for (JsonNode slot : inventory) {
      if (slot.path("quantity").asInt(0) > 0) return true;
    }
    return false;
  }

  /** Ticks all characters using their cached character data. */
  public void tickAll() {
    for (Map.Entry<String, CharacterLoopState> entry : states.entrySet()) {
      String name = entry.getKey();
      CharacterLoopState state = entry.getValue();
      if (Instant.now().isBefore(state.getCooldownExpiration())) continue;
      if (state.getCachedCharacter() == null) continue;
      try {
        tickCharacter(name, state.getCachedCharacter(), state);
      } catch (Exception e) {
        log.error("Loop error for {}: {}", name, e.getMessage(), e);
      }
    }
  }

  private void tickCharacter(String name, JsonNode character, CharacterLoopState state) {
    log.debug("{} role={} phase={}", name, state.getRole(), state.getPhase());
    switch (state.getRole()) {
      case GATHERER -> tickGatherer(name, character, state);
      case HUNTER   -> tickHunter(name, character, state);
      case CRAFTER  -> tickCrafter(name, character, state);
    }
  }

  // ── Role dispatchers ──────────────────────────────────────────────────────

  private void tickGatherer(String name, JsonNode character, CharacterLoopState state) {
    switch (state.getPhase()) {
      case IDLE               -> planResourceCollection(name, character, state);
      case MOVING_TO_RESOURCE,
           MOVING_TO_BANK     -> continueMoving(name, character, state);
      case GATHERING          -> doGather(name, character, state);
      case BANKING            -> doBanking(name, character, state);
      default                 -> state.setPhase(CharacterPhase.IDLE);
    }
  }

  private void tickHunter(String name, JsonNode character, CharacterLoopState state) {
    // Hunter role not yet implemented
    log.debug("{} (hunter) role not yet implemented — idle", name);
  }

  private void tickCrafter(String name, JsonNode character, CharacterLoopState state) {
    switch (state.getPhase()) {
      case IDLE                    -> planCraft(name, character, state);
      case MOVING_TO_BANK_WITHDRAW,
           MOVING_TO_WORKSHOP,
           MOVING_TO_BANK          -> continueMoving(name, character, state);
      case WITHDRAWING             -> doWithdraw(name, character, state);
      case CRAFTING                -> doCraft(name, character, state);
      case BANKING                 -> doBanking(name, character, state);
      default                      -> state.setPhase(CharacterPhase.IDLE);
    }
  }

  // ── Gatherer phase handlers ───────────────────────────────────────────────

  private void planResourceCollection(String name, JsonNode character, CharacterLoopState state) {
    int cx = character.path("x").asInt();
    int cy = character.path("y").asInt();

    Collection<MapTile> resources = mapCache.getResourceTiles();
    List<MapTile> unvisited = resources.stream()
        .filter(t -> !visitedResourceTiles.contains(t.getX() + "," + t.getY()))
        .toList();

    if (unvisited.isEmpty()) {
      log.info("All resource tiles visited — resetting shared visited set");
      visitedResourceTiles.clear();
      unvisited = resources.stream().toList();
    }

    if (unvisited.isEmpty()) {
      log.warn("No resource tiles found in map cache for {}", name);
      return;
    }

    MapTile target = unvisited.stream()
        .min(Comparator.comparingInt(t -> Math.abs(t.getX() - cx) + Math.abs(t.getY() - cy)))
        .orElseThrow();

    String targetKey = target.getX() + "," + target.getY();
    Deque<int[]> path = pathfinding.findPath(cx, cy, target.getX(), target.getY());

    if (path.isEmpty() && (cx != target.getX() || cy != target.getY())) {
      log.warn("{} cannot reach resource tile {} — skipping it", name, targetKey);
      visitedResourceTiles.add(targetKey);
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

  private void doGather(String name, JsonNode character, CharacterLoopState state) {
    try {
      JsonNode response = client.actionGathering(name);
      storeCooldown(state, response);
      broadcastCharacterState(response, state);
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
        visitedResourceTiles.add(state.getTargetResourceKey());
        state.setPhase(CharacterPhase.IDLE);
      }
    } catch (Exception e) {
      log.warn("{} gathering failed: {} — skipping tile {} and moving on", name, e.getMessage(), state.getTargetResourceKey());
      visitedResourceTiles.add(state.getTargetResourceKey());
      state.setPhase(CharacterPhase.IDLE);
    }
  }

  private void planRouteToBank(String name, JsonNode character, CharacterLoopState state, boolean markTileVisited) {
    if (markTileVisited) {
      visitedResourceTiles.add(state.getTargetResourceKey());
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

  // ── Crafter phase handlers ────────────────────────────────────────────────

  private void planCraft(String name, JsonNode character, CharacterLoopState state) {
    Map<String, Integer> bank = bankService.getBankInventoryMap();
    int depth = state.getCraftDepth();

    // For depth-1+, build the set of item codes already held anywhere (bank or any character)
    // so we don't craft duplicates.
    Set<String> alreadyOwned = depth > 0 ? buildOwnedItemCodes(bank) : Set.of();

    // All items at the current depth we can make at least once from the bank
    List<CraftableItem> craftable = Arrays.stream(CraftableItem.values())
        .filter(item -> item.getDepth() == depth
            && canCraftFromBank(item, bank)
            && !alreadyOwned.contains(item.getCode()))
        .toList();

    if (craftable.isEmpty()) {
      // Nothing at this depth — reset to depth 0 and wait
      if (depth > 0) {
        log.info("{} (crafter) no depth-{} items craftable — resetting to depth 0", name, depth);
        state.setCraftDepth(0);
      } else {
        log.info("{} (crafter) no depth-0 items craftable from bank — waiting", name);
      }
      return;
    }

    // Advance depth now so banking → IDLE triggers the next depth automatically
    state.setCraftDepth(depth < MAX_CRAFT_DEPTH ? depth + 1 : 0);

    // Build workshop queue from the distinct skills we'll need, in encounter order
    state.getWorkshopSkillQueue().clear();
    craftable.stream()
        .map(CraftableItem::getSkill)
        .distinct()
        .forEach(state.getWorkshopSkillQueue()::add);

    // Collect the ingredient codes we need to withdraw.
    // Quantities are not stored here — doWithdraw re-queries the bank each tick
    // so it always withdraws the current maximum even if the gatherer deposited
    // more while the crafter was walking to the bank.
    Map<String, Integer> plan = new HashMap<>();
    for (CraftableItem item : craftable) {
      for (CraftableItem.Ingredient ing : item.getIngredients()) {
        plan.put(ing.code(), 0); // value unused; key = "this ingredient should be withdrawn"
      }
    }
    state.setWithdrawPlan(plan);

    log.info("{} (crafter) depth-{} plan: {} items across {} workshops, withdrawing {} ingredient types",
        name, depth, craftable.size(), state.getWorkshopSkillQueue().size(), plan.size());

    // Route to bank to withdraw
    int cx = character.path("x").asInt();
    int cy = character.path("y").asInt();
    MapTile bankTile = mapCache.getClosestBankTile(cx, cy);
    if (bankTile == null) {
      log.warn("No bank tile found for {} — going idle", name);
      return;
    }
    Deque<int[]> path = pathfinding.findPath(cx, cy, bankTile.getX(), bankTile.getY());
    state.getMovePath().clear();
    state.getMovePath().addAll(path);
    state.setPhase(path.isEmpty() ? CharacterPhase.WITHDRAWING : CharacterPhase.MOVING_TO_BANK_WITHDRAW);
  }

  private void doWithdraw(String name, JsonNode character, CharacterLoopState state) {
    // Process any deferred delete from the previous withdrawal before continuing
    if (state.getPendingDeleteCode() != null) {
      DeleteItemRequest req = new DeleteItemRequest();
      req.setCode(state.getPendingDeleteCode());
      req.setQuantity(state.getPendingDeleteQuantity());
      try {
        JsonNode deleteResponse = client.actionDeleteItem(name, req);
        storeCooldown(state, deleteResponse);
        broadcastCharacterState(deleteResponse, state);
        log.info("{} deleted {} excess {}", name, state.getPendingDeleteQuantity(), state.getPendingDeleteCode());
      } catch (Exception e) {
        log.warn("{} failed to delete excess {}: {}", name, state.getPendingDeleteCode(), e.getMessage());
      }
      state.setPendingDeleteCode(null);
      state.setPendingDeleteQuantity(0);
      return; // one action per tick
    }

    Map<String, Integer> plan = state.getWithdrawPlan();
    if (plan.isEmpty()) {
      routeToNextWorkshop(name, character, state);
      return;
    }

    // Query bank fresh each tick so we always withdraw the current maximum,
    // not a snapshot that may have been taken before the gatherer deposited more.
    Map<String, Integer> bank = bankService.getBankInventoryMap();
    Map<String, Integer> inventory = buildInventoryMap(character);

    for (String code : plan.keySet()) {
      int inBank = bank.getOrDefault(code, 0);
      int have = inventory.getOrDefault(code, 0);
      int need = inBank - have;
      if (need <= 0) continue; // already have everything the bank currently holds

      SimpleItem item = new SimpleItem();
      item.setCode(code);
      item.setQuantity(need);
      try {
        JsonNode response = client.actionWithdrawBankItem(name, List.of(item));
        storeCooldown(state, response);
        broadcastCharacterState(response, state);
        log.info("{} withdrew {} x {}", name, need, code);
        trimInventoryExcess(name, code, response, state);
      } catch (RestClientResponseException e) {
        if (e.getStatusCode().value() == 497) {
          log.info("{} inventory full during withdrawal — proceeding to workshops", name);
          routeToNextWorkshop(name, character, state);
        } else {
          log.warn("{} withdraw of {} failed ({}) — skipping ingredient", name, code, e.getStatusCode().value());
          plan.remove(code);
        }
      } catch (Exception e) {
        log.warn("{} withdraw of {} failed: {} — skipping ingredient", name, code, e.getMessage());
        plan.remove(code);
      }
      return; // one withdrawal per tick
    }

    // Every ingredient: bank has none left or we already hold it all
    routeToNextWorkshop(name, character, state);
  }

  private void doCraft(String name, JsonNode character, CharacterLoopState state) {
    CraftSkill skill = state.getCurrentWorkshopSkill();
    if (skill == null) {
      routeToNextWorkshop(name, character, state);
      return;
    }

    Map<String, Integer> inventory = buildInventoryMap(character);

    // Find a depth-0 item for this skill that inventory can support
    Optional<CraftableItem> target = Arrays.stream(CraftableItem.values())
        .filter(item -> item.getSkill() == skill
            && canCraftFromInventory(item, inventory))
        .findFirst();

    if (target.isEmpty()) {
      // Nothing left to craft here — move on
      log.info("{} nothing left to craft at {} — moving to next workshop", name, skill.getWorkshopCode());
      routeToNextWorkshop(name, character, state);
      return;
    }

    CraftRequest req = new CraftRequest();
    req.setCode(target.get().getCode());
    req.setQuantity(1);

    try {
      JsonNode response = client.actionCrafting(name, req);
      storeCooldown(state, response);
      broadcastCharacterState(response, state);
      log.debug("{} crafted {}", name, target.get().getCode());
    } catch (Exception e) {
      log.warn("{} crafting {} failed: {} — moving to next workshop", name, target.get().getCode(), e.getMessage());
      routeToNextWorkshop(name, character, state);
    }
  }

  /** Pops the next workshop from the queue and routes there, or routes to bank if queue is empty. */
  private void routeToNextWorkshop(String name, JsonNode character, CharacterLoopState state) {
    int cx = character.path("x").asInt();
    int cy = character.path("y").asInt();

    while (!state.getWorkshopSkillQueue().isEmpty()) {
      CraftSkill skill = state.getWorkshopSkillQueue().poll();
      MapTile workshop = mapCache.getClosestWorkshopTile(skill.getWorkshopCode(), cx, cy);
      if (workshop == null) {
        log.warn("{} no workshop tile found for skill {} — skipping", name, skill);
        continue;
      }
      state.setCurrentWorkshopSkill(skill);
      Deque<int[]> path = pathfinding.findPath(cx, cy, workshop.getX(), workshop.getY());
      log.info("{} heading to {} workshop at ({},{}) ({} remaining after)",
          name, skill.getWorkshopCode(), workshop.getX(), workshop.getY(),
          state.getWorkshopSkillQueue().size());
      state.getMovePath().clear();
      state.getMovePath().addAll(path);
      state.setPhase(path.isEmpty() ? CharacterPhase.CRAFTING : CharacterPhase.MOVING_TO_WORKSHOP);
      return;
    }

    // All workshops visited — deposit everything
    log.info("{} finished all workshops — heading to bank to deposit", name);
    state.setCurrentWorkshopSkill(null);
    MapTile bank = mapCache.getClosestBankTile(cx, cy);
    if (bank == null) {
      log.warn("No bank tile found for {} — going idle", name);
      state.setPhase(CharacterPhase.IDLE);
      return;
    }
    Deque<int[]> path = pathfinding.findPath(cx, cy, bank.getX(), bank.getY());
    state.getMovePath().clear();
    state.getMovePath().addAll(path);
    state.setPhase(path.isEmpty() ? CharacterPhase.BANKING : CharacterPhase.MOVING_TO_BANK);
  }

  // ── Shared phase handlers ─────────────────────────────────────────────────

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
      broadcastCharacterState(response, state);

      if (path.isEmpty()) {
        arriveAtDestination(name, state);
      }
    } catch (Exception e) {
      log.warn("{} move to ({},{}) failed: {} — replanning", name, next[0], next[1], e.getMessage());
      state.setPhase(CharacterPhase.IDLE);
    }
  }

  private void arriveAtDestination(String name, CharacterLoopState state) {
    switch (state.getPhase()) {
      case MOVING_TO_RESOURCE -> {
        log.info("{} arrived at resource tile {}", name, state.getTargetResourceKey());
        state.setPhase(CharacterPhase.GATHERING);
      }
      case MOVING_TO_BANK -> {
        log.info("{} arrived at bank", name);
        state.setPhase(CharacterPhase.BANKING);
      }
      case MOVING_TO_BANK_WITHDRAW -> {
        log.info("{} arrived at bank for withdrawal", name);
        state.setPhase(CharacterPhase.WITHDRAWING);
      }
      case MOVING_TO_WORKSHOP -> {
        CraftSkill skill = state.getCurrentWorkshopSkill();
        log.info("{} arrived at {} workshop ({})", name,
            skill != null ? skill.name() : "unknown",
            skill != null ? skill.getWorkshopCode() : "?");
        state.setPhase(CharacterPhase.CRAFTING);
      }
      default -> state.setPhase(CharacterPhase.IDLE);
    }
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
            broadcastCharacterState(response, state);
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
        broadcastCharacterState(response, state);
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
   * Returns every item code that exists anywhere — in the bank or in any character's inventory.
   * Used to avoid crafting depth-1+ items that are already owned.
   */
  private Set<String> buildOwnedItemCodes(Map<String, Integer> bank) {
    Set<String> owned = new HashSet<>(bank.keySet());
    for (CharacterLoopState s : states.values()) {
      JsonNode charData = s.getCachedCharacter();
      if (charData != null) {
        owned.addAll(buildInventoryMap(charData).keySet());
      }
    }
    return owned;
  }

  private boolean canCraftFromBank(CraftableItem item, Map<String, Integer> bank) {
    for (CraftableItem.Ingredient ing : item.getIngredients()) {
      if (bank.getOrDefault(ing.code(), 0) < ing.quantity()) {
        return false;
      }
    }
    return true;
  }

  private boolean canCraftFromInventory(CraftableItem item, Map<String, Integer> inventory) {
    for (CraftableItem.Ingredient ing : item.getIngredients()) {
      if (inventory.getOrDefault(ing.code(), 0) < ing.quantity()) {
        return false;
      }
    }
    return true;
  }

  private Map<String, Integer> buildInventoryMap(JsonNode character) {
    Map<String, Integer> map = new HashMap<>();
    JsonNode inventory = character.path("inventory");
    if (inventory.isArray()) {
      for (JsonNode slot : inventory) {
        String code = slot.path("code").asText(null);
        int qty = slot.path("quantity").asInt(0);
        if (code != null && !code.isBlank() && qty > 0) {
          map.put(code, qty);
        }
      }
    }
    return map;
  }

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

  private void broadcastCharacterState(JsonNode actionResponse, CharacterLoopState state) {
    JsonNode data = actionResponse.path("data");
    JsonNode charData = data.path("character");
    if (charData.isMissingNode()) return;
    state.setCachedCharacter(charData); // keep local cache current
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
            .role(state.getRole())
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
