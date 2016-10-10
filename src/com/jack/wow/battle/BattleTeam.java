package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BattleTeam implements Iterable<BattlePet>
{
  private final BattlePet[] pets;
  private final List<EffectStatus> effects;
  
  public BattleTeam(BattlePet... pets)
  {
    if (pets.length > 3) throw new IllegalArgumentException("BattleTeam must have at most 3 pets");
    
    this.pets = pets;
    
    this.effects = new ArrayList<>();
  }
  
  public BattlePet pet(int i) { return pets[i]; }
  
  public List<EffectStatus> effects() { return effects; }

  @Override public Iterator<BattlePet> iterator() { return Arrays.asList(pets).iterator(); }
}
