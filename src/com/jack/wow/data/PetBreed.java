package com.jack.wow.data;

import java.util.Arrays;

public enum PetBreed
{
  BB("B/B",  3, 13,  0.5f,  0.5f,  0.5f),
  
  PP("P/P",  4, 14,  0, 2.f,  0),
  SS("S/S",  5, 15,  0,  0, 2.f),
  HH("H/H",  6, 16, 2.f,  0,  0),
  
  HP("H/P",  7, 17,  .9f,  .9f,  0),
  PS("P/S",  8, 18,  0,  .9f,  .9f),
  HS("H/S",  9, 19,  .9f,  0,  .9f),
  
  PB("P/B", 10, 20,  .4f,  .9f,  .4f),
  SB("S/B", 11, 21,  .4f,  .4f,  .9f),
  HB("H/B", 12, 22,  .9f,  .4f,  .4f)

;
  
  PetBreed(String description, int maleId, int femaleId, float health, float power, float speed)
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
  public final float health;
  public final float power;
  public final float speed;
  
  public static int count() { return values().length; }
  
  public static PetBreed forID(int id)
  {
    return Arrays.stream(values()).filter(b -> b.maleId == id || b.femaleId == id).findFirst().orElse(null);
  }
}
