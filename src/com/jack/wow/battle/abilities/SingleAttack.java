package com.jack.wow.battle.abilities;

public class SingleAttack implements ActiveEffect
{
  private EffectPower power;
  
  public SingleAttack(EffectPower power)
  {
    this.power = power;
  }
  
  public EffectPower power() { return power; }
}
