package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import com.jack.wow.battle.abilities.EffectApply;
import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.Formulas;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetStats;

/**
 * This class is used as an instance of a pet inside a battle, it contains all the additional data required to manage a battle,
 * for example current hit points, which abilities are chosen, which passive effects are active on it and so on.
 * 
 * @author Jack
 */
public class BattlePet
{
  private final Pet pet;
  
  private int hitPoints;
  private int power;
  private int speed;
  
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
  
  public void addEffect(EffectApply effect)
  {
    passiveEffects.add(effect.ability, effect.turns);
  }
  
  public EffectList effects() { return passiveEffects; }
  
  public BattleAbilityStatus abilityStatus(int i) { return abilities[i]; }
  
  public void hurt(int value) { hitPoints -= value; }
  public void heal(int value) { hitPoints += value; }
 
  public void resetHitPoints()
  {
    PetStats astats = Formulas.adjustedStats(pet.stats(), pet.breed(), pet.level(), pet.quality());
    this.hitPoints = (int)astats.health();
    this.power = (int)astats.power();
    this.speed = (int)astats.speed();
  }
  
  public void resetCooldownsAndCharges()
  {
    for (BattleAbilityStatus status : abilities)
    {
      status.resetCharges();
      status.resetCooldown();
    }
  }
  
  public PetOwnedAbility ability(int i) { return abilities[i].ability(); }
  public Pet pet() { return pet; }
}
