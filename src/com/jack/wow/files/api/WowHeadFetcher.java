package com.jack.wow.files.api;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jack.wow.files.StreamException;

public class WowHeadFetcher
{
  public static class TooltipInfo
  {
    public final int id;
    public final String description;
    public Optional<Integer> hitChance;
    
    TooltipInfo(int id, String description) { this.id = id; this.description = description; hitChance = Optional.empty(); }
    TooltipInfo(int id, String description, int hitChance) { this(id, description); this.hitChance = Optional.of(hitChance); }
    
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
        
        return new TooltipInfo(id, m.group(2), Integer.valueOf(m.group(1)));
      }
      else
        return new TooltipInfo(id, tooltip.text().trim());
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
  
  public static void parseChangeLogForAbility(int id)
  {
    try
    {
      Document doc = Jsoup.connect("http://www.thottbot.com/pet-ability="+id+"#changelog").get();
    
      Elements changelog = doc.select("* > div#jkbfksdbl4");
      
      System.out.println("Antani");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }


  }
}
