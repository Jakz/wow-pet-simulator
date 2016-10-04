package com.jack.wow.data;

import java.util.Objects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jack.wow.files.api.ApiStats;

public class PetStats
{
  private float health;
  private float power;
  private float speed;
  
  public float health() { return health; }
  public float power() { return power; }
  public float speed() { return speed; }
  
  public PetStats(float health, float power, float speed)
  {
    this.health = health;
    this.power = power;
    this.speed = speed;
  }
  
  public PetStats(ApiStats s)
  {
    this(s.health, s.power, s.speed);
  }
  
  public String toString() { return String.format("[%f, %f, %f]", health, power, speed); }
  
  public PetStats derive(PetBreed breed)
  {
    return new PetStats(health + breed.health, power + breed.power, speed + breed.speed);
  }
  
  @Override public boolean equals(Object o)
  {
    if (o instanceof PetStats)
    {
      PetStats s = (PetStats)o;
      return health == s.health && power == s.power && speed == s.speed;
    }
    else
      return false;
  }
  
  @Override public int hashCode() { return Objects.hash(health, power, speed); }
}
