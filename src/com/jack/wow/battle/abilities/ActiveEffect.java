package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

public interface ActiveEffect extends Effect
{
  
  
  default void onUse(BattleStatus status) { }
}
