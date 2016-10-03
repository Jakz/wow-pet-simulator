package com.jack.wow;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.google.gson.*;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetSpec;
import com.jack.wow.files.IconDownloader;
import com.jack.wow.files.api.ApiAbility;
import com.jack.wow.files.api.ApiFetcher;
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
  
  public static void main(String[] args)
  {
    setLNF();
    
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(PetSpec.class, new ImplicitContextedAdapter<PetSpec>(PetSpec.class));
    Gson gson = builder.create();
    
    try
    {
      //ApiSpecie specie = ApiFetcher.fetchSpecie(258);
      
      PetSpec.data = gson.fromJson(new BufferedReader(new FileReader("./data/pets.json")), PetSpec[].class);
      System.out.printf("Loaded %s pets.", PetSpec.data.length);
      
      Set<String> icons = Arrays.stream(PetSpec.data).map(p -> p.icon).collect(Collectors.toSet());
      
      
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
