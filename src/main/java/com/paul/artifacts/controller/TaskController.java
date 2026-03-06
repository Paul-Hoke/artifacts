package com.paul.artifacts.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.client.ArtifactsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final ArtifactsApiClient client;

  @GetMapping("/list")
  public JsonNode getAllTasks(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllTasks(page, size);
  }

  @GetMapping("/list/{code}")
  public JsonNode getTask(@PathVariable String code) {
    return client.getTask(code);
  }

  @GetMapping("/rewards")
  public JsonNode getAllTaskRewards(
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {
    return client.getAllTaskRewards(page, size);
  }

  @GetMapping("/rewards/{code}")
  public JsonNode getTaskReward(@PathVariable String code) {
    return client.getTaskReward(code);
  }
}
