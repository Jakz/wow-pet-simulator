package com.jack.wow.battle.abilities;

import java.util.function.Predicate;
import com.jack.wow.battle.BattleStatus;
import com.jack.wow.data.PetAbility;

@FunctionalInterface
public interface Condition extends Predicate<BattleStatus>
{

  public static class HasAbility implements Condition
  {
    public final PetAbility ability;
    public final Target target;
    
    public HasAbility(PetAbility ability, Target target)
    {
      this.ability = ability;
      this.target = target;
    }
    
    @Override public boolean test(BattleStatus status)
    {
      throw new RuntimeException();
    }
    
    @Override public String toString() { return "has-ability("+ability.id+"-"+ability.name.toLowerCase()+", "+target+")"; }
  }
  
  public static class HasStatus implements Condition
  {
    public final SpecialEffect status;
    public final Target target;
    
    public HasStatus(SpecialEffect status, Target target)
    {
      this.status = status;
      this.target = target;
    }
    
    @Override public boolean test(BattleStatus status)
    {
      throw new RuntimeException();
    }
    
    @Override public String toString() { return "has-status("+status+", "+target+")"; }

  }
}
