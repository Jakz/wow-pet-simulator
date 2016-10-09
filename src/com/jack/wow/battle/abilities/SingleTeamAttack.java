package com.jack.wow.battle.abilities;

public class SingleTeamAttack implements ActiveEffect
{
  private EffectPower power;
  
  public SingleTeamAttack(EffectPower power)
  {
    this.power = power;
  }
  
  public EffectPower power() { return power; }
}
