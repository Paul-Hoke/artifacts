package com.paul.artifacts.model.crafting;

/**
 * Crafting skills in Artifacts MMO. Each skill maps to a workshop content code on the overworld map.
 */
public enum CraftSkill {
  ALCHEMY("alchemy"),
  COOKING("cooking"),
  GEARCRAFTING("gearcrafting"),
  JEWELRYCRAFTING("jewelrycrafting"),
  MINING("mining"),
  WEAPONCRAFTING("weaponcrafting"),
  WOODCUTTING("woodcutting");

  private final String workshopCode;

  CraftSkill(String workshopCode) {
    this.workshopCode = workshopCode;
  }

  public String getWorkshopCode() {
    return workshopCode;
  }
}
