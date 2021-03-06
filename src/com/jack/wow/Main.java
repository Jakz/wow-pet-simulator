package com.jack.wow;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.google.gson.*;
import com.jack.wow.battle.AbilitySet;
import com.jack.wow.battle.Battle;
import com.jack.wow.battle.BattlePet;
import com.jack.wow.battle.BattleTeam;
import com.jack.wow.battle.abilities.EffectApply;
import com.jack.wow.battle.abilities.Effects;
import com.jack.wow.battle.abilities.Target;
import com.jack.wow.data.Database;
import com.jack.wow.data.InvalidIdException;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetBreed;
import com.jack.wow.data.PetQuality;
import com.jack.wow.data.PetSpec;
import com.jack.wow.data.interfaces.Iconed;
import com.jack.wow.files.IconDownloader;
import com.jack.wow.files.Json;
import com.jack.wow.files.api.ApiFetcher;
import com.jack.wow.files.api.ApiMasterList;
import com.jack.wow.files.api.ApiPet;
import com.jack.wow.files.api.ApiSpecie;
import com.jack.wow.files.api.DatabaseBuilder;
import com.jack.wow.files.api.WowHeadFetcher;
import com.jack.wow.ui.UI;
import com.pixbits.lib.ui.UIUtils;

public class Main
{
  public static void buildDatabase() throws IOException, InterruptedException
  {
    DatabaseBuilder builder = new DatabaseBuilder();
    
    List<PetAbility> abilities = builder.fetchAbilitiesFromAPI();
    System.out.println("Fetched "+abilities.size()+" abilities");
    abilities.stream().forEach(PetAbility::add);

    List<PetSpec> pets = builder.fetchPetsFromAPI();
    System.out.println("Fetched "+pets.size()+" pets");
    PetSpec.data = pets.toArray(new PetSpec[pets.size()]);

    List<WowHeadFetcher.TooltipInfo> tooltips = builder.fetchTooltipsFromWowHead();
    System.out.println("Fetched "+tooltips.size()+" tooltips");
    
    for (WowHeadFetcher.TooltipInfo tooltip : tooltips)
    {
      try
      {
        PetAbility ability = PetAbility.forId(tooltip.id);
        if (tooltip.hitChance.isPresent())
          ability.setHitChance(tooltip.hitChance.get());
      
        ability.setTooltip(tooltip.description);
      }
      catch (InvalidIdException e)
      {
        // ignore
      }
    }

    /* mark all usable pets */
    ApiMasterList master = ApiFetcher.fetchMasterList();
    for (ApiPet pet : master.pets)
    {
      try
      {
        PetSpec.forId(pet.stats.speciesId).markUsable();
      }
      catch (IllegalArgumentException e)
      {
        //
      }
    }

    Database db = new Database(PetAbility.data.values(), PetSpec.data);
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
       
        /*DatabaseBuilder builder = new DatabaseBuilder();
        
        List<Iconed> iconed = new ArrayList<>(Arrays.asList(PetSpec.data));
        iconed.addAll(PetAbility.data.values());
        
        builder.fetchIconsFromWowHead(iconed);*/
        

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
    UIUtils.setNimbusLNF();
    
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
      
      System.out.printf("Loaded %s pets.\n", PetSpec.data.length);
      System.out.printf("Loaded %d abilities\n", PetAbility.data.size());
      
      PetAbility.prune();
      PetAbility.computeUsageOfAbilities();

      Effects.init();
      
      Set<String> icons = Arrays.stream(PetSpec.data).map(p -> p.icon).collect(Collectors.toSet());
      PetAbility.data.values().stream().map(a -> a.icon).forEach(icons::add);

      IconDownloader downloader = new IconDownloader(null, icons);
      
      //downloader.start();
      
      UI.init();

      
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
      
      pet1.abilityStatus(1).startCooldown();
      pet2.hurt(pet2.hitPoints());
      
      pet1.hurt(pet1.hitPoints()/2);
      
      pet3.effects().add(new EffectApply(PetAbility.forName("shattered defenses"), Target.SELF, 2));
      pet3.effects().add(new EffectApply(PetAbility.forName("pumped up"), Target.SELF, 2));

      BattleTeam team1 = new BattleTeam(pet1, pet2, pet3);
      BattleTeam team2 = new BattleTeam(pet3, pet5, pet6);
      
      team2.effects().add(new EffectApply(PetAbility.forId(365), Target.TEAM, 9));
      
      Battle battle = new Battle(team1, team2);
      
      UI.battleFrame.panel().setBattle(battle);
      UI.battleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
