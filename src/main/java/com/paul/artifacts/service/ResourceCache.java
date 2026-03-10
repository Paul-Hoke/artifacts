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
public class ResourceCache {

  private final ArtifactsApiClient client;
  private volatile Map<String, JsonNode> resources = Map.of();

  @PostConstruct
  public void load() {
    try {
      log.info("Loading resource cache...");
      Map<String, JsonNode> loaded = new HashMap<>();
      JsonNode first = client.getAllResources(1, 100);
      int pages = first.path("pages").asInt(1);
      addResources(loaded, first);
      for (int p = 2; p <= pages; p++) {
        addResources(loaded, client.getAllResources(p, 100));
      }
      resources = Map.copyOf(loaded);
      log.info("Resource cache loaded: {} resources", resources.size());
    } catch (Exception e) {
      log.error("Failed to load resource cache", e);
    }
  }

  private void addResources(Map<String, JsonNode> loaded, JsonNode response) {
    JsonNode data = response.path("data");
    if (!data.isArray()) return;
    data.forEach(resource -> {
      String code = resource.path("code").asText();
      loaded.put(code, resource);
    });
  }

  public Optional<JsonNode> getResource(String code) {
    return Optional.ofNullable(resources.get(code));
  }

  public Map<String, JsonNode> getAllResources() {
    return resources;
  }
}
