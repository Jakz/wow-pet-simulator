package com.jack.wow.battle;

import com.jack.wow.data.PetAbility;

class EffectStatus
{
  PetAbility effect;
  int turns;
  
  EffectStatus(PetAbility effect, int turns)
  {
    this.effect = effect;
    this.turns = turns;
  }
  
  public boolean isEnded() { return turns == 0; }
  public boolean isFinite() { return turns >= 0; }
  public void consume() { --turns; }
}