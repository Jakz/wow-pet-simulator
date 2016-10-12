package com.jack.wow.battle;

public class PetAction
{ 
  private final BattlePet swapToPet;
  private final boolean pass;
  private final BattleAbilityStatus ability;
    
  private PetAction(BattleAbilityStatus ability, BattlePet swapToPet, boolean pass)
  {
    this.swapToPet = swapToPet;
    this.pass = pass;
    this.ability = ability;
  }
  
  public static PetAction swapTo(BattlePet pet) { return new PetAction(null, pet, false); }
  public static PetAction pass() { return new PetAction(null, null, true); }
  public static PetAction useAbility(BattleAbilityStatus ability) { return new PetAction(ability, null, false); }
  
  public boolean isSwap() { return swapToPet != null; }
  public boolean isPass() { return pass; }
  public boolean isAbility() { return ability != null; }
  
  public BattlePet getSwapPet() { return swapToPet; }
  public BattleAbilityStatus getAbility() { return ability; }
}
