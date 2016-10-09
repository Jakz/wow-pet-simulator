package com.jack.wow.battle.abilities;

public class SingleHeal implements ActiveEffect
{
  private EffectPower power;
  
  public SingleHeal(EffectPower power)
  {
    this.power = power;
  }
  
  public EffectPower power() { return power; }
}
