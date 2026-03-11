package com.paul.artifacts.model;

public enum CharacterPhase {
  // Shared
  IDLE,
  MOVING_TO_BANK,
  BANKING,

  // Gatherer
  MOVING_TO_RESOURCE,
  GATHERING,

  // Crafter
  MOVING_TO_BANK_WITHDRAW,
  WITHDRAWING,
  MOVING_TO_WORKSHOP,
  CRAFTING
}
