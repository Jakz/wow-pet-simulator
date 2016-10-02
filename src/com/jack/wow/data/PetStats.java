package com.jack.wow.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PetStats
{
  private int health;
  private int power;
  private int speed;
  
  public int health() { return health; }
  public int power() { return power; }
  public int speed() { return speed; }
  
  
  public void unserialize(JsonElement element, JsonDeserializationContext context) throws IllegalAccessException
  {
    JsonObject o = element.getAsJsonObject();
    
    this.power = o.get("power").getAsInt();
    this.speed = o.get("speed").getAsInt();
  }
}
