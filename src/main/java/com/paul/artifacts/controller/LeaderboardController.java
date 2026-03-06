package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

  private final ArtifactsApiClient client;

  @GetMapping("/accounts")
  public JsonNode getAccountsLeaderboard(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAccountsLeaderboard(page, size);
  }

  @GetMapping("/characters")
  public JsonNode getCharactersLeaderboard(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getCharactersLeaderboard(page, size);
  }
}
