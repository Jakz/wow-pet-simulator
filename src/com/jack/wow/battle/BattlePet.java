package com.jack.wow.battle;

import java.util.Arrays;

import com.jack.wow.battle.abilities.EffectApply;
import com.jack.wow.data.Formulas;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetQuality;
import com.jack.wow.data.PetSpec;
import com.jack.wow.data.PetStats;
import com.jack.wow.data.interfaces.*;

/**
 * This class is used as an instance of a pet inside a battle, it contains all the additional data required to manage a battle,
 * for example current hit points, which abilities are chosen, which passive effects are active on it and so on.
 * 
 * @author Jack
 */
public class BattlePet implements Statsed, Specced, Qualitied, Abilited
{
  private final Pet pet;
  
  
  private int maxHitPoints;
  private int hitPoints;
  private int power;
  private int speed;
  
  private int lastDamageReceived;
  
  private BattleTeam team;
  private final BattleAbilityStatus[] abilities;
  private final EffectList passiveEffects;
  
  public BattlePet(Pet pet, AbilitySet set)
  {
    this.pet = pet;
    resetHitPoints();
    
    abilities = new BattleAbilityStatus[3];
    for (int i = 0; i < abilities.length; ++i)
      abilities[i] = new BattleAbilityStatus(this, pet.spec().slot(i).get(set.index(i)));
    
    passiveEffects = new EffectList();
  }
  
  public void setTeam(BattleTeam team) { this.team = team; }
  public BattleTeam team() { return team; }
  
  public EffectList effects() { return passiveEffects; }
  
  public BattleAbilityStatus abilityStatus(int i) { return abilities[i]; }
  
  public int lastDamageReceived() { return lastDamageReceived; }
  public int maxHitPoints() { return maxHitPoints; }
  public int hitPoints() { return hitPoints; }
  public void hurt(int value) { hitPoints -= value; }
  public void heal(int value) { hitPoints += value; }
  //TODO: probably we should use a passive hidden effect to manage special cases (eg. unborn valk'jir)
  public boolean isDead() { return hitPoints <= 0; } 
 
  public void resetHitPoints()
  {
    PetStats astats = pet.stats();
    this.maxHitPoints = (int)astats.health();
    this.hitPoints = maxHitPoints;
    this.power = (int)astats.power();
    this.speed = (int)astats.speed();
    this.lastDamageReceived = 0;
  }
  
  public void resetCooldownsAndCharges()
  {
    for (BattleAbilityStatus status : abilities)
    {
      status.resetCharges();
      status.resetCooldown();
    }
  }
  
  public boolean isAbilitySelected(PetAbility ability) { return Arrays.stream(abilities).anyMatch(as -> as.ability().get() == ability); }
  public PetOwnedAbility ability(int i) { return abilities[i].ability(); }
  public Pet pet() { return pet; }
  
  @Override public PetQuality quality() { return pet.quality(); }
  @Override public PetStats stats() { return new PetStats(hitPoints, power, speed); }
  @Override public PetAbility getAbility(int slot, int index) { return pet.getAbility(slot, index); }
  @Override public PetSpec spec() { return pet.spec(); }
  
  public String icon() { return pet.spec().icon; }
  public PetFamily family() { return pet.spec().family; }
  public String name() { return pet.spec().name; }
}
