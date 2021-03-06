package com.jack.wow.files.api;

public class ApiSpecie
{
  public String name;
  
  public int speciesId;
  public int petTypeId;
  public int creatureId;
  
  public boolean canBattle;
  public String icon;
  public String description;
  public String source;
  
  public ApiAbility[] abilities;
  
  public String toString() { return String.format("(%s, id: %d, pid: %d, cid: %d)", name, speciesId, petTypeId, creatureId); }
}
