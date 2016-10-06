package com.jack.wow.files.api;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jack.wow.data.Formulas;
import com.jack.wow.data.PetBreed;
import com.jack.wow.data.PetQuality;
import com.jack.wow.data.PetStats;

public class ApiFetcher
{
  private static String API_KEY = null;
  private final static String LOCALE = "en_GB";
  
  private final static String SPECIE_URL = "https://eu.api.battle.net/wow/pet/species/";
  private final static String STATS_URL = "https://eu.api.battle.net/wow/pet/stats/";
  private final static String MASTER_URL = "https://eu.api.battle.net/wow/pet/";
  private final static String ABILITY_URL = "https://eu.api.battle.net/wow/pet/ability/";

  
  private static String apiKey()
  {
    if (API_KEY == null)
    {
      try
      {
        Path path = Paths.get("data/api.key");
        API_KEY = Files.lines(path).findFirst().get();
      }
      catch (Exception e)
      {
        throw new IllegalArgumentException("missing data/api.key file");
      }
    }
    
    return API_KEY;
  }
  
  private static Gson gson()
  {
    return new GsonBuilder().create();
  }
  
  public static ApiMasterList fetchMasterList()
  {
    try
    {
      URL url = new URL(MASTER_URL + "?locale=" + LOCALE + "&apikey=" + apiKey());
      
      try (InputStream is = url.openStream())
      {
        try (Scanner scanner = new Scanner(is, "UTF-8"))
        {
          String data = scanner.useDelimiter("\\A").next();
          return gson().fromJson(data, ApiMasterList.class);
        }
      } 
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static ApiAbility fetchAbility(int id)
  {
    try
    {
      URL url = new URL(ABILITY_URL + id + "?locale=" + LOCALE + "&apikey=" + apiKey());
            
      try (InputStream is = url.openStream())
      {
        try (Scanner scanner = new Scanner(is, "UTF-8"))
        {
          String data = scanner.useDelimiter("\\A").next();
          return gson().fromJson(data, ApiAbility.class);
        }
      } 
    } 
    catch (FileNotFoundException e)
    {
      return null;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static ApiSpecie fetchSpecie(int id)
  {
    try
    {
      URL url = new URL(SPECIE_URL + id + "?locale=" + LOCALE + "&apikey=" + apiKey());
      
      try (InputStream is = url.openStream())
      {
        try (Scanner scanner = new Scanner(is, "UTF-8"))
        {
          String data = scanner.useDelimiter("\\A").next();
          return gson().fromJson(data, ApiSpecie.class);
        }
      } 
    } 
    catch (FileNotFoundException e)
    {
      return null;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static ApiStats fetchStats(int id, int level, PetBreed breed, PetQuality rarity)
  {
    try
    {
      URL url = new URL(STATS_URL + id + "?level=" + level + "&breedId=" + breed.maleId + "&qualityId=" + rarity.id + "&locale=" + LOCALE + "&apikey=" + apiKey());
      
      try (InputStream is = url.openStream())
      {
        try (Scanner scanner = new Scanner(is, "UTF-8"))
        {
          String data = scanner.useDelimiter("\\A").next();
          return gson().fromJson(data, ApiStats.class);
        }
      } 
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
  private static class StatsAverager
  {
    float health = 0, power = 0, speed = 0;
    int total = 0;
    
    public void accept(ApiStats s)
    {
      health += s.health;
      power += s.power;
      speed += s.speed;
      ++total;
    }
    
    public PetStats average(PetBreed breed)
    {
      return new PetStats(
          health/total - breed.health,
          power/total - breed.power,
          speed/total - breed.speed
      );
    }
    
    public void combine(StatsAverager avg)
    {
      health += avg.health;
      power += avg.power;
      speed += avg.speed;
      total += avg.total;
    }
  }
  
  public static PetStats calculateBaseStats(int id, PetBreed breed)
  {
    ApiStats[] stats = new ApiStats[Formulas.LEVELS];
    
    /* fetch stats for each level of specified breed */
    for (int i = 0; i < Formulas.LEVELS; ++i)
      stats[i] = fetchStats(id, i + Formulas.MIN_LEVEL, breed, PetQuality.POOR);
    
    /* normalize stats on level */
    Arrays.stream(stats).forEach(s -> {
      s.health = ((s.health - 100) / 5) / s.level;
      s.power = s.power / s.level;
      s.speed = s.speed / s.level;
    });
    
    /* calculate average on levels and offset values by breed specified */
    StatsAverager averager = Arrays.stream(stats).collect(StatsAverager::new, StatsAverager::accept, StatsAverager::combine);
    
    PetStats averaged = averager.average(breed);
    
    /* round values to 0.1 part*/
    return new PetStats(
        Math.round(averaged.health()/0.1f) * 0.1f,
        Math.round(averaged.power()/0.1f) * 0.1f,
        Math.round(averaged.speed()/0.1f) * 0.1f
    );
  }
  
  public static boolean verifyBaseStatsConsistency(int id)
  {
    return Arrays.stream(PetBreed.values()).map(b -> calculateBaseStats(id, b)).collect(Collectors.toSet()).size() == 1;
  }
  
}
