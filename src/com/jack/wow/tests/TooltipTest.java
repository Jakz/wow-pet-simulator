package com.jack.wow.tests;

import org.junit.Assert;
import org.junit.Test;

import com.jack.wow.data.tooltips.MatchInfo;
import com.jack.wow.data.tooltips.ToolTipUtil;

public class TooltipTest
{

  @Test
  public void testSingleNumericMatch()
  {
    final String tooltip = "Bites at the enemy, dealing 248 Beast damage.";
    final MatchInfo mi = new MatchInfo(ToolTipUtil.NUMBER_REGEXP, tooltip);
    Assert.assertEquals(mi.count, 1);
  }
  
  @Test
  public void testDoubleNumericMatch()
  {
    final String tooltip = "Slams into the enemy, dealing 432 Humanoid damage and dealing 187 Humanoid damage to themselves.";
    final MatchInfo mi = new MatchInfo(ToolTipUtil.NUMBER_REGEXP, tooltip);
    Assert.assertEquals(mi.count, 2);
  }
  
  @Test
  public void testNoNumericMatch()
  {
    final String tooltip = "Creates a wooden dam that blocks two attacks. Both enemy and allied attacks will be blocked.";
    final MatchInfo mi = new MatchInfo(ToolTipUtil.NUMBER_REGEXP, tooltip);
    Assert.assertEquals(mi.count, 0);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testWrongAmountOfReplacements()
  {
    final String tooltip = "Bites at the enemy, dealing 248 Beast damage.";
    final MatchInfo mi = new MatchInfo(ToolTipUtil.NUMBER_REGEXP, tooltip);
    mi.apply(tooltip, "$DMG", "$DMG2");
  }
  
  @Test
  public void testSingleReplacement()
  {
    final String tooltip = "Bites at the enemy, dealing 248 Beast damage.";
    final MatchInfo mi = new MatchInfo(ToolTipUtil.NUMBER_REGEXP, tooltip);
    String value = mi.apply(tooltip, "$DMG");
    Assert.assertEquals(value, tooltip.replace("248", "$DMG"));
  }
  
  @Test
  public void testDoubleReplacement()
  {
    final String tooltip = "Slams into the enemy, dealing 432 Humanoid damage and dealing 187 Humanoid damage to themselves.";
    final MatchInfo mi = new MatchInfo(ToolTipUtil.NUMBER_REGEXP, tooltip);
    String value = mi.apply(tooltip, "$DMG", "$SELFDMG");
    Assert.assertEquals(value, tooltip.replace("432", "$DMG").replace("187", "$SELFDMG"));
  }
}
