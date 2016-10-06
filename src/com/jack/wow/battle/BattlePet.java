package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.Formulas;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetStats;

/**
 * This class is used as an instance of a pet inside a battle, it contains all the additional data required to manage a battle,
 * for example current hit points, which abilities are chosen, which passive effects are active on it and so on.
 * 
 * @author jack
 */
public class BattlePet
{
  private class AbilityStatus
  {
    final PetOwnedAbility ability;
    int cooldown;
    
    AbilityStatus(PetOwnedAbility ability)
    {
      this.ability = ability;
      this.cooldown = 0;
    }
  }
  
  private class EffectStatus
  {
    PassiveEffect effect;
    int turns;
  }
  
  private final Pet pet;
  
  private int hitPoints;
  private int power;
  private int speed;
  
  private final AbilityStatus[] abilities;
  private final List<EffectStatus> passiveEffects;
  
  public BattlePet(Pet pet, AbilitySet set)
  {
    this.pet = pet;
    resetHitPoints();
    
    abilities = new AbilityStatus[3];
    for (int i = 0; i < abilities.length; ++i)
      abilities[i] = new AbilityStatus(pet.spec().abilities[2*i + set.index(i)]);
    
    passiveEffects = new ArrayList<>();
  }

  public void resetHitPoints()
  {
    PetStats astats = Formulas.adjustedStats(pet.stats(), pet.breed(), pet.level(), pet.quality());
    this.hitPoints = (int)astats.health();
    this.power = (int)astats.power();
    this.speed = (int)astats.speed();
  }
  
  public PetOwnedAbility ability(int i) { return abilities[i].ability; }
  public Pet pet() { return pet; }
}
