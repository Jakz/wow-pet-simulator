package com.jack.wow.data;

public class PetOwnedAbility
{
  public final PetAbility ability;
  public final int order;
  public final  int slot;
  public final int requiredLevel;
  
  public PetOwnedAbility(PetAbility ability, int order, int slot, int requiredLevel)
  {
    this.ability = ability;
    this.order = order;
    this.slot = slot;
    this.requiredLevel = requiredLevel;
  }
}
