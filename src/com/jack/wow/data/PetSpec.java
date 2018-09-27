package com.jack.wow.data;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.jack.wow.data.interfaces.Abilited;
import com.jack.wow.data.interfaces.Iconed;
import com.jack.wow.data.interfaces.Specced;
import com.jack.wow.data.interfaces.Statsed;
import com.jack.wow.files.api.ApiAbility;
import com.jack.wow.files.api.ApiSpecie;
import com.pixbits.lib.json.JsonnableContext;

public class PetSpec implements JsonnableContext, Abilited, Statsed, Specced, Iconed
{  
  public String name;
  public PetFamily family;
  public int id;
  public boolean usable;
  
  public boolean canBattle;
  public int creatureId;
  
  public final List<?>[] abilities = new List<?>[] { 
    new ArrayList<PetOwnedAbility>(),
    new ArrayList<PetOwnedAbility>(),
    new ArrayList<PetOwnedAbility>()
  };
    
  public String icon;
  public String description;
  public String source;
  public PetStats stats = new PetStats(8,8,8);
  
  public boolean areAbilitiesPresent()
  {
    return Arrays.stream(abilities).anyMatch(s -> !s.isEmpty());
  }
  
  public void markUsable() { usable = true; }
  
  public PetSpec() { }
  
  @SuppressWarnings("unchecked")
  public List<PetOwnedAbility> slot(int index) { return (List<PetOwnedAbility>)abilities[index]; }

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
      PetAbility ability = PetAbility.forId(a.id);
      
      if (ability == null)
        throw new IllegalArgumentException("ability "+a.id+" not found");
      
      slot(a.slot).add(new PetOwnedAbility(ability, a.order, a.slot, a.requiredLevel));
    }
    
    for (int i = 0; i < 3; ++i)
      slot(i).sort((a1, a2) -> Integer.compare(a1.order(), a2.order()));
  }
  

  @Override public PetAbility getAbility(int slot, int index)
  {
    List<PetOwnedAbility> abilities = slot(slot);
    return index < abilities.size() ? abilities.get(index).get() : null;
  }
  
  @Override public PetStats stats() { return new PetStats(8,8,8); }
  @Override public PetSpec spec() { return this; }
  @Override public String iconName() { return icon; }
  
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
    
    for (int i = 0; i < pa.size(); ++i)
      abilities[i] = context.deserialize(pa.get(i), new TypeToken<List<PetOwnedAbility>>(){}.getType());
    
    for (int i = 0; i < abilities.length; ++i)
      slot(i).sort((a1, a2) -> Integer.compare(a1.order(), a2.order()));

    
    this.stats = context.deserialize(o.get("stats"), PetStats.class);
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
    
    JsonArray aabilities = new JsonArray();
    for (int i = 0; i < abilities.length; ++i)
      aabilities.add(context.serialize(slot(i)));

    object.add("abilities", aabilities);
    object.add("stats", context.serialize(stats));
    
    
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
