package com.jack.wow.data;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jack.wow.battle.abilities.Effect;
import com.jack.wow.files.api.ApiAbility;

public class PetAbility implements Iterable<Effect>
{  
  public int id;
  public String name;

  public PetFamily family;
  public int cooldown;
  public int rounds;
  public boolean isPassive;
  
  public String description;
  public String icon;
  
  private List<Effect> effects;
  
  public PetAbility() { }
  
  private PetAbility(int id, String name, PetFamily family, int cooldown, int rounds, boolean isPassive, String icon)
  {
    this.id = id;
    this.name = name;
    this.family = family;
    this.cooldown = cooldown;
    this.rounds = rounds;
    this.isPassive = isPassive;
    this.icon = icon;
  }
  
  public void addEffect(Effect effect) { effects.add(effect); }
  public Iterator<Effect> iterator() { return effects.iterator(); }
  public Effect effectAt(int i) { return effects.get(i); }
  public int effectCount() { return effects.size(); }
  
  public static final Map<Integer, PetAbility> data = new HashMap<>();
  
  public static PetAbility get(int id) { return data.get(id); }
  
  public static PetAbility generate(int id, String name, PetFamily family, int cooldown, int rounds, boolean isPassive, String icon)
  {
    PetAbility ability = new PetAbility(id, name, family, cooldown, rounds, isPassive, icon);
    data.putIfAbsent(id, ability);
    return ability;
  }
 
  public static PetAbility generate(ApiAbility a)
  {
    PetAbility ability = new PetAbility(a.id, a.name, PetFamily.unserialize(a.petTypeId), a.cooldown, a.rounds, a.isPassive, a.icon);
    data.putIfAbsent(a.id, ability);
    return ability;
  }
  
  public static PetAbility forName(String name)
  {
    PetAbility ability = data.values().stream().filter(a -> a.name.compareToIgnoreCase(name) == 0).findAny().orElse(null);
    
    if (ability == null)
      throw new IllegalArgumentException("no ability named "+name+" found.");
    
    return ability;
  }
  
}
