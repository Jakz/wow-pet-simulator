package com.jack.wow.tests;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

import com.jack.wow.battle.Mechanics;
import com.jack.wow.battle.abilities.ModifierEffect;
import com.jack.wow.battle.abilities.ModifierFunction;
import com.jack.wow.battle.abilities.PassiveEffect;

public class ModifierTest
{
  static Mechanics mechanics;
  
  private final static float FLT_DELTA = 0.0005f;
  private final static float base = 200;
  private final static float multiplier = 1.0f;
  
  private static Stream<PassiveEffect> build(PassiveEffect... effects) { return Arrays.stream(effects); }
  private static void assertFloat(float v, float k) { Assert.assertEquals(v, k, FLT_DELTA); }
  
  @BeforeClass
  public static void setup()
  {
    mechanics = new Mechanics();
  }
  
  @Test
  public void testSinglePositiveMultiplicative()
  {
    Stream<PassiveEffect> effects = build(ModifierEffect.buildSpeed(0.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, 200, multiplier, effects, null);
    assertFloat(base*1.25f, result);
  }
  
  @Test
  public void testSingleNegativeMultiplicative()
  {
    Stream<PassiveEffect> effects = build(ModifierEffect.buildSpeed(-0.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, 200, multiplier, effects, null);
    assertFloat(base*0.75f, result);
  }
  
  @Test
  public void testCancelingMultiplicativeModifiers()
  {
    Stream<PassiveEffect> effects = build(ModifierEffect.buildSpeed(-0.25f), ModifierEffect.buildSpeed(0.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, 200, multiplier, effects, null);
    assertFloat(base, result);
  }
  
  @Test
  public void testMultiplicativeCapToZero()
  {
    Stream<PassiveEffect> effects = build(ModifierEffect.buildSpeed(-1.25f));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, 200, multiplier, effects, null);
    assertFloat(0.0f, result);
  }
  
  @Test
  public void testManyMultiplicative()
  {
    Stream<PassiveEffect> effects = build(
        ModifierEffect.buildSpeed(0.25f),
        ModifierEffect.buildSpeed(-0.5f),
        ModifierEffect.buildSpeed(0.75f),
        ModifierEffect.buildSpeed(0.5f)
    );
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, 200, multiplier, effects, null);
    assertFloat(base*(1.0f+0.25f-0.5f+0.75f+0.5f), result);
  }
  
  @Test
  public void singlePositiveAdditive()
  {
    Stream<PassiveEffect> effects = build(ModifierEffect.buildRawDamageReceived(10));
    float result = mechanics.computeModifiedValue(ModifierFunction.Target.SPEED, 200, multiplier, effects, null);
    assertFloat(base+10, result);
  }
}
