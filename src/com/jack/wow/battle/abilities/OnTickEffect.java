package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

public class OnTickEffect implements PassiveEffect
{
  final public ActiveEffect effect;
  
  public OnTickEffect(ActiveEffect effect)
  {
    this.effect = effect;
  }
  
  @Override public void onTickEffect(BattleStatus battle) { throw new RuntimeException(); }
  
  @Override public boolean isNegative() { throw new RuntimeException(); }
  
  @Override public String toString() { return "on-tick("+effect+")"; }
}
