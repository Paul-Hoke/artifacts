package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyCharactersController {

  private final ArtifactsApiClient client;

  @GetMapping("/logs")
  public JsonNode getAllCharactersLogs(@RequestParam(required = false) Integer page) {
    return client.getAllCharactersLogs(page);
  }

  @GetMapping("/logs/{name}")
  public JsonNode getCharacterLogs(@PathVariable String name,
      @RequestParam(required = false) Integer page) {
    return client.getCharacterLogs(name, page);
  }

  @GetMapping("/characters")
  public JsonNode getMyCharacters() {
    return client.getMyCharacters();
  }
}
