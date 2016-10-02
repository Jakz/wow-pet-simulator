package com.jack.wow.data;

public enum PetQuality
{
  POOR("Poor", 0, 1.0f),
  COMMON("Common", 1, 1.1f),
  UNCOMMON("Uncommon", 2, 1.2f),
  RARE("Rare", 3, 1.3f),
  EPIC("Epic", 4, 1.4f),
  LEGENDARY("Legendary", 5, 1.5f)
  ;
  
  public final String description;
  public final int id;
  public final float coefficient;
  
  private PetQuality(String description, int id, float coefficient)
  {
    this.description = description;
    this.id = id;
    this.coefficient = coefficient;
  }
}
