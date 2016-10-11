package com.jack.wow.battle.abilities;

import com.jack.wow.data.PetAbility;

public class EffectApply implements ActiveEffect
{
  public final PetAbility ability;
  public final Target target;
  public final int turns;
  
  public EffectApply(PetAbility ability, Target target, int turns)
  {
    this.ability = ability;
    this.target = target;
    this.turns = turns;
  }
  
  public boolean isFinite() { return turns > 0; }
  
  @Override public String toString() { return "apply("+ability.id+"-"+ability.name.toLowerCase()+", "+target+(turns > 0 ? (", "+turns+")") : ""); }
}
