package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllItems(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllItems(page, size);
  }

  @GetMapping("/{code}")
  public JsonNode getItem(@PathVariable String code) {
    return client.getItem(code);
  }
}
