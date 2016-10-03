package com.jack.wow.data;

import java.util.HashMap;
import java.util.Map;

public class Database
{
  public final PetAbility[] abilities;
  
  Database(Map<Integer, PetAbility> abilities)
  {
    this.abilities = abilities.values().toArray(new PetAbility[abilities.size()]);
  }
}
