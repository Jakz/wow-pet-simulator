package com.jack.wow.battle.abilities;

public class ChanceEffect implements ActiveEffect
{
  public final ActiveEffect effect;
  public float chance;
  
  public ChanceEffect(float chance, ActiveEffect effect)
  {
    this.effect = effect;
    this.chance = chance;
  }
  
  @Override public String toString() { return "chance("+(int)(chance*100)+"%, "+effect+")"; }
}
