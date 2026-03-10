package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.service.MonsterCache;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/monsters")
@RequiredArgsConstructor
public class MonsterController {

  private final MonsterCache cache;

  @GetMapping
  public Collection<JsonNode> getAllMonsters() {
    return cache.getAllMonsters().values();
  }

  @GetMapping("/{code}")
  public JsonNode getMonster(@PathVariable String code) {
    return cache.getMonster(code).orElse(null);
  }
}
