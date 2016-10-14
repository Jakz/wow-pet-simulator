package com.jack.wow.battle;

import java.util.Optional;

import com.jack.wow.battle.abilities.EffectApply;

public class Battle
{
  private int turn;
  
  private final BattleTeam[] teams;
  private final BattlePet[] activePets;
  private Optional<EffectStatus> globalEffect;
  
  public Battle(BattleTeam team1, BattleTeam team2)
  {
    teams = new BattleTeam[] { team1, team2 };
    activePets = new BattlePet[] { team1.pet(0), team2.pet(0) };
    globalEffect = Optional.empty();
  }
  
  public BattleTeam[] teams() { return teams; }
  public BattleTeam team(int i) { return teams[i]; }
  public BattlePet activePet(int i) { return activePets[i]; }
  
  public Optional<EffectStatus> globalEffect() { return globalEffect; }
  public void clearGlobalEffect() { globalEffect = Optional.empty(); }
  public void setGlobalEffect(EffectApply effect) { globalEffect = Optional.of(new EffectStatus(effect)); }
  
  /**
   * Resets the status of a battle: clear all effects from pets, teams and global effects. Reset hit points and cooldowns for all pets
   */
  public void reset()
  {
    turn = 0;
    for (BattleTeam team : teams)
    {
      team.effects().clear();
      for (BattlePet pet : team)
      {
        pet.resetHitPoints();
        pet.resetCooldownsAndCharges();
        pet.effects().clear();
      }
    }
    
    activePets[0] = teams[0].pet(0);
    activePets[1] = teams[1].pet(0);
  }

  public int elapsedTurns() { return turn; }
  public void incrementTurn() { ++turn; }
}
