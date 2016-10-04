package com.jack.wow.data;

public class Formulas
{
  public static int MAX_LEVEL = 25;
  public static int MIN_LEVEL = 1;
  public static int LEVELS = MAX_LEVEL - MIN_LEVEL + 1;
  
  /* ((baseHealth + breedHealth) * 5 * level * quality) + 100 */
  public static int hitPoints(PetStats stats, PetBreed breed, int level, PetQuality quality)
  {
     return Math.round(((stats.health() + breed.health) * 5.0f * level * quality.coefficient) + 100);
  }
  
  /* (basePower + breedPower) * level * quality */
  public static int power(PetStats stats, PetBreed breed, int level, PetQuality quality)
  {
    return Math.round((stats.power() + breed.power) * level * quality.coefficient);
  }
  
  /* (baseSpeed + breedSpeed) * level * quality */
  public static int speed(PetStats stats, PetBreed breed, int level, PetQuality quality)
  {
    return Math.round((stats.speed() + breed.speed) * level * quality.coefficient);
  }
  
  public static PetStats adjustedStats(PetStats stats, PetBreed breed, int level, PetQuality quality)
  {
    return new PetStats(
      hitPoints(stats, breed, level, quality),
      power(stats, breed, level, quality),
      speed(stats, breed, level, quality)
    );
  }
}
