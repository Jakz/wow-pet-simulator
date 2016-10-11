package com.jack.wow.battle.abilities;

import com.jack.wow.data.PetFamily;

public class Attack implements ActiveEffect
{
  private EffectPower power;
  private Target target;
  private PetFamily family;

  public Attack(Target target, PetFamily family, EffectPower power)
  {
    this.power = power;
    this.target = target;
    this.family = family;
  }
  
  public Attack(Target target, EffectPower power)
  {
    this(target, null, power);
  }
  
  public Target target() { return target; }
  public EffectPower power() { return power; }
  public PetFamily family() { return family; }
  
  @Override public String toString()
  { 
    if (family == null)
      return "damage("+target+", "+power+")";
    else
      return "damage("+target+", "+family.description.toLowerCase()+", "+power+")";
  }
}
