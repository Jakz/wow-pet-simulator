package com.jack.wow.data;

import java.util.Random;

public class Formulas
{
  private static final Random random = new Random();
  
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
  
  private static int floor(float f) { return (int)Math.floor(f); }
  
  public static boolean chance(float c) { return random.nextFloat() < c; }
  
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
  
  /**
   * Standard power formula to obtain damage/heal from base power and pet power.
   * It's <code>base + (power*base)/20</code>
   * @param base power of ability
   * @param power power of pet
   * @return final power of ability
   */
  public static int standardDamage(float baseDamage, float power)
  {
    return round(baseDamage + (power*0.05f)*baseDamage);
  }
  
  public static int damageWithRange(float baseDamage, float variance, float hitChance, boolean isCritical)
  {
    int minDamage = floor(baseDamage * (1.0f - variance));
    int maxDamage = floor(baseDamage * (1.0f + variance));
    
    int halfStep = (maxDamage - minDamage)/2;
    
    /* toss three values and keep the higher */
    int max = random.ints(3, 0, halfStep + 1).max().getAsInt();
    
    /* chance of damage to be added base is 50%, if hit > 100% then it's 50% + (hit - 100%)
     * critical hits are always positive
     */
    boolean isPositive = isCritical || chance(hitChance > 1.0f ? (hitChance - 0.5f) : 0.5f);
    
    return isPositive ? (minDamage + max) : (minDamage - max);
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
