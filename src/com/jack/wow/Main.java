package com.jack.wow;

import java.util.List;
import java.util.Objects;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.google.gson.*;
import com.jack.wow.battle.AbilitySet;
import com.jack.wow.battle.Battle;
import com.jack.wow.battle.BattlePet;
import com.jack.wow.battle.BattleTeam;
import com.jack.wow.data.Database;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetBreed;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetQuality;
import com.jack.wow.data.PetSpec;
import com.jack.wow.files.IconDownloader;
import com.jack.wow.files.Json;
import com.jack.wow.files.StreamException;
import com.jack.wow.files.api.ApiAbility;
import com.jack.wow.files.api.ApiFetcher;
import com.jack.wow.files.api.ApiMasterList;
import com.jack.wow.files.api.ApiPet;
import com.jack.wow.files.api.ApiSpecie;
import com.jack.wow.ui.UI;

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
  
  public static void buildDatabase() throws IOException, InterruptedException
  {
    final int MAX_ABILITY_ID = 2000;
    final int MAX_PET_ID = 2000;
    
    {
      final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
      
      List<Callable<ApiAbility>> tasks = 
        IntStream.range(0, MAX_ABILITY_ID)
        .boxed()
        .map(i -> (Callable<ApiAbility>)( () -> ApiFetcher.fetchAbility(i) ) )
        .collect(Collectors.toList());
      
      new Thread(() -> {
        while (!executor.isTerminated())
        {
          System.out.println(String.format("Fetching abilities %2.1f%%..", ((executor.getCompletedTaskCount()/(float)MAX_ABILITY_ID)*100)));
          try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }
      }).start();
    
      executor.invokeAll(tasks).stream()
        .map(StreamException.rethrowFunction(f -> f.get()))
        .filter(Objects::nonNull)
        .forEach(PetAbility::generate);

      System.out.println("Fetched "+PetAbility.data.size()+" abilities");
    }
    
    {
      final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
      
      List<Callable<ApiSpecie>> tasks = 
        IntStream.range(0, MAX_PET_ID)
        .boxed()
        .map(i -> (Callable<ApiSpecie>)( () -> ApiFetcher.fetchSpecie(i) ) )
        .collect(Collectors.toList());
      
      new Thread(() -> {
        while (!executor.isTerminated())
        {
          System.out.println(String.format("Fetching pets %2.1f%%..", ((executor.getCompletedTaskCount()/(float)MAX_PET_ID)*100)));
          try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }
      }).start();
    
      PetSpec.data = executor.invokeAll(tasks).stream()
        .map(StreamException.rethrowFunction(f -> f.get()))
        .filter(Objects::nonNull)
        .map(p -> new PetSpec(p))
        .toArray(i -> new PetSpec[i]);

      System.out.println("Fetched "+PetSpec.data.length+" pets");
    }

    /* mark all usable pets */
    ApiMasterList master = ApiFetcher.fetchMasterList();
    for (ApiPet pet : master.pets)
      PetSpec.forId(pet.stats.speciesId).markUsable();

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
    
    /*PetStats[][] table = new PetStats[PetBreed.count()][25];

    for (PetBreed breed : PetBreed.values())
    {
      System.out.println("Fetching "+breed.description);
      
      for (int i = 0; i < 25; ++i)
      {
        ApiStats stats = ApiFetcher.fetchStats(1155, i+1, breed, PetQuality.POOR);
        table[breed.ordinal()][i] = new PetStats(stats);
      }
    }
    
    try (BufferedWriter wrt = Files.newBufferedWriter(Paths.get("data/test.csv")))
    {
      for (int i = 0; i < 25; ++i)
        for (PetBreed breed : PetBreed.values())
        {
          PetStats s = table[breed.ordinal()][i];
          wrt.write(String.format("\"%s\",%d,%d,%d,%d\n", breed.description, i+1, s.health(), s.power(), s.speed()));
        }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if (true)
      return;*/

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
      
      if (true)
        return;
      
      UI.battleFrame.setLocationRelativeTo(null);
      
      BattlePet pet1 = new BattlePet(
          new Pet(PetSpec.forName("anubisath idol"), PetBreed.HH, PetQuality.RARE, 25),
          new AbilitySet(1,1,2)
      );
      BattlePet pet2 = new BattlePet(
          new Pet(PetSpec.forName("darkmoon zeppelin"), PetBreed.HH, PetQuality.RARE, 25),
          new AbilitySet(1,1,2)
      );
      BattlePet pet3 = new BattlePet(
          new Pet(PetSpec.forName("chrominius"), PetBreed.HH, PetQuality.RARE, 25),
          new AbilitySet(1,1,2)
      );
      
      BattlePet pet4 = new BattlePet(
          new Pet(PetSpec.forName("pandaren water spirit"), PetBreed.HH, PetQuality.RARE, 25),
          new AbilitySet(1,1,2)
      );
      BattlePet pet5 = new BattlePet(
          new Pet(PetSpec.forName("murky"), PetBreed.HH, PetQuality.RARE, 25),
          new AbilitySet(1,1,2)
      );
      BattlePet pet6 = new BattlePet(
          new Pet(PetSpec.forName("son of sethe"), PetBreed.HH, PetQuality.RARE, 25),
          new AbilitySet(1,1,2)
      );
      
      BattleTeam team1 = new BattleTeam(pet1, pet2, pet3);
      BattleTeam team2 = new BattleTeam(pet3, pet5, pet6);
      
      Battle battle = new Battle(team1, team2);
      
      UI.battleFrame.panel().setBattle(battle);
      UI.battleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
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
