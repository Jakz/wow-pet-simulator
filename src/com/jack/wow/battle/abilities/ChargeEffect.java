package com.jack.wow.battle.abilities;

public class ChargeEffect implements ActiveEffect
{
  public final ActiveEffect effect;
  public final int turns;
  
  public ChargeEffect(ActiveEffect effect, int turns)
  {
    this.effect = effect;
    this.turns = turns;
  }
  
  @Override public String toString() { return "charge("+turns+", "+effect+")"; }
}
