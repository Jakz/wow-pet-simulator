package com.jack.wow.data.tooltips;

import java.util.regex.Pattern;

import com.jack.wow.data.PetAbility;
import com.jack.wow.data.interfaces.Statsed;

public class SingleDamageTooltip extends AbilityTooltip
{
  
  private final PetAbility ability;
  
  
  public SingleDamageTooltip(String text, PetAbility ability)
  {
    super(text);
    this.ability = ability;
    
    if (ToolTipUtil.countMatches(ToolTipUtil.NUMBER_REGEXP, text) != 1)
      throw new IllegalArgumentException("SingleDamageTooltip text must contain only one damage reference");
  }
  
  @Override public String getText(Object... args)
  {
    ToolTipUtil.requireThrowing(new Class<?>[] { Statsed.class }, args);
    
    
    
    return null;
  }
}
