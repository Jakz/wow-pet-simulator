package com.jack.wow.data;

public class Formulas
{
  public static int MAX_LEVEL = 25;
  public static int MIN_LEVEL = 1;
  public static int LEVELS = MAX_LEVEL - MIN_LEVEL + 1;
  
  public static float FAMILY_BONUS_MULTIPLIER = 1.5f;
  public static float FAMILY_MALUS_MULTIPLIER = 1.0f/3.0f;
  
  private static int round(float v)
  {
    int s = Math.round(v / 0.1f);
    return Math.round(s*0.1f);
  }
  
  /* ((baseHealth + breedHealth) * 5 * level * quality) + 100 */
  public static int hitPoints(PetStats stats, PetBreed breed, int level, PetQuality quality)
  {
     return round(((stats.health() + breed.health) * 5.0f * level * quality.coefficient) + 100);
  }
  
  /* (basePower + breedPower) * level * quality */
  public static int power(PetStats stats, PetBreed breed, int level, PetQuality quality)
  {
    return round((stats.power() + breed.power) * level * quality.coefficient);
  }
  
  /* (baseSpeed + breedSpeed) * level * quality */
  public static int speed(PetStats stats, PetBreed breed, int level, PetQuality quality)
  {
    return round((stats.speed() + breed.speed) * level * quality.coefficient);
  }
  
  /* baseDmg + power*(baseDmg*0.05f) */
  public static int standardDamage(float baseDamage, float power)
  {
    return round(baseDamage + (power*0.05f)*baseDamage);
  }
  
  public static int damageWithRange(int minDamage, int maxDamage, float hitChance)
  {
    return 0;
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
