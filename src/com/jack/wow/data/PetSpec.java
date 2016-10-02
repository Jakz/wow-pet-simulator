package com.jack.wow.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.jack.wow.json.JsonnableContext;

public class PetSpec
{
  String name;

  public void unserialize(JsonElement element, JsonDeserializationContext context) throws IllegalAccessException
  {
    JsonObject o = element.getAsJsonObject();
    
    name = o.get("name").getAsString();
  }
  
  
}
