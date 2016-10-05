package com.jack.wow.data;

/**
 * This class represents a real pet, so it associates a PetSpec with a specific breed, quality and level.
 * 
 * @author jack
 */
public class Pet
{
  private PetSpec spec;
  private PetBreed breed;
  private PetQuality quality;
  private PetStats stats;
  private int level;
  
  public Pet(PetSpec spec, PetBreed breed, PetQuality quality, int level)
  {
    this.spec = spec;
    this.breed = breed;
    this.quality = quality;
    this.level = level;
  }
  
  public PetSpec spec() { return spec; }
  public PetBreed breed() { return breed; }
  public PetQuality quality() { return quality; }
  public int level() { return level; }
  public PetStats stats() { return new PetStats(8,8,8); }
  
}
