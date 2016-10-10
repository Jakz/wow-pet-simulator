package com.jack.wow.battle.abilities;

public abstract class ModifierEffect implements ModifierFunction, PassiveEffect
{
  final public float parameter;
  
  private ModifierEffect(float parameter)
  {
    this.parameter = parameter;
  }
  
  public abstract float apply(Target target, float value);
  
  public static ModifierFunction buildSpeed(float p) { return (t, v) -> t == Target.SPEED ? v*p : p; }  
  public static ModifierFunction buildDamageDone(float p) { return (t, v) -> t == Target.DAMAGE ? v*p : p; }  
  public static ModifierFunction buildDamageReceived(float p) { return (t, v) -> t == Target.DAMAGE_RECEIVED ? v*p : p; }  
  
  public static ModifierFunction buildHitChance(float p) { return (t, v) -> t == Target.HIT_CHANCE ? v+p : p; }  
  public static ModifierFunction buildCritChance(float p) { return (t, v) -> t == Target.CRIT_CHANCE ? v+p : p; }  
  public static ModifierFunction buildDodgeChance(float p) { return (t, v) -> t == Target.DODGE_CHANCE ? v+p : p; }  

}
