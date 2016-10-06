package com.jack.wow.data;

import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.jack.wow.files.api.ApiAbility;
import com.jack.wow.files.api.ApiPet;
import com.jack.wow.files.api.ApiSpecie;
import com.jack.wow.json.JsonnableContext;

public class PetSpec implements JsonnableContext
{  
  public String name;
  public PetFamily family;
  public int id;
  public boolean usable;
  
  public boolean canBattle;
  public int creatureId;
  
  public final PetOwnedAbility[] abilities = new PetOwnedAbility[6];
  
  public String icon;
  public String description;
  public String source;
  
  public boolean areAbilitiesPresent()
  {
    return Arrays.stream(abilities).allMatch(oa -> oa != null);
  }
  
  public void markUsable() { usable = true; }
  
  public PetSpec() { }

  public PetSpec(ApiSpecie s)
  {
    this.usable = false;
    
    this.name = s.name;
    this.family = PetFamily.unserialize(s.petTypeId);
    this.id = s.speciesId;
    this.canBattle = s.canBattle;
    this.creatureId = s.creatureId;
    
    this.icon = s.icon;
    this.description = s.description;
    this.source = s.source;
    
    for (ApiAbility a : s.abilities)
    {
      PetAbility ability = PetAbility.get(a.id);
      
      if (ability == null)
        throw new IllegalArgumentException("ability "+a.id+" not found");
      
      if (a.order < abilities.length) /* required for Enchanted Pen which returns 9?! abilities */
        abilities[a.order] = new PetOwnedAbility(ability, a.order, a.slot, a.requiredLevel);
    }
  }

  public void unserialize(JsonElement element, JsonDeserializationContext context) throws IllegalAccessException
  {
    JsonObject o = element.getAsJsonObject();
    
    /* parsing general data */
    this.name = o.get("name").getAsString();
    //this.creatureId = o.get("creatureId").getAsInt();
    this.canBattle = o.get("canBattle").getAsBoolean();
    this.usable = o.get("usable").getAsBoolean();
    
    this.icon = o.get("icon").getAsString();
    this.id = o.get("id").getAsInt();
    
    this.family = PetFamily.unserialize(o.get("family").getAsString());

    this.source = o.get("source").getAsString();
    this.description = o.get("description").getAsString();
    
    JsonArray pa = o.get("abilities").getAsJsonArray();
    
    for (int i = 0; i < abilities.length; ++i)
      abilities[i] = context.deserialize(pa.get(i), PetOwnedAbility.class);
  }

  @Override public JsonElement serialize(JsonSerializationContext context) throws IllegalAccessException
  {
    JsonObject object = new JsonObject();
    
    object.add("name", new JsonPrimitive(name));
    object.add("family", new JsonPrimitive(family.jsonName));
    object.add("id", new JsonPrimitive(id));
    object.add("canBattle", new JsonPrimitive(canBattle));
    object.add("usable", new JsonPrimitive(usable));
    object.add("icon", new JsonPrimitive(icon));
    
    object.add("description", new JsonPrimitive(description));
    object.add("source", new JsonPrimitive(source));

    object.add("abilities", context.serialize(abilities));
    
    
    return object;
  }
  
  
  public static PetSpec[] data;
  
  public static PetSpec forName(String name)
  {
    PetSpec spec = Arrays.stream(data).filter(p -> p.name.compareToIgnoreCase(name) == 0).findFirst().orElse(null);
    
    if (spec == null)
      throw new IllegalArgumentException("PetSpec \'"+name+"\' not found.");
    
    return spec;
  }
  
  public static PetSpec forId(int id)
  {
    PetSpec spec = Arrays.stream(data).filter(p -> p.id == id).findFirst().orElse(null);
    
    if (spec == null)
      throw new IllegalArgumentException("PetSpec \'id="+id+"\' not found.");
    
    return spec;
  }
  
  
}
