package com.paul.artifacts.model;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

@Data
public class CharacterLoopState {

  private CharacterPhase phase = CharacterPhase.IDLE;

  /** Local copy of when this character's cooldown expires; Instant.MIN means ready now. */
  private Instant cooldownExpiration = Instant.MIN;

  /** Position key "x,y" of the resource tile we are targeting. */
  private String targetResourceKey;

  /** Item code being gathered (e.g. "copper", "ash_wood"). */
  private String targetResourceCode;

  /** Number of items gathered so far in the current gathering run. */
  private int gatherCount;

  /** Remaining path steps to the current destination. */
  private final Deque<int[]> movePath = new ArrayDeque<>();

  /** Resource tile keys already gathered this cycle; cleared when exhausted. */
  private final Set<String> visitedResourceTiles = new HashSet<>();
}
