package com.paul.artifacts.model.ws;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CharacterPositionMessage {

  private String name;
  private String skin;
  private int x;
  private int y;
  private double cooldownSeconds;
}
