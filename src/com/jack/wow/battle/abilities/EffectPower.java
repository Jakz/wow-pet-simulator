package com.jack.wow.battle.abilities;

public class EffectPower
{
  private float base;
  private float variance;
  
  EffectPower(float base)
  {
    this(base, 0);
  }
  
  EffectPower(float base, float variance)
  {
    this.base = base;
    this.variance = variance;
  }
  
  public float base() { return base; }
  
  public float apply(float power) { return base + (base*0.05f)*power; }
  
  @Override public String toString()
  {
    if (variance == 0)
      return String.valueOf((int)base);
    else
      return String.valueOf((int)base) + "\u00B1" + String.valueOf((int)(variance*100)) +"%";
  }
}
