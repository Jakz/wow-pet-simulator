package com.jack.wow.battle;

import com.jack.wow.data.PetOwnedAbility;

/**
 * This class is used to store the information about an ability on a specific <code>BattlePet</code>.
 * This is necessary to track cooldown of the ability and charges (used for damage increasing abilities).
 * @author Jack
 */
class BattleAbilityStatus
{
  private final PetOwnedAbility ability;
  private int cooldown;
  private int charges;
  
  BattleAbilityStatus(PetOwnedAbility ability)
  {
    this.ability = ability;
    this.cooldown = 0;
    this.charges = 0;
  }
  
  public void startCooldown() { cooldown = ability.get().cooldown; }
  public void decreaseCooldown() { --cooldown; }
  public boolean isReady() { return cooldown == 0; }
  
  public int charges() { return charges; }
  public void increaseCharges() { ++charges; }
  public void decreaseCharges() { --charges; }
  
}