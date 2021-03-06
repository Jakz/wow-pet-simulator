package com.jack.wow.battle.abilities;

import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;

public class Effects
{
  private static ActiveEffect singleAttack(float power) { return new Attack(Target.ENEMY_PET, new EffectPower(power)); }
  private static ActiveEffect singleAttack(float power, PetFamily family) { return new Attack(Target.ENEMY_PET, family, new EffectPower(power)); }
  private static ActiveEffect singleAttack(float power, float variance) { return new Attack(Target.ENEMY_PET, new EffectPower(power, variance)); }

  private static ActiveEffect backlineAttack(float power) { return new Attack(Target.ENEMY_BACK_LINE, new EffectPower(power)); }
  private static ActiveEffect teamSplitAttack(float power) { return new Attack(Target.ENEMY_TEAM_SPLIT, new EffectPower(power)); }
  private static ActiveEffect teamAttack(float power) { return new Attack(Target.ENEMY_TEAM, new EffectPower(power)); }
  
  private static PassiveEffect periodicDamage(float power, PetFamily family, Target target) { return new PeriodicDamage(target, family, new EffectPower(power)); } 
  private static PassiveEffect periodicHeal(float power, Target target) { return new PeriodicHeal(target, new EffectPower(power)); } 
  private static PassiveEffect expireEffect(ActiveEffect effect) { return new ExpireEffect(effect); }
  
  private static Effect singleHeal(float power) { return new Heal(Target.SELF, new EffectPower(power)); }
  private static Effect teamHeal(float power) { return new Heal(Target.TEAM, new EffectPower(power)); }
  private static Effect selfDamage(float power) { return new Attack(Target.SELF, new EffectPower(power)); }
  
  private static ActiveEffect applyEffect(PetAbility ability, Target target, int turns) { return new EffectApply(ability, target, turns); }
  private static ActiveEffect applyEffect(String ability, Target target, int turns) { return new EffectApply(PetAbility.forName(ability), target, turns); }
  private static ActiveEffect applyEffect(int ability, Target target, int turns) { return new EffectApply(PetAbility.forId(ability), target, turns); }
  
