package com.jack.wow.battle.abilities;

public class TeamSplitAttack implements ActiveEffect
{
  private EffectPower power;
  
  public TeamSplitAttack(EffectPower power)
  {
    this.power = power;
  }
  
  public EffectPower power() { return power; }
}
