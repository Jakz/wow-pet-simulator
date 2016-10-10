package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BattleTeam implements Iterable<BattlePet>
{
  private final BattlePet[] pets;
  private final EffectList effects;
  
  public BattleTeam(BattlePet... pets)
  {
    if (pets.length > 3) throw new IllegalArgumentException("BattleTeam must have at most 3 pets");
    
    this.pets = pets;
    
    this.effects = new EffectList();
  }
  
  public BattlePet pet(int i) { return pets[i]; }
  
  public EffectList effects() { return effects; }

  @Override public Iterator<BattlePet> iterator() { return Arrays.asList(pets).iterator(); }
}
