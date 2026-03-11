package com.paul.artifacts.model.crafting;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Every craftable item in Artifacts MMO, ordered from easiest (depth 0) to hardest (depth 2).
 * Depth reflects how many layers of crafting are required before the item itself can be made.
 */
public enum CraftableItem {

  // ── Depth 0 — all raw ingredients ────────────────────────────────────
  APPRENTICE_GLOVES("apprentice_gloves", "Apprentice Gloves", CraftSkill.WEAPONCRAFTING, 1, 1, 0,
      ing("feather", 6)),

  ASH_PLANK("ash_plank", "Ash Plank", CraftSkill.WOODCUTTING, 1, 1, 0,
      ing("ash_wood", 10)),

  COOKED_CHICKEN("cooked_chicken", "Cooked Chicken", CraftSkill.COOKING, 1, 1, 0,
      ing("raw_chicken", 1)),

  COOKED_GUDGEON("cooked_gudgeon", "Cooked Gudgeon", CraftSkill.COOKING, 1, 1, 0,
      ing("gudgeon", 1)),

  COPPER_BAR("copper_bar", "Copper Bar", CraftSkill.MINING, 1, 1, 0,
      ing("copper_ore", 10)),

  WOODEN_STAFF("wooden_staff", "Wooden Staff", CraftSkill.WEAPONCRAFTING, 1, 1, 0,
      ing("wooden_stick", 1), ing("ash_wood", 4)),

  COOKED_BEEF("cooked_beef", "Cooked Beef", CraftSkill.COOKING, 5, 1, 0,
      ing("raw_beef", 1)),

  FRIED_EGGS("fried_eggs", "Fried Eggs", CraftSkill.COOKING, 5, 1, 0,
      ing("egg", 2)),

  LIFE_AMULET("life_amulet", "Life Amulet", CraftSkill.JEWELRYCRAFTING, 5, 1, 0,
      ing("feather", 4), ing("red_slimeball", 2)),

  SATCHEL("satchel", "Satchel", CraftSkill.GEARCRAFTING, 5, 1, 0,
      ing("cowhide", 5), ing("feather", 2), ing("jasper_crystal", 1)),

  SMALL_HEALTH_POTION("small_health_potion", "Small Health Potion", CraftSkill.ALCHEMY, 5, 1, 0,
      ing("sunflower", 3)),

  AIR_BOOST_POTION("air_boost_potion", "Air Boost Potion", CraftSkill.ALCHEMY, 10, 1, 0,
      ing("green_slimeball", 1), ing("sunflower", 1), ing("algae", 1)),

  CHEESE("cheese", "Cheese", CraftSkill.COOKING, 10, 1, 0,
      ing("milk_bucket", 1)),

  COOKED_SHRIMP("cooked_shrimp", "Cooked Shrimp", CraftSkill.COOKING, 10, 1, 0,
      ing("shrimp", 1)),

  EARTH_BOOST_POTION("earth_boost_potion", "Earth Boost Potion", CraftSkill.ALCHEMY, 10, 1, 0,
      ing("yellow_slimeball", 1), ing("sunflower", 1), ing("algae", 1)),

  FIRE_BOOST_POTION("fire_boost_potion", "Fire Boost Potion", CraftSkill.ALCHEMY, 10, 1, 0,
      ing("red_slimeball", 1), ing("sunflower", 1), ing("algae", 1)),

  IRON_BAR("iron_bar", "Iron Bar", CraftSkill.MINING, 10, 1, 0,
      ing("iron_ore", 10)),

  LEATHER_HAT("leather_hat", "Leather Hat", CraftSkill.GEARCRAFTING, 10, 1, 0,
      ing("cowhide", 5), ing("yellow_slimeball", 3)),

  SPRUCE_PLANK("spruce_plank", "Spruce Plank", CraftSkill.WOODCUTTING, 10, 1, 0,
      ing("spruce_wood", 10)),

  WATER_BOOST_POTION("water_boost_potion", "Water Boost Potion", CraftSkill.ALCHEMY, 10, 1, 0,
      ing("blue_slimeball", 1), ing("sunflower", 1), ing("algae", 1)),

  COOKED_WOLF_MEAT("cooked_wolf_meat", "Cooked Wolf Meat", CraftSkill.COOKING, 15, 1, 0,
      ing("raw_wolf_meat", 1)),

  LUCKY_WIZARD_HAT("lucky_wizard_hat", "Lucky Wizard Hat", CraftSkill.GEARCRAFTING, 15, 1, 0,
      ing("green_cloth", 6), ing("flying_wing", 6), ing("snakeskin", 3)),

  MUSHMUSH_JACKET("mushmush_jacket", "Mushmush Jacket", CraftSkill.GEARCRAFTING, 15, 1, 0,
      ing("hard_leather", 3), ing("flying_wing", 6), ing("mushroom", 6)),

  MUSHMUSH_WIZARD_HAT("mushmush_wizard_hat", "Mushmush Wizard Hat", CraftSkill.GEARCRAFTING, 15, 1, 0,
      ing("cowhide", 4), ing("wolf_hair", 4), ing("mushroom", 6)),

  MUSHROOM_SOUP("mushroom_soup", "Mushroom Soup", CraftSkill.COOKING, 15, 1, 0,
      ing("mushroom", 2)),

  APPLE_PIE("apple_pie", "Apple Pie", CraftSkill.COOKING, 20, 1, 0,
      ing("apple", 2), ing("egg", 1)),

  COOKED_TROUT("cooked_trout", "Cooked Trout", CraftSkill.COOKING, 20, 1, 0,
      ing("trout", 1)),

  EMERALD("emerald", "Emerald", CraftSkill.MINING, 20, 1, 0,
      ing("emerald_stone", 24)),

  HARDWOOD_PLANK("hardwood_plank", "Hardwood Plank", CraftSkill.WOODCUTTING, 20, 1, 0,
      ing("ash_wood", 4), ing("birch_wood", 6)),

  MINOR_HEALTH_POTION("minor_health_potion", "Minor Health Potion", CraftSkill.ALCHEMY, 20, 1, 0,
      ing("nettle_leaf", 2), ing("algae", 1)),

  RUBY("ruby", "Ruby", CraftSkill.MINING, 20, 1, 0,
      ing("ruby_stone", 24)),

  SAPPHIRE("sapphire", "Sapphire", CraftSkill.MINING, 20, 1, 0,
      ing("sapphire_stone", 24)),

  STEEL_BAR("steel_bar", "Steel Bar", CraftSkill.MINING, 20, 1, 0,
      ing("iron_ore", 3), ing("coal", 7)),

  TOPAZ("topaz", "Topaz", CraftSkill.MINING, 20, 1, 0,
      ing("topaz_stone", 24)),

  LIZARD_SKIN_LEGS_ARMOR("lizard_skin_legs_armor", "Lizard Skin Legs Armor", CraftSkill.GEARCRAFTING, 25, 1, 0,
      ing("lizard_skin", 5), ing("vermin_leather", 5), ing("ogre_eye", 4), ing("jasper_crystal", 2)),

  SNAKESKIN_ARMOR("snakeskin_armor", "Snakeskin Armor", CraftSkill.GEARCRAFTING, 25, 1, 0,
      ing("snakeskin", 2), ing("skeleton_bone", 5), ing("vampire_blood", 4), ing("jasper_crystal", 1)),

  SNAKESKIN_LEGS_ARMOR("snakeskin_legs_armor", "Snakeskin Legs Armor", CraftSkill.GEARCRAFTING, 25, 1, 0,
      ing("snakeskin", 2), ing("hard_leather", 3), ing("wolf_bone", 5)),

  STORMFORGED_PANTS("stormforged_pants", "Stormforged Pants", CraftSkill.GEARCRAFTING, 25, 1, 0,
      ing("lizard_eye", 4), ing("vermin_leather", 4), ing("ogre_skin", 6), ing("jasper_crystal", 2)),

  COOKED_BASS("cooked_bass", "Cooked Bass", CraftSkill.COOKING, 30, 1, 0,
      ing("bass", 1)),

  COOKED_RAT_MEAT("cooked_rat_meat", "Cooked Rat Meat", CraftSkill.COOKING, 30, 1, 0,
      ing("raw_rat_meat", 1)),

  DEAD_WOOD_PLANK("dead_wood_plank", "Dead Wood Plank", CraftSkill.WOODCUTTING, 30, 1, 0,
      ing("dead_wood", 10)),

  GOLD_BAR("gold_bar", "Gold bar", CraftSkill.MINING, 30, 1, 0,
      ing("gold_ore", 10)),

  HEALTH_SPLASH_POTION("health_splash_potion", "Health Splash Potion", CraftSkill.ALCHEMY, 30, 1, 0,
      ing("nettle_leaf", 2), ing("sunflower", 1), ing("algae", 1)),

  OBSIDIAN_BAR("obsidian_bar", "Obsidian Bar", CraftSkill.MINING, 30, 1, 0,
      ing("piece_of_obsidian", 4)),

  SAP("sap", "Sap", CraftSkill.WOODCUTTING, 30, 1, 0,
      ing("ash_wood", 5), ing("spruce_wood", 5), ing("dead_wood", 5)),

