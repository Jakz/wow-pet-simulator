package com.jack.wow.battle.abilities;

public class ComputedStat
{
  float value;
  float multiplier;
  
  public ComputedStat(float value)
  {
    this(value, 1.0f);
  }
  
  public ComputedStat(float value, float multiplier)
  {
    this.value = value;
    this.multiplier = multiplier;
  }
 
  public ComputedStat combine(ComputedStat other) { return new ComputedStat(value+other.value, multiplier+other.multiplier); }
  public ComputedStat multiply(float value) { return new ComputedStat(this.value, multiplier + value); }
  public ComputedStat add(float value) { return new ComputedStat(this.value + value, multiplier); }
  public ComputedStat copy() { return new ComputedStat(value, multiplier); }
  
  public float calculate() { return value * Math.max(multiplier, 0.0f); }
 
}
