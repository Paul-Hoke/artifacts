package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grandexchange")
@RequiredArgsConstructor
public class GrandExchangeController {

  private final ArtifactsApiClient client;

  @GetMapping("/orders")
  public JsonNode getGeOrders(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getGeOrders(code, page, size);
  }

  @GetMapping("/orders/{id}")
  public JsonNode getGeOrder(@PathVariable String id) {
    return client.getGeOrder(id);
  }

  @GetMapping("/history/{code}")
  public JsonNode getGeHistory(
      @PathVariable String code,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getGeHistory(code, page, size);
  }
}
