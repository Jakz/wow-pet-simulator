package com.jack.wow.battle.abilities;

import com.jack.wow.battle.Battle;

public interface PassiveEffect extends Effect
{
  default void onEndEffect(Battle battle) { }
}
