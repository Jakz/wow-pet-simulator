package com.jack.wow.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jack.wow.battle.abilities.Effect;
import com.jack.wow.data.interfaces.Familied;
import com.jack.wow.data.interfaces.Iconed;
import com.jack.wow.files.api.ApiAbility;

public class PetAbility implements Iterable<Effect>, Iconed, Familied
{  
  public int id;
  public String name;

  public PetFamily family;
  public int cooldown;
  public int rounds;
  public boolean isPassive;
  public boolean hideHints;
  private Optional<Float> hitChance;
  public boolean isFiltered;
  
  
  public String tooltip;
  public String icon;
    
  private List<Effect> effects;
  
  public PetAbility()
  { 
    this.effects = new ArrayList<>();
  }
  
  private PetAbility(int id, String name, PetFamily family, int cooldown, int rounds, boolean isPassive, boolean hideHints, String icon)
  {
    this();
    this.id = id;
    this.name = name;
    this.family = family;
    this.cooldown = cooldown;
    this.rounds = rounds;
    this.isPassive = isPassive;
    this.hideHints = hideHints;
    this.icon = icon;
    this.hitChance = Optional.empty();
    this.isFiltered = CustomData.isFiltered(this);
  }
  
  public PetAbility(ApiAbility a)
  {
    this(a.id, a.name, PetFamily.unserialize(a.petTypeId), a.cooldown, a.rounds, a.isPassive, a.hideHints, a.icon);
  }
  
  public PetAbility(PetFamily family, int cooldown, float hitChance, Effect... effects)
  {
    this.family = family;
    this.cooldown = cooldown;
    this.hitChance = Optional.of(hitChance * 100.0f);
    this.effects = Arrays.asList(effects);
  }
  
  @Override public String iconName() { return icon; }
  @Override public PetFamily family() { return family; }
  
  /* effect management */
  public PetAbility addEffect(Effect... effects) { this.effects.addAll(Arrays.asList(effects)); return this; }
  public Iterator<Effect> iterator() { return effects.iterator(); }
  public Stream<Effect> effects() { return effects.stream(); }
  public Effect effectAt(int i) { return effects.get(i); }
  public int effectCount() { return effects.size(); }
  public <T extends Effect> Effect findEffect(Class<T> type) { return effects.stream().filter(e -> type.isAssignableFrom(e.getClass())).findFirst().orElse(null); }
  
  @SuppressWarnings("unchecked")
  public <T extends Effect> Stream<T> findAllEffects(Class<T> type)
  { 
    return effects.stream()
        .filter(e -> type.isAssignableFrom(e.getClass()))
        .map(e -> (T)e);
  }
  
  public void setHitChance(float chance) { this.hitChance = Optional.of(chance); }
  public void setTooltip(String tooltip) { this.tooltip = tooltip; }
  
  public float hitChance() { return hitChance.orElse(0.0f)/100.0f; }
  
  /* static methods to manage the abilities database (generate and retrieval) */
  public static final Map<Integer, PetAbility> data = new HashMap<>();
  public static final Map<PetAbility, List<PetSpec>> usage = new HashMap<>();
  
  public static void add(PetAbility ability) { data.putIfAbsent(ability.id, ability); }

  public static PetAbility forId(int id)
  { 
    PetAbility ability = data.get(id);
    
    if (ability == null)
      throw new InvalidIdException("no ability with id "+id+" found.");
    
    return ability;
  }

  public static List<PetAbility> forNameAll(String name)
  {
    List<PetAbility> abilities = data.values().stream().filter(a -> a.name.compareToIgnoreCase(name) == 0).collect(Collectors.toList());
    
    if (abilities.isEmpty())
      throw new IllegalArgumentException("no ability named "+name+" found.");
    
    return abilities;
  }
  
  public static void prune()
  {
    data.values().stream().forEach(a -> a.isFiltered = CustomData.isFiltered(a));
  }

  public static PetAbility forNameAny(String name)
  {
    List<PetAbility> abilities = forNameAll(name);
    return abilities.get(0);
  }
  
  public static PetAbility forName(String name)
  {
    List<PetAbility> abilities = forNameAll(name);

    if (abilities.size() > 1)
      throw new IllegalArgumentException("ability lookup by name "+name+" has multiple results.");

    return abilities.get(0);
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
