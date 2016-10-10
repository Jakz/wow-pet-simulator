package com.jack.wow.battle.abilities;

public class ConditionalEffect implements ActiveEffect
{
  Condition condition;
  ActiveEffect trueEffect;
  ActiveEffect falseEffect;
  
  public ConditionalEffect(Condition condition, ActiveEffect trueEffect, ActiveEffect falseEffect)
  {
    this.condition = condition;
    this.trueEffect = trueEffect;
    this.falseEffect = falseEffect;
  }
  
  @Override public String toString() { return "conditional("+condition+", "+trueEffect+", "+falseEffect+")"; }
}
