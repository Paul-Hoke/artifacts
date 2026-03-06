package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.request.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

  private final ArtifactsApiClient client;

  @PostMapping
  public JsonNode generateToken(@RequestBody TokenRequest request) {
    return client.generateToken(request);
  }
}
