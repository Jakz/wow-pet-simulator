package com.jack.wow.battle.abilities;

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
    HIT_CHANCE(true, "hit-chance"),
    CRIT_CHANCE(true, "crit-chance"),
    DODGE_CHANCE(true, "dodge-chance"),
    
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
  
  public float apply(Target target, float value);
}
