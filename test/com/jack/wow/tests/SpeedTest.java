package com.jack.wow.tests;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

import static com.jack.wow.tests.TestSuite.build;

import com.jack.wow.battle.Battle;
import com.jack.wow.battle.BattleStatus;
import com.jack.wow.battle.BattleTeam;
import com.jack.wow.battle.EffectInfo;
import com.jack.wow.battle.Mechanics;
import com.jack.wow.battle.abilities.ModifierEffect;
import com.jack.wow.battle.abilities.ModifierFunction;
import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.battle.abilities.SpecialEffect;

public class SpeedTest
{
  static Mechanics mechanics;
  static Battle battle;
  
  private final static float FLT_DELTA = 0.0005f;
  private final static float base = 200;
  private final static float multiplier = 1.0f;
  
 
  private static void assertFloat(float v, float k) { Assert.assertEquals(v, k, FLT_DELTA); }
  
  @BeforeClass
  public static void setup()
  {
    mechanics = new Mechanics();
    battle = new Battle(new BattleTeam(null, null, null), new BattleTeam(null, null, null));
  }
  
  @Test
  public void testSinglePositiveMultiplicative()
  {
    Stream<EffectInfo> effects = build(ModifierEffect.buildSpeed(0.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, base, multiplier, effects, new BattleStatus(battle));
    assertFloat(base*1.25f, result);
  }
  
  @Test
  public void testSingleNegativeMultiplicative()
  {
    Stream<EffectInfo> effects = build(ModifierEffect.buildSpeed(-0.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, base, multiplier, effects, new BattleStatus(battle));
    assertFloat(base*0.75f, result);
  }
  
  @Test
  public void testCancelingMultiplicativeModifiers()
  {
    Stream<EffectInfo> effects = build(ModifierEffect.buildSpeed(-0.25f), ModifierEffect.buildSpeed(0.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, base, multiplier, effects, new BattleStatus(battle));
    assertFloat(base, result);
  }
  
  @Test
  public void testMultiplicativeCapToZero()
  {
    Stream<EffectInfo> effects = build(ModifierEffect.buildSpeed(-1.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, base, multiplier, effects, new BattleStatus(battle));
    assertFloat(0.0f, result);
  }
  
  @Test
  public void testManyMultiplicative()
  {
    Stream<EffectInfo> effects = build(
        ModifierEffect.buildSpeed(0.25f),
        ModifierEffect.buildSpeed(-0.5f),
        ModifierEffect.buildSpeed(0.75f),
        ModifierEffect.buildSpeed(0.5f)
    );
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, base, multiplier, effects, new BattleStatus(battle));
    assertFloat(base*(1.0f+0.25f-0.5f+0.75f+0.5f), result);
  }

  /*@Test
  public void singlePositiveAdditive()
  {
    Stream<EffectInfo> effects = build(SpecialEffect.alwaysGoestFirst(50));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.DAMAGE_DONE_RAW, base, multiplier, effects, null);
    assertFloat(base+10, result);
  }*/
}
