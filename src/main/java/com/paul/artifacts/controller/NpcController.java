package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/npcs")
@RequiredArgsConstructor
public class NpcController {

  private final ArtifactsApiClient client;

  @GetMapping("/details")
  public JsonNode getAllNpcs(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllNpcs(page, size);
  }

  @GetMapping("/details/{code}")
  public JsonNode getNpc(@PathVariable String code) {
    return client.getNpc(code);
  }

  @GetMapping("/items")
  public JsonNode getAllNpcItems(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllNpcItems(page, size);
  }

  @GetMapping("/items/{code}")
  public JsonNode getNpcItems(@PathVariable String code) {
    return client.getNpcItems(code);
  }
}
