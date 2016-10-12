package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

public class SpecialEffect implements PassiveEffect
{
  public static final SpecialEffect STUNNED = new SpecialEffect() { @Override public String toString() { return "stunned"; } };
  public static final SpecialEffect BURNING = new SpecialEffect() { @Override public String toString() { return "burning"; } };
  public static final SpecialEffect POISONED = new SpecialEffect() { @Override public String toString() { return "poisoned"; } };
  public static final SpecialEffect BLINDED = new SpecialEffect() { @Override public String toString() { return "blinded"; } };
  public static final SpecialEffect CHILLED = new SpecialEffect() { @Override public String toString() { return "chilled"; } };
  public static final ActiveEffect UNABLE_TO_SWAP = new ActiveEffect() { @Override public String toString() { return "unable-to-swap"; } };
  
  public static final ActiveEffect DESTROY_OBJECTS = new ActiveEffect() { @Override public String toString() { return "destroy-objects"; } };
  public static final ActiveEffect INTERRUPT_OPPONENT_ROUND_IF_FIRST = new ActiveEffect() { @Override public String toString() { return "interrupt-opponent-if-first"; } };
  
  public static final ActiveEffect DUMMY = new ActiveEffect() { @Override public String toString() { return "dummy"; } };
  
  public static ActiveHiddenEffect alwaysGoestFirst(int multiplier)
  {
    return new ActiveHiddenEffect(new AlwaysGoesFirst(multiplier));
  }
  
  @Override public boolean isNegative() { throw new RuntimeException(); }
  
  private static class AlwaysGoesFirst extends ModifierEffect
  {    
    private AlwaysGoesFirst(int multiplier)
    {
      super(Target.SPEED, multiplier);
    }
    
    @Override public boolean isAdditive() { return true; }
    
    @Override public String toString() { return "always-goes-first(x"+parameter+")"; }

    @Override public ComputedStat onCalculateStat(BattleStatus battle, ModifierFunction.Target target, ComputedStat value)
    {
      if (target == ModifierFunction.Target.SPEED)
        return value.add(battle.self.pet().level()*parameter);
      else
        return value.copy();
    }
  }
}
