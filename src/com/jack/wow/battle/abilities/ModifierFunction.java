package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

@FunctionalInterface
public interface ModifierFunction extends PassiveEffect
{
  public static enum Target
  {
    DAMAGE(false, "dmg-done"),
    DAMAGE_RECEIVED(false, "dmg-recv"),
    DAMAGE_RECEIVED_RAW(true, "dmg-recv-raw"),
    DAMAGE_DONE_RAW(true, "dmg-recv-raw"),
    HEALING_RECEIVED(false, "healing-recv"),
    SPEED(false, "speed"),
    HIT_CHANCE(false, "hit-chance"),
    CRIT_CHANCE(false, "crit-chance"),
    DODGE_CHANCE(false, "dodge-chance"),
    
    HEALTH(false, "health")
    
    ;
    
    public final String description;
    public final boolean isAdditive;
    
    Target(boolean isAdditive, String description)
    { 
      this.description = description;
      this.isAdditive = isAdditive;
    }
    
    @Override public String toString() { return description; }
  }
  
  @Override default int priority() { return isAdditive() ? PassiveEffect.PRIORITY_HIGH : PassiveEffect.PRIORITY_DEFAULT; }
  default boolean isAdditive() { return true; }
  public ComputedStat apply(BattleStatus status, Target target, ComputedStat value);
}
