package com.jack.wow.battle;

import java.util.Optional;

/**
 * This class is used as the point of interaction used by all the effects to retrieve informations
 * and possibly act on the battle.
 * @author Jack
 */
public class BattleStatus
{
  public final Battle battle;
  public final BattlePet self, enemy;
  
  BattleStatus(Battle battle, BattlePet self, BattlePet enemy)
  {
    this.battle = battle;
    this.self = self;
    this.enemy = enemy;
  }
  
  BattleStatus(Battle battle)
  {
    this.battle = battle;
    this.self = null;
    this.enemy = null;
  }
}