  CURSED_PLANK("cursed_plank", "Cursed Plank", CraftSkill.WOODCUTTING, 35, 1, 0,
      ing("cursed_wood", 10)),

  DIAMOND("diamond", "Diamond", CraftSkill.MINING, 35, 1, 0,
      ing("diamond_stone", 24)),

  MAGIC_SAP("magic_sap", "Magic Sap", CraftSkill.WOODCUTTING, 35, 1, 0,
      ing("magic_wood", 15)),

  MAGICAL_PLANK("magical_plank", "Magical Plank", CraftSkill.WOODCUTTING, 35, 1, 0,
      ing("dead_wood", 4), ing("magic_wood", 6)),

  STRANGOLD_BAR("strangold_bar", "Strangold Bar", CraftSkill.MINING, 35, 1, 0,
      ing("gold_ore", 4), ing("strange_ore", 6)),

  COOKED_HELLHOUND_MEAT("cooked_hellhound_meat", "Cooked Hellhound Meat", CraftSkill.COOKING, 40, 1, 0,
      ing("raw_hellhound_meat", 1)),

  COOKED_SALMON("cooked_salmon", "Cooked Salmon", CraftSkill.COOKING, 40, 1, 0,
      ing("salmon", 1)),

  FISH_SOUP("fish_soup", "Fish Soup", CraftSkill.COOKING, 40, 1, 0,
      ing("milk_bucket", 1), ing("salmon", 1), ing("trout", 1)),

  GREATER_HEALTH_POTION("greater_health_potion", "Greater Health Potion", CraftSkill.ALCHEMY, 40, 1, 0,
      ing("glowstem_leaf", 2), ing("egg", 1), ing("algae", 1)),

  MAPLE_PLANK("maple_plank", "Maple Plank", CraftSkill.WOODCUTTING, 40, 1, 0,
      ing("maple_wood", 10)),

  MAPLE_SAP("maple_sap", "Maple Sap", CraftSkill.WOODCUTTING, 40, 1, 0,
      ing("maple_wood", 15)),

  MITHRIL_BAR("mithril_bar", "Mithril Bar", CraftSkill.MINING, 40, 1, 0,
      ing("mithril_ore", 10)),

  ADAMANTITE_BAR("adamantite_bar", "Adamantite Bar", CraftSkill.MINING, 50, 1, 0,
      ing("adamantite_ore", 10)),

  ALEXANDRITE("alexandrite", "Alexandrite", CraftSkill.MINING, 50, 1, 0,
      ing("alexandrite_stone", 24)),

  COOKED_DESERT_SCORPION_MEAT("cooked_desert_scorpion_meat", "Cooked Desert Scorpion Meat", CraftSkill.COOKING, 50, 1, 0,
      ing("desert_scorpion_meat", 1)),

  COOKED_SWORDFISH("cooked_swordfish", "Cooked Swordfish", CraftSkill.COOKING, 50, 1, 0,
      ing("swordfish", 1)),

  PALM_PLANK("palm_plank", "Palm Plank", CraftSkill.WOODCUTTING, 50, 1, 0,
      ing("palm_wood", 10)),

  // ── Depth 1 — requires 1 craft layer ─────────────────────────────────
  COPPER_AXE("copper_axe", "Copper Axe", CraftSkill.WEAPONCRAFTING, 1, 1, 1,
      ing("copper_bar", 6)),

  COPPER_BOOTS("copper_boots", "Copper Boots", CraftSkill.GEARCRAFTING, 1, 1, 1,
      ing("copper_bar", 8)),

  COPPER_DAGGER("copper_dagger", "Copper Dagger", CraftSkill.WEAPONCRAFTING, 1, 1, 1,
      ing("copper_bar", 6)),

  COPPER_HELMET("copper_helmet", "Copper Helmet", CraftSkill.GEARCRAFTING, 1, 1, 1,
      ing("copper_bar", 6)),

  COPPER_PICKAXE("copper_pickaxe", "Copper Pickaxe", CraftSkill.WEAPONCRAFTING, 1, 1, 1,
      ing("copper_bar", 6)),

  COPPER_RING("copper_ring", "Copper Ring", CraftSkill.JEWELRYCRAFTING, 1, 1, 1,
      ing("copper_bar", 6)),

  FISHING_NET("fishing_net", "Fishing Net", CraftSkill.WEAPONCRAFTING, 1, 1, 1,
      ing("ash_plank", 6)),

  WOODEN_SHIELD("wooden_shield", "Wooden Shield", CraftSkill.GEARCRAFTING, 1, 1, 1,
      ing("ash_plank", 6)),

  COPPER_ARMOR("copper_armor", "Copper Armor", CraftSkill.GEARCRAFTING, 5, 1, 1,
      ing("copper_bar", 5), ing("wool", 2)),

  COPPER_LEGS_ARMOR("copper_legs_armor", "Copper Legs Armor", CraftSkill.GEARCRAFTING, 5, 1, 1,
      ing("copper_bar", 5), ing("feather", 2)),

  FEATHER_COAT("feather_coat", "Feather Coat", CraftSkill.GEARCRAFTING, 5, 1, 1,
      ing("feather", 5), ing("ash_plank", 2)),

  FIRE_STAFF("fire_staff", "Fire Staff", CraftSkill.WEAPONCRAFTING, 5, 1, 1,
      ing("red_slimeball", 2), ing("ash_plank", 5)),

  STICKY_DAGGER("sticky_dagger", "Sticky Dagger", CraftSkill.WEAPONCRAFTING, 5, 1, 1,
      ing("copper_bar", 5), ing("green_slimeball", 2)),

  STICKY_SWORD("sticky_sword", "Sticky Sword", CraftSkill.WEAPONCRAFTING, 5, 1, 1,
      ing("yellow_slimeball", 2), ing("copper_bar", 5)),

  WATER_BOW("water_bow", "Water Bow", CraftSkill.WEAPONCRAFTING, 5, 1, 1,
      ing("blue_slimeball", 2), ing("ash_plank", 5)),

  ADVENTURER_HELMET("adventurer_helmet", "Adventurer Helmet", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("feather", 4), ing("cowhide", 3), ing("spruce_plank", 3), ing("mushroom", 4)),

  ADVENTURER_VEST("adventurer_vest", "Adventurer Vest", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("wool", 2), ing("cowhide", 6), ing("spruce_plank", 4), ing("yellow_slimeball", 4)),

  AIR_AND_WATER_AMULET("air_and_water_amulet", "Air & Water Amulet", CraftSkill.JEWELRYCRAFTING, 10, 1, 1,
      ing("iron_bar", 4), ing("green_slimeball", 2), ing("blue_slimeball", 2)),

  FIRE_AND_EARTH_AMULET("fire_and_earth_amulet", "Fire & Earth Amulet", CraftSkill.JEWELRYCRAFTING, 10, 1, 1,
      ing("iron_bar", 4), ing("red_slimeball", 2), ing("yellow_slimeball", 2)),

  FIRE_BOW("fire_bow", "Fire Bow", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("spruce_plank", 6), ing("red_slimeball", 2)),

  GREATER_WOODEN_STAFF("greater_wooden_staff", "Greater Wooden Staff", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("spruce_plank", 6), ing("blue_slimeball", 2)),

  IRON_ARMOR("iron_armor", "Iron Armor", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("iron_bar", 5), ing("cowhide", 3)),

  IRON_AXE("iron_axe", "Iron Axe", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("spruce_plank", 2), ing("iron_bar", 8), ing("jasper_crystal", 1)),

  IRON_BOOTS("iron_boots", "Iron Boots", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("iron_bar", 5), ing("feather", 3)),

  IRON_DAGGER("iron_dagger", "Iron Dagger", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("iron_bar", 6), ing("feather", 2)),

  IRON_HELM("iron_helm", "Iron Helm", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("iron_bar", 5), ing("wool", 3)),

  IRON_LEGS_ARMOR("iron_legs_armor", "Iron Legs Armor", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("iron_bar", 5), ing("cowhide", 3)),

  IRON_PICKAXE("iron_pickaxe", "Iron Pickaxe", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("spruce_plank", 2), ing("iron_bar", 8), ing("jasper_crystal", 1)),

  IRON_RING("iron_ring", "Iron Ring", CraftSkill.JEWELRYCRAFTING, 10, 1, 1,
      ing("iron_bar", 6), ing("wool", 2)),

  IRON_SHIELD("iron_shield", "Iron Shield", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("iron_bar", 5), ing("wool", 3)),

  IRON_SWORD("iron_sword", "Iron Sword", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("iron_bar", 6), ing("feather", 2)),

  LEATHER_ARMOR("leather_armor", "Leather Armor", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("spruce_plank", 4), ing("cowhide", 4)),

  LEATHER_BOOTS("leather_boots", "Leather Boots", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("ash_plank", 4), ing("cowhide", 4)),

  LEATHER_GLOVES("leather_gloves", "Leather Gloves", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("ash_plank", 2), ing("cowhide", 8), ing("jasper_crystal", 1)),

  LEATHER_LEGS_ARMOR("leather_legs_armor", "Leather Legs Armor", CraftSkill.GEARCRAFTING, 10, 1, 1,
      ing("spruce_plank", 5), ing("cowhide", 3)),

