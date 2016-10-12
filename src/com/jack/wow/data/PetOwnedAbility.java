package com.jack.wow.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.jack.wow.json.JsonnableContext;

public class PetOwnedAbility implements JsonnableContext
{
  private PetAbility ability;
  private int order;
  private int slot;
  private int requiredLevel;
  
  public PetOwnedAbility() { }
  
  public PetOwnedAbility(PetAbility ability, int order, int slot, int requiredLevel)
  {
    this.ability = ability;
    this.order = order;
    this.slot = slot;
    this.requiredLevel = requiredLevel;
  }
  
  public PetAbility get() { return ability; }
  public int order() { return order; }
  public int slot() { return slot; }
  public int requiredLevel() { return requiredLevel; }
  

  @Override public JsonElement serialize(JsonSerializationContext context) throws IllegalAccessException
  {
    JsonObject object = new JsonObject();
    
    object.add("id", new JsonPrimitive(ability.id));
    object.add("order", new JsonPrimitive(order));
    object.add("slot", new JsonPrimitive(slot));
    object.add("level", new JsonPrimitive(requiredLevel));
    
    return object;
  }

  @Override public void unserialize(JsonElement element, JsonDeserializationContext context) throws IllegalAccessException
  {
    JsonObject object = element.getAsJsonObject();
    
    PetAbility ability = PetAbility.forId(object.get("id").getAsInt());

    this.ability = ability;
    this.order = object.get("order").getAsInt();
    this.slot = object.get("slot").getAsInt();
    this.requiredLevel = object.get("level").getAsInt(); 
  }
}
