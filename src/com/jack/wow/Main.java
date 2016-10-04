package com.jack.wow;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.google.gson.*;
import com.jack.wow.data.Database;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetSpec;
import com.jack.wow.files.IconDownloader;
import com.jack.wow.files.Json;
import com.jack.wow.files.api.ApiAbility;
import com.jack.wow.files.api.ApiFetcher;
import com.jack.wow.files.api.ApiPet;
import com.jack.wow.files.api.ApiSpecie;
import com.jack.wow.json.ImplicitContextedAdapter;
import com.jack.wow.ui.PetListPanel;
import com.jack.wow.ui.UI;
import com.jack.wow.ui.misc.UIUtils;

public class Main
{
  private static void setLNF()
  {
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }
  }
  
  public static void buildDatabase() throws IOException
  {
    Gson gson = Json.build();

    ApiPet[] apets = gson.fromJson(new BufferedReader(new FileReader("./data/pets.json")), ApiPet[].class);
    Arrays.stream(apets).forEach(a -> a.isConsistent());
    PetSpec.data = Arrays.stream(apets).map(a -> new PetSpec(a)).toArray(i -> new PetSpec[i]);
    
    /* fetch abilities from WoW API */
    for (PetSpec pet : PetSpec.data)
    {
      ApiSpecie specie = ApiFetcher.fetchSpecie(pet.id);
      System.out.println("Fetched "+specie.name);
      pet.fillData(specie);
    }
    
    Database db = new Database(PetAbility.data, PetSpec.data);
    db.save(Paths.get("data/database.json"));
  }
  
  public static boolean loadDatabase()
  {
    Path path = Paths.get("data/database.json");
    
    if (Files.exists(path))
    {
      try (BufferedReader rdr = Files.newBufferedReader(path))
      {
        Gson gson = Json.build();
        gson.fromJson(rdr, Database.class);

        return true;
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    
    return false;
  }
  
  public static void main(String[] args)
  {
    setLNF();

    try
    {
      if (!loadDatabase())
        buildDatabase();
      
      System.out.printf("Loaded %s pets.", PetSpec.data.length);
      
      Set<String> icons = Arrays.stream(PetSpec.data).map(p -> p.icon).collect(Collectors.toSet());
      PetAbility.data.values().stream().map(a -> a.icon).forEach(icons::add);

      IconDownloader downloader = new IconDownloader(null, icons);
      
      downloader.start();
      
      UI.init();
      UI.petListFrame.panel().populate(Arrays.asList(PetSpec.data), p -> true);
      
      UI.petListFrame.setLocationRelativeTo(null);
      UI.petListFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  void fetchAbilitiesFromAPI(PetSpec[] pets)
  {
    for (PetSpec pet : pets)
    {
      if (!pet.areAbilitiesPresent())
      {
        ApiSpecie specie = ApiFetcher.fetchSpecie(pet.id);
        
        pet.source = specie.source;
        pet.description = specie.description;
        
        for (ApiAbility a : specie.abilities)
        {
          PetAbility ability = PetAbility.get(a.id);
          
          if (ability == null)
            ability = PetAbility.generate(a);
          
          pet.abilities[a.order] = new PetOwnedAbility(ability, a.order, a.slot, a.requiredLevel);
        }
      }
    }
  }
}
