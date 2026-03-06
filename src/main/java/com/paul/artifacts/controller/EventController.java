package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.request.SpawnEventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllEvents(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllEvents(page, size);
  }

  @GetMapping("/active")
  public JsonNode getAllActiveEvents(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllActiveEvents(page, size);
  }

  @PostMapping("/spawn")
  public JsonNode spawnEvent(@RequestBody SpawnEventRequest request) {
    return client.spawnEvent(request);
  }
}
