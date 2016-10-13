package com.jack.wow.ui.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import com.jack.wow.battle.EffectStatus;
import com.jack.wow.data.PetFamily;

public class MyGfx extends Gfx
{
  public static final Color GRAYED_OUT_COLOR = new Color(0,0,0,200);
  public static final Color DEAD_COLOR = new Color(100,0,0,230);
  
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
  
  public void drawEffect(EffectStatus effect, int x, int y, int iconSize)
  {
    drawIcon(effect.ability().icon, null, iconSize, x, y);
    rect(x, y, iconSize, iconSize, Color.GRAY);
    
    if (effect.isFinite())
    {
      font(-4.0f);
      stringCentered(String.valueOf(effect.remainingTurns()), x + iconSize, y + iconSize, Color.YELLOW);
      restoreFont();
    }
    
    if (effect.hasCharges())
    {
      font(-4.0f);
      stringCentered(String.valueOf(effect.remainingTurns()), x, y + iconSize, Color.RED);
      restoreFont();
    }
  }
  
  public void drawStats(int x, int y, int health, int power, int speed)
  {
    setFont(font(0.0f, Font.BOLD));
    image(Icons.Misc.HEALTH.image(), x, y);
    string(Integer.toString(health), x + 20, y + 20 - fontHeight()/2, Color.WHITE);
    image(Icons.Misc.POWER.image(), x + 70, y);
    string(Integer.toString(power), x + 20 + 70, y + 20 - fontHeight()/2, Color.WHITE);
    image(Icons.Misc.SPEED.image(), x + 70 + 60, y);
    string(Integer.toString(speed), x + 70 + 60 + 20, y + 20 - fontHeight()/2, Color.WHITE);
  }
  
  public void drawFamily(PetFamily family, int x, int y)
  {
    Image fimg = family.getTinyIcon().getImage();
    int w = fimg.getWidth(null), h = fimg.getHeight(null);
    
    fillOval(x - 10, y - 10, 20, 20, new Color(0,0,0,200));
    image(fimg, x - w/2, y - h/2);
  }
  
  public void drawTitle(String title, int x, int y, float sizeDelta)
  {
    setFont(font(sizeDelta, Font.BOLD));
    string(title, x, y + fontHeight()/2, Color.WHITE);
  }
  
  public void drawHealthBar(int value, int max, int x, int y, int w, int h)
  {
    float percent = value / (float)max;
    final int MAX = 150;
    
    fillRect(x, y, w, h, 80, 80, 80);
    rect(x, y, w, h, 160, 160, 160);
    
    if (percent > 0.0f)
    {
      fillRect(x, y, w * percent, h, 0, 120, 0);
      rect(x, y, w * percent, h, 0, 200, 0);
    }
    
    setFont(font(-3.0f));
    stringCentered(String.format("%d/%d", value, max), x + w/2, y + h/2, new Color(255,255,255,255));
    restoreFont();
  }
}
