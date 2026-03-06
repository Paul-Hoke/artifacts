package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllMaps(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllMaps(page, size);
  }

  @GetMapping("/id/{mapId}")
  public JsonNode getMapById(@PathVariable Integer mapId) {
    return client.getMapById(mapId);
  }

  @GetMapping("/{layer}")
  public JsonNode getLayerMaps(
      @PathVariable String layer,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getLayerMaps(layer, page, size);
  }

  @GetMapping("/{layer}/{x}/{y}")
  public JsonNode getMapByPosition(
      @PathVariable String layer,
      @PathVariable Integer x,
      @PathVariable Integer y) {
    return client.getMapByPosition(layer, x, y);
  }
}
