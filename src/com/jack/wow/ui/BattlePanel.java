package com.jack.wow.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

import com.jack.wow.battle.Battle;
import com.jack.wow.battle.BattlePet;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.MouseRegionHoverListener;
import com.jack.wow.ui.misc.MyGfx;

public class BattlePanel extends JPanel
{
  private Battle battle;
  private final CustomToolTip toolTip;
  private MouseRegionHoverListener listener;
  private String toolTipEnabler = null;

  BattlePanel()
  {
    setPreferredSize(new Dimension(800, 800));
    ToolTipManager.sharedInstance().registerComponent(this);
    ToolTipManager.sharedInstance().setEnabled(true);
    ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
    ToolTipManager.sharedInstance().setInitialDelay(0);
    this.setFocusable(true);
    this.setOpaque(true);
    
    toolTip = new CustomToolTip(this);
    toolTip.setSize(400, 300);
    
    listener = new MouseRegionHoverListener(b -> toolTipEnabler = b ? "" : null);
    this.addMouseMotionListener(listener);
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
    
  Image image(String name) { return Icons.getIcon(name, false).getImage(); }
  Image image(String name, int w, int h) { return Icons.getIcon(name, false).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH); }
  
  @Override public void paintComponent(Graphics gg)
  {
    listener.clear();
    
    setGfx((Graphics2D)gg);
    
    MyGfx gfx = new MyGfx(gg, true);
    
    gfx.clear(36,36,36);
    gfx.setMargin(20);

    final int PET_ICON_SIZE = 64;
    final int PET_SPACING = 16;
    final int PET_TOTAL_SPACING = PET_ICON_SIZE + PET_SPACING;
    final int BY = 30;
    
    final int ABILITY_SIZE = 28;
    final int ABILITY_SPACING = ABILITY_SIZE + 1;
    
    if (battle != null)
    {
      for (int i = 0; i < 3; ++i)
      {
        BattlePet pet = battle.team(0).pet(i);
        
        int px = 0;
        int py = BY + i*PET_TOTAL_SPACING;
        
        gfx.drawIcon(pet.icon(), pet.pet().quality().color, PET_ICON_SIZE, px, py);
        gfx.drawFamily(pet.family(), px - 10 + PET_ICON_SIZE, py - 10 + PET_ICON_SIZE);
        listener.addZone(px, py, PET_ICON_SIZE, PET_ICON_SIZE, () -> toolTip.setPet(pet.pet()));

        
        for (int j = 0; j < 3; ++j)
          gfx.drawIcon(pet.ability(j).get().icon, null, ABILITY_SIZE, px + PET_ICON_SIZE + 5 + ABILITY_SPACING*j, py);
      } 
      
      for (int i = 0; i < 3; ++i)
      {
        BattlePet pet = battle.team(1).pet(i);
        
        int px = gfx.w() - gfx.margin()*2 - PET_ICON_SIZE;
        int py = BY + i*PET_TOTAL_SPACING;
        
        gfx.drawIcon(pet.icon(), pet.pet().quality().color, PET_ICON_SIZE, px, py);
        gfx.drawFamily(pet.family(), px - 10 + PET_ICON_SIZE, py - 10 + PET_ICON_SIZE);
        
        for (int j = 0; j < 3; ++j)
          gfx.drawIcon(pet.ability(j).get().icon, null, ABILITY_SIZE, px - 5 + ABILITY_SPACING*(j-3), py);
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
    return toolTip;
  }
  
  @Override public Point getToolTipLocation(MouseEvent e)
  {
    repaint();
    Point p = e.getPoint();
    
    if (p.x > getWidth()/2)
      p.x -= toolTip.getWidth();
    
    if (p.y > getHeight()/2)
      p.y -= toolTip.getHeight() + 15;
    else
      p.y += 15;
    
    return p;
  }
  
  @Override public String getToolTipText(MouseEvent event)
  {
    return toolTipEnabler;
  }
}
