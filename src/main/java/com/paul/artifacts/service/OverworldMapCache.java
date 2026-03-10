package com.paul.artifacts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.map.MapTile;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverworldMapCache {

  private final ArtifactsApiClient client;

  // "x,y" → MapTile
  private volatile Map<String, MapTile> tiles = Map.of();

  @PostConstruct
  public void load() {
    try {
      log.info("Loading overworld map cache...");
      Map<String, MapTile> loaded = new HashMap<>();
      JsonNode first = client.getLayerMaps("overworld", 1, 500);
      int pages = first.path("pages").asInt(1);
      addTiles(loaded, first);
      for (int p = 2; p <= pages; p++) {
        addTiles(loaded, client.getLayerMaps("overworld", p, 500));
      }
      tiles = Map.copyOf(loaded);
      long resources = tiles.values().stream().filter(t -> "resource".equals(t.getContentType())).count();
      long banks     = tiles.values().stream().filter(t -> "bank".equals(t.getContentType())).count();
      log.info("Overworld map cache loaded: {} tiles ({} resource, {} bank)", tiles.size(), resources, banks);
    } catch (Exception e) {
      log.error("Failed to load overworld map cache", e);
    }
  }

  private void addTiles(Map<String, MapTile> loaded, JsonNode response) {
    JsonNode data = response.path("data");
    if (!data.isArray()) return;
    data.forEach(tile -> {
      int x = tile.path("x").asInt();
      int y = tile.path("y").asInt();

      // Top-level content covers banks, npcs, monsters, etc.
      JsonNode content = tile.path("content");
      String contentType = content.isMissingNode() || content.isNull() ? null : content.path("type").asText(null);
      String contentCode = content.isMissingNode() || content.isNull() ? null : content.path("code").asText(null);

      // interactions is an ObjectNode with a nested "content" object — check if type is "resource"
      JsonNode interactionContent = tile.path("interactions").path("content");
      if ("resource".equals(interactionContent.path("type").asText(null))
        || "bank".equals(interactionContent.path("type").asText(null))) {
        contentType = interactionContent.path("type").asText(null);
        contentCode = interactionContent.path("code").asText(null);
      }

      loaded.put(x + "," + y, MapTile.builder()
          .x(x)
          .y(y)
          .mapId(tile.path("map_id").asInt())
          .contentType(contentType)
          .contentCode(contentCode)
          .build());
    });
  }

  public boolean isWalkable(int x, int y) {
    return tiles.containsKey(x + "," + y);
  }

  public Integer getMapId(int x, int y) {
    MapTile tile = tiles.get(x + "," + y);
    return tile == null ? null : tile.getMapId();
  }

  public MapTile getTile(int x, int y) {
    return tiles.get(x + "," + y);
  }

  public Collection<MapTile> getResourceTiles() {
    return tiles.values().stream()
        .filter(t -> "resource".equals(t.getContentType()))
        .toList();
  }

  public MapTile getClosestBankTile(int x, int y) {
    return tiles.values().stream()
        .filter(t -> "bank".equals(t.getContentType()))
        .min((a, b) -> {
          int da = Math.abs(a.getX() - x) + Math.abs(a.getY() - y);
          int db = Math.abs(b.getX() - x) + Math.abs(b.getY() - y);
          return Integer.compare(da, db);
        })
        .orElse(null);
  }
}
