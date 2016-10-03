package com.jack.wow.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.jack.wow.json.JsonnableContext;

public class PetSpec implements JsonnableContext
{
  public String name;
  public PetFamily family;
  
  public boolean canBattle;
  public int creatureId;
  
  public String icon;

  public void unserialize(JsonElement element, JsonDeserializationContext context) throws IllegalAccessException
  {
    JsonObject o = element.getAsJsonObject();
    
    /* parsing general data */
    this.name = o.get("name").getAsString();
    this.creatureId = o.get("creatureId").getAsInt();
    this.canBattle = o.get("canBattle").getAsBoolean();
    
    this.icon = o.get("icon").getAsString();
    
    /* parsing family */
    PetFamily family = PetFamily.unserialize(o.get("family").getAsString());
    
    if (family == null)
      throw new IllegalArgumentException("Unknown family name: "+o.get("family"));
    else if (family.id != o.get("typeId").getAsInt())
      throw new IllegalArgumentException("Mismatching family with typeId: "+family.id+" != "+o.get("typeId"));
    
    this.family = family;
    
    /* verifying strongAgainst and weakAgainst */
    {
      JsonElement e1 = o.get("strongAgainst"), e2 = o.get("weakAgainst");
      
      if (!e1.isJsonArray() || !e2.isJsonArray())
        throw new IllegalArgumentException("strongAgainst and weakAgainst are not present or are not arrays for pet "+this.name);
      
      JsonArray strong = e1.getAsJsonArray(), weak = e2.getAsJsonArray();
      
      if (strong.size() != 1 || weak.size() != 1)
        throw new IllegalArgumentException("strongAgainst and weakAgainst must be of size 1 for pet "+this.name);

      PetFamily strongFamily = PetFamily.unserialize(strong.get(0).getAsString());
      PetFamily weakFamily = PetFamily.unserialize(weak.get(0).getAsString());
      
      if (strongFamily != this.family.getStrongFamily())
        throw new IllegalArgumentException("wrong matching for strong family for pet "+this.name+": expected "+this.family.getStrongFamily()+", found "+strongFamily);
      if (weakFamily != this.family.getWeakFamily())
        throw new IllegalArgumentException("wrong matching for weak family for pet "+this.name+": expected "+this.family.getWeakFamily()+", found "+weakFamily);
    }
        
    this.family = family;
  }

  @Override
  public JsonElement serialize(JsonSerializationContext context) throws IllegalAccessException
  {
    throw new IllegalStateException("PetSpec is not serializable");
  }
  
  
}
