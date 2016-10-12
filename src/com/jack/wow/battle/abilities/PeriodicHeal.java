package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;

public class PeriodicHeal implements PassiveEffect
{
  private EffectPower power;
  private Target target;
  
  public PeriodicHeal(Target target, EffectPower power)
  {
    this.power = power;
    this.target = target;
  }
  
  public Target target() { return target; }
  public EffectPower power() { return power; }
  
  @Override public boolean isNegative() { return false; }

  @Override public String toString() { return "hot("+target+", "+power+")"; }
  
  @Override public void onTickEffect(BattleStatus status)
  {
    throw new RuntimeException("must be implemented");
  }
}
