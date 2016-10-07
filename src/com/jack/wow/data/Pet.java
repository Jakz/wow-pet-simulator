package com.jack.wow.data;

import com.jack.wow.data.interfaces.Abilited;
import com.jack.wow.data.interfaces.Qualitied;
import com.jack.wow.data.interfaces.Statsed;

/**
 * This class represents a real pet, so it associates a PetSpec with a specific breed, quality and level.
 * 
 * @author jack
 */
public class Pet implements Abilited, Qualitied, Statsed
{
  private final PetSpec spec;
  private PetBreed breed;
  private PetQuality quality;
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
  public PetStats stats() { return Formulas.adjustedStats(spec.stats(), breed, level, quality); }
  
  public void setLevel(int level) { this.level = level; }
  public void setQuality(PetQuality quality) { this.quality = quality; }
  public void setBreed(PetBreed breed) { this.breed = breed; }

  @Override public PetAbility getAbility(int slot, int index) { return spec.getAbility(slot, index); }
}
