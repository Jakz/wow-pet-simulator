package com.jack.wow.battle.abilities;

import com.jack.wow.battle.BattleStatus;
import com.jack.wow.data.PetFamily;

public class ModifierEffect implements ModifierFunction, PassiveEffect
{
  final public float parameter;
  final public Target target;
  
  private ModifierEffect(Target target, float parameter)
  {
    this.parameter = parameter;
    this.target = target;
  }
  
  @Override public float onCalculateStat(BattleStatus status, Target target, float value)
  {
    return apply(status, target, value);
  }
  
  @Override public float apply(BattleStatus status, Target target, float value)
  { 
    if (this.target == target)
      return target.isAdditive ? (parameter + value) : (parameter * value);
    else
      return value;
  }
  
  @Override public boolean isNegative() { return parameter < 0; }
  
  @Override public String toString()
  {
    String partial = "modifier("+target.description+", ";
    
    if (target.isAdditive)
      return partial += (int)(parameter*100) + "%)";
    else
      return partial += (int)((parameter-1.0f)*100) + "%)";
  }
  
  public static ModifierFunction buildSpeed(float p) { return new ModifierEffect(Target.SPEED, p); }  
  public static ModifierFunction buildDamageDone(float p) { return new ModifierEffect(Target.DAMAGE, p); }  
  
  public static ModifierFunction buildDamageDone(final PetFamily family, float p) { 
    return new ModifierEffect(Target.DAMAGE, p) {
      @Override public float apply(BattleStatus status, Target target, float value)
      {
        throw new RuntimeException();
      }
      
      @Override public String toString()
      {
        String base = super.toString();
        return base.substring(0, base.length()-1) + ", " + family.description.toLowerCase() + ")";
      }
    };  
  }

  
  public static ModifierFunction buildDamageReceived(float p) { 
    return new ModifierEffect(Target.DAMAGE_RECEIVED, p)
    {
      @Override public boolean isNegative() { return parameter > 0; }
    };  
  }
  
  public static ModifierFunction buildRawDamageReceived(float p) {
    return new ModifierEffect(Target.DAMAGE_RECEIVED_RAW, p) {
      @Override public float apply(BattleStatus status, Target target, float value)
      {
        // must calculate final value with power of pet
        throw new RuntimeException();
      }
      
      @Override public boolean isNegative() { return parameter > 0; }

      @Override public String toString()
      {
        return "modifier("+target.description+", "+(int)parameter+")";
      }
    };  
  }

  public static ModifierFunction buildHealingReceived(float p) { return new ModifierEffect(Target.HEALING_RECEIVED, p); }
 
  public static ModifierFunction buildHealthModifier(float p) { return new ModifierEffect(Target.HEALTH, p); }
  
  public static ModifierFunction buildHitChance(float p) { return new ModifierEffect(Target.HIT_CHANCE, p); }  
  public static ModifierFunction buildCritChance(float p) { return new ModifierEffect(Target.CRIT_CHANCE, p); }  
  public static ModifierFunction buildDodgeChance(float p) { return new ModifierEffect(Target.DODGE_CHANCE, p); }  

}
