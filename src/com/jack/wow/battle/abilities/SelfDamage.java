package com.jack.wow.battle.abilities;

public class SelfDamage implements ActiveEffect
{
  private EffectPower power;
  
  public SelfDamage(EffectPower power)
  {
    this.power = power;
  }
  
  public EffectPower power() { return power; }
}
