package com.jack.wow.data;

import java.util.Arrays;

public enum PetFamily
{
  HUMANOID("Humanoid", "humanoid", 0),
  DRAGONKIN("Dragonkin", "dragonkin", 1),
  FLYING("Flying", "flying", 2),
  UNDEAD("Undead", "undead", 3),
  CRITTER("Critter", "critter", 4),
  MAGICAL("Magical", "magical", 5),
  ELEMENTAL("Elemental", "elemental", 6),
  BEAST("Beast", "beast", 7),
  WATER("Water", "water", 8),
  MECHANICAL("Mechanical", "mechanical", 9)
  ;
  
  private PetFamily(String description, String jsonName, int id)
  {
    this.description = description;
    this.jsonName = jsonName;
    this.id = id;
  }
  
  public final String description;
  public final String jsonName;
  public final int id;
  
  private PetFamily strongVS;
  private PetFamily weakVS;
  
  public PetFamily getStrongFamily() { return strongVS; }
  public PetFamily getWeakFamily() { return weakVS; }
  
  
  public static PetFamily unserialize(String name)
  {
    return Arrays.stream(values()).filter(c -> c.jsonName.equals(name)).findFirst().orElse(null);
  }
  
  public static PetFamily unserialize(int id)
  {
    return Arrays.stream(values()).filter(c -> c.id == id).findFirst().orElse(null);
  }
  
  static
  {
    PetFamily.HUMANOID.strongVS = PetFamily.DRAGONKIN;
    PetFamily.HUMANOID.weakVS = PetFamily.BEAST;
    
    PetFamily.DRAGONKIN.strongVS = PetFamily.MAGICAL;
    PetFamily.DRAGONKIN.weakVS = PetFamily.UNDEAD;
    
    PetFamily.FLYING.strongVS = PetFamily.WATER;
    PetFamily.FLYING.weakVS = PetFamily.DRAGONKIN;
    
    PetFamily.UNDEAD.strongVS = PetFamily.HUMANOID;
    PetFamily.UNDEAD.weakVS = PetFamily.WATER;
    
    PetFamily.CRITTER.strongVS = PetFamily.UNDEAD;
    PetFamily.CRITTER.weakVS = PetFamily.HUMANOID;
    
    PetFamily.MAGICAL.strongVS = PetFamily.FLYING;
    PetFamily.MAGICAL.weakVS = PetFamily.MECHANICAL;
    
    PetFamily.ELEMENTAL.strongVS = PetFamily.MECHANICAL;
    PetFamily.ELEMENTAL.weakVS = PetFamily.CRITTER;
    
    PetFamily.BEAST.strongVS = PetFamily.CRITTER;
    PetFamily.BEAST.weakVS = PetFamily.FLYING;
    
    PetFamily.WATER.strongVS = PetFamily.ELEMENTAL;
    PetFamily.WATER.weakVS = PetFamily.MAGICAL;
    
    PetFamily.MECHANICAL.strongVS = PetFamily.BEAST;
    PetFamily.MECHANICAL.weakVS = PetFamily.ELEMENTAL;
    
    for (PetFamily family : values())
      if (family.getStrongFamily() == null || family.getWeakFamily() == null)
        throw new IllegalArgumentException("Strong or weak family is null for "+family.description);
    
  }
}
