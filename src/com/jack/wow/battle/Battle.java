package com.jack.wow.battle;

public class Battle
{
  private final BattleTeam[] teams;
  
  public Battle(BattleTeam team1, BattleTeam team2)
  {
    teams = new BattleTeam[] { team1, team2 };
  }
  
  public BattleTeam team(int i) { return teams[i]; }
}
