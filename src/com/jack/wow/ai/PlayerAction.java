package com.jack.wow.ai;

/**
 * This class is used to tell the simulator which action is requested
 * by a player / AI.
 * 
 * @author Jack
 */
public class PlayerAction
{
  public static enum Type
  {
    ABILITY,
    SWAP,
    PASS
  };
  
  public final Type type;
  public final int index;
  
  public PlayerAction(Type type, int index)
  {
    this.type = type;
    this.index = index;
  }
}
