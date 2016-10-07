package com.jack.wow.data;

/**
 * This class represents a real pet, so it associates a PetSpec with a specific breed, quality and level.
 * 
 * @author jack
 */
public class Pet implements Abilited, Qualitied
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
  
  public void setLevel(int level) { this.level = level; }
  public void setQuality(PetQuality quality) { this.quality = quality; }

  @Override public PetAbility getAbility(int slot, int index) { return spec.getAbility(slot, index); }
}
