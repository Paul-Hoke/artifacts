package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.service.ItemCache;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemCache cache;

  @GetMapping
  public Collection<JsonNode> getAllItems() {
    return cache.getAllItems().values();
  }

  @GetMapping("/{code}")
  public JsonNode getItem(@PathVariable String code) {
    return cache.getItem(code).orElse(null);
  }
}
