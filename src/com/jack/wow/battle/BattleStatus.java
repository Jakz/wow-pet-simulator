package com.jack.wow.battle;

import java.util.Optional;

public class BattleStatus
{
  public final Battle battle;
  public final Optional<BattleTeam> team;
  public final Optional<BattlePet> pet;
  
  BattleStatus(Battle battle)
  {
    this.battle = battle;
    this.team = Optional.empty();
    this.pet = Optional.empty();
  }
  
  BattleStatus(Battle battle, BattleTeam team)
  {
    this.battle = battle;
    this.team = Optional.of(team);
    this.pet = Optional.empty();
  }
  
  BattleStatus(Battle battle, BattlePet pet)
  {
    this.battle = battle;
    this.team = Optional.empty();
    this.pet = Optional.of(pet);
  }
}
