package com.jack.wow.battle.abilities;

import com.jack.wow.data.PetAbility;

public class Effects
{
  private static Effect singleAttack(float power) { return new Attack(Target.ENEMY_PET, new EffectPower(power)); }
  private static Effect backlineAttack(float power) { return new Attack(Target.ENEMY_BACK_LINE, new EffectPower(power)); }
  private static Effect teamSplitAttack(float power) { return new Attack(Target.ENEMY_TEAM_SPLIT, new EffectPower(power)); }
  
  private static Effect singleHeal(float power) { return new SingleHeal(new EffectPower(power)); }
  private static Effect selfDamage(float power) { return new SelfDamage(new EffectPower(power)); }
  
  private static Effect applyEffect(PetAbility ability, Target target, int turns) { return new EffectApply(ability, target, turns); }
  private static Effect applyEffect(String ability, Target target, int turns) { return new EffectApply(PetAbility.forName(ability), target, turns); }
  private static Effect applyEffect(int ability, Target target, int turns) { return new EffectApply(PetAbility.forId(ability), target, turns); }

  private static PassiveEffect speedMultiplier(float v) { return ModifierEffect.buildSpeed(v); }
  private static PassiveEffect damageDoneMultiplier(float v) { return ModifierEffect.buildDamageDone(v); }
  private static PassiveEffect damageReceivedMultiplier(float v) { return ModifierEffect.buildDamageReceived(v); }
  
  private static PassiveEffect hitChanceModifier(float v) { return ModifierEffect.buildHitChance(v); }
  private static PassiveEffect critChanceModifier(float v) { return ModifierEffect.buildCritChance(v); }
  private static PassiveEffect dodgeChanceModifier(float v) { return ModifierEffect.buildDodgeChance(v); }

  
  private static PetAbility forName(String name) { return PetAbility.forName(name); }
  private static PetAbility forId(int id) { return PetAbility.forId(id); }
  
  public static PetAbility mapPassiveEffect(int sourceId, int destId, Target target, int turns, PassiveEffect... effects)
  {
    return forId(sourceId).addEffect(applyEffect(forId(destId).addEffect(effects), target, turns));
  }
  
  public static void init()
  {
    /* beast */
    forName("bite").addEffect(singleAttack(20));
    forName("snap").addEffect(singleAttack(20));
    forName("smash").addEffect(singleAttack(22));
    PetAbility.forNameAll("claw").forEach(a -> a.addEffect(singleAttack(21)));
    forName("horn gore").addEffect(singleAttack(21));

    /* humanoid */
    forName("punch").addEffect(singleAttack(20));
    forName("jab").addEffect(singleAttack(21));
    forName("bow shot").addEffect(singleAttack(20));
    forName("logic").addEffect(singleAttack(23));
    forName("crush").addEffect(singleAttack(22));
    forName("gilded fist").addEffect(singleAttack(21));
    forName("backflip").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("spin kick").addEffect(singleAttack(17), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("body slam").addEffect(singleAttack(30), selfDamage(13));
    forName("broom").addEffect(singleAttack(20));
    forName("club").addEffect(singleAttack(20));
    forName("holy strike").addEffect(singleAttack(21));
    mapPassiveEffect(426, 425, Target.SELF, 5, hitChanceModifier(0.25f), speedMultiplier(1.25f), critChanceModifier(0.25f)); // focus
    mapPassiveEffect(312, 311, Target.SELF, 1, dodgeChanceModifier(1.0f)); // dodge


    /* flying */
    forName("peck").addEffect(singleAttack(20));
    mapPassiveEffect(188, 187, Target.TEAM, 4, hitChanceModifier(0.25f)); // accuracy
    mapPassiveEffect(186, 185, Target.SELF, 1, damageReceivedMultiplier(1.25f)).addEffect(singleAttack(25)); // reckless strike
    mapPassiveEffect(521, 520, Target.SELF, 3, critChanceModifier(0.75f)); // hawk eye
    mapPassiveEffect(515, 516, Target.ENEMY_PET, 3, damageReceivedMultiplier(1.25f)).addEffect(singleAttack(10)); // flyby

    
    /* elemental */
    forName("burn").addEffect(singleAttack(21));
    forName("snowball").addEffect(singleAttack(20));
    forName("stone rush").addEffect(singleAttack(35), selfDamage(15));

    
    /* magic */
    forName("beam").addEffect(singleAttack(20));
    forName("moon fang").addEffect(singleAttack(22));
    forName("spiritfire bolt").addEffect(singleAttack(22));
    forName("dark talon").addEffect(singleAttack(18));
    forName("expunge").addEffect(singleAttack(32));
    forName("feedback").addEffect(singleAttack(20));
    forName("magic hat").addEffect(singleAttack(20));
    forName("laser").addEffect(singleAttack(18));
    forName("gift of winter's veil").addEffect(singleAttack(40));
    forName("interrupting gaze").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("counterspell").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);

    
    forId(471).addEffect(singleAttack(10), applyEffect(forId(470).addEffect(speedMultiplier(0.5f), damageDoneMultiplier(0.5f)), Target.ENEMY_PET, 1)); // weakness
    mapPassiveEffect(488, 487, Target.ACTIVE_PET, 2, damageDoneMultiplier(1.5f)); // amplify magic
    
    /* dragonkin */
    forName("breath").addEffect(singleAttack(21));
    forName("shadowflame").addEffect(singleAttack(22));
    forName("cataclysm").addEffect(singleAttack(45));
    forName("jade breath").addEffect(singleAttack(20));

    /* mechanical */
    forName("zap").addEffect(singleAttack(20));
    forName("tesla cannon").addEffect(singleAttack(22));
    forName("metal fist").addEffect(singleAttack(20));
    forName("ooze touch").addEffect(singleAttack(20));

    forName("demolish").addEffect(singleAttack(40));
    forName("interrupting jolt").addEffect(singleAttack(20), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    mapPassiveEffect(392, 391, Target.SELF, 3, damageReceivedMultiplier(0.5f)); // extra plating


    /* undead */
    forName("infected claw").addEffect(singleAttack(22));
    forName("diseased bite").addEffect(singleAttack(21));
    forName("bone bite").addEffect(singleAttack(20));
    forName("shadow slash").addEffect(singleAttack(21));
    forName("shadow shock").addEffect(singleAttack(22));
    PetAbility.forNameAll("gargoyle strike").forEach(a -> a.addEffect(singleAttack(20)));

    /* water */
    forName("water jet").addEffect(singleAttack(20));
    forName("steam vent").addEffect(singleAttack(22));
    forName("tail slap").addEffect(singleAttack(22));
    forName("fish slap").addEffect(singleAttack(22));
    forName("brew bolt").addEffect(singleAttack(20));
    forName("healing wave").addEffect(singleHeal(30));
    forName("carpnado").addEffect(teamSplitAttack(30));

    /* critter */
    PetAbility.forName("scratch").addEffect(singleAttack(20));
    mapPassiveEffect(165, 164, Target.SELF, 3, damageReceivedMultiplier(0.5f)); // crouch
    mapPassiveEffect(162, 161, Target.SELF, 3, speedMultiplier(1.75f)); // adrenaline rush

    
    
    
    PetAbility.forNameAll("stunned").forEach(a -> a.addEffect(SpecialEffect.STUNNED) );

  }
}
