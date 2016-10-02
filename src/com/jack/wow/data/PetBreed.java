package com.jack.wow.data;

import java.util.Arrays;

public enum PetBreed
{
  BB("B/B",  3, 13,  5,  5,  5),
  
  PP("P/P",  4, 14,  0, 20,  0),
  SS("S/S",  5, 15,  0,  0, 20),
  HH("H/H",  6, 16, 20,  0,  0),
  
  HP("H/P",  7, 17,  9,  9,  0),
  PS("P/S",  8, 18,  0,  9,  9),
  HS("H/S",  9, 19,  9,  0,  9),
  
  PB("P/B", 10, 20,  4,  9,  4),
  SB("S/B", 11, 21,  4,  4,  9),
  HB("H/B", 12, 22,  9,  4,  4)

;
  
  PetBreed(String description, int maleId, int femaleId, int health, int power, int speed)
  {
    this.description = description;
    this.maleId = maleId;
    this.femaleId = femaleId;
    this.health = health;
    this.power = power;
    this.speed = speed;
  }
  
  
  public final String description;
  public final int maleId, femaleId;
  public final int health;
  public final int power;
  public final int speed;
  
  
  public static PetBreed forID(int id)
  {
    return Arrays.stream(values()).filter(b -> b.maleId == id || b.femaleId == id).findFirst().orElse(null);
  }
}
