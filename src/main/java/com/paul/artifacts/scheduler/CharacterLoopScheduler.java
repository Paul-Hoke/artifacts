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

    // Bootstrap: call getMyCharacters() exactly once to register characters and assign roles.
    if (!loopService.hasCharacters()) {
      log.info("Bootstrapping characters...");
      JsonNode charsNode;
      try {
        charsNode = client.getMyCharacters();
      } catch (Exception e) {
        log.warn("Could not fetch characters for bootstrap: {}", e.getMessage());
        return;
      }
      JsonNode characters = charsNode.path("data");
      if (!characters.isArray()) return;
      int index = 0;
      for (JsonNode character : characters) {
        loopService.bootstrapCharacter(character, index++);
      }
      return;
    }

    // After bootstrap, skip if no character is ready yet
    if (!loopService.isAnyCharacterReady()) return;

    loopService.tickAll();
  }
}
