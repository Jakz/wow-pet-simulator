package com.jack.wow.battle.abilities;

import java.util.Comparator;

import com.jack.wow.battle.BattleStatus;
import com.jack.wow.data.PetFamily;

public interface PassiveEffect extends Effect
{
  public static final int PRIORITY_HIGHEST = 0;
  public static final int PRIORITY_HIGHER = 2;
  public static final int PRIORITY_HIGH = 4;
  public static final int PRIORITY_DEFAULT = 5;
  public static final int PRIORITY_LOW = 6;
  public static final int PRIORITY_LOWER = 7;
  public static final int PRIORITY_LOWEST = 9;
  
  default boolean isNegative() { return false; }
  
  default void onEndEffect(BattleStatus status) { }
  default void onTickEffect(BattleStatus status) { }
  default void onStartTurn(BattleStatus status) { }
  default void onEndTurn(BattleStatus status) { }
  
  default PetFamily onGetPetFamily(BattleStatus status, PetFamily family) { return family; } 
  default ComputedStat onCalculateStat(BattleStatus battle, ModifierFunction.Target target, ComputedStat value) { return value; }
  
  default int priority() { return PRIORITY_DEFAULT; }
  
  public static class PriorityComparator implements Comparator<PassiveEffect>
  {
    @Override public int compare(PassiveEffect o1, PassiveEffect o2)
    {
      return 0;
    }
  }
}
