package com.paul.artifacts.model;

public enum CharacterRole {
  GATHERER,
  HUNTER,
  CRAFTER;

  public static CharacterRole fromIndex(int index) {
    return switch (index) {
      case 0 -> GATHERER;
      case 1 -> HUNTER;
      default -> CRAFTER;
    };
  }
}
