package com.jack.wow.battle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.PetAbility;

public class EffectList implements Iterable<EffectStatus>
{
  private final List<EffectStatus> effects;
  
  public EffectList()
  {
    effects = new ArrayList<>();
  }

  public Stream<PassiveEffect> passiveEffects()
  {
    return effects.stream().flatMap(s -> s.passiveEffects());
  }
  
  public Stream<PassiveEffect> findAll(Predicate<PassiveEffect> predicate)
  {
    return passiveEffects().filter(predicate);
  }
  
  
  public int size() { return effects.size(); }
  public void clear() { effects.clear(); }
  public void add(PetAbility effect, int turns) { effects.add(new EffectStatus(effect, turns)); }

  @Override public Iterator<EffectStatus> iterator() { return effects.iterator(); }
  public Stream<EffectStatus> stream() { return effects.stream(); }
}
