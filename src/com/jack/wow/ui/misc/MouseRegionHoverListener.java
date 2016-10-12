package com.jack.wow.ui.misc;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class MouseRegionHoverListener implements MouseMotionListener
{
  private class ActiveZone
  {
    Rectangle area;
    Runnable callback;
    public boolean isInside(int x, int y) { return area.contains(x, y); }
  }
  
  private final Consumer<Boolean> foundLambda;
  private final List<ActiveZone> zones;
  private boolean enabled;
  
  public MouseRegionHoverListener(Consumer<Boolean> foundLambda)
  {
    zones = new ArrayList<>();
    enabled = true;
    this.foundLambda = foundLambda;
  }
  
  public void clear()
  {
    zones.clear();
  }
  
  public void addZone(int x, int y, int w, int h, Runnable lambda)
  {
    ActiveZone zone = new ActiveZone();
    zone.area = new Rectangle(x,y,w,h);
    zone.callback = lambda;
    zones.add(zone);
  }
  
  public void toogle(boolean enabled) { this.enabled = enabled; }
  
  @Override public void mouseDragged(MouseEvent e) { }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    if (enabled)
    {    
      int x = e.getX(), y = e.getY();
      
      for (ActiveZone zone : zones)
      {
        if (zone.isInside(x,y))
        {
          zone.callback.run();
          foundLambda.accept(true);
          return;
        }
      }
    }
    
    foundLambda.accept(false);
  }

}
