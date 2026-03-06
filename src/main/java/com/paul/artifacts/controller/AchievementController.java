package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/achievements")
@RequiredArgsConstructor
public class AchievementController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllAchievements(
      @RequestParam(required = false) String type,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllAchievements(type, page, size);
  }

  @GetMapping("/{code}")
  public JsonNode getAchievement(@PathVariable String code) {
    return client.getAchievement(code);
  }
}
