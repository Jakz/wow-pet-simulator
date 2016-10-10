package com.jack.wow.battle.abilities;

@FunctionalInterface
public interface ModifierFunction extends PassiveEffect
{
  public static enum Target
  {
    DAMAGE,
    DAMAGE_RECEIVED,
    SPEED,
    HIT_CHANCE,
    CRIT_CHANCE,
    DODGE_CHANCE
  }
  
  public float apply(Target target, float value);
}
