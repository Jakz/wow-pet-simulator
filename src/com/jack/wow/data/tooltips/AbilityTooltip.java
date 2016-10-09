package com.jack.wow.data.tooltips;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
