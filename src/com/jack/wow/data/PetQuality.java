package com.jack.wow.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public enum PetQuality
{
  POOR("Poor", 0, 1.0f, new Color(157, 157, 157)),
  COMMON("Common", 1, 1.1f, new Color(255, 255, 255)),
  UNCOMMON("Uncommon", 2, 1.2f, new Color(29, 255, 0)),
  RARE("Rare", 3, 1.3f, new Color(0, 112, 221)),
  EPIC("Epic", 4, 1.4f, new Color(130, 48, 238)),
  LEGENDARY("Legendary", 5, 1.5f, new Color(255, 128, 30))
  ;

  public final String description;
  public final int id;
  public final float coefficient;
  public final Color color;
  private ImageIcon icon;
  
  private PetQuality(String description, int id, float coefficient, Color color)
  {
    this.description = description;
    this.id = id;
    this.coefficient = coefficient;
    this.color = color;
  }
  
  public ImageIcon icon(int size)
  {
    if (icon == null || icon.getImage().getWidth(null) != size)
    {
      BufferedImage img = new BufferedImage(size, size,BufferedImage.TYPE_INT_ARGB);
      Graphics g = img.getGraphics();
      g.setColor(color);
      g.fillRect(0, 0, size, size);
      g.setColor(Color.DARK_GRAY);
      g.drawRect(0, 0, size-1, size-1);
      icon = new ImageIcon(img);
    }
    
    return icon;
  }
}
