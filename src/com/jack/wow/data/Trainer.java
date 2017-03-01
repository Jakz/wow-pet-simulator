package com.jack.wow.data;

import com.jack.wow.battle.BattlePet;
import com.jack.wow.battle.BattleTeam;

public class Trainer
{
  public static enum Expansion
  {
    VANILLA,
    TBC,
    WOTLK,
    CATACLYSM,
    MOP,
    LEGION
  }
  
  String name;
  String zone;
  String continent;
  Expansion expansion;
  
  private BattleTeam team;
  
  public Trainer(String name, String zone, String continent, Expansion expansion, BattlePet... pets)
  {
    this.name = name;
    this.zone = zone;
    this.continent = continent;
    this.expansion = expansion;
    this.team = new BattleTeam(pets);
  }
}
