package com.jack.wow.battle;

import com.jack.wow.data.PetOwnedAbility;

/**
 * This class is used to store the information about an ability on a specific <code>BattlePet</code>.
 * This is necessary to track cooldown of the ability and charges (used for damage increasing abilities).
 * @author Jack
 */
public class BattleAbilityStatus
{
  private BattlePet owner;
  private final PetOwnedAbility ability;
  private int cooldown;
  private int charges;
  
  public BattleAbilityStatus(BattlePet owner, PetOwnedAbility ability)
  {
    this.ability = ability;
    this.cooldown = 0;
    this.charges = 0;
    this.owner = owner;
  }
  
  public BattlePet owner() { return owner; }
  public PetOwnedAbility ability() { return ability; }
  
  public int cooldown() { return cooldown; }
  public void resetCooldown() { cooldown = 0; }
  public void startCooldown()
  { 
    if (ability.get().cooldown == 0) 
      throw new IllegalArgumentException(String.format("startCooldown() has been called on ability %s that doesn't have cooldown", ability.get().name));
    cooldown = ability.get().cooldown;
  }
  public void decreaseCooldown() { --cooldown; }
  public boolean isReady() { return cooldown == 0; }
  
  public int charges() { return charges; }
  public void increaseCharges() { ++charges; }
  public void decreaseCharges() { --charges; }
  public void resetCharges() { charges = 0; }
  
}