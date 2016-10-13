package com.jack.wow.battle;

import com.jack.wow.data.PetFamily;

public class AttackInfo
{
  float[] values;
  PetFamily[] family;
  
  public AttackInfo(PetFamily family, float... values)
  {
    this.values = values;
    this.family = new PetFamily[] { family };
  }
  
  public float value() { return values[0]; }
  public float value(int i) { return values[i]; }
  
  public PetFamily family() { return family[0]; }
  public PetFamily family(int i) { return family[i]; }
  
  public int count() { return values.length; }
  public boolean hasMultipleFamilies() { return family.length > 1; }
}
