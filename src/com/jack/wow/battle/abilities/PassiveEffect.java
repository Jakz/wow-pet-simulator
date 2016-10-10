package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

public interface PassiveEffect extends Effect
{
  default void onEndEffect(BattleStatus battle) { }
  default void onTickEffect(BattleStatus battle) { }
}
