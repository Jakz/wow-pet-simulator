package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

public class ExpireEffect implements PassiveEffect
{
  final public ActiveEffect effect;
  
  public ExpireEffect(ActiveEffect effect)
  {
    this.effect = effect;
  }
  
  @Override public void onEndEffect(BattleStatus battle) { throw new RuntimeException(); }
  
  @Override public boolean isNegative() { throw new RuntimeException(); }
  
  @Override public String toString() { return "on-expire("+effect+")"; }
}
