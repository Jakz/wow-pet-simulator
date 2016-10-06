package com.jack.wow.files.api;

import com.jack.wow.data.PetFamily;

public class ApiPet
{
  public boolean canBattle;
  public int creatureId;
  public int typeId;
  public String name;
  public String family;
  public String icon;
  public int qualityId;
  public ApiStats stats;
  public String[] strongAgainst;
  public String[] weakAgainst;
  
  public boolean isConsistent()
  {
    PetFamily family = PetFamily.unserialize(this.family);
    
    if (family == null)
      throw new IllegalArgumentException("Unknown family name: "+this.family);
    else if (family.id != typeId)
      throw new IllegalArgumentException("Mismatching family with typeId: "+family.id+" != "+typeId);
        
    if (strongAgainst == null || weakAgainst == null)
      throw new IllegalArgumentException("strongAgainst and weakAgainst are not present or are not arrays for pet "+this.name);
    
    if (strongAgainst.length != 1 || weakAgainst.length != 1)
      throw new IllegalArgumentException("strongAgainst and weakAgainst must be of size 1 for pet "+this.name);

    PetFamily strongFamily = PetFamily.unserialize(strongAgainst[0]);
    PetFamily weakFamily = PetFamily.unserialize(weakAgainst[0]);
    
    if (strongFamily != family.getStrongAttacking())
      throw new IllegalArgumentException("wrong matching for strong family for pet "+this.name+": expected "+family.getStrongAttacking()+", found "+strongFamily);
    if (weakFamily != family.getWeakAttacking())
      throw new IllegalArgumentException("wrong matching for weak family for pet "+this.name+": expected "+family.getWeakAttacking()+", found "+weakFamily);
    
    return true;
  }
}
