package com.paul.artifacts.model.ws;

import com.paul.artifacts.model.common.SimpleItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CharacterPositionMessage {

  private String name;
  private String skin;
  private int x;
  private int y;
  private double cooldownSeconds;
  private List<SimpleItem> inventory;
}
