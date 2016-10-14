package com.jack.wow.tests;

import org.junit.BeforeClass;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.jack.wow.Main;
import com.jack.wow.battle.abilities.Effects;

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
}
