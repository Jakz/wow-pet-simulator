package com.jack.wow.battle;

/**
 * This class is used as the point of interaction used by all the effects to retrieve informations
 * and possibly act on the battle.
 * @author Jack
 */
public class BattleStatus
{
  public final Battle battle;
  public final BattlePet self, enemy;
  public final EffectInfo passive;

  public BattleStatus(Battle battle, BattlePet self, BattlePet enemy, EffectInfo passive)
  {
    this.battle = battle;
    this.self = self;
    this.enemy = enemy;
    this.passive = passive;
  }
  
  public BattleStatus(Battle battle, BattlePet self, BattlePet enemy)
  {
    this(battle, self, enemy, null);
  }
  
  public BattleStatus(Battle battle)
  {
    this(battle, null, null, null);
  }
  
  public BattleStatus forPassive(EffectInfo passive)
  {
    return new BattleStatus(battle, null, null, passive);
  }
}
