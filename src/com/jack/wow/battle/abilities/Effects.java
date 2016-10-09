package com.jack.wow.battle.abilities;

import com.jack.wow.data.PetAbility;

public class Effects
{
  private static Effect singleAttack(float power) { return new SingleAttack(new EffectPower(power)); }
  private static Effect teamAttack(float power) { return new SingleTeamAttack(new EffectPower(power)); }
  private static Effect singleHeal(float power) { return new SingleHeal(new EffectPower(power)); }
  private static Effect selfDamage(float power) { return new SelfDamage(new EffectPower(power)); }
  
  public static void init()
  {
    PetAbility.forName("bite").addEffect(singleAttack(20));
    PetAbility.forNameAll("claw").forEach(a -> a.addEffect(singleAttack(21)));

    
    PetAbility.forName("punch").addEffect(singleAttack(20));
    PetAbility.forName("jab").addEffect(singleAttack(21));
    PetAbility.forName("bow shot").addEffect(singleAttack(20));
    PetAbility.forName("logic").addEffect(singleAttack(23));
    PetAbility.forName("backflip").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    PetAbility.forName("body slam").addEffect(singleAttack(30), selfDamage(13));
    PetAbility.forName("broom").addEffect(singleAttack(20));
    PetAbility.forName("club").addEffect(singleAttack(20));

    
    PetAbility.forName("peck").addEffect(singleAttack(20));
    
    PetAbility.forName("burn").addEffect(singleAttack(21));
    
    PetAbility.forName("beam").addEffect(singleAttack(20));
    PetAbility.forName("moon fang").addEffect(singleAttack(22));

    
    PetAbility.forName("breath").addEffect(singleAttack(21));
    PetAbility.forName("cataclysm").addEffect(singleAttack(45));

    PetAbility.forName("zap").addEffect(singleAttack(20));
    
    PetAbility.forName("infected claw").addEffect(singleAttack(22));
    PetAbility.forName("diseased bite").addEffect(singleAttack(21));

    
    PetAbility.forName("water jet").addEffect(singleAttack(20));
    PetAbility.forName("brew bolt").addEffect(singleAttack(20));
    PetAbility.forName("healing wave").addEffect(singleHeal(30));
    PetAbility.forName("carpnado").addEffect(teamAttack(30));

    
    PetAbility.forName("scratch").addEffect(singleAttack(20));
    
    PetAbility.forNameAll("stunned").forEach(a -> a.addEffect(SpecialEffect.STUNNED) );

  }
}
