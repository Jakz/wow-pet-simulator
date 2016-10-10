package com.jack.wow.battle.abilities;

public interface Effect
{
  default PassiveEffect asPassive() { return (PassiveEffect)this; } 
}
