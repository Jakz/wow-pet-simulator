package com.jack.wow.battle.abilities;

import java.util.Comparator;

import com.jack.wow.battle.BattleStatus;

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
  default void onEndEffect(BattleStatus battle) { }
  default void onTickEffect(BattleStatus battle) { }
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
