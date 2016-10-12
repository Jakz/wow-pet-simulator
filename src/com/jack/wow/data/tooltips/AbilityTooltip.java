package com.jack.wow.data.tooltips;

public class AbilityTooltip
{  
  private final String text;
    
  public AbilityTooltip(String text)
  {
    this.text = text;
  }
  
  public String getText(Object... args)
  {
    return text;
  }
}
