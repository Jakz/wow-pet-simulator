package com.jack.wow.files.api;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WowHeadFetcher
{
  public static class TooltipInfo
  {
    String description;
    Optional<Integer> hitChance;
    
    TooltipInfo(String description) { this.description = description; hitChance = Optional.empty(); }
    TooltipInfo(String description, int hitChance) { this(description); this.hitChance = Optional.of(hitChance); }
    
    @Override public String toString() { return description + " " + hitChance.orElse(-1); }
  }
  
  private final static Pattern hitPattern = Pattern.compile("^([0-9]+)% Hit Chance (.*)$");
  
  public static TooltipInfo parseAbilityTooltip(int id)
  {
    try
    {   
      Document doc = Jsoup.connect("http://www.wowhead.com/pet-ability="+id).get();
    
      Element tooltip = doc.select("div[class=main-contents] > div[class=text] > noscript > table:nth-child(2) > tbody > tr > td").get(0);
      
      if (!tooltip.select("span").isEmpty())
      {
        String text = tooltip.text().trim();
        Matcher m = hitPattern.matcher(text);
        m.find();
        
        return new TooltipInfo(m.group(2), Integer.valueOf(m.group(1)));
      }
      else
        return new TooltipInfo(tooltip.text().trim());
    }
    catch (org.jsoup.HttpStatusException e)
    {
      if (e.getStatusCode() == 404)
        ;
      else
        e.printStackTrace();
    }
    catch (Exception e)
    {
      //throw new RuntimeException("error parsing id "+id, e);
    }
    
    return null;
  }
}
