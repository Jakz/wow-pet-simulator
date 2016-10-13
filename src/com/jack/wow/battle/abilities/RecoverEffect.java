package com.jack.wow.battle.abilities;

public class RecoverEffect implements ActiveEffect
{
  public final ActiveEffect effect;
  public final int turns;
  
  public RecoverEffect(ActiveEffect effect, int turns)
  {
    this.effect = effect;
    this.turns = turns;
  }
  
  @Override public String toString() { return "recover("+turns+", "+effect+")"; }
}
