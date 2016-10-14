package com.jack.wow.battle;

import com.jack.wow.battle.abilities.PassiveEffect;

public class EffectInfo
{
  public final EffectStatus status;
  public final PassiveEffect effect;
  
  public EffectInfo(PassiveEffect effect)
  {
    this(null, effect);
  }

  
  public EffectInfo(EffectStatus status, PassiveEffect effect)
  {
    this.status = status;
    this.effect = effect;
  }
}
