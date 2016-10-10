package com.jack.wow.battle.abilities;

import com.jack.wow.data.PetAbility;

public class Effects
{
  private static Effect singleAttack(float power) { return new Attack(Target.ENEMY_PET, new EffectPower(power)); }
  private static Effect backlineAttack(float power) { return new Attack(Target.ENEMY_BACK_LINE, new EffectPower(power)); }
  private static Effect teamSplitAttack(float power) { return new Attack(Target.ENEMY_TEAM_SPLIT, new EffectPower(power)); }
  
  private static Effect singleHeal(float power) { return new Heal(Target.SELF, new EffectPower(power)); }
  private static Effect teamHeal(float power) { return new Heal(Target.TEAM, new EffectPower(power)); }
  private static Effect selfDamage(float power) { return new SelfDamage(new EffectPower(power)); }
  
  private static ActiveEffect applyEffect(PetAbility ability, Target target, int turns) { return new EffectApply(ability, target, turns); }
  private static ActiveEffect applyEffect(String ability, Target target, int turns) { return new EffectApply(PetAbility.forName(ability), target, turns); }
  private static ActiveEffect applyEffect(int ability, Target target, int turns) { return new EffectApply(PetAbility.forId(ability), target, turns); }
  
  private static ActiveEffect chanceEffect(float chance, ActiveEffect effect) { return new ChanceEffect(chance, effect); }
  
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
  
  private final static int STUN_ID = 174;
  
  private static Effect[] damageWithChanceOfStun(float power, float chance)
  {
    return new Effect[] {
        singleAttack(power),
        chanceEffect(chance, applyEffect(STUN_ID, Target.ENEMY_PET, 1))
    };
  }
  
