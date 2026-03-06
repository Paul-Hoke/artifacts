package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monsters")
@RequiredArgsConstructor
public class MonsterController {

  private final ArtifactsApiClient client;

  @GetMapping
  public JsonNode getAllMonsters(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllMonsters(page, size);
  }

  @GetMapping("/{code}")
  public JsonNode getMonster(@PathVariable String code) {
    return client.getMonster(code);
  }
}
