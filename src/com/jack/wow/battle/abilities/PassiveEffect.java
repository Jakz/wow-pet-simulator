package com.jack.wow.battle.abilities;

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
  
  default void onEndEffect(BattleStatus battle) { }
  default void onTickEffect(BattleStatus battle) { }
  default float onCalculateStat(BattleStatus battle, ModifierFunction.Target target, float value) { return value; }
  default int priority() { return PRIORITY_DEFAULT; }
}
