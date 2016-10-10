package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jack.wow.battle.abilities.PassiveEffect;

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
      team.effects().stream()
        .filter(EffectStatus::isEnded)
        .forEach(s -> passives.add(new PassiveEffectInfo(s, new BattleStatus(battle, team))));
      
      /* add all passives from pets */
      for (BattlePet pet : team)
      {
        pet.effects().stream()
          .filter(EffectStatus::isEnded)
          .forEach(s -> passives.add(new PassiveEffectInfo(s, new BattleStatus(battle, pet))));
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
    findAllPassiveEffects().stream().forEach(i -> i.effect.consume() );
  }
  
  
  public void executeTurn(Battle battle)
  {
    
  }
}
