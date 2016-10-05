package com.jack.wow.battle.abilities;

public class EffectPower
{
  private float base;
  
  EffectPower(float base)
  {
    this.base = base;
  }
  
  public float base() { return base; }
  
  public float apply(float power) { return base + (base*0.05f)*power; }
}
