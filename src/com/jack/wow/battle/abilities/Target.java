package com.jack.wow.battle.abilities;

public enum Target
{
  SELF("self"),
  ACTIVE_PET("active-pet"),
  ENEMY_PET("enemy-pet"),
  ENEMY_ACTIVE_PET("enemy-active-pet"),
  ENEMY_BACK_LINE("enemy-back-line"),
  TEAM("team"),
  ENEMY_TEAM("enemy-team"),
  ENEMY_TEAM_SPLIT("enemy-team-split"),
  BATTLE_FIELD("battlefield"),
  CUSTOM("custom")
  
  ;

  public final String description;
  
  private Target(String description) { this.description = description; }
  @Override public String toString() { return description; }
}
