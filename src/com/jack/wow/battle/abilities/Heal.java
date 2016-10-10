package com.jack.wow.battle.abilities;

public class Heal implements ActiveEffect
{
  private EffectPower power;
  private Target target;
  
  public Heal(Target target, EffectPower power)
  {
    this.power = power;
    this.target = target;
  }
  
  public Target target() { return target; }
  public EffectPower power() { return power; }
}
