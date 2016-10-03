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
import com.jack.wow.data.PetSpec;
import com.jack.wow.files.IconDownloader;
import com.jack.wow.json.ImplicitContextedAdapter;
import com.jack.wow.ui.PetListPanel;

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
    
    PetSpec[] pets;
    
    try
    {
      pets = gson.fromJson(new BufferedReader(new FileReader("./data/pets.json")), PetSpec[].class);
      System.out.printf("Loaded %s pets.", pets.length);
      
      Set<String> icons = Arrays.stream(pets).map(p -> p.icon).collect(Collectors.toSet());
      
      
      IconDownloader downloader = new IconDownloader(null, icons);
      
      downloader.start();
      
      PetListPanel petListPanel = new PetListPanel(300, 800);
      
      petListPanel.populate(Arrays.asList(pets), p -> true);
      
      JFrame frame = new JFrame();
      frame.setTitle("Pet List");
      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(petListPanel, BorderLayout.CENTER);
      frame.pack();
      frame.setVisible(true);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    
  }
}
