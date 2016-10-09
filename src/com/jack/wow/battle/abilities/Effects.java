package com.jack.wow.battle.abilities;

import com.jack.wow.data.PetAbility;

public class Effects
{
  public static final SingleAttack BITE = new SingleAttack(new EffectPower(20));
  
  void init()
  {
    PetAbility.forName("bite").addEffect(BITE);
  }
}
