package com.jack.wow.battle;

import java.util.stream.Stream;

import com.jack.wow.battle.abilities.Effect;
import com.jack.wow.battle.abilities.EffectApply;
import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.PetAbility;

public class EffectStatus
{
  PetAbility effect;
  int turns;
  boolean hasCharges;
  int charges;
  
  public EffectStatus(EffectApply apply)
  {
    this(apply.ability, apply.turns);
  }
  
  public EffectStatus(PetAbility effect, int turns)
  {
    this.effect = effect;
    this.turns = turns;
  }
  
  public EffectStatus(PetAbility effect, int turns, int charges)
  {
    this.effect = effect;
    this.turns = turns;
    this.charges = charges;
    this.hasCharges = true;
  }
  
  public Stream<PassiveEffect> passiveEffects() { return effect.effects().filter(e -> e instanceof PassiveEffect).map(Effect::asPassive); }
  
  public PetAbility ability() { return effect; }
  public int remainingTurns() { return turns; }
  
  public boolean isEnded() { return turns == 0 || (hasCharges && charges == 0); }
  public boolean isFinite() { return turns >= 0; }
  public void consumeTurn() { --turns; }
  
  public boolean hasCharges() { return hasCharges; }
  public int remainingCharges() { return charges; }
  public void consumeCharge() { --charges; }
}