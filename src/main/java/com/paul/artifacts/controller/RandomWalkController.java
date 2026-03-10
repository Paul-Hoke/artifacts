package com.paul.artifacts.controller;

import com.paul.artifacts.scheduler.CharacterLoopScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/random-walk")
@RequiredArgsConstructor
public class RandomWalkController {

  private final CharacterLoopScheduler scheduler;
  private final SimpMessagingTemplate messagingTemplate;

  @GetMapping("/state")
  public Map<String, Object> state() {
    return Map.of("enabled", scheduler.isEnabled());
  }

  @PostMapping("/start")
  public Map<String, Object> start() {
    scheduler.setEnabled(true);
    messagingTemplate.convertAndSend("/topic/walker-state", Map.of("enabled", true));
    return Map.of("enabled", true);
  }

  @PostMapping("/stop")
  public Map<String, Object> stop() {
    scheduler.setEnabled(false);
    messagingTemplate.convertAndSend("/topic/walker-state", Map.of("enabled", false));
    return Map.of("enabled", false);
  }
}
