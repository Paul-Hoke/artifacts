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
public class ItemCache {

  private final ArtifactsApiClient client;
  private volatile Map<String, JsonNode> items = Map.of();

  @PostConstruct
  public void load() {
    try {
      log.info("Loading item cache...");
      Map<String, JsonNode> loaded = new HashMap<>();
      JsonNode first = client.getAllItems(1, 100);
      int pages = first.path("pages").asInt(1);
      addItems(loaded, first);
      for (int p = 2; p <= pages; p++) {
        addItems(loaded, client.getAllItems(p, 100));
      }
      items = Map.copyOf(loaded);
      log.info("Item cache loaded: {} items", items.size());
    } catch (Exception e) {
      log.error("Failed to load item cache", e);
    }
  }

  private void addItems(Map<String, JsonNode> loaded, JsonNode response) {
    JsonNode data = response.path("data");
    if (!data.isArray()) return;
    data.forEach(item -> {
      String code = item.path("code").asText();
      loaded.put(code, item);
    });
  }

  public Optional<JsonNode> getItem(String code) {
    return Optional.ofNullable(items.get(code));
  }

  public Map<String, JsonNode> getAllItems() {
    return items;
  }
}
