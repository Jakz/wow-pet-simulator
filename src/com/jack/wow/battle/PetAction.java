package com.jack.wow.battle;

/* This class specify an action that a player can done in a turn, there are basically three choices:
 * or it's an ability, or it's pass or it's
 */
public class PetAction
{ 
  private static enum Which { ABILITY, PASS, SWAP, SURREND };
  
  private final Which type;
  private final BattlePet swapToPet;
  private final BattleAbilityStatus ability;
 
    
  private PetAction(Which type, BattleAbilityStatus ability, BattlePet swapToPet)
  {
    this.type = type;
    this.swapToPet = swapToPet;
    this.ability = ability;
  }
  
  public static PetAction swapTo(BattlePet pet) { return new PetAction(Which.SWAP, null, pet); }
  public static PetAction pass() { return new PetAction(Which.PASS, null, null); }
  public static PetAction useAbility(BattleAbilityStatus ability) { return new PetAction(Which.ABILITY, ability, null); }
  public static PetAction surrend() { return new PetAction(Which.SURREND, null, null); }
  
  public boolean isSwap() { return swapToPet != null; }
  public boolean isPass() { return type == Which.PASS; }
  public boolean isSurrend() { return type == Which.SURREND; }
  public boolean isAbility() { return ability != null; }
  
  public BattlePet swappingPet() { return swapToPet; }
  public BattleAbilityStatus ability() { return ability; }
}
