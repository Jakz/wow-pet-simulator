package com.jack.wow.json;

import com.google.gson.JsonElement;

public interface Jsonnable<T>
{
  public JsonElement serialize();
  public void unserialize(JsonElement element);
}
