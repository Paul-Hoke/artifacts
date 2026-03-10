package com.paul.artifacts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverworldMapCache {

  private final ArtifactsApiClient client;

  // "x,y" → map_id
  private volatile Map<String, Integer> tileMapIds = Map.of();

  @PostConstruct
  public void load() {
    try {
      log.info("Loading overworld map cache...");
      Map<String, Integer> tiles = new HashMap<>();
      JsonNode first = client.getLayerMaps("overworld", 1, 500);
      int pages = first.path("pages").asInt(1);
      addTiles(tiles, first);
      for (int p = 2; p <= pages; p++) {
        addTiles(tiles, client.getLayerMaps("overworld", p, 500));
      }
      tileMapIds = Map.copyOf(tiles);
      log.info("Overworld map cache loaded: {} walkable tiles", tileMapIds.size());
    } catch (Exception e) {
      log.error("Failed to load overworld map cache", e);
    }
  }

  private void addTiles(Map<String, Integer> tiles, JsonNode response) {
    JsonNode data = response.path("data");
    if (data.isArray()) {
      data.forEach(tile -> {
        String key = tile.path("x").asInt() + "," + tile.path("y").asInt();
        tiles.put(key, tile.path("map_id").asInt());
      });
    }
  }

  public boolean isWalkable(int x, int y) {
    return tileMapIds.containsKey(x + "," + y);
  }

  public Integer getMapId(int x, int y) {
    return tileMapIds.get(x + "," + y);
  }
}
