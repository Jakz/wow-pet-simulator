package com.jack.wow.battle;

import java.util.Optional;

public class Battle
{
  private int turn;
  
  private final BattleTeam[] teams;
  private Optional<EffectStatus> globalEffect;
  
  
  public Battle(BattleTeam team1, BattleTeam team2)
  {
    teams = new BattleTeam[] { team1, team2 };
    globalEffect = Optional.empty();
  }
  
  public BattleTeam[] teams() { return teams; }
  public BattleTeam team(int i) { return teams[i]; }
  
  public Optional<EffectStatus> globalEffect() { return globalEffect; }
  
  /**
   * Resets the status of a battle: clear all effects from pets, teams and global effects. Reset hit points and cooldowns for all pets
   */
  public void reset()
  {
    int turn = 0;
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
  }

  public int elapsedTurns() { return turn; }
  public void incrementTurn() { ++turn; }
}
