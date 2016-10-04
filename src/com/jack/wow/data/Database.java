package com.jack.wow.data;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.jack.wow.files.Json;
import com.jack.wow.json.JsonnableContext;

public class Database implements JsonnableContext
{
  public PetAbility[] abilities;
  public PetSpec[] pets;
  
  public Database() { }
  
  public Database(Map<Integer, PetAbility> abilities, PetSpec[] pets)
  {
    this.abilities = abilities.values().toArray(new PetAbility[abilities.size()]);
    Arrays.sort(this.abilities, (a1, a2) -> Integer.compare(a1.id, a2.id));
    this.pets = pets;
  }
  
  public void save(Path fileName)
  {
    Gson gson = Json.build();
    
    try (BufferedWriter wrt = Files.newBufferedWriter(fileName))
    {
      gson.toJson(this, wrt);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override public JsonElement serialize(JsonSerializationContext context) throws IllegalAccessException
  {
    JsonObject o = new JsonObject();
    
    o.add("pets", context.serialize(pets));
    o.add("abilities", context.serialize(abilities));
    
    return o;
  }

  @Override public void unserialize(JsonElement element, JsonDeserializationContext context) throws IllegalAccessException
  {
    JsonObject o = element.getAsJsonObject();
    
    abilities = context.deserialize(o.get("abilities"), PetAbility[].class);
    
    for (PetAbility ability : abilities)
      PetAbility.data.put(ability.id, ability);

    PetSpec.data = context.deserialize(o.get("pets"), PetSpec[].class);
  }
}
