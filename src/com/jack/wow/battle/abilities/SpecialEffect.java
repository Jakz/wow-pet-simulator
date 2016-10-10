package com.jack.wow.battle.abilities;

public class SpecialEffect implements PassiveEffect
{
  public static final SpecialEffect STUNNED = new SpecialEffect() { @Override public String toString() { return "stunned"; } };
  public static final SpecialEffect BURNING = new SpecialEffect() { @Override public String toString() { return "burning"; } };
  public static final SpecialEffect POISONED = new SpecialEffect() { @Override public String toString() { return "poisoned"; } };

  
  
  public static final SpecialEffect ALWAYS_GOES_FIRST = new SpecialEffect() { @Override public String toString() { return "always-goes-first"; } };
  public static final SpecialEffect DESTROY_OBJECTS = new SpecialEffect() { @Override public String toString() { return "destroy-objects"; } };
  public static final SpecialEffect INTERRUPT_OPPONENT_ROUND_IF_FIRST = new SpecialEffect() { @Override public String toString() { return "interrupt-opponent-if-first"; } };
  
  public static final ActiveEffect DUMMY = new ActiveEffect() { @Override public String toString() { return "dummy"; } };
}
