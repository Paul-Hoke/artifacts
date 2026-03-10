package com.paul.artifacts.model.map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapTile {

  private int x;
  private int y;
  private int mapId;
  private String contentType;  // e.g. "resource", "bank", "monster", "npc" — null if empty
  private String contentCode;  // e.g. "copper_rocks", "bank" — null if empty
}
