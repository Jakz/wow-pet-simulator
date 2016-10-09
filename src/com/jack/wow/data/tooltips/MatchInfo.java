package com.jack.wow.data.tooltips;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchInfo
{
  public final int count;
  public final int[][] positions;
  
  public MatchInfo(Pattern pattern, String string)
  {
    Matcher m = pattern.matcher(string);
    
    List<int[]> pairs = new ArrayList<>();
    
    int count = 0;
    
    while (m.find())
    {
      ++count;
      pairs.add(new int[] { m.start(), m.end() });
    }
    
    this.count = count;
    this.positions = pairs.toArray(new int[pairs.size()][]);
  }
  
  public String apply(String text, String... replacements)
  {
    if (replacements.length != positions.length)
      throw new IllegalArgumentException("replacements amount is different from found ones");
    
    StringBuilder builder = new StringBuilder();
    
    for (int i = positions.length - 1; i >= 0; --i)
    {
      int[] match = positions[i];
      builder.setLength(0);
      builder.append(text.substring(0, match[0]));
      builder.append(replacements[i]);
      builder.append(text.substring(match[1]));
      text = builder.toString();
    }
    
    return text;
  }
}
