package com.jack.wow.battle;

public class BattleTeam
{
  private final BattlePet[] pets;
  
  public BattleTeam(BattlePet... pets)
  {
    if (pets.length > 3) throw new IllegalArgumentException("BattleTeam must have at most 3 pets");
    
    this.pets = pets;
  }
  
  public BattlePet pet(int i) { return pets[i]; }
}
