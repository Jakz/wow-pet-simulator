package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

public class SpecialEffect implements PassiveEffect
{
  public static final SpecialEffect STUNNED = new SpecialEffect() { @Override public String toString() { return "stunned"; } };
  public static final SpecialEffect BURNING = new SpecialEffect() { @Override public String toString() { return "burning"; } };
  public static final SpecialEffect POISONED = new SpecialEffect() { @Override public String toString() { return "poisoned"; } };
  public static final SpecialEffect BLINDED = new SpecialEffect() { @Override public String toString() { return "blinded"; } };
  public static final SpecialEffect CHILLED = new SpecialEffect() { @Override public String toString() { return "chilled"; } };
  public static final SpecialEffect UNABLE_TO_SWAP = new SpecialEffect() { @Override public String toString() { return "unable-to-swap"; } };
  
  public static final SpecialEffect DESTROY_OBJECTS = new SpecialEffect() { @Override public String toString() { return "destroy-objects"; } };
  public static final ActiveEffect INTERRUPT_OPPONENT_ROUND_IF_FIRST = new ActiveEffect() { @Override public String toString() { return "interrupt-opponent-if-first"; } };
  
  public static final ActiveEffect DUMMY = new ActiveEffect() { @Override public String toString() { return "dummy"; } };
  
  public static ActiveHiddenEffect alwaysGoestFirst(int multiplier)
  {
    return new ActiveHiddenEffect(new AlwaysGoesFirst(multiplier));
  }
  
  @Override public boolean isNegative() { throw new RuntimeException(); }
  
  private static class AlwaysGoesFirst extends SpecialEffect
  {
    private final int multiplier;
    
    private AlwaysGoesFirst(int multiplier)
    {
      this.multiplier = multiplier;
    }
    
    @Override public String toString() { return "always-goes-first(x"+multiplier+")"; }

    @Override public float onCalculateStat(BattleStatus battle, ModifierFunction.Target target, float value)
    {
      if (target == ModifierFunction.Target.SPEED)
        return battle.self.pet().level()*multiplier + value;
      else
        return value;
    }
    
    @Override public int priority() { return PassiveEffect.PRIORITY_HIGHEST; }
  }
}
