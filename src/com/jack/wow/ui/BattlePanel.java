package com.jack.wow.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

import com.jack.wow.battle.Battle;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.ui.misc.Gfx;
import com.jack.wow.ui.misc.Icons;

public class BattlePanel extends JPanel
{
  private Battle battle;
  
  BattlePanel()
  {
    setPreferredSize(new Dimension(500,500));
    ToolTipManager.sharedInstance().registerComponent(this);
    ToolTipManager.sharedInstance().setEnabled(true);
    ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
    ToolTipManager.sharedInstance().setInitialDelay(0);
    this.setFocusable(true);
  }
  
  private Graphics2D gfx;
  
  void setGfx(Graphics2D gfx) { this.gfx = gfx; }
  
  void fillRect(float x, float y, float w, float h, Color c)
  {
    gfx.setColor(c);
    gfx.fillRect((int)x, (int)y, (int)w, (int)h);
  }
  
  void drawImage(float x, float y, float w, float h, Image image)
  {
    gfx.drawImage(image, (int)x, (int)y, (int)(x+w), (int)(y+h), 0, 0, image.getWidth(null), image.getHeight(null), Color.BLACK, null); 
  }
  
  private final int PET_WIDTH = 64;
  
  Image image(String name) { return Icons.getIcon(name, false).getImage(); }
  Image image(String name, int w, int h) { return Icons.getIcon(name, false).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH); }

  
  @Override public void paintComponent(Graphics gg)
  {
    setGfx((Graphics2D)gg);
    
    Gfx gfx = new Gfx(gg, true);
    
    gfx.clear(36,36,36);
    
    float width = this.getWidth();
    float height = this.getHeight();
    
    //fillRect(0, height*0.66f, width, height*0.34f, Color.RED);
    
    if (battle != null)
    {
      for (int i = 0; i < 3; ++i)
      {
        drawImage(10, 10 + i*80, 64, 64, image(battle.team(0).pet(i).pet().spec().icon));
        
        for (int j = 0; j < 3; ++j)
          drawImage(10 + 64 + 5 + 25*j, 10 + i*80, 24, 24, image(battle.team(0).pet(i).ability(j).get().icon));

      } 
    }
  }
    
  public void setBattle(Battle battle)
  {
    this.battle = battle;
    this.repaint();
  }
  
  @Override public JToolTip createToolTip()
  {
    CustomToolTip toolTip = new CustomToolTip(this);
    toolTip.setPetSpec(battle.team(0).pet(0).pet().spec());
    //toolTip.setAbility(PetAbility.forName("bombing run"));

    return null;
  }
  
  @Override public Point getToolTipLocation(MouseEvent e)
  {
    Point p = e.getPoint();
    p.y += 15;
    return p;
  }
  
  @Override public String getToolTipText(MouseEvent event)
  {
    return "Antani";
  }
}
