package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllResources(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllResources(page, size);
  }

  @GetMapping("/{code}")
  public JsonNode getResource(@PathVariable String code) {
    return client.getResource(code);
  }
}
