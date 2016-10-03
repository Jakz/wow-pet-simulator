package com.jack.wow.files.api;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiFetcher
{
  private final static String API_KEY = "hbpn3m6kx2ra59rycuxzgh7wch8ew67j";
  private final static String LOCALE = "en_GB";
  
  private final static String SPECIE_URL = "https://eu.api.battle.net/wow/pet/species/";
  
  private static Gson gson()
  {
    return new GsonBuilder().create();
  }
  
  public static ApiSpecie fetchSpecie(int id)
  {
    try
    {
      URL url = new URL(SPECIE_URL + id + "?locale=" + LOCALE + "&apikey=" + API_KEY);
      
      try (InputStream is = url.openStream())
      {
        try (Scanner scanner = new Scanner(is, "UTF-8"))
        {
          String data = scanner.useDelimiter("\\A").next();
          return gson().fromJson(data, ApiSpecie.class);
        }
      } 
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
}
