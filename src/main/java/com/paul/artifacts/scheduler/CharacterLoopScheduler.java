package com.paul.artifacts.scheduler;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.service.CharacterLoopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CharacterLoopScheduler {

  private final ArtifactsApiClient client;
  private final CharacterLoopService loopService;

  private volatile boolean enabled = true;

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Scheduled(fixedDelay = 500)
  public void tick() {
    if (!enabled) return;

    // Skip API call entirely if all characters are still on cooldown
    if (!loopService.isAnyCharacterReady()) return;

    JsonNode charsNode;
    try {
      charsNode = client.getMyCharacters();
    } catch (Exception e) {
      log.warn("Could not fetch characters: {}", e.getMessage());
      return;
    }

    JsonNode characters = charsNode.path("data");
    if (!characters.isArray()) return;

    for (JsonNode character : characters) {
      try {
        loopService.tick(character);
      } catch (Exception e) {
        log.error("Loop error for {}: {}", character.path("name").asText(), e.getMessage(), e);
      }
    }
  }
}
