package com.jack.wow.battle;

/**
 * This class encloses which abilities are chosen for a specific pet. It is used to decide which 
 * abilities are used by a <code>BattlePet</code> instance.
 * 
 * @author jack
 */
public class AbilitySet
{
  private final int[] indices;
  
  /**
   * Build a new <code>AbilitySet</code> with specified indices for abilities.
   * Please note that indices are relative to the slot and no absolute, so their range is <code>[1,2]</code>
   * 
   * @param i0 first index
   * @param i1 second index
   * @param i2 third index
   */
  public AbilitySet(int i0, int i1, int i2)
  {
    indices = new int[] { i0-1, i1-1, i2-1 };
  }
  
  public int index(int i) { return indices[i]; }
  public int[] indices() { return indices; }
}
