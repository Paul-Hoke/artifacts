package com.paul.artifacts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonsterCache {

  private final ArtifactsApiClient client;
  private volatile Map<String, JsonNode> monsters = Map.of();

  @PostConstruct
  public void load() {
    try {
      log.info("Loading monster cache...");
      Map<String, JsonNode> loaded = new HashMap<>();
      JsonNode first = client.getAllMonsters(1, 100);
      int pages = first.path("pages").asInt(1);
      addMonsters(loaded, first);
      for (int p = 2; p <= pages; p++) {
        addMonsters(loaded, client.getAllMonsters(p, 100));
      }
      monsters = Map.copyOf(loaded);
      log.info("Monster cache loaded: {} monsters", monsters.size());
    } catch (Exception e) {
      log.error("Failed to load monster cache", e);
    }
  }

  private void addMonsters(Map<String, JsonNode> loaded, JsonNode response) {
    JsonNode data = response.path("data");
    if (!data.isArray()) return;
    data.forEach(monster -> {
      String code = monster.path("code").asText();
      loaded.put(code, monster);
    });
  }

  public Optional<JsonNode> getMonster(String code) {
    return Optional.ofNullable(monsters.get(code));
  }

  public Map<String, JsonNode> getAllMonsters() {
    return monsters;
  }
}
