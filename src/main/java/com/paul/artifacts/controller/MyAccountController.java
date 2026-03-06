package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import com.paul.artifacts.model.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyAccountController {

  private final ArtifactsApiClient client;

  @GetMapping("/bank")
  public JsonNode getBankDetails() {
    return client.getBankDetails();
  }

  @GetMapping("/bank/items")
  public JsonNode getBankItems(
      @RequestParam(required = false) String itemCode,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getBankItems(itemCode, page, size);
  }

  @GetMapping("/grandexchange/orders")
  public JsonNode getMyGeOrders(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String type,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getMyGeOrders(code, type, page, size);
  }

  @GetMapping("/grandexchange/history")
  public JsonNode getMyGeHistory(
      @RequestParam(required = false) String id,
      @RequestParam(required = false) String code,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getMyGeHistory(id, code, page, size);
  }

  @GetMapping("/details")
  public JsonNode getAccountDetails() {
    return client.getAccountDetails();
  }

  @PostMapping("/change_password")
  public JsonNode changePassword(@RequestBody ChangePasswordRequest request) {
    return client.changePassword(request);
  }

  @GetMapping("/pending-items")
  public JsonNode getPendingItems(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getPendingItems(page, size);
  }
}
