package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.request.CreateCharacterRequest;
import com.paul.artifacts.model.request.DeleteCharacterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {

  private final ArtifactsApiClient client;

  @PostMapping("/create")
  public JsonNode createCharacter(@RequestBody CreateCharacterRequest request) {
    return client.createCharacter(request);
  }

  @PostMapping("/delete")
  public JsonNode deleteCharacter(@RequestBody DeleteCharacterRequest request) {
    return client.deleteCharacter(request);
  }

  @GetMapping("/active")
  public JsonNode getActiveCharacters(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getActiveCharacters(page, size);
  }

  @GetMapping("/{name}")
  public JsonNode getCharacter(@PathVariable String name) {
    return client.getCharacter(name);
  }
}
