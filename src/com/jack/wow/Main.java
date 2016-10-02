package com.jack.wow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import com.google.gson.*;
import com.jack.wow.data.PetSpec;
import com.jack.wow.json.ImplicitContextedAdapter;

public class Main
{
  public static void main(String[] args)
  {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(PetSpec.class, new ImplicitContextedAdapter<PetSpec>(PetSpec.class));
    Gson gson = builder.create();
    
    PetSpec[] pets;
    
    try
    {
      pets = gson.fromJson(new BufferedReader(new FileReader("./data/pets.json")), PetSpec[].class);
      System.out.printf("Loaded %s pets.", pets.length);

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    
  }
}
