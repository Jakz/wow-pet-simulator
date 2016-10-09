package com.jack.wow.data.tooltips;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolTipUtil
{
  public static final Pattern NUMBER_REGEXP = Pattern.compile("([0-9]+)");

  
  static void requireThrowing(Class<?>[] classes, Object... args)
  {
    if (!require(classes, args))
      throw new IllegalArgumentException("tooltip has wrong amount of arguments");
  }
  
  static boolean require(Class<?>[] classes, Object... args)
  {
    if (classes.length != args.length)
      return false;
    else
      for (int i = 0; i < classes.length; ++i)
        if (!classes[i].isAssignableFrom(args[i].getClass()))
          return false;
    
    return true;
  }
  
  static int countMatches(Pattern pattern, String text)
  {
    int c = 0;
    Matcher m = pattern.matcher(text);
    
    while (m.find())
      ++c;
    
    return c;
  }
}