  private static ActiveEffect chanceEffect(float chance, ActiveEffect effect) { return new ChanceEffect(chance, effect); }
  private static ActiveEffect conditionalEffect(Condition condition, ActiveEffect effect) { return new ConditionalEffect(condition, effect); }
  private static ActiveEffect conditionalEffect(Condition condition, ActiveEffect trueEffect, ActiveEffect falseEffect) { return new ConditionalEffect(condition, trueEffect, falseEffect); }

  
  private static PassiveEffect speedMultiplier(float v) { return ModifierEffect.buildSpeed(v); }
  private static PassiveEffect damageDoneMultiplier(float v) { return ModifierEffect.buildDamageDone(v); }
  private static PassiveEffect damageDoneMultiplier(float v, PetFamily f) { return ModifierEffect.buildDamageDone(f, v); }
  private static PassiveEffect damageReceivedModifier(float v) { return ModifierEffect.buildRawDamageReceived(v); }
  private static PassiveEffect damageReceivedMultiplier(float v) { return ModifierEffect.buildDamageReceived(v); }
  private static PassiveEffect healingReceivedMultiplier(float v) { return ModifierEffect.buildHealingReceived(v); }
  
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
  private final static int SPEED_BOOST100 = 544;
  private final static int SPEED_BOOST50 = 735;
  private final static int SPEED_BOOST20_CUMULATIVE = 831;
  private final static int SPEED_REDUCTION_25 = 154;

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
    forName("smash").addEffect(singleAttack(22, 0.3f));
    forName("paralyzing venom").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("horn attack").addEffect(singleAttack(15), chanceEffect(0.5f, SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST));
    PetAbility.forNameAll("claw").forEach(a -> a.addEffect(singleAttack(21, 0.2f)));
    forName("horn gore").addEffect(singleAttack(21, 0.2f));
    forName("udder destruction").addEffect(damageWithChanceOfStun(30, 0.25f));
    forName("headbutt").addEffect(damageWithChanceOfStun(30, 0.25f));
    forName("trihorn charge").addEffect(singleAttack(23), SpecialEffect.alwaysGoestFirst(50));
    forName("barbed stinger").addEffect(singleAttack(19), chanceEffect(0.2f, applyEffect("poisoned", Target.ENEMY_PET, 1)));
    forName("hiss").addEffect(singleAttack(20), applyEffect(SPEED_REDUCTION_25, Target.ENEMY_PET, 4));
    mapPassiveEffect(1054, 1053, Target.SELF, 2, critChanceModifier(1.0f)); // hawk eye
    mapPassiveEffect(1112, 1111, Target.SELF, 3, damageReceivedMultiplier(-0.5f)); // though n' cuddly
    mapPassiveEffect(310, 309, Target.SELF, 5, damageReceivedModifier(-5));
    mapPassiveEffect(1049, 1048, Target.ENEMY_PET, 1, hitChanceModifier(-1.0f), SpecialEffect.BLINDED, SpecialEffect.POISONED); // blinding poison
    mapPassiveEffect(314, 313, Target.ENEMY_PET, 10, damageReceivedModifier(3)); // mangle

    
    /* humanoid */
    forName("punch").addEffect(singleAttack(20));
    forName("jab").addEffect(singleAttack(21, 0.2f));
    forName("bow shot").addEffect(singleAttack(20));
    forName("logic").addEffect(singleAttack(23));
    forName("crush").addEffect(singleAttack(22, 0.3f));
    forName("gauss rifle").addEffect(singleAttack(22, 0.3f));
    forName("gilded fist").addEffect(singleAttack(21, 0.2f));
    forName("holy sword").addEffect(singleAttack(21, 0.2f));
    forName("mighty charge").addEffect(singleAttack(32));
    forName("backflip").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("spin kick").addEffect(singleAttack(17), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("charge").addEffect(singleAttack(15), SpecialEffect.alwaysGoestFirst(50));
    forName("body slam").addEffect(singleAttack(30), selfDamage(13));
    forName("broom").addEffect(singleAttack(20));
    forName("club").addEffect(singleAttack(20));
    forName("vicious slice").addEffect(singleAttack(20));
    forName("holy strike").addEffect(singleAttack(21, 0.2f));
    forName("ban hammer").addEffect(singleAttack(21, 0.2f));
    forName("whirlwind").addEffect(teamSplitAttack(27));
    forName("tornado punch").addEffect(damageWithChanceOfStun(30, 0.25f));
    forName("perfumed arrow").addEffect(damageWithChanceOfStun(20, 0.25f));
    forName("time stop").addEffect(applyEffect(STUN_ID, Target.ENEMY_PET, 1));
    forName("blackout kick").addEffect(applyEffect(STUN_ID, Target.ENEMY_PET, 1));
    forName("clobber").addEffect(applyEffect(STUN_ID, Target.ENEMY_PET, 1));
    forName("fury of 1,000 fists").addEffect(singleAttack(30), conditionalEffect(Condition.hasStatus(SpecialEffect.BLINDED, Target.ENEMY_ACTIVE_PET), applyEffect(STUN_ID, Target.ENEMY_PET, 1)));
    mapPassiveEffect(426, 425, Target.SELF, 5, hitChanceModifier(0.25f), speedMultiplier(0.25f), critChanceModifier(0.25f)); // focus
    mapPassiveEffect(312, 311, Target.SELF, 1, dodgeChanceModifier(1.0f)); // dodge
    mapPassiveEffect(1047, 1046, Target.SELF, 2, dodgeChanceModifier(0.5f)); // frolick
    mapPassiveEffect(1535, 1534, Target.SELF, 5, critChanceModifier(0.25f)); // murkmorphosis
    mapPassiveEffect(225, 224, Target.SELF, 2, damageReceivedMultiplier(-0.5f)); // staggered steps


    /* flying */
    forName("peck").addEffect(singleAttack(20));
    forName("savage talon").addEffect(singleAttack(22, 0.3f));
    mapPassiveEffect(188, 187, Target.TEAM, 4, hitChanceModifier(0.25f)); // accuracy
    mapPassiveEffect(186, 185, Target.SELF, 1, damageReceivedMultiplier(0.25f)).addEffect(singleAttack(25, 0.8f)); // reckless strike
    mapPassiveEffect(521, 520, Target.SELF, 3, critChanceModifier(0.75f)); // hawk eye
    mapPassiveEffect(515, 516, Target.ENEMY_PET, 3, damageReceivedMultiplier(0.25f)).addEffect(singleAttack(10)); // flyby
    mapPassiveEffect(514, 677, Target.ENEMY_ACTIVE_PET, 2, periodicDamage(4.0f, PetFamily.flying, Target.SELF)).addEffect(singleAttack(18)); // wild winds
    mapPassiveEffect(454, 453, Target.BATTLE_FIELD, 5, damageReceivedModifier(5), hitChanceModifier(-0.1f)).addEffect(singleAttack(25)); // sandstorm

    
    /* elemental */
    forName("burn").addEffect(singleAttack(21, 0.2f));
    forName("snowball").addEffect(singleAttack(20));
    forName("stone shot").addEffect(singleAttack(20));
    forName("sulfuras smash").addEffect(singleAttack(22, 0.3f));
    forName("seethe").addEffect(singleAttack(22, 0.3f));
    forName("railgun").addEffect(singleAttack(21, 0.2f));
    forName("stone rush").addEffect(singleAttack(35), selfDamage(15));
    forName("avalanche").addEffect(teamSplitAttack(33));
    forName("poisoned").addEffect(periodicDamage(5, PetFamily.elemental, Target.SELF), SpecialEffect.POISONED);
    forName("howling blast").addEffect(singleAttack(25), conditionalEffect(Condition.hasStatus(SpecialEffect.CHILLED, Target.ENEMY_ACTIVE_PET), teamAttack(5)));
    forName("incineration security measures").addEffect(singleAttack(15), backlineAttack(10));
    forName("conflagrate").addEffect(singleAttack(30), conditionalEffect(Condition.hasStatus(SpecialEffect.BURNING, Target.ENEMY_PET), singleAttack(10)));
    mapPassiveEffect(792, 793, Target.ENEMY_PET, 2, healingReceivedMultiplier(-0.5f)); // darkflame
    forId(1041).addEffect(periodicDamage(5, PetFamily.elemental, Target.ENEMY_ACTIVE_PET), SpecialEffect.BURNING); // flame jet
    forId(1042).addEffect(singleAttack(30), chanceEffect(0.5f, applyEffect(1041, Target.ENEMY_TEAM, 3)));
    mapPassiveEffect(1010, 1009, Target.ENEMY_TEAM, 4, hitChanceModifier(-0.25f)); // inebriate
    mapPassiveEffect(178, 177, Target.ENEMY_PET, 4, periodicDamage(5, PetFamily.elemental, Target.SELF), SpecialEffect.BURNING).addEffect(singleAttack(12)); // immolate
    mapPassiveEffect(206, 205, Target.BATTLE_FIELD, 9, SpecialEffect.CHILLED).addEffect(singleAttack(25)); // immolate
    forId(820).addEffect(periodicHeal(8, Target.SELF), new OnTickEffect(applyEffect(forName("elemental mark"), Target.SELF, 1))); // nature's ward
    forId(574).addEffect(applyEffect(forId(820), Target.SELF, 1));
    
    /* magic */
    forName("beam").addEffect(singleAttack(20));
    forName("moon fang").addEffect(singleAttack(22, 0.3f));
    forName("spiritfire bolt").addEffect(singleAttack(22, 0.3f));
    forName("dark talon").addEffect(singleAttack(18));
    forName("expunge").addEffect(singleAttack(32, 0.3f));
    forName("feedback").addEffect(singleAttack(20));
    forName("magic hat").addEffect(singleAttack(20));
    forName("laser").addEffect(singleAttack(18));
    forName("gift of winter's veil").addEffect(singleAttack(40));
    forName("interrupting gaze").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("counterspell").addEffect(singleAttack(15), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    forName("arcane dash").addEffect(damageWithChanceOfStun(40, 0.25f));
    forName("soulrush").addEffect(damageWithChanceOfStun(30, 0.25f));

    forId(471).addEffect(singleAttack(10), applyEffect(forId(470).addEffect(speedMultiplier(-0.5f), damageDoneMultiplier(-0.5f)), Target.ENEMY_PET, 1)); // weakness
    mapPassiveEffect(488, 487, Target.ACTIVE_PET, 2, damageDoneMultiplier(0.5f)); // amplify magic
    mapPassiveEffect(791, 790, Target.SELF, 5, critChanceModifier(0.5f)); // stimpack
    mapPassiveEffect(838, 837, Target.SELF, 2, speedMultiplier(1.0f)); // centrifual hooks
    mapPassiveEffect(1040, 1039, Target.TEAM, 9, hitChanceModifier(0.20f)); // nimbus
    mapPassiveEffect(614, 613, Target.SELF, 3, damageDoneMultiplier(0.25f)); // competitive spirit
    mapPassiveEffect(216, 215, Target.SELF, 1, damageDoneMultiplier(1.0f)); // inner vision
    mapPassiveEffect(977, 976, Target.SELF, 1, hitChanceModifier(-0.25f), critChanceModifier(1.0f)); // inner vision
    mapPassiveEffect(194, 195, Target.SELF, 1, speedMultiplier(1.0f)).addEffect(singleAttack(15)); // metabolic boost
    mapPassiveEffect(197, 841, Target.SELF, 5, critChanceModifier(0.5f)); // adrenal glands
    //mapPassiveEffect(273, 274, Target.SELF, 1, charg) //wish

    /* dragonkin */
    PetAbility.forNameAll("breath").forEach(a -> a.addEffect(singleAttack(21, 0.2f)));
    forName("shadowflame").addEffect(singleAttack(22, 0.3f));
    forName("cataclysm").addEffect(singleAttack(45));
    forName("instability").addEffect(singleAttack(50));
    forName("jade breath").addEffect(singleAttack(20));
    forName("frost breath").addEffect(singleAttack(20));
    forName("deep breath").addEffect(new ChargeEffect(singleAttack(50), 1));
    forName("tail sweep").addEffect(ConditionalEffect.whenAttackingLast(singleAttack(25), singleAttack(18)));

    
    /* mechanical */
    forId(116).addEffect(singleAttack(20)); // zap
    forName("missile").addEffect(singleAttack(21, 0.2f));
    forName("tesla cannon").addEffect(singleAttack(22, 0.3f));
    forName("metal fist").addEffect(singleAttack(20));
    forName("ooze touch").addEffect(singleAttack(20));
    forName("jolt").addEffect(singleAttack(25));
    forName("demolish").addEffect(singleAttack(40));
    forName("greench's gift").addEffect(singleAttack(40));
    forName("screeching gears").addEffect(damageWithChanceOfStun(20, 0.25f));
    forName("shock and awe").addEffect(damageWithChanceOfStun(30, 0.25f));
    forName("interrupting jolt").addEffect(singleAttack(20), SpecialEffect.INTERRUPT_OPPONENT_ROUND_IF_FIRST);
    mapPassiveEffect(392, 391, Target.SELF, 3, damageReceivedMultiplier(-0.5f)); // extra plating
    forName("lock-on").addEffect(conditionalEffect(Condition.hasAbility(forName("locked on"), Target.ENEMY_ACTIVE_PET), singleAttack(45), applyEffect("locked on", Target.ENEMY_PET, -1)));
    forName("locked on").addEffect(SpecialEffect.DUMMY);
    forName("launch rocket").addEffect(conditionalEffect(Condition.hasAbility(forName("setup rocket"), Target.SELF), singleAttack(45), applyEffect("setup rocket", Target.SELF, -1)));
    forName("setup rocket").addEffect(SpecialEffect.DUMMY);
    mapPassiveEffect(1733, 1734, Target.SELF, 3, damageReceivedMultiplier(-0.5f)); // extra plating 2
    
    /* undead */
    forName("infected claw").addEffect(singleAttack(22, 0.3f));
    forName("diseased bite").addEffect(singleAttack(21, 0.2f));
    forName("bone bite").addEffect(singleAttack(20));
    forName("creepy chomp").addEffect(singleAttack(20));
    forName("shadow slash").addEffect(singleAttack(21, 0.2f));
    forName("shadow shock").addEffect(singleAttack(22, 0.3f));
    forName("cleave").addEffect(teamSplitAttack(20));
    forName("ghostly bite").addEffect(singleAttack(40), applyEffect(STUN_ID, Target.SELF, 1));
    forName("haunting song").addEffect(teamHeal(16));
    PetAbility.forNameAll("gargoyle strike").forEach(a -> a.addEffect(singleAttack(20)));
    mapPassiveEffect(214, 213, Target.ENEMY_TEAM, 9, periodicDamage(3, PetFamily.undead, Target.ENEMY_ACTIVE_PET)); // death and decay
    mapPassiveEffect(218, 217, Target.ENEMY_PET, 4, expireEffect(singleAttack(40, PetFamily.undead))); // curse of doom

    
    /* water */
    forName("water jet").addEffect(singleAttack(20));
    forName("steam vent").addEffect(singleAttack(22, 0.3f));
    forName("tail slap").addEffect(singleAttack(22, 0.3f));
    forName("fish slap").addEffect(singleAttack(22, 0.15f)); // TODO: verify variance
    forName("brew bolt").addEffect(singleAttack(20,0.3f));
    forName("tentacle slap").addEffect(singleAttack(22, 0.2f)); // TODO: verify variance, formula is not precise
    forName("healing wave").addEffect(singleHeal(30));
    forName("carpnado").addEffect(teamSplitAttack(30));
    mapPassiveEffect(1572, 1571, Target.ENEMY_ACTIVE_PET, 2, speedMultiplier(-0.5f)); // vicious streak
    mapPassiveEffect(1062, 1061, Target.TEAM, 2, hitChanceModifier(0.5f), critChanceModifier(0.5f)); // rain dance
    forName("pumped up").addEffect(damageDoneMultiplier(0.1f));
    forName("pump").addEffect(conditionalEffect(Condition.hasAbility(forName("pumped up"), Target.SELF), singleAttack(45), applyEffect("pumped up", Target.SELF, -1)));
    forName("grasp").addEffect(singleAttack(15), applyEffect("rooted", Target.ENEMY_PET, 2));

    
    /* critter */
    forName("scratch").addEffect(singleAttack(20));
    forName("skitter").addEffect(singleAttack(20));
    forName("chomp").addEffect(singleAttack(22, 0.3f));
    forName("inspiring song").addEffect(teamHeal(12));
    forName("nature's touch").addEffect(singleHeal(30));
    forName("quick attack").addEffect(singleAttack(15), SpecialEffect.alwaysGoestFirst(50)); // multiplier is just guessed
    mapPassiveEffect(165, 164, Target.SELF, 3, damageReceivedMultiplier(-0.5f)); // crouch
    mapPassiveEffect(162, 161, Target.SELF, 3, speedMultiplier(0.75f)); // adrenaline rush
    mapPassiveEffect(366, 365, Target.TEAM, 9, speedMultiplier(0.25f)); // dazzling dance
    mapPassiveEffect(252, 251, Target.TEAM, 4, critChanceModifier(0.25f), hitChanceModifier(0.5f)); // uncanny luck
    mapPassiveEffect(851, 850, Target.SELF, 2, speedMultiplier(1.0f)); // vicious streak
    mapPassiveEffect(254, 255, Target.TEAM, 2, periodicHeal(10, Target.ACTIVE_PET)); // tranquillity

    
    
    /* special */
    forName("who's the best elekk in the whole world?").addEffect(SpecialEffect.DUMMY);
    forId(1345).addEffect(SpecialEffect.DUMMY); // nap time elekk
    forName("plushie rush").addEffect(SpecialEffect.DUMMY);
    forName("rawr!").addEffect(SpecialEffect.DUMMY);

    
    forName("moonlight").addEffect(healingReceivedMultiplier(0.25f), damageDoneMultiplier(0.1f, PetFamily.magical));
    forName("elemental mark").addEffect(new SpecialEffect.AlterFamily(PetFamily.elemental));
    
    forId(SPEED_REDUCTION_25).addEffect(speedMultiplier(-0.25f)); // speed reduction
    forId(928).addEffect(speedMultiplier(-0.25f)); // speed reduction
    forName("rooted").addEffect(SpecialEffect.UNABLE_TO_SWAP);
    PetAbility.forNameAll("stunned").forEach(a -> a.addEffect(SpecialEffect.STUNNED) );

    long mapped = PetAbility.data.values().stream().filter(a -> a.effectCount() > 0).count();
    long total = PetAbility.data.values().stream().filter(a -> !a.isFiltered).count();
    System.out.println(String.format("Ablities with mechanics %d/%d (%2.2f%%)", (int)mapped, (int)total, (mapped/(float)total)*100));
  }
}
