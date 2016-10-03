package com.jack.wow.data;

import java.nio.file.AccessMode;
import java.util.HashMap;
import java.util.Map;

import com.jack.wow.files.api.ApiAbility;

public class PetAbility
{  
  int id;
  String name;

  PetFamily family;
  int cooldown;
  int rounds;
  boolean isPassive;
  
  String description;
  String icon;
  
  private PetAbility(int id, String name, PetFamily family, int cooldown, int rounds, boolean isPassive, String icon)
  {
    this.id = id;
    this.name = name;
    this.family = family;
    this.cooldown = cooldown;
    this.rounds = rounds;
    this.isPassive = isPassive;
    this.icon = icon;
  }
  
  public static final Map<Integer, PetAbility> data = new HashMap<>();
  
  public static PetAbility get(int id) { return data.get(id); }
  
  public static PetAbility generate(int id, String name, PetFamily family, int cooldown, int rounds, boolean isPassive, String icon)
  {
    PetAbility ability = new PetAbility(id, name, family, cooldown, rounds, isPassive, icon);
    data.putIfAbsent(id, ability);
    return ability;
  }
 
  public static PetAbility generate(ApiAbility a)
  {
    PetAbility ability = new PetAbility(a.id, a.name, PetFamily.unserialize(a.petTypeId), a.cooldown, a.rounds, a.isPassive, a.icon);
    data.putIfAbsent(a.id, ability);
    return ability;
  }
  
}
