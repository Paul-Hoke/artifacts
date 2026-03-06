package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/effects")
@RequiredArgsConstructor
public class EffectController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllEffects(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllEffects(page, size);
  }

  @GetMapping("/{code}")
  public JsonNode getEffect(@PathVariable String code) {
    return client.getEffect(code);
  }
}
