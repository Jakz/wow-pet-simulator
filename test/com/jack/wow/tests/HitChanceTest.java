package com.jack.wow.tests;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import com.jack.wow.battle.AbilitySet;
import com.jack.wow.battle.Battle;
import com.jack.wow.battle.BattleAbilityStatus;
import com.jack.wow.battle.BattlePet;
import com.jack.wow.battle.BattleTeam;
import com.jack.wow.battle.Mechanics;
import com.jack.wow.battle.abilities.EffectApply;
import com.jack.wow.battle.abilities.ModifierEffect;
import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.battle.abilities.Target;
import com.jack.wow.data.Formulas;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetBreed;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetQuality;
import com.jack.wow.data.PetSpec;
import com.jack.wow.data.PetStats;

public class HitChanceTest
{
  static Mechanics mechanics;
  static Battle battle;
  static BattlePet pet;
  static BattleTeam team;
  
  private final static float FLT_DELTA = 0.0005f;
  
  private static Stream<PassiveEffect> build(PassiveEffect... effects) { return Arrays.stream(effects); }
  private static void assertFloat(float expected, float value) { Assert.assertEquals(expected, value, FLT_DELTA); }
  
  @BeforeClass
  public static void setup()
  {
    mechanics = new Mechanics();
    
    Pet pet = new Pet(PetSpec.forName("anubisath idol"), PetBreed.BB, PetQuality.RARE, 25);
    HitChanceTest.pet = new BattlePet(pet, AbilitySet.of(1,1,1));
    team = new BattleTeam(HitChanceTest.pet, null, null);
    battle = new Battle(team, team);
    mechanics.setBattle(battle);
  }
  
  @Before
  public void clearStatus()
  {
    pet.effects().clear();
    team.effects().clear();
    battle.clearGlobalEffect();
  }
  
  @Test
  public void testUnmodifiedChance()
  {
    PetOwnedAbility ability = new PetOwnedAbility(PetAbility.forName("instability"));
    BattleAbilityStatus status = new BattleAbilityStatus(pet, ability);
    
    float chance = mechanics.computeHitChanceForAbility(status);
    
    assertFloat(0.5f, chance);
  }
  
  @Test
  public void testPositiveModifierOnPet()
  {
    PetOwnedAbility instability = new PetOwnedAbility(PetAbility.forName("instability")); // base 50%
    PetAbility accuracy = PetAbility.forId(187); // +25%
    pet.effects().add(new EffectApply(accuracy, Target.SELF));
    
    BattleAbilityStatus status = new BattleAbilityStatus(pet, instability);
    float chance = mechanics.computeHitChanceForAbility(status);
    assertFloat(0.75f, chance);
  }
  
  @Test
  public void testNegativeModifierOnBattlefield()
  {
    PetOwnedAbility instability = new PetOwnedAbility(PetAbility.forName("instability")); // base 50%
    PetAbility standstorm = PetAbility.forId(453); // -10%
    battle.setGlobalEffect(new EffectApply(standstorm, Target.BATTLE_FIELD, 5));
    
    BattleAbilityStatus status = new BattleAbilityStatus(pet, instability);
    float chance = mechanics.computeHitChanceForAbility(status);
    assertFloat(0.4f, chance);
  }
  
  @Test
  public void testCumulativePetAndBattlefield()
  {
    PetOwnedAbility instability = new PetOwnedAbility(PetAbility.forName("instability")); // base 50%
    PetAbility accuracy = PetAbility.forId(187); // +25%
    pet.effects().add(new EffectApply(accuracy, Target.SELF));
    PetAbility standstorm = PetAbility.forId(453); // -10%
    battle.setGlobalEffect(new EffectApply(standstorm, Target.BATTLE_FIELD, 5));
    
    BattleAbilityStatus status = new BattleAbilityStatus(pet, instability);
    float chance = mechanics.computeHitChanceForAbility(status);
    assertFloat(0.65f, chance);
  }
}
