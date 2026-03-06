package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {

  private final ArtifactsApiClient client;

  @PostMapping("/fight")
  public JsonNode fightSimulation(@RequestBody Object request) {
    return client.fightSimulation(request);
  }
}
