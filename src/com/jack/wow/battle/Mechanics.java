package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static java.util.stream.Stream.concat;
import com.jack.wow.battle.abilities.ActiveHiddenEffect;
import com.jack.wow.battle.abilities.ComputedStat;
import com.jack.wow.battle.abilities.ModifierEffect;
import com.jack.wow.battle.abilities.ModifierFunction;
import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.Formulas;

public class Mechanics
{
  private Battle battle;
  
  public void setBattle(Battle battle)
  {
    this.battle = battle;
  }
  
  static class PassiveEffectInfo
  {
    public final EffectStatus effect;
    public final BattleStatus status;
    
    PassiveEffectInfo(EffectStatus effect, BattleStatus status)
    {
      this.effect = effect;
      this.status = status;
    }
  }
  
  /**
   * Utility function which finds all <code>EffectStatus</code> in the whole battle, so global, team and pet wise.
   * It returns a list of pairs, each pair contains the specific status and a <code>BattleStatus</code> instance, which
   * is used to attach the source of the effect so that the object can be used properly.
   * @return the list of all effect active in a battle paired with their target
   */
  protected List<PassiveEffectInfo> findAllPassiveEffects()
  {
    List<PassiveEffectInfo> passives = new ArrayList<>();
    
    /* add global effect if it's finished */
    Optional<EffectStatus> globalEffect = battle.globalEffect();
    if (globalEffect.isPresent())
      passives.add(new PassiveEffectInfo(globalEffect.get(), new BattleStatus(battle)));
    
    /* add all passives from both teams */
    for (BattleTeam team : battle.teams())
    {
      /*team.effects().stream()
        .filter(EffectStatus::isEnded)
        .forEach(s -> passives.add(new PassiveEffectInfo(s, new BattleStatus(battle, team))));*/
      
      /* add all passives from pets */
      for (BattlePet pet : team)
      {
        /*pet.effects().stream()
          .filter(EffectStatus::isEnded)
          .forEach(s -> passives.add(new PassiveEffectInfo(s, new BattleStatus(battle, pet))));*/
      }
    }
    
    return passives;
  }
  
  /**
   * This functions invokes <code>onEndEffect</code> on all passive effects (global, team and pet). This is used to manage effects which
   * do something special when they end (eg. Curse of Doom)
   */
  public void passiveEndLogic()
  {
    findAllPassiveEffects().stream()
      .filter(i -> i.effect.isEnded())
      .forEach(i -> {
        i.effect.passiveEffects().forEach(e -> e.onEndEffect(i.status));
      });
  }
  
  /**
   * This functions invokes <code>onTickEffect</code> on all passive effects (global, team and pet)
   */
  public void passiveTickLogic()
  {
    findAllPassiveEffects().stream()
    .filter(i -> i.effect.isEnded())
    .forEach(i -> {
      i.effect.passiveEffects().forEach(e -> e.onTickEffect(i.status));
    });
  }
  
  /**
   * This function updates the remaining turns on all effects by 1, mind that it doesn't remove them if they are expired.
   */
  public void updatePassiveCounters()
  {   
    findAllPassiveEffects().stream().forEach(i -> i.effect.consumeTurn() );
  }
  
  
  public void executeTurn(Battle battle)
  {
    
  }
  
  public float computeModifiedValue(ModifierFunction.Target type, float value, float multiplier, Stream<PassiveEffect> effects, BattleStatus status)
  {
    /* find all effects that are able to modify values */
    Stream<ModifierEffect> casted = effects.filter(e -> e instanceof ModifierEffect).map(e -> (ModifierEffect)e);
    
    /* apply reduction by passing through ComputedStat which is able to track multipliers and raw values indipendently */
    ComputedStat result = casted.reduce(new ComputedStat(value, multiplier), (v, effect) -> effect.onCalculateStat(status, type, v), ComputedStat::combine);

    return result.calculate();
  }

  
  /**
   * Computes the final speed for an ability.
   * It takes into account all the passive effects on the pet, all the passive effects on the team and the possible
   * hidden effects of the ability itself.
   * @param ability
   * @return the speed of the ability in the current context
   */
  public float computeSpeedForAbility(BattleAbilityStatus ability)
  {
    BattlePet pet = ability.owner();
    
    Stream<PassiveEffect> petEffects = pet.effects().passiveEffects();
    Stream<PassiveEffect> teamEffects = pet.team().effects().passiveEffects();
    Stream<PassiveEffect> hiddenEffects = ability.ability().get().findAllEffects(ActiveHiddenEffect.class).map(e -> e.effect);
    
    final BattleStatus status = new BattleStatus(battle, pet, null);
    
    Stream<PassiveEffect> effects = concat(concat(petEffects, teamEffects), hiddenEffects);

    return computeModifiedValue(ModifierFunction.Target.SPEED, pet.pet().stats().speed(), 1.0f, effects, status);
  }
  
  /**
   * Determines the order of two abilities used by two pets.
   * Algorithm computes the final speed of the pet by reducing all the passive bonuses/maluses of the pet and of the team
   * which affect speed to compute the final value.
   * @param ability1 first ability
   * @param ability2 second ability
   * @return an array which contains the two abilities in order of execution
   */
  public BattleAbilityStatus[] determineWhichAbilityAppliesFirst(BattleAbilityStatus ability1, BattleAbilityStatus ability2)
  {
    float speed1 = computeSpeedForAbility(ability1);
    float speed2 = computeSpeedForAbility(ability2);
    
    if (speed1 > speed2)
      return new BattleAbilityStatus[] { ability1, ability2 };
    else if (speed2 > speed1)
      return new BattleAbilityStatus[] { ability2, ability1 };
    else
    {
      if (Formulas.chance(0.5f))
        return new BattleAbilityStatus[] { ability1, ability2 };
      else
        return new BattleAbilityStatus[] { ability2, ability1 };
    }
  }
}
