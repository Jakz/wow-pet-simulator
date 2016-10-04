package com.jack.wow.data.abilities;

public class SingleEffect implements ActiveEffect
{
  private EffectPower power;
  
  public SingleEffect(EffectPower power)
  {
    this.power = power;
  }
  
  public EffectPower power() { return power; }
}
