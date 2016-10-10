package com.jack.wow.battle;

import java.util.stream.Stream;

import com.jack.wow.battle.abilities.Effect;
import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.PetAbility;

public class EffectStatus
{
  PetAbility effect;
  int turns;
  
  EffectStatus(PetAbility effect, int turns)
  {
    this.effect = effect;
    this.turns = turns;
  }
  
  public Stream<PassiveEffect> passiveEffects() { return effect.stream().filter(e -> e instanceof PassiveEffect).map(Effect::asPassive); }
  
  public boolean isEnded() { return turns == 0; }
  public boolean isFinite() { return turns >= 0; }
  public void consume() { --turns; }
}