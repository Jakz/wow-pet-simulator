package com.jack.wow.ui.misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import com.jack.wow.data.PetFamily;

public class MyGfx extends Gfx
{
  public MyGfx(Graphics g, boolean antialias)
  {
    super(g, antialias);
  }

  public void drawIcon(String icon, Color color, int ICON_SIZE, int x, int y)
  {
    Image img = Icons.getIcon(icon, false).getImage();
    
    if (color != null)
      fillRect(x-1, y-1, ICON_SIZE+2, ICON_SIZE+2, color);
    
    if (img != null)
      image(img, x, y, ICON_SIZE, ICON_SIZE);

    if (color != null)
    {
      Color acolor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 180);
      rect(x, y, ICON_SIZE-1, ICON_SIZE-1, acolor);

      acolor = new Color(acolor.getRed(), acolor.getGreen(), acolor.getBlue(), 128);
      rect(x+1, y+1, ICON_SIZE-3, ICON_SIZE-3, acolor);
    }
  }
  
  public void drawFamily(PetFamily family, int x, int y)
  {
    Image fimg = family.getTinyIcon().getImage();
    int w = fimg.getWidth(null), h = fimg.getHeight(null);
    
    fillOval(x - 10, y - 10, 20, 20, new Color(0,0,0,200));
    image(fimg, x - w/2, y - h/2);
  }
}
