package com.paul.artifacts.scheduler;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.request.DestinationRequest;
import com.paul.artifacts.model.ws.CharacterPositionMessage;
import com.paul.artifacts.service.OverworldMapCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class RandomWalkScheduler {

  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
  private static final Random RANDOM = new Random();

  private final ArtifactsApiClient client;
  private final OverworldMapCache mapCache;
  private final SimpMessagingTemplate messagingTemplate;

  private volatile boolean enabled = true;

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Scheduled(fixedDelay = 5000)
  public void randomWalk() {
    if (!enabled) return;

    JsonNode charsNode;
    try {
      charsNode = client.getMyCharacters();
    } catch (Exception e) {
      log.warn("Could not fetch characters for random walk: {}", e.getMessage());
      return;
    }

    JsonNode characters = charsNode.path("data");
    if (!characters.isArray()) return;

    for (JsonNode character : characters) {
      tryMoveCharacter(character);
    }
  }

  private void tryMoveCharacter(JsonNode character) {
    String name = character.path("name").asText();

    // Respect cooldown — skip if character is still waiting
    String cooldownExpStr = character.path("cooldown_expiration").asText(null);
    if (cooldownExpStr != null && !cooldownExpStr.isBlank()) {
      try {
        Instant expiry = Instant.parse(cooldownExpStr);
        if (Instant.now().isBefore(expiry)) {
          log.debug("{} on cooldown until {}", name, expiry);
          return;
        }
      } catch (DateTimeParseException e) {
        log.debug("Could not parse cooldown_expiration '{}' for {}", cooldownExpStr, name);
      }
    }

    int cx = character.path("x").asInt();
    int cy = character.path("y").asInt();

    // Collect walkable neighbors then pick one at random
    List<int[]> walkable = Arrays.stream(DIRECTIONS)
        .filter(d -> mapCache.isWalkable(cx + d[0], cy + d[1]))
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

    if (walkable.isEmpty()) {
      log.debug("No walkable neighbor for {} at ({}, {})", name, cx, cy);
      return;
    }

    int[] chosen = walkable.get(RANDOM.nextInt(walkable.size()));
    int nx = cx + chosen[0];
    int ny = cy + chosen[1];

    DestinationRequest req = new DestinationRequest();
    req.setX(nx);
    req.setY(ny);
    req.setMapId(mapCache.getMapId(nx, ny));

    try {
      JsonNode response = client.actionMove(name, req);
      JsonNode data = response.path("data");
      JsonNode charData = data.path("character");
      if (!charData.isMissingNode()) {
        double cooldownSeconds = data.path("cooldown").path("total_seconds").asDouble(0);
        CharacterPositionMessage msg = CharacterPositionMessage.builder()
            .name(charData.path("name").asText())
            .skin(charData.path("skin").asText())
            .x(charData.path("x").asInt())
            .y(charData.path("y").asInt())
            .cooldownSeconds(cooldownSeconds)
            .build();
        messagingTemplate.convertAndSend("/topic/characters", msg);
        log.debug("Random walk: moved {} → ({}, {}) cooldown={}s", name, msg.getX(), msg.getY(), cooldownSeconds);
      }
    } catch (Exception e) {
      log.warn("Move failed for {} → ({}, {}): {}", name, nx, ny, e.getMessage());
    }
  }
}
