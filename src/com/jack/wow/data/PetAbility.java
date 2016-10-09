package com.jack.wow.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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
  public boolean hideHints;
  
  public String description;
  public String icon;
    
  private List<Effect> effects;
  
  public PetAbility() { }
  
  private PetAbility(int id, String name, PetFamily family, int cooldown, int rounds, boolean isPassive, boolean hideHints, String icon)
  {
    this.id = id;
    this.name = name;
    this.family = family;
    this.cooldown = cooldown;
    this.rounds = rounds;
    this.isPassive = isPassive;
    this.hideHints = hideHints;
    this.icon = icon;
  }
  
  /* effect management */
  public void addEffect(Effect effect) { effects.add(effect); }
  public Iterator<Effect> iterator() { return effects.iterator(); }
  public Effect effectAt(int i) { return effects.get(i); }
  public int effectCount() { return effects.size(); }
  
  /* static methods to manage the abilities database (generate and retrieval) */
  public static final Map<Integer, PetAbility> data = new HashMap<>();
  public static final Map<PetAbility, List<PetSpec>> usage = new HashMap<>();
  
  public static PetAbility get(int id) { return data.get(id); }
  
  public static PetAbility generate(int id, String name, PetFamily family, int cooldown, int rounds, boolean isPassive, boolean hideHints, String icon)
  {
    PetAbility ability = new PetAbility(id, name, family, cooldown, rounds, isPassive, hideHints, icon);
    data.putIfAbsent(id, ability);
    return ability;
  }
 
  public static PetAbility generate(ApiAbility a)
  {
    PetAbility ability = new PetAbility(a.id, a.name, PetFamily.unserialize(a.petTypeId), a.cooldown, a.rounds, a.isPassive, a.hideHints, a.icon);
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
  
  public static void computeUsageOfAbilities()
  {
    usage.clear();
    Arrays.stream(PetSpec.data).forEach(p -> {
      for (int i = 0; i < 3; ++i)
        p.slot(i).stream().forEach(a -> { 
          usage.computeIfAbsent(a.get(), t -> new ArrayList<PetSpec>()).add(p);
        });
    });
  }
}