  SPRUCE_FISHING_ROD("spruce_fishing_rod", "Spruce Fishing Rod", CraftSkill.WEAPONCRAFTING, 10, 1, 1,
      ing("spruce_plank", 8), ing("iron_bar", 2), ing("jasper_crystal", 1)),

  ADVENTURER_BOOTS("adventurer_boots", "Adventurer Boots", CraftSkill.GEARCRAFTING, 15, 1, 1,
      ing("wolf_hair", 5), ing("mushroom", 5), ing("spruce_plank", 5)),

  ADVENTURER_PANTS("adventurer_pants", "Adventurer Pants", CraftSkill.GEARCRAFTING, 15, 1, 1,
      ing("ash_plank", 7), ing("hard_leather", 3), ing("green_cloth", 3), ing("cloth", 2)),

  AIR_RING("air_ring", "Air Ring", CraftSkill.JEWELRYCRAFTING, 15, 1, 1,
      ing("iron_bar", 5), ing("green_slimeball", 4), ing("flying_wing", 3)),

  EARTH_RING("earth_ring", "Earth Ring", CraftSkill.JEWELRYCRAFTING, 15, 1, 1,
      ing("iron_bar", 5), ing("yellow_slimeball", 4), ing("flying_wing", 3)),

  FIRE_RING("fire_ring", "Fire Ring", CraftSkill.JEWELRYCRAFTING, 15, 1, 1,
      ing("iron_bar", 5), ing("red_slimeball", 4), ing("flying_wing", 3)),

  KING_SLIME_SWORD("king_slime_sword", "King Slime Sword", CraftSkill.WEAPONCRAFTING, 15, 1, 1,
      ing("iron_bar", 8), ing("king_slimeball", 6), ing("jasper_crystal", 1)),

  LIFE_RING("life_ring", "Life Ring", CraftSkill.JEWELRYCRAFTING, 15, 1, 1,
      ing("iron_bar", 8), ing("cloth", 2), ing("mushroom", 5)),

  MUSHMUSH_BOW("mushmush_bow", "Mushmush Bow", CraftSkill.WEAPONCRAFTING, 15, 1, 1,
      ing("spruce_plank", 5), ing("wolf_hair", 2), ing("mushroom", 4), ing("jasper_crystal", 1)),

  MUSHSTAFF("mushstaff", "Mushstaff", CraftSkill.WEAPONCRAFTING, 15, 1, 1,
      ing("spruce_plank", 5), ing("mushroom", 4), ing("green_cloth", 2), ing("jasper_crystal", 1)),

  WATER_RING("water_ring", "Water Ring", CraftSkill.JEWELRYCRAFTING, 15, 1, 1,
      ing("iron_bar", 5), ing("blue_slimeball", 4), ing("flying_wing", 3)),

  WISDOM_AMULET("wisdom_amulet", "Wisdom Amulet", CraftSkill.JEWELRYCRAFTING, 15, 1, 1,
      ing("spruce_plank", 4), ing("green_cloth", 3), ing("snake_hide", 3), ing("jasper_crystal", 1)),

