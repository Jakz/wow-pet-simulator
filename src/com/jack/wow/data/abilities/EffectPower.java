package com.jack.wow.data.abilities;

public class EffectPower
{
  private float base;
  private float coefficient;
  
  EffectPower(float base, float coefficient)
  {
    this.base = base;
    this.coefficient = coefficient;
  }
  
  public float base() { return base; }
  public float coefficient() { return coefficient; }
  
  public float apply(float power) { return base + coefficient*power; }
}
