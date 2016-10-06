package com.jack.wow.data;

import java.net.URL;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum PetFamily
{
  beast("Beast", "beast", 7),
  critter("Critter", "critter", 4),
  dragonkin("Dragonkin", "dragonkin", 1),
  elemental("Elemental", "elemental", 6),
  flying("Flying", "flying", 2),
  magical("Magical", "magical", 5),
  mechanical("Mechanical", "mechanical", 9),
  humanoid("Humanoid", "humanoid", 0),
  undead("Undead", "undead", 3),
  water("Water", "water", 8),
  ;
  
  private PetFamily(String description, String jsonName, int id)
  {
    this.description = description;
    this.jsonName = jsonName;
    this.id = id;
  }
  
  public final String description;
  public final String jsonName;
  public final int id;
  
  private PetFamily strongVS;
  private PetFamily weakVS;
  
  private ImageIcon tinyIcon;
  
  public PetFamily getStrongAttacking() { return strongVS; }
  public PetFamily getWeakAttacking() { return weakVS; }
  
  public PetFamily getStrongDefending() { return Arrays.stream(values()).filter(f -> f.getWeakAttacking() == this).findFirst().get(); }
  public PetFamily getWeakDefending() { return Arrays.stream(values()).filter(f -> f.getStrongAttacking() == this).findFirst().get(); }
  
  public ImageIcon getTinyIcon()
  {
    try
    {
      if (tinyIcon == null)
      {
        URL file = this.getClass().getClassLoader().getResource("com/jack/wow/ui/resources/family-"+jsonName+"-tiny.png");
        tinyIcon = new ImageIcon(ImageIO.read(file));
        return tinyIcon;
      }
      else
        return tinyIcon;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    return new ImageIcon();

  }
  
  
  public static PetFamily unserialize(String name)
  {
    return Arrays.stream(values()).filter(c -> c.jsonName.equals(name)).findFirst().orElse(null);
  }
  
  public static PetFamily unserialize(int id)
  {
    return Arrays.stream(values()).filter(c -> c.id == id).findFirst().orElse(null);
  }
  
  public static int count() { return values().length; }
  
  static
  {
    PetFamily.humanoid.strongVS = PetFamily.dragonkin;
    PetFamily.humanoid.weakVS = PetFamily.beast;
    
    PetFamily.dragonkin.strongVS = PetFamily.magical;
    PetFamily.dragonkin.weakVS = PetFamily.undead;
    
    PetFamily.flying.strongVS = PetFamily.water;
    PetFamily.flying.weakVS = PetFamily.dragonkin;
    
    PetFamily.undead.strongVS = PetFamily.humanoid;
    PetFamily.undead.weakVS = PetFamily.water;
    
    PetFamily.critter.strongVS = PetFamily.undead;
    PetFamily.critter.weakVS = PetFamily.humanoid;
    
    PetFamily.magical.strongVS = PetFamily.flying;
    PetFamily.magical.weakVS = PetFamily.mechanical;
    
    PetFamily.elemental.strongVS = PetFamily.mechanical;
    PetFamily.elemental.weakVS = PetFamily.critter;
    
    PetFamily.beast.strongVS = PetFamily.critter;
    PetFamily.beast.weakVS = PetFamily.flying;
    
    PetFamily.water.strongVS = PetFamily.elemental;
    PetFamily.water.weakVS = PetFamily.magical;
    
    PetFamily.mechanical.strongVS = PetFamily.beast;
    PetFamily.mechanical.weakVS = PetFamily.elemental;
    
    for (PetFamily family : values())
      if (family.getStrongAttacking() == null || family.getWeakAttacking() == null)
        throw new IllegalArgumentException("Strong or weak family is null for "+family.description);
  }
}
