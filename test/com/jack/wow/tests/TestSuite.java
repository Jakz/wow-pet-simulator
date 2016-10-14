package com.jack.wow.tests;

import org.junit.BeforeClass;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.jack.wow.Main;
import com.jack.wow.battle.abilities.Effect;
import com.jack.wow.battle.abilities.Effects;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  FormulaTest.class,
  HitChanceTest.class,
  ModifierTest.class,
  TooltipTest.class
})
public class TestSuite
{
  @BeforeClass
  public static void setup()
  {
    Main.loadDatabase();
    Effects.init();
  }
  
  public static PetAbility dummyAbility(Effect... effects)
  {
    return new PetAbility(PetFamily.humanoid, 0, 0.0f, effects);
  }
  
  public static PetAbility dummyAbility(float hitChance, Effect... effects)
  {
    return new PetAbility(PetFamily.humanoid, 0, hitChance, effects);
  }
  
  public static PetAbility dummyAbility(int cooldown, Effect... effects)
  {
    return new PetAbility(PetFamily.humanoid, cooldown, 0.0f, effects);
  }
  
  public static PetAbility dummyAbility(int cooldown, float hitChance, Effect... effects)
  {
    return new PetAbility(PetFamily.humanoid, cooldown, hitChance, effects);
  }
}