  public static void init()
  {
    /* beast */
    forName("bite").addEffect(singleAttack(20));
    forName("snap").addEffect(singleAttack(20));
    forName("strike").addEffect(singleAttack(20));
    forName("smash").addEffect(singleAttack(22));
    forName("paralyzing venom").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    PetAbility.forNameAll("claw").forEach(a -> a.addEffect(singleAttack(21)));
    forName("horn gore").addEffect(singleAttack(21));
    mapPassiveEffect(1054, 1053, Target.SELF, 2, critChanceModifier(1.0f)); // hawk eye

    /* humanoid */
    forName("punch").addEffect(singleAttack(20));
    forName("jab").addEffect(singleAttack(21));
    forName("bow shot").addEffect(singleAttack(20));
    forName("logic").addEffect(singleAttack(23));
    forName("crush").addEffect(singleAttack(22));
    forName("gauss rifle").addEffect(singleAttack(22));
    forName("gilded fist").addEffect(singleAttack(21));
    forName("holy sword").addEffect(singleAttack(21));
    forName("mighty charge").addEffect(singleAttack(32));
    forName("backflip").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("spin kick").addEffect(singleAttack(17), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("charge").addEffect(singleAttack(15), SpecialEffect.ALWAYS_GOES_FIRST);
    forName("body slam").addEffect(singleAttack(30), selfDamage(13));
    forName("broom").addEffect(singleAttack(20));
    forName("club").addEffect(singleAttack(20));
    forName("holy strike").addEffect(singleAttack(21));
    forName("whirlwind").addEffect(teamSplitAttack(27));
    forName("tornado punch").addEffect(damageWithChanceOfStun(30, 0.25f));
    forName("time stop").addEffect(applyEffect(STUN_ID, Target.ENEMY_PET, 1));
    mapPassiveEffect(426, 425, Target.SELF, 5, hitChanceModifier(0.25f), speedMultiplier(1.25f), critChanceModifier(0.25f)); // focus
    mapPassiveEffect(312, 311, Target.SELF, 1, dodgeChanceModifier(1.0f)); // dodge
    mapPassiveEffect(1047, 1046, Target.SELF, 2, dodgeChanceModifier(0.5f)); // frolick
    mapPassiveEffect(1535, 1534, Target.SELF, 5, critChanceModifier(0.25f)); // murkmorphosis


    /* flying */
    forName("peck").addEffect(singleAttack(20));
    forName("savage talon").addEffect(singleAttack(22));
    mapPassiveEffect(188, 187, Target.TEAM, 4, hitChanceModifier(0.25f)); // accuracy
    mapPassiveEffect(186, 185, Target.SELF, 1, damageReceivedMultiplier(1.25f)).addEffect(singleAttack(25)); // reckless strike
    mapPassiveEffect(521, 520, Target.SELF, 3, critChanceModifier(0.75f)); // hawk eye
    mapPassiveEffect(515, 516, Target.ENEMY_PET, 3, damageReceivedMultiplier(1.25f)).addEffect(singleAttack(10)); // flyby

    
    /* elemental */
    forName("burn").addEffect(singleAttack(21));
    forName("snowball").addEffect(singleAttack(20));
    forName("stone shot").addEffect(singleAttack(20));
    forName("sulfuras smash").addEffect(singleAttack(22));
    forName("seethe").addEffect(singleAttack(22));
    forName("railgun").addEffect(singleAttack(21));
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
    forName("arcane dash").addEffect(damageWithChanceOfStun(40, 0.25f));
    forId(471).addEffect(singleAttack(10), applyEffect(forId(470).addEffect(speedMultiplier(0.5f), damageDoneMultiplier(0.5f)), Target.ENEMY_PET, 1)); // weakness
    mapPassiveEffect(488, 487, Target.ACTIVE_PET, 2, damageDoneMultiplier(1.5f)); // amplify magic
    mapPassiveEffect(791, 790, Target.SELF, 5, critChanceModifier(0.5f)); // stimpack
    mapPassiveEffect(838, 837, Target.SELF, 2, speedMultiplier(2.0f)); // centrifual hooks
    mapPassiveEffect(1040, 1039, Target.TEAM, 9, hitChanceModifier(0.20f)); // nimbus
    mapPassiveEffect(614, 613, Target.SELF, 3, damageDoneMultiplier(1.25f)); // competitive spirit

    
    /* dragonkin */
    forName("breath").addEffect(singleAttack(21));
    forName("shadowflame").addEffect(singleAttack(22));
    forName("cataclysm").addEffect(singleAttack(45));
    forName("instability").addEffect(singleAttack(50));
    forName("jade breath").addEffect(singleAttack(20));

    /* mechanical */
    forName("zap").addEffect(singleAttack(20));
    forName("missile").addEffect(singleAttack(21));
    forName("tesla cannon").addEffect(singleAttack(22));
    forName("metal fist").addEffect(singleAttack(20));
    forName("ooze touch").addEffect(singleAttack(20));
    forName("jolt").addEffect(singleAttack(25));
    forName("demolish").addEffect(singleAttack(40));
    forName("screeching gears").addEffect(damageWithChanceOfStun(20, 0.25f));
    forName("shock and awe").addEffect(damageWithChanceOfStun(30, 0.25f));
    forName("interrupting jolt").addEffect(singleAttack(20), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    mapPassiveEffect(392, 391, Target.SELF, 3, damageReceivedMultiplier(0.5f)); // extra plating


    /* undead */
    forName("infected claw").addEffect(singleAttack(22));
    forName("diseased bite").addEffect(singleAttack(21));
    forName("bone bite").addEffect(singleAttack(20));
    forName("creepy chomp").addEffect(singleAttack(20));
    forName("shadow slash").addEffect(singleAttack(21));
    forName("shadow shock").addEffect(singleAttack(22));
    forName("ghostly bite").addEffect(singleAttack(40), applyEffect(STUN_ID, Target.ENEMY_PET, 1));

    forName("haunting song").addEffect(teamHeal(16));
    PetAbility.forNameAll("gargoyle strike").forEach(a -> a.addEffect(singleAttack(20)));

    /* water */
    forName("water jet").addEffect(singleAttack(20));
    forName("steam vent").addEffect(singleAttack(22));
    forName("tail slap").addEffect(singleAttack(22));
    forName("fish slap").addEffect(singleAttack(22));
    forName("brew bolt").addEffect(singleAttack(20));
    forName("tentacle slap").addEffect(singleAttack(22));
    forName("healing wave").addEffect(singleHeal(30));
    forName("carpnado").addEffect(teamSplitAttack(30));
    mapPassiveEffect(1572, 1571, Target.ENEMY_ACTIVE_PET, 2, speedMultiplier(0.5f)); // vicious streak

    
    /* critter */
    forName("scratch").addEffect(singleAttack(20));
    forName("skitter").addEffect(singleAttack(20));
    forName("chomp").addEffect(singleAttack(22));
    forName("inspiring song").addEffect(teamHeal(12));
    mapPassiveEffect(165, 164, Target.SELF, 3, damageReceivedMultiplier(0.5f)); // crouch
    mapPassiveEffect(162, 161, Target.SELF, 3, speedMultiplier(1.75f)); // adrenaline rush
    mapPassiveEffect(366, 365, Target.TEAM, 9, speedMultiplier(1.25f)); // dazzling dance
    mapPassiveEffect(851, 850, Target.SELF, 2, speedMultiplier(2.0f)); // vicious streak


    
    
    
    PetAbility.forNameAll("stunned").forEach(a -> a.addEffect(SpecialEffect.STUNNED) );

  }
}
