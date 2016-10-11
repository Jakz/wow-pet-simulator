package com.jack.wow.battle.abilities;

/**
 * This special <code>ActiveEffect</code> is used as a special effect which applies an hidden effect
 * on calculations done on the fly on the ability main effect. This was required to handle special situations
 * like attacks which goes forcibly first which apply hidden buff when calculating speed.
 * @author Jack
 */
public class ActiveHiddenEffect implements ActiveEffect
{
  public final PassiveEffect effect;
  
  public ActiveHiddenEffect(PassiveEffect effect)
  {
    this.effect = effect;
  }
  
  public String toString() { return "hidden("+effect+")"; }
}
