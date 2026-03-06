package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.request.CreateAccountRequest;
import com.paul.artifacts.model.request.ForgotPasswordRequest;
import com.paul.artifacts.model.request.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final ArtifactsApiClient client;

  @PostMapping("/create")
  public JsonNode createAccount(@RequestBody CreateAccountRequest request) {
    return client.createAccount(request);
  }

  @PostMapping("/forgot_password")
  public JsonNode forgotPassword(@RequestBody ForgotPasswordRequest request) {
    return client.forgotPassword(request);
  }

  @PostMapping("/reset_password")
  public JsonNode resetPassword(@RequestBody ResetPasswordRequest request) {
    return client.resetPassword(request);
  }

  @GetMapping("/{account}/achievements")
  public JsonNode getAccountAchievements(@PathVariable String account) {
    return client.getAccountAchievements(account);
  }

  @GetMapping("/{account}/characters")
  public JsonNode getAccountCharacters(@PathVariable String account) {
    return client.getAccountCharacters(account);
  }

  @GetMapping("/{account}")
  public JsonNode getAccount(@PathVariable String account) {
    return client.getAccount(account);
  }
}