  BATTLESTAFF("battlestaff", "Battlestaff", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 6), ing("steel_bar", 4), ing("wolf_bone", 3), ing("blue_slimeball", 5)),

  DREADFUL_AMULET("dreadful_amulet", "Dreadful Amulet", CraftSkill.JEWELRYCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 6), ing("ogre_eye", 4), ing("hard_leather", 2), ing("king_slimeball", 2)),

  DREADFUL_RING("dreadful_ring", "Dreadful Ring", CraftSkill.JEWELRYCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("ogre_eye", 4), ing("cyclops_eye", 3), ing("jasper_crystal", 1)),

  FOREST_WHIP("forest_whip", "Forest Whip", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("king_slimeball", 2), ing("wolf_hair", 5), ing("ogre_eye", 4), ing("hardwood_plank", 4)),

  HARD_LEATHER_ARMOR("hard_leather_armor", "Hard Leather Armor", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("hard_leather", 6), ing("spider_leg", 3), ing("pig_skin", 2), ing("steel_bar", 4)),

  HARD_LEATHER_BOOTS("hard_leather_boots", "Hard Leather Boots", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 5), ing("green_cloth", 2), ing("hard_leather", 3), ing("pig_skin", 5)),

  HARD_LEATHER_HELMET("hard_leather_helmet", "Hard Leather Helmet", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 7), ing("hard_leather", 4), ing("wolf_bone", 2), ing("astralyte_crystal", 1)),

  HARD_LEATHER_PANTS("hard_leather_pants", "Hard Leather Pants", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("steel_bar", 6), ing("green_cloth", 2), ing("hard_leather", 5), ing("skeleton_skull", 2)),

  HUNTING_BOW("hunting_bow", "Hunting Bow", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 5), ing("green_cloth", 4), ing("ogre_skin", 3), ing("pig_skin", 3)),

  MAGIC_WIZARD_HAT("magic_wizard_hat", "Magic Wizard Hat", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("ogre_skin", 2), ing("wolf_hair", 4), ing("blue_slimeball", 10), ing("hardwood_plank", 2)),

  RING_OF_CHANCE("ring_of_chance", "Ring of Chance", CraftSkill.JEWELRYCRAFTING, 20, 1, 1,
      ing("jasper_crystal", 1), ing("steel_bar", 6), ing("king_slimeball", 4), ing("pig_skin", 4)),

  SHURIKEN("shuriken", "Shuriken", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("steel_bar", 5), ing("wolf_bone", 4), ing("ogre_skin", 3), ing("flying_wing", 3)),

  SKELETON_ARMOR("skeleton_armor", "Skeleton Armor", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("skeleton_bone", 6), ing("wolf_bone", 3), ing("pig_skin", 2), ing("steel_bar", 4)),

  SKELETON_HELMET("skeleton_helmet", "Skeleton Helmet", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("skeleton_skull", 1), ing("skeleton_bone", 3), ing("wolf_bone", 2), ing("iron_bar", 7)),

  SKELETON_PANTS("skeleton_pants", "Skeleton Pants", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("wolf_bone", 3), ing("skeleton_bone", 3), ing("wolf_hair", 2), ing("ash_plank", 7)),

  SKULL_AMULET("skull_amulet", "Skull Amulet", CraftSkill.JEWELRYCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 7), ing("skeleton_skull", 3), ing("king_slimeball", 2), ing("snake_hide", 3)),

  SKULL_RING("skull_ring", "Skull Ring", CraftSkill.JEWELRYCRAFTING, 20, 1, 1,
      ing("steel_bar", 4), ing("wolf_bone", 4), ing("skeleton_skull", 1), ing("jasper_crystal", 2)),

  SKULL_STAFF("skull_staff", "Skull Staff", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("skeleton_skull", 1), ing("skeleton_bone", 4), ing("steel_bar", 5), ing("hardwood_plank", 5)),

  SLIME_SHIELD("slime_shield", "Slime Shield", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 6), ing("king_slimeball", 6), ing("cloth", 3)),

  SMALL_ANTIDOTE("small_antidote", "Small Antidote", CraftSkill.ALCHEMY, 20, 1, 1,
      ing("milk_bucket", 1), ing("sap", 1), ing("nettle_leaf", 1)),

  SNAKESKIN_BOOTS("snakeskin_boots", "Snakeskin Boots", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 5), ing("spider_leg", 2), ing("snakeskin", 2), ing("green_cloth", 2)),

  STEEL_ARMOR("steel_armor", "Steel Armor", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("green_cloth", 2), ing("cloth", 3), ing("spider_leg", 3)),

  STEEL_AXE("steel_axe", "Steel Axe", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("ogre_eye", 4), ing("flying_wing", 2), ing("astralyte_crystal", 2)),

  STEEL_BATTLEAXE("steel_battleaxe", "Steel Battleaxe", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("steel_bar", 4), ing("hardwood_plank", 4), ing("skeleton_bone", 4), ing("wolf_hair", 4)),

  STEEL_BOOTS("steel_boots", "Steel Boots", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("hardwood_plank", 5), ing("steel_bar", 5), ing("snakeskin", 2), ing("ogre_skin", 3)),

  STEEL_FISHING_ROD("steel_fishing_rod", "Steel Fishing Rod", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("ogre_skin", 3), ing("green_cloth", 3), ing("astralyte_crystal", 2)),

  STEEL_GLOVES("steel_gloves", "Steel Gloves", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("pig_skin", 3), ing("skeleton_bone", 3), ing("astralyte_crystal", 2)),

  STEEL_HELM("steel_helm", "Steel Helm", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("steel_bar", 8), ing("ogre_skin", 3), ing("wolf_bone", 2), ing("cloth", 3)),

  STEEL_LEGS_ARMOR("steel_legs_armor", "Steel Legs Armor", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("skeleton_skull", 2), ing("cloth", 3), ing("king_slimeball", 3)),

  STEEL_PICKAXE("steel_pickaxe", "Steel Pickaxe", CraftSkill.WEAPONCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("pig_skin", 3), ing("spider_leg", 3), ing("astralyte_crystal", 2)),

  STEEL_RING("steel_ring", "Steel Ring", CraftSkill.JEWELRYCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("skeleton_bone", 3), ing("hard_leather", 2), ing("snake_hide", 3)),

  TROMATISING_MASK("tromatising_mask", "Tromatising Mask", CraftSkill.GEARCRAFTING, 20, 1, 1,
      ing("steel_bar", 7), ing("pig_skin", 3), ing("cloth", 2), ing("skeleton_bone", 3)),

  DREADFUL_STAFF("dreadful_staff", "Dreadful Staff", CraftSkill.WEAPONCRAFTING, 25, 1, 1,
      ing("cyclops_eye", 4), ing("vampire_blood", 5), ing("hardwood_plank", 6), ing("jasper_crystal", 1)),

  EMERALD_AMULET("emerald_amulet", "Emerald Amulet", CraftSkill.JEWELRYCRAFTING, 25, 1, 1,
      ing("hardwood_plank", 8), ing("emerald", 1), ing("wolf_hair", 5), ing("jasper_crystal", 2)),

  LIZARD_SKIN_ARMOR("lizard_skin_armor", "Lizard Skin Armor", CraftSkill.GEARCRAFTING, 25, 1, 1,
      ing("lizard_skin", 5), ing("dead_wood_plank", 5), ing("vampire_tooth", 4), ing("jasper_crystal", 2)),

  PIGGY_ARMOR("piggy_armor", "Piggy Armor", CraftSkill.GEARCRAFTING, 25, 1, 1,
      ing("pig_skin", 5), ing("dead_wood_plank", 5), ing("ogre_skin", 4), ing("jasper_crystal", 2)),

  PIGGY_HELMET("piggy_helmet", "Piggy Helmet", CraftSkill.GEARCRAFTING, 25, 1, 1,
      ing("steel_bar", 6), ing("pig_skin", 6), ing("cyclops_eye", 2), ing("vampire_blood", 2)),

  PIGGY_PANTS("piggy_pants", "Piggy Pants", CraftSkill.GEARCRAFTING, 25, 1, 1,
      ing("pig_skin", 5), ing("hardwood_plank", 5), ing("snakeskin", 3), ing("jasper_crystal", 2)),

  RUBY_AMULET("ruby_amulet", "Ruby Amulet", CraftSkill.JEWELRYCRAFTING, 25, 1, 1,
      ing("hardwood_plank", 8), ing("ruby", 1), ing("wolf_hair", 5), ing("jasper_crystal", 2)),

  SAPPHIRE_AMULET("sapphire_amulet", "Sapphire Amulet", CraftSkill.JEWELRYCRAFTING, 25, 1, 1,
      ing("hardwood_plank", 8), ing("sapphire", 1), ing("wolf_hair", 5), ing("jasper_crystal", 2)),

  SKULL_WAND("skull_wand", "Skull Wand", CraftSkill.WEAPONCRAFTING, 25, 1, 1,
      ing("hardwood_plank", 4), ing("skeleton_skull", 3), ing("vampire_tooth", 2), ing("spider_leg", 3), ing("jasper_crystal", 1)),

  STORMFORGED_ARMOR("stormforged_armor", "Stormforged Armor", CraftSkill.GEARCRAFTING, 25, 1, 1,
      ing("lizard_skin", 5), ing("dead_wood_plank", 5), ing("ogre_eye", 4), ing("jasper_crystal", 2)),

  TOPAZ_AMULET("topaz_amulet", "Topaz Amulet", CraftSkill.JEWELRYCRAFTING, 25, 1, 1,
      ing("hardwood_plank", 8), ing("topaz", 1), ing("wolf_hair", 5), ing("jasper_crystal", 2)),

  VAMPIRE_BOW("vampire_bow", "Vampire Bow", CraftSkill.WEAPONCRAFTING, 25, 1, 1,
      ing("steel_bar", 4), ing("vampire_blood", 4), ing("spider_leg", 4), ing("vermin_leather", 2), ing("magical_cure", 1)),

  ANTIDOTE("antidote", "Antidote", CraftSkill.ALCHEMY, 30, 1, 1,
      ing("strangold_bar", 2), ing("maple_sap", 1), ing("glowstem_leaf", 1)),

  CONJURER_CLOAK("conjurer_cloak", "Conjurer Cloak", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("obsidian_bar", 6), ing("cyclops_eye", 5), ing("owlbear_hair", 4), ing("demon_horn", 4), ing("enchanted_fabric", 1)),

  CONJURER_SKIRT("conjurer_skirt", "Conjurer Skirt", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("obsidian_bar", 6), ing("vampire_tooth", 4), ing("owlbear_claw", 3), ing("lizard_eye", 4), ing("vermin_leather", 3)),

  ELDERWOOD_STAFF("elderwood_staff", "Elderwood Staff", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 5), ing("lizard_skin", 4), ing("cyclops_eye", 5), ing("red_cloth", 3), ing("skeleton_skull", 3)),

  EMERALD_RING("emerald_ring", "Emerald Ring", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("obsidian_bar", 4), ing("emerald", 1), ing("vampire_blood", 5), ing("magical_cure", 2)),

  ENCHANTED_BOW("enchanted_bow", "Enchanted Bow", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("spider_leg", 3), ing("demon_horn", 2), ing("ogre_eye", 4), ing("red_cloth", 3)),

  FLYING_BOOTS("flying_boots", "Flying Boots", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 8), ing("hard_leather", 3), ing("demoniac_dust", 5), ing("owlbear_hair", 3), ing("magical_cure", 1)),

  GOLD_AXE("gold_axe", "Gold Axe", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 2), ing("gold_bar", 7), ing("ruby", 1), ing("magical_cure", 2), ing("red_cloth", 3)),

  GOLD_BOOTS("gold_boots", "Gold Boots", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("vampire_blood", 4), ing("lizard_eye", 3), ing("owlbear_hair", 4), ing("magical_cure", 1)),

  GOLD_FISHING_ROD("gold_fishing_rod", "Gold Fishing Rod", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 2), ing("gold_bar", 7), ing("sapphire", 1), ing("magical_cure", 2), ing("owlbear_claw", 3)),

  GOLD_HELM("gold_helm", "Gold Helm", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("demon_horn", 2), ing("imp_tail", 3), ing("vampire_tooth", 3), ing("owlbear_hair", 4)),

  GOLD_MASK("gold_mask", "Gold Mask", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("red_cloth", 2), ing("owlbear_claw", 4), ing("demon_horn", 2), ing("skeleton_skull", 4)),

  GOLD_PICKAXE("gold_pickaxe", "Gold Pickaxe", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 2), ing("gold_bar", 7), ing("topaz", 1), ing("magical_cure", 2), ing("demon_horn", 2)),

  GOLD_PLATEBODY("gold_platebody", "Gold Platebody", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("owlbear_hair", 3), ing("red_cloth", 3), ing("demoniac_dust", 4), ing("demon_horn", 2)),

  GOLD_PLATELEGS("gold_platelegs", "Gold Platelegs", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("lizard_eye", 3), ing("vermin_leather", 3), ing("vampire_tooth", 4), ing("ogre_skin", 2)),

  GOLD_RING("gold_ring", "Gold Ring", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("dead_wood_plank", 3), ing("wolf_bone", 3), ing("vampire_blood", 3), ing("skeleton_bone", 3)),

  GOLD_SHIELD("gold_shield", "Gold Shield", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 7), ing("gold_bar", 7), ing("demon_horn", 4), ing("sapphire", 1), ing("magical_cure", 1)),

  GOLD_SWORD("gold_sword", "Gold Sword", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("imp_tail", 4), ing("red_cloth", 3), ing("demon_horn", 2), ing("dead_wood_plank", 3)),

  GOLDEN_GLOVES("golden_gloves", "Golden Gloves", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 7), ing("obsidian_bar", 2), ing("emerald", 1), ing("magical_cure", 2), ing("demoniac_dust", 3)),

  HEALTH_POTION("health_potion", "Health Potion", CraftSkill.ALCHEMY, 30, 1, 1,
      ing("nettle_leaf", 2), ing("sunflower", 1), ing("sap", 1)),

  LIZARD_BOOTS("lizard_boots", "Lizard Boots", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 8), ing("lizard_skin", 4), ing("imp_tail", 4), ing("vermin_leather", 3), ing("magical_cure", 1)),

  LOST_AMULET("lost_amulet", "Lost Amulet", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("obsidian_bar", 4), ing("cyclops_eye", 4), ing("imp_tail", 3), ing("red_cloth", 3)),

  OBSIDIAN_ARMOR("obsidian_armor", "Obsidian Armor", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("obsidian_bar", 6), ing("ruby", 1), ing("spider_leg", 5), ing("demon_horn", 4), ing("demoniac_dust", 4)),

  OBSIDIAN_BATTLEAXE("obsidian_battleaxe", "Obsidian Battleaxe", CraftSkill.WEAPONCRAFTING, 30, 1, 1,
      ing("obsidian_bar", 7), ing("dead_wood_plank", 4), ing("lizard_skin", 3), ing("cyclops_eye", 3), ing("imp_tail", 3)),

  OBSIDIAN_HELMET("obsidian_helmet", "Obsidian Helmet", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("obsidian_bar", 6), ing("emerald", 1), ing("owlbear_hair", 3), ing("lizard_skin", 5), ing("vampire_tooth", 5)),

  OBSIDIAN_LEGS_ARMOR("obsidian_legs_armor", "Obsidian Legs Armor", CraftSkill.GEARCRAFTING, 30, 1, 1,
      ing("obsidian_bar", 6), ing("sapphire", 1), ing("owlbear_claw", 3), ing("lizard_eye", 5), ing("red_cloth", 5)),

  PROSPECTING_AMULET("prospecting_amulet", "Prospecting Amulet", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("dead_wood_plank", 4), ing("spider_leg", 3), ing("ogre_skin", 6), ing("owlbear_hair", 4), ing("magical_cure", 1)),

  ROYAL_SKELETON_RING("royal_skeleton_ring", "Royal Skeleton Ring", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("owlbear_claw", 3), ing("vampire_tooth", 3), ing("spider_leg", 2), ing("ogre_skin", 4)),

  RUBY_RING("ruby_ring", "Ruby Ring", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("obsidian_bar", 4), ing("ruby", 1), ing("vampire_blood", 5), ing("magical_cure", 2)),

  SAPPHIRE_RING("sapphire_ring", "Sapphire Ring", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("obsidian_bar", 4), ing("sapphire", 1), ing("vampire_blood", 5), ing("magical_cure", 2)),

  TOPAZ_RING("topaz_ring", "Topaz Ring", CraftSkill.JEWELRYCRAFTING, 30, 1, 1,
      ing("gold_bar", 8), ing("obsidian_bar", 4), ing("topaz", 1), ing("vampire_blood", 5), ing("magical_cure", 2)),

  ANCESTRAL_TALISMAN("ancestral_talisman", "Ancestral Talisman", CraftSkill.JEWELRYCRAFTING, 35, 1, 1,
      ing("magical_plank", 8), ing("diamond", 1), ing("cursed_book", 4), ing("goblin_tooth", 5), ing("priestess_orb", 2)),

  ANCIENT_JEAN("ancient_jean", "Ancient Jean", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("magical_plank", 6), ing("magical_cure", 2), ing("goblin_guard_foot", 3), ing("obsidian_bar", 4), ing("lizard_skin", 5)),

  CORRUPTED_STONE_AMULET("corrupted_stone_amulet", "Corrupted Stone Amulet", CraftSkill.JEWELRYCRAFTING, 35, 1, 1,
      ing("strangold_bar", 6), ing("malefic_cloth", 2), ing("corrupted_stone", 5), ing("orc_bone", 5), ing("magical_cure", 2)),

  CURSED_HAT("cursed_hat", "Cursed Hat", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("cursed_plank", 8), ing("malefic_cloth", 2), ing("cursed_book", 4), ing("owlbear_hair", 4), ing("diamond", 1), ing("enchanted_fabric", 1)),

  CURSED_SCEPTRE("cursed_sceptre", "Cursed Sceptre", CraftSkill.WEAPONCRAFTING, 35, 1, 1,
      ing("magical_plank", 8), ing("cursed_book", 3), ing("corrupted_stone", 3), ing("malefic_cloth", 3), ing("diamond", 1), ing("magical_cure", 2)),

  DIAMOND_AMULET("diamond_amulet", "Diamond Amulet", CraftSkill.JEWELRYCRAFTING, 35, 1, 1,
      ing("magical_plank", 8), ing("diamond", 1), ing("obsidian_bar", 5), ing("cursed_book", 4), ing("magical_cure", 2)),

  DIAMOND_SWORD("diamond_sword", "Diamond Sword", CraftSkill.WEAPONCRAFTING, 35, 1, 1,
      ing("magical_plank", 7), ing("goblin_eye", 3), ing("spider_leg", 3), ing("corrupted_stone", 3), ing("diamond", 1), ing("magical_cure", 2)),

  DREADFUL_ARMOR("dreadful_armor", "Dreadful Armor", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("obsidian_bar", 8), ing("priestess_orb", 2), ing("ogre_eye", 5), ing("goblin_guard_foot", 4), ing("enchanted_fabric", 1)),

  DREADFUL_BATTLEAXE("dreadful_battleaxe", "Dreadful Battleaxe", CraftSkill.WEAPONCRAFTING, 35, 1, 1,
      ing("magical_plank", 7), ing("lizard_eye", 4), ing("goblin_eye", 3), ing("goblin_guard_foot", 3), ing("jasper_crystal", 3)),

  DREADFUL_SHIELD("dreadful_shield", "Dreadful Shield", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("obsidian_bar", 8), ing("imp_tail", 5), ing("cursed_book", 5), ing("ruby", 1), ing("astralyte_crystal", 1)),

  ENCHANTER_BOOTS("enchanter_boots", "Enchanter Boots", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("magical_plank", 8), ing("priestess_orb", 2), ing("lizard_eye", 4), ing("vermin_leather", 5), ing("enchanted_fabric", 1)),

  ENCHANTER_PANTS("enchanter_pants", "Enchanter Pants", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("magical_plank", 8), ing("demon_horn", 2), ing("cursed_book", 3), ing("owlbear_claw", 4), ing("spider_leg", 2), ing("enchanted_fabric", 1)),

  JESTER_HAT("jester_hat", "Jester Hat", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("cursed_plank", 8), ing("vampire_tooth", 2), ing("cursed_book", 3), ing("owlbear_hair", 3), ing("goblin_guard_foot", 3), ing("enchanted_fabric", 1)),

  MAGIC_BOW("magic_bow", "Magic Bow", CraftSkill.WEAPONCRAFTING, 35, 1, 1,
      ing("magical_plank", 7), ing("corrupted_stone", 3), ing("lizard_skin", 3), ing("wolf_hair", 3), ing("sapphire", 1), ing("magical_cure", 2)),

  MALEFIC_ARMOR("malefic_armor", "Malefic Armor", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("magical_plank", 8), ing("malefic_cloth", 2), ing("owlbear_hair", 4), ing("corrupted_stone", 3), ing("magical_cure", 2)),

  MALEFIC_RING("malefic_ring", "Malefic Ring", CraftSkill.JEWELRYCRAFTING, 35, 1, 1,
      ing("strangold_bar", 8), ing("cursed_plank", 4), ing("ruby", 2), ing("lizard_eye", 2), ing("owlbear_claw", 2), ing("astralyte_crystal", 2)),

  MASTERFUL_NECKLACE("masterful_necklace", "Masterful Necklace", CraftSkill.JEWELRYCRAFTING, 35, 1, 1,
      ing("strangold_bar", 6), ing("priestess_orb", 2), ing("corrupted_stone", 5), ing("goblin_tooth", 5), ing("astralyte_crystal", 2)),

  STRANGOLD_ARMOR("strangold_armor", "Strangold Armor", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("strangold_bar", 8), ing("owlbear_hair", 3), ing("corrupted_stone", 3), ing("demon_horn", 4), ing("magical_cure", 2)),

  STRANGOLD_HELMET("strangold_helmet", "Strangold Helmet", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("strangold_bar", 7), ing("demoniac_dust", 4), ing("corrupted_stone", 3), ing("lizard_skin", 4), ing("diamond", 1), ing("magical_cure", 1)),

  STRANGOLD_LEGS_ARMOR("strangold_legs_armor", "Strangold Legs Armor", CraftSkill.GEARCRAFTING, 35, 1, 1,
      ing("strangold_bar", 8), ing("magical_cure", 2), ing("cursed_book", 3), ing("vermin_leather", 4), ing("red_cloth", 3)),

  STRANGOLD_SWORD("strangold_sword", "Strangold Sword", CraftSkill.WEAPONCRAFTING, 35, 1, 1,
      ing("strangold_bar", 7), ing("goblin_tooth", 3), ing("goblin_guard_foot", 3), ing("corrupted_stone", 4), ing("magical_cure", 2)),

  AIR_RES_POTION("air_res_potion", "Air Res Potion", CraftSkill.ALCHEMY, 40, 1, 1,
      ing("green_slimeball", 2), ing("maple_sap", 1), ing("glowstem_leaf", 1)),

  AIR_SHIELD("air_shield", "Air Shield", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("strangold_bar", 6), ing("emerald", 1), ing("green_slimeball", 20), ing("wolfrider_ponytail", 5), ing("rosenblood_elixir", 1)),

  BATWING_HELMET("batwing_helmet", "Batwing Helmet", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("strangold_bar", 6), ing("topaz", 1), ing("rosenblood_elixir", 1), ing("bat_wing", 5), ing("cursed_flask", 5), ing("enchanted_fabric", 2)),

  BLOODBLADE("bloodblade", "Bloodblade", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("goblin_tooth", 5), ing("wolfrider_hair", 4), ing("broken_sword", 1), ing("astralyte_crystal", 2)),

  CELEST_RING("celest_ring", "Celest Ring", CraftSkill.JEWELRYCRAFTING, 40, 1, 1,
      ing("strangold_bar", 9), ing("wolfrider_hair", 3), ing("ruby", 2), ing("sapphire", 2), ing("rosenblood_elixir", 1), ing("astralyte_crystal", 2)),

  CULTIST_BOOTS("cultist_boots", "Cultist Boots", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("maple_plank", 7), ing("malefic_cloth", 2), ing("hellhound_hair", 4), ing("orc_bone", 5), ing("enchanted_fabric", 2)),

  CULTIST_CLOAK("cultist_cloak", "Cultist Cloak", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("maple_plank", 8), ing("malefic_cloth", 2), ing("red_cloth", 3), ing("hellhound_collar", 4), ing("astralyte_crystal", 2)),

  CULTIST_HAT("cultist_hat", "Cultis Hat", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("maple_plank", 8), ing("malefic_cloth", 1), ing("hellhound_hair", 5), ing("orc_skin", 4), ing("astralyte_crystal", 2)),

  CULTIST_PANTS("cultist_pants", "Cultist Pants", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("cursed_plank", 8), ing("malefic_cloth", 2), ing("wolfrider_ponytail", 3), ing("hellhound_hair", 3), ing("magical_cure", 4)),

  DIVINITY_RING("divinity_ring", "Divinity Ring", CraftSkill.JEWELRYCRAFTING, 40, 1, 1,
      ing("strangold_bar", 9), ing("hellhound_collar", 4), ing("topaz", 2), ing("sapphire", 2), ing("rosenblood_elixir", 1), ing("astralyte_crystal", 2)),

  EARTH_RES_POTION("earth_res_potion", "Earth Res Potion", CraftSkill.ALCHEMY, 40, 1, 1,
      ing("yellow_slimeball", 2), ing("maple_sap", 1), ing("glowstem_leaf", 1)),

  EARTH_SHIELD("earth_shield", "Earth Shield", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("strangold_bar", 6), ing("topaz", 1), ing("yellow_slimeball", 20), ing("bat_wing", 3), ing("rosenblood_elixir", 1)),

  ENCHANTED_BOOST_POTION("enchanted_boost_potion", "Enchanted Boost Potion", CraftSkill.ALCHEMY, 40, 1, 1,
      ing("glowstem_leaf", 2), ing("bat_wing", 1), ing("magic_sap", 1)),

  ETERNITY_RING("eternity_ring", "Eternity Ring", CraftSkill.JEWELRYCRAFTING, 40, 1, 1,
      ing("strangold_bar", 9), ing("wolfrider_ponytail", 3), ing("topaz", 2), ing("emerald", 2), ing("rosenblood_elixir", 1), ing("astralyte_crystal", 2)),

  FIRE_RES_POTION("fire_res_potion", "Fire Res Potion", CraftSkill.ALCHEMY, 40, 1, 1,
      ing("red_slimeball", 2), ing("maple_sap", 1), ing("glowstem_leaf", 1)),

  FIRE_SHIELD("fire_shield", "Fire Shield", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("strangold_bar", 6), ing("ruby", 1), ing("red_slimeball", 20), ing("orc_skin", 5), ing("rosenblood_elixir", 1)),

  HEALTH_BOOST_POTION("health_boost_potion", "Health Boost Potion", CraftSkill.ALCHEMY, 40, 1, 1,
      ing("shrimp", 1), ing("sap", 1), ing("nettle_leaf", 2)),

  HORK_HELMET("hork_helmet", "Hork Helmet", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("strangold_bar", 7), ing("orc_skin", 4), ing("owlbear_claw", 3), ing("bat_wing", 3), ing("dark_essence", 3)),

  LIGHTNING_SWORD("lightning_sword", "Lightning Sword", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("maple_plank", 7), ing("goblin_eye", 5), ing("hellhound_hair", 4), ing("broken_sword", 1), ing("magical_cure", 3)),

  MAPLE_SYRUP("maple_syrup", "Maple Syrup", CraftSkill.COOKING, 40, 1, 1,
      ing("maple_sap", 2)),

  MITHRIL_AXE("mithril_axe", "Mithril Axe", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("owlbear_claw", 3), ing("wolfrider_ponytail", 3), ing("dark_essence", 3), ing("vampire_tooth", 3)),

  MITHRIL_BOOTS("mithril_boots", "Mithril Boots", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 7), ing("diamond", 1), ing("hellhound_hair", 5), ing("goblin_eye", 5), ing("enchanted_fabric", 2)),

  MITHRIL_FISHING_ROD("mithril_fishing_rod", "Mithril Fishing Rod", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("cursed_plank", 3), ing("hellhound_hair", 3), ing("cursed_flask", 3), ing("goblin_guard_foot", 3)),

  MITHRIL_GLOVES("mithril_gloves", "Mithril Gloves", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("cursed_book", 3), ing("hellhound_collar", 3), ing("imp_tail", 3), ing("cursed_flask", 3)),

  MITHRIL_HELM("mithril_helm", "Mithril Helm", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("diamond", 1), ing("goblin_tooth", 3), ing("wolfrider_ponytail", 3), ing("jasper_crystal", 5)),

  MITHRIL_PICKAXE("mithril_pickaxe", "Mithril Pickaxe", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("owlbear_claw", 3), ing("broken_sword", 1), ing("dark_essence", 3), ing("vampire_blood", 5)),

  MITHRIL_PLATEBODY("mithril_platebody", "Mithril Platebody", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("goblin_guard_foot", 3), ing("bat_wing", 3), ing("goblin_tooth", 4), ing("enchanted_fabric", 2)),

  MITHRIL_PLATELEGS("mithril_platelegs", "Mithril Platelegs", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("lizard_eye", 2), ing("demoniac_dust", 3), ing("vampire_tooth", 3), ing("owlbear_hair", 4)),

  MITHRIL_RING("mithril_ring", "Mithril Ring", CraftSkill.JEWELRYCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("lizard_eye", 2), ing("wolfrider_hair", 3), ing("dark_essence", 3), ing("hellhound_hair", 4)),

  MITHRIL_SHIELD("mithril_shield", "Mithril Shield", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 7), ing("lizard_skin", 3), ing("cyclops_eye", 3), ing("hellhound_hair", 3), ing("goblin_eye", 4)),

  MITHRIL_SWORD("mithril_sword", "Mithril Sword", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("mithril_bar", 7), ing("corrupted_stone", 3), ing("goblin_guard_foot", 4), ing("wolfrider_hair", 3), ing("broken_sword", 1), ing("astralyte_crystal", 2)),

  SACRED_RING("sacred_ring", "Sacred Ring", CraftSkill.JEWELRYCRAFTING, 40, 1, 1,
      ing("strangold_bar", 9), ing("hellhound_collar", 4), ing("ruby", 2), ing("emerald", 2), ing("rosenblood_elixir", 1), ing("astralyte_crystal", 2)),

  WATER_RES_POTION("water_res_potion", "Water Res Potion", CraftSkill.ALCHEMY, 40, 1, 1,
      ing("blue_slimeball", 2), ing("maple_sap", 1), ing("glowstem_leaf", 1)),

  WATER_SHIELD("water_shield", "Water Shield", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("strangold_bar", 6), ing("sapphire", 1), ing("blue_slimeball", 20), ing("hellhound_collar", 3), ing("rosenblood_elixir", 1)),

  WHITE_KNIGHT_ARMOR("white_knight_armor", "White Knight Armor", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("corrupted_stone", 3), ing("hellhound_hair", 3), ing("wolfrider_ponytail", 4), ing("enchanted_fabric", 2)),

  WHITE_KNIGHT_HELMET("white_knight_helmet", "White Knight Helmet", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("diamond", 1), ing("hellhound_collar", 4), ing("orc_bone", 3), ing("owlbear_claw", 4)),

  WHITE_KNIGHT_PANTS("white_knight_pants", "White Knight Pants", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("hellhound_hair", 3), ing("wolfrider_hair", 3), ing("goblin_tooth", 4), ing("astralyte_crystal", 2)),

  WHITE_KNIGHT_SHIELD("white_knight_shield", "White Knight Shield", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("maple_plank", 7), ing("lizard_eye", 3), ing("wolfrider_hair", 3), ing("hellhound_hair", 3), ing("goblin_eye", 4)),

  WRATHARMOR("wratharmor", "Wratharmor", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("rosenblood_elixir", 1), ing("goblin_eye", 4), ing("hellhound_collar", 5), ing("enchanted_fabric", 2)),

  WRATHELMET("wrathelmet", "Wrathelmet", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("rosenblood_elixir", 1), ing("wolfrider_hair", 4), ing("cursed_book", 5), ing("astralyte_crystal", 2)),

  WRATHPANTS("wrathpants", "Wrathpants", CraftSkill.GEARCRAFTING, 40, 1, 1,
      ing("mithril_bar", 8), ing("priestess_orb", 2), ing("goblin_tooth", 5), ing("hellhound_collar", 3), ing("enchanted_fabric", 2)),

  WRATHSWORD("wrathsword", "Wrathsword", CraftSkill.WEAPONCRAFTING, 40, 1, 1,
      ing("mithril_bar", 7), ing("orc_bone", 5), ing("bat_wing", 4), ing("broken_sword", 1), ing("magical_cure", 3)),

  BLADE_OF_HELL("blade_of_hell", "Blade of Hell", CraftSkill.WEAPONCRAFTING, 45, 1, 1,
      ing("strangold_bar", 11), ing("lava_bucket", 4), ing("broken_sword", 2), ing("book_from_hell", 1), ing("orc_bone", 6)),

  BOW_FROM_HELL("bow_from_hell", "Bow from Hell", CraftSkill.WEAPONCRAFTING, 45, 1, 1,
      ing("magical_plank", 10), ing("efreet_cloth", 3), ing("demon_horn", 4), ing("book_from_hell", 1), ing("imp_tail", 5)),

  DARKFORGED_BOOTS("darkforged_boots", "Darkforged Boots", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("maple_plank", 10), ing("dark_essence", 5), ing("sand_snakeskin", 2), ing("lava_bucket", 4), ing("astralyte_crystal", 3)),

  DARKFORGED_HELMET("darkforged_helmet", "Darkforged Helmet", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("mithril_bar", 10), ing("dark_essence", 5), ing("marauder_hand", 2), ing("grimlet_bone", 4), ing("enchanted_fabric", 3)),

  DARKFORGED_PLATE("darkforged_plate", "Darkforged Plate", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("cursed_plank", 10), ing("efreet_cloth", 5), ing("rosenblood_elixir", 1), ing("grimlet_bone", 4), ing("sand_snake_poison", 4)),

  DARKFORGED_SHIELD("darkforged_shield", "Darkforged Shield", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("cursed_plank", 10), ing("marauder_hand", 4), ing("diamond", 1), ing("bat_wing", 4), ing("dark_essence", 5)),

  DEMONIAC_DAGGER("demoniac_dagger", "Demoniac Dagger", CraftSkill.WEAPONCRAFTING, 45, 1, 1,
      ing("strangold_bar", 10), ing("efreet_cloth", 3), ing("obsidian_bar", 5), ing("book_from_hell", 1), ing("cursed_book", 5)),

  DEMONIAC_SHIELD("demoniac_shield", "Demoniac Shield", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("cursed_plank", 10), ing("grimlet_bone", 3), ing("book_from_hell", 1), ing("bat_wing", 4), ing("goblin_tooth", 6)),

  ENCHANTED_ANTIDOTE("enchanted_antidote", "Enchanted Antidote", CraftSkill.ALCHEMY, 45, 1, 1,
      ing("strangold_bar", 2), ing("magic_sap", 1), ing("torch_cactus_flower", 1)),

  ENCHANTED_HEALTH_POTION("enchanted_health_potion", "Enchanted Health Potion", CraftSkill.ALCHEMY, 45, 1, 1,
      ing("glowstem_leaf", 2), ing("sunflower", 1), ing("magic_sap", 1)),

  HELL_ARMOR("hell_armor", "Hell Armor", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("cursed_plank", 10), ing("efreet_cloth", 5), ing("rosenblood_elixir", 1), ing("grimlet_bone", 3), ing("demon_horn", 5)),

  HELL_HELMET("hell_helmet", "Hell Helmet", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("maple_plank", 10), ing("wolfrider_ponytail", 3), ing("lava_bucket", 5), ing("orc_skin", 4), ing("enchanted_fabric", 3)),

  HELL_LEGS_ARMOR("hell_legs_armor", "Hell Legs Armor", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("maple_plank", 10), ing("grimlet_bone", 4), ing("lava_bucket", 4), ing("malefic_cloth", 2), ing("hellhound_collar", 4)),

  HELL_REAPER("hell_reaper", "Hell Reaper", CraftSkill.WEAPONCRAFTING, 45, 1, 1,
      ing("mithril_bar", 11), ing("efreet_cloth", 4), ing("broken_sword", 3), ing("book_from_hell", 1), ing("grimlet_bone", 5)),

  HELL_RING("hell_ring", "Hell Ring", CraftSkill.JEWELRYCRAFTING, 45, 1, 1,
      ing("strangold_bar", 10), ing("grimlet_bone", 4), ing("diamond", 2), ing("efreet_cloth", 3), ing("goblin_eye", 5)),

  HELL_STAFF("hell_staff", "Hell Staff", CraftSkill.WEAPONCRAFTING, 45, 1, 1,
      ing("cursed_plank", 10), ing("efreet_cloth", 3), ing("obsidian_bar", 5), ing("book_from_hell", 1), ing("imp_tail", 5)),

  MESH_ARMOR("mesh_armor", "Mesh Armor", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("mithril_bar", 10), ing("efreet_cloth", 5), ing("rosenblood_elixir", 1), ing("grimlet_bone", 3), ing("hellhound_hair", 5)),

  MESH_LEGS_ARMOR("mesh_legs_armor", "Mesh Legs Armor", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("mithril_bar", 10), ing("efreet_cloth", 5), ing("rosenblood_elixir", 1), ing("orc_skin", 4), ing("dark_essence", 4)),

  SAND_SNAKESKIN_ARMOR("sand_snakeskin_armor", "Sand Snakeskin Armor", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("maple_plank", 10), ing("sand_snakeskin", 5), ing("enchanted_fabric", 2), ing("cursed_flask", 3), ing("wolfrider_ponytail", 4)),

  SAND_SNAKESKIN_BANDANA("sand_snakeskin_bandana", "Sand Snakeskin Bandana", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("maple_plank", 10), ing("sand_snakeskin", 5), ing("enchanted_fabric", 2), ing("grimlet_bone", 3), ing("marauder_hand", 4)),

  SAND_SNAKESKIN_BOOTS("sand_snakeskin_boots", "Sand Snakeskin Boots", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("maple_plank", 10), ing("dark_essence", 2), ing("sand_snakeskin", 5), ing("bat_wing", 4), ing("astralyte_crystal", 3)),

  SAND_SNAKESKIN_PANTS("sand_snakeskin_pants", "Sand Snakeskin Pants", CraftSkill.GEARCRAFTING, 45, 1, 1,
      ing("maple_plank", 10), ing("sand_snakeskin", 5), ing("marauder_hand", 2), ing("grimlet_bone", 3), ing("wolfrider_hair", 4)),

  ADAMANTITE_AXE("adamantite_axe", "Adamantite Axe", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 10), ing("lava_bucket", 4), ing("adventurer_skull", 3), ing("cursed_flask", 3), ing("golden_dust", 4), ing("astralyte_crystal", 2)),

  ADAMANTITE_BOOTS("adamantite_boots", "Adamantite Boots", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("wolfrider_hair", 4), ing("dusk_beetle_shell", 4), ing("diamond", 1), ing("golden_dust", 3), ing("enchanted_fabric", 2)),

  ADAMANTITE_FISHING_ROD("adamantite_fishing_rod", "Adamantite Fishing Rod", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 5), ing("palm_plank", 5), ing("lava_bucket", 4), ing("desert_scorpion_carapace", 4), ing("cursed_flask", 3), ing("cursed_plank", 3), ing("astralyte_crystal", 2)),

  ADAMANTITE_GLOVES("adamantite_gloves", "Adamantite Gloves", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 10), ing("efreet_cloth", 4), ing("desert_scorpion_carapace", 4), ing("goblin_guard_foot", 4), ing("rosenblood_elixir", 1), ing("astralyte_crystal", 3)),

  ADAMANTITE_MASK("adamantite_mask", "Adamantite Mask", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("duskworm_skin", 3), ing("cursed_book", 5), ing("alexandrite", 1), ing("hellhound_collar", 3), ing("jasper_crystal", 2)),

  ADAMANTITE_PICKAXE("adamantite_pickaxe", "Adamantite Pickaxe", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 10), ing("efreet_cloth", 4), ing("sand_snake_poison", 4), ing("dark_essence", 4), ing("broken_sword", 2), ing("astralyte_crystal", 2)),

  ADAMANTITE_PLATEBODY("adamantite_platebody", "Adamantite Platebody", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("desert_scorpion_carapace", 3), ing("adventurer_skull", 3), ing("malefic_cloth", 3), ing("golden_dust", 4), ing("enchanted_fabric", 2)),

  ADAMANTITE_PLATELEGS("adamantite_platelegs", "Adamantite Platelegs", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("marauder_hand", 3), ing("sand_snakeskin", 3), ing("duskworm_skin", 3), ing("golden_dust", 3), ing("enchanted_fabric", 2)),

  ADAMANTITE_RING("adamantite_ring", "Adamantite Ring", CraftSkill.JEWELRYCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("corrupted_stone", 3), ing("sand_snakeskin", 3), ing("duskworm_skin", 3), ing("golden_dust", 3), ing("desert_scorpion_carapace", 2)),

  ADAMANTITE_SHIELD("adamantite_shield", "Adamantite Shield", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("adventurer_skull", 3), ing("dusk_beetle_shell", 3), ing("bat_wing", 3), ing("hellhound_collar", 3), ing("jasper_crystal", 2)),

  ADAMANTITE_SWORD("adamantite_sword", "Adamantite Sword", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("adventurer_skull", 3), ing("dusk_beetle_shell", 3), ing("bat_wing", 3), ing("marauder_hand", 3), ing("broken_sword", 2)),

  DARK_HORNED_HELMET("dark_horned_helmet", "Dark Horned Helmet", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("hellhound_collar", 4), ing("sand_snake_poison", 3), ing("topaz", 2), ing("duskworm_skin", 4), ing("jasper_crystal", 3)),

  DESERT_WHIP("desert_whip", "Desert Whip", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("palm_plank", 12), ing("sand_snake_poison", 3), ing("efreet_cloth", 3), ing("duskworm_skin", 3), ing("desert_scorpion_carapace", 3), ing("cursed_flask", 2)),

  DUSKARMOR("duskarmor", "Duskarmor", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 10), ing("orc_skin", 4), ing("dusk_beetle_shell", 3), ing("sapphire", 2), ing("duskworm_skin", 4), ing("enchanted_fabric", 3)),

  DUSKPANTS("duskpants", "Duskpants", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("goblin_guard_foot", 4), ing("dusk_beetle_shell", 3), ing("priestess_orb", 2), ing("duskworm_skin", 4), ing("enchanted_fabric", 3)),

  DUST_AMULET("dust_amulet", "Dust Amulet", CraftSkill.JEWELRYCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 10), ing("dark_essence", 4), ing("duskworm_skin", 4), ing("demoniac_dust", 3), ing("golden_dust", 4), ing("alexandrite", 1)),

  DUST_HELMET("dust_helmet", "Dust Helmet", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("desert_scorpion_carapace", 4), ing("adventurer_skull", 3), ing("corrupted_stone", 3), ing("golden_dust", 4), ing("astralyte_crystal", 2)),

  DUST_SWORD("dust_sword", "Dust Sword", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 10), ing("marauder_hand", 4), ing("adventurer_skull", 3), ing("cursed_flask", 3), ing("golden_dust", 4), ing("broken_sword", 2)),

  ENCHANTED_HEALTH_SPLASH_POTION("enchanted_health_splash_potion", "Enchanted Health Splash Potion", CraftSkill.ALCHEMY, 50, 1, 1,
      ing("torch_cactus_flower", 2), ing("coconut", 1), ing("magic_sap", 1)),

  ETERNAL_RED_RING("eternal_red_ring", "Eternal Red Ring", CraftSkill.JEWELRYCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 12), ing("alexandrite", 1), ing("sand_snake_poison", 4), ing("duskworm_skin", 4), ing("golden_dust", 4), ing("desert_scorpion_carapace", 2)),

  HEART_AMULET("heart_amulet", "Heart Amulet", CraftSkill.JEWELRYCRAFTING, 50, 1, 1,
      ing("palm_plank", 12), ing("corrupted_stone", 3), ing("grimlet_bone", 3), ing("duskworm_skin", 3), ing("golden_dust", 3), ing("goblin_eye", 2)),

  MAGIC_SHIELD("magic_shield", "Magic Shield", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("desert_scorpion_carapace", 5), ing("alexandrite", 1), ing("grimlet_bone", 4), ing("marauder_hand", 4)),

  MOONLIGHT_STAFF("moonlight_staff", "Moonlight Staff", CraftSkill.WEAPONCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("dusk_beetle_shell", 5), ing("alexandrite", 1), ing("marauder_hand", 4), ing("orc_bone", 4), ing("priestess_orb", 2)),

  SKULLFORGED_ARMOR("skullforged_armor", "Skullforged Armor", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("desert_scorpion_carapace", 4), ing("lava_bucket", 3), ing("corrupted_stone", 3), ing("adventurer_skull", 4), ing("astralyte_crystal", 2)),

  SKULLFORGED_PANTS("skullforged_pants", "Skullforged Pants", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("desert_scorpion_carapace", 4), ing("adventurer_skull", 3), ing("demon_horn", 4), ing("dusk_beetle_shell", 3), ing("astralyte_crystal", 2)),

  SKULLFORGED_RING("skullforged_ring", "Skullforged Ring", CraftSkill.JEWELRYCRAFTING, 50, 1, 1,
      ing("adamantite_bar", 10), ing("lava_bucket", 4), ing("dusk_beetle_shell", 3), ing("alexandrite", 1), ing("adventurer_skull", 4), ing("jasper_crystal", 4)),

  VITAL_ARMOR("vital_armor", "Vital Armor", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 10), ing("duskworm_skin", 4), ing("sand_snake_poison", 4), ing("lava_bucket", 4), ing("ruby", 2), ing("enchanted_fabric", 3)),

  VITAL_BOOTS("vital_boots", "Vital Boots", CraftSkill.GEARCRAFTING, 50, 1, 1,
      ing("palm_plank", 12), ing("sand_snake_poison", 3), ing("efreet_cloth", 3), ing("duskworm_skin", 3), ing("desert_scorpion_carapace", 3), ing("enchanted_fabric", 2)),

  // ── Depth 2 — requires 2 craft layers ────────────────────────────────
  GREATER_DREADFUL_AMULET("greater_dreadful_amulet", "Greater Dreadful Amulet", CraftSkill.JEWELRYCRAFTING, 30, 1, 2,
      ing("gold_bar", 8), ing("dreadful_amulet", 1), ing("cyclops_eye", 4), ing("ogre_eye", 4), ing("red_cloth", 3)),

  GREATER_DREADFUL_STAFF("greater_dreadful_staff", "Greater Dreadful Staff", CraftSkill.WEAPONCRAFTING, 30, 1, 2,
      ing("dead_wood_plank", 5), ing("dreadful_staff", 1), ing("ogre_eye", 4), ing("cyclops_eye", 5), ing("red_cloth", 3)),

  ROYAL_SKELETON_ARMOR("royal_skeleton_armor", "Royal Skeleton Armor", CraftSkill.GEARCRAFTING, 30, 1, 2,
      ing("gold_bar", 8), ing("skeleton_armor", 1), ing("red_cloth", 3), ing("demoniac_dust", 3)),

  ROYAL_SKELETON_HELMET("royal_skeleton_helmet", "Royal Skeleton Helmet", CraftSkill.GEARCRAFTING, 30, 1, 2,
      ing("gold_bar", 8), ing("skeleton_helmet", 1), ing("owlbear_claw", 4), ing("vermin_leather", 4)),

  ROYAL_SKELETON_PANTS("royal_skeleton_pants", "Royal Skeleton Pants", CraftSkill.GEARCRAFTING, 30, 1, 2,
      ing("gold_bar", 8), ing("skeleton_pants", 1), ing("owlbear_hair", 3), ing("vampire_blood", 3)),

  GREATER_EMERALD_AMULET("greater_emerald_amulet", "Greater Emerald Amulet", CraftSkill.JEWELRYCRAFTING, 40, 1, 2,
      ing("maple_plank", 8), ing("emerald_amulet", 1), ing("emerald", 2), ing("cursed_flask", 6), ing("astralyte_crystal", 2)),

  GREATER_RUBY_AMULET("greater_ruby_amulet", "Greater Ruby Amulet", CraftSkill.JEWELRYCRAFTING, 40, 1, 2,
      ing("maple_plank", 8), ing("ruby_amulet", 1), ing("ruby", 2), ing("hellhound_collar", 6), ing("astralyte_crystal", 2)),

  GREATER_SAPPHIRE_AMULET("greater_sapphire_amulet", "Greater Sapphire Amulet", CraftSkill.JEWELRYCRAFTING, 40, 1, 2,
      ing("maple_plank", 8), ing("sapphire_amulet", 1), ing("sapphire", 2), ing("cursed_flask", 6), ing("astralyte_crystal", 2)),

  GREATER_TOPAZ_AMULET("greater_topaz_amulet", "Greater Topaz Amulet", CraftSkill.JEWELRYCRAFTING, 40, 1, 2,
      ing("maple_plank", 8), ing("topaz_amulet", 1), ing("topaz", 2), ing("hellhound_collar", 6), ing("astralyte_crystal", 2));

  // ── Ingredient record ────────────────────────────────────────────────────
  public record Ingredient(String code, int quantity) {}

  // ── Fields ───────────────────────────────────────────────────────────────
  private final String code;
  private final String name;
  private final CraftSkill skill;
  private final int level;
  private final int outputQuantity;
  private final int depth;
  private final List<Ingredient> ingredients;

  // ── Constructor ──────────────────────────────────────────────────────────
  CraftableItem(String code, String name, CraftSkill skill, int level,
      int outputQuantity, int depth, Ingredient... ingredients) {
    this.code = code;
    this.name = name;
    this.skill = skill;
    this.level = level;
    this.outputQuantity = outputQuantity;
    this.depth = depth;
    this.ingredients = List.of(ingredients);
  }

  private static Ingredient ing(String code, int quantity) {
    return new Ingredient(code, quantity);
  }

  // ── Accessors ────────────────────────────────────────────────────────────
  public String getCode()            { return code; }
  public String getName()            { return name; }
  public CraftSkill getSkill()       { return skill; }
  public int getLevel()              { return level; }
  public int getOutputQuantity()     { return outputQuantity; }
  public int getDepth()              { return depth; }
  public List<Ingredient> getIngredients() { return ingredients; }

  // ── Lookup ───────────────────────────────────────────────────────────────
  private static final Map<String, CraftableItem> BY_CODE =
      Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(CraftableItem::getCode, i -> i));

  public static Optional<CraftableItem> fromCode(String code) {
    return Optional.ofNullable(BY_CODE.get(code));
  }

  public static List<CraftableItem> bySkill(CraftSkill skill) {
    return Arrays.stream(values()).filter(i -> i.skill == skill).toList();
  }

  /** Returns the ingredients that are themselves craftable items. */
  public List<CraftableItem> craftableIngredients() {
    return ingredients.stream()
        .map(i -> BY_CODE.get(i.code()))
        .filter(Objects::nonNull)
        .toList();
  }
}