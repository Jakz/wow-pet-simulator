package com.jack.wow.battle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class encapsulates a battle team composed by 3 <code>BattlePet</code> and
 * an <code>EffectList</code> which contains all the effects which are globally
 * affecting the team.
 * 
 * @see BattlePet
 * @see EffectList
 * @author Jack
 */
public class BattleTeam implements Iterable<BattlePet>
{
  private final BattlePet[] pets;
  private final EffectList effects;
  
  public BattleTeam(BattlePet... pets)
  {
    if (pets.length > 3) throw new IllegalArgumentException("BattleTeam must have at most 3 pets");
    
    this.pets = pets;
    
    Arrays.stream(pets)
      .filter(Objects::nonNull)
      .forEach(p -> p.setTeam(this));
    
    this.effects = new EffectList();
  }
  
  public BattlePet pet(int i) { return pets[i]; } 
  public EffectList effects() { return effects; }

  @Override public Iterator<BattlePet> iterator() { return Arrays.asList(pets).iterator(); }
}
