package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.Formulas;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetOwnedAbility;

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
  private final AbilityStatus[] abilities;
  private final List<EffectStatus> passiveEffects;
  
  public BattlePet(Pet pet, AbilitySet set)
  {
    this.pet = pet;
    resetHitPoints();
    
    abilities = Arrays.stream(set.indices()).boxed()
                                .map(i -> new AbilityStatus(pet.spec().abilities[3*i]))
                                .toArray(i -> new AbilityStatus[i]);
    
    passiveEffects = new ArrayList<>();
  }
  
  public void resetHitPoints()
  {
    this.hitPoints = Formulas.hitPoints(pet.stats(), pet.breed(), pet.level(), pet.quality());
  }
}
