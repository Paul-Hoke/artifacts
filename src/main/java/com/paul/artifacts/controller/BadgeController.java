package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/badges")
@RequiredArgsConstructor
public class BadgeController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllBadges(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllBadges(page, size);
  }

  @GetMapping("/{code}")
  public JsonNode getBadge(@PathVariable String code) {
    return client.getBadge(code);
  }
}
