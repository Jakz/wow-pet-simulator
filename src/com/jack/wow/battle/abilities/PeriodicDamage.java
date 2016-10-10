package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;
import com.jack.wow.data.PetFamily;

public class PeriodicDamage implements PassiveEffect
{
  private EffectPower power;
  private Target target;
  private PetFamily family;
  
  public PeriodicDamage(Target target, PetFamily family, EffectPower power)
  {
    this.power = power;
    this.target = target;
    this.family = family;
  }
  
  public Target target() { return target; }
  public EffectPower power() { return power; }
  public PetFamily family() { return family; }
  
  @Override public String toString() { return "dot("+target+", "+family.description.toLowerCase()+", "+power+")"; }
  
  @Override public void onTickEffect(BattleStatus status)
  {
    throw new RuntimeException("must be implemented");
  }
}
