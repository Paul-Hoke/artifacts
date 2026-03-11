package com.paul.artifacts.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.paul.artifacts.model.crafting.CraftSkill;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@Data
public class CharacterLoopState {

  private CharacterRole role;

  private CharacterPhase phase = CharacterPhase.IDLE;

  /** Full character JSON from the most recent action response (data.character). */
  private JsonNode cachedCharacter;

  /** Local copy of when this character's cooldown expires; Instant.MIN means ready now. */
  private Instant cooldownExpiration = Instant.MIN;

  // ── Gatherer fields ───────────────────────────────────────────────────────

  /** Position key "x,y" of the resource tile we are targeting. */
  private String targetResourceKey;

  /** Item code being gathered (e.g. "copper", "ash_wood"). */
  private String targetResourceCode;

  /** Number of items gathered so far in the current gathering run. */
  private int gatherCount;

  // ── Crafter fields ────────────────────────────────────────────────────────

  /** Depth of craftable items to target in the current cycle (0 = raw ingredients only, 1 = one layer deep). */
  private int craftDepth = 0;

  /**
   * Ingredients to withdraw this cycle: item code → total quantity to have in inventory.
   * Populated once in planCraft(), consumed by doWithdraw().
   */
  private Map<String, Integer> withdrawPlan = new HashMap<>();

  /** Item to delete at the start of the next tick (excess after a withdrawal). */
  private String pendingDeleteCode;
  private int pendingDeleteQuantity;

  /** Remaining workshops to visit this cycle, in order. */
  private final Deque<CraftSkill> workshopSkillQueue = new ArrayDeque<>();

  /** The skill/workshop the crafter is currently crafting at. */
  private CraftSkill currentWorkshopSkill;

  // ── Shared ────────────────────────────────────────────────────────────────

  /** Remaining path steps to the current destination. */
  private final Deque<int[]> movePath = new ArrayDeque<>();
}
