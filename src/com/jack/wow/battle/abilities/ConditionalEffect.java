package com.jack.wow.battle.abilities;

import java.util.Optional;

public class ConditionalEffect implements ActiveEffect
{
  Condition condition;
  ActiveEffect trueEffect;
  Optional<ActiveEffect> falseEffect;
  
  public ConditionalEffect(Condition condition, ActiveEffect trueEffect, ActiveEffect falseEffect)
  {
    this.condition = condition;
    this.trueEffect = trueEffect;
    this.falseEffect = Optional.of(falseEffect);
  }
  
  public ConditionalEffect(Condition condition, ActiveEffect trueEffect)
  {
    this.condition = condition;
    this.trueEffect = trueEffect;
    this.falseEffect = Optional.empty();
  }
  
  @Override public String toString()
  { 
      if (falseEffect.isPresent())
        return "conditional("+condition+", "+trueEffect+", "+falseEffect+")";
      else
        return "conditional("+condition+", "+trueEffect+")";
  }
}
