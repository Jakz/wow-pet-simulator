package com.jack.wow.files.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jack.wow.data.interfaces.Iconed;

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
  
  
  public static void downloadIcon(String iconName, boolean small)
  {
    final String prefix = (small ? "small" : "large") +"/";
    final Path path = Iconed.pathForIcon(iconName, small);

    try
    {
      final URL url = new URL("https://wow.zamimg.com/images/wow/icons/"+ prefix + iconName + ".jpg");
      
      ReadableByteChannel rbc = Channels.newChannel(url.openStream());
      FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      channel.transferFrom(rbc, 0, 1 << 24);
      channel.close();
    }
    catch (FileNotFoundException e)
    {
      //e.printStackTrace();
      return;
    }
    catch (java.nio.channels.ClosedByInterruptException e)
    {   
      try
      {
        if (Files.exists(path))
          Files.delete(path);
      }
      catch (IOException ee)
      {
        ee.printStackTrace();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }
}
