package com.jack.wow.battle.abilities;

public class Attack implements ActiveEffect
{
  private EffectPower power;
  private Target target;
  
  public Attack(Target target, EffectPower power)
  {
    this.power = power;
    this.target = target;
  }
  
  public Target target() { return target; }
  public EffectPower power() { return power; }
  
  @Override public String toString() { return "damage("+target+", "+power+")"; }
}
