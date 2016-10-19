package com.jack.wow.ai;

import com.jack.wow.battle.BattleStatus;

public interface PlayerAI
{
  PlayerAction thinkNextAction(BattleStatus status);
}
