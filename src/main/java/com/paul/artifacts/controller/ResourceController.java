package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.service.ResourceCache;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

  private final ResourceCache cache;

  @GetMapping
  public Collection<JsonNode> getAllResources() {
    return cache.getAllResources().values();
  }

  @GetMapping("/{code}")
  public JsonNode getResource(@PathVariable String code) {
    return cache.getResource(code).orElse(null);
  }
}
