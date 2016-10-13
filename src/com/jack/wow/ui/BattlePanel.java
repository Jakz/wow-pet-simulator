package com.jack.wow.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

import com.jack.wow.battle.Battle;
import com.jack.wow.battle.BattlePet;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.MouseRegionHoverListener;
import com.jack.wow.ui.misc.MyGfx;

import com.pixbits.functional.TriFunction;

public class BattlePanel extends JPanel
{
  private Battle battle;
  private final CustomToolTip toolTip;
  private MouseRegionHoverListener listener;
  private String toolTipEnabler = null;
    
  BattlePanel()
  {
    setPreferredSize(new Dimension(800, 800));
    
    listener = new MouseRegionHoverListener(b -> toolTipEnabler = b ? "" : null);

    this.addMouseMotionListener(listener);
    
    ToolTipManager.sharedInstance().registerComponent(this);
    ToolTipManager.sharedInstance().setEnabled(true);
    ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
    ToolTipManager.sharedInstance().setInitialDelay(0);
    ToolTipManager.sharedInstance().setReshowDelay(0);
    this.setFocusable(true);
    this.setOpaque(true);
    
    toolTip = new CustomToolTip(this);
    toolTip.setSize(400, 300);
    

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
  
  @SuppressWarnings("unchecked")
  @Override public void paintComponent(Graphics gg)
  {
    listener.clear();
    
    setGfx((Graphics2D)gg);
    
    MyGfx gfx = new MyGfx(gg, true);
    
    final int PET_ICON_SIZE = 64;
    final int PET_SPACING = 16;
    final int PET_TOTAL_SPACING = PET_ICON_SIZE + PET_SPACING;
    final int BY = 30;
    final int MARGIN = 40;
    
    final int ABILITY_SIZE = 28;
    final int ABILITY_SPACING = ABILITY_SIZE + 3;
    final int ABILITY_SPACING_FROM_PET = 10;
    
    BiFunction<Integer, Integer, Integer> lambdaX = (team, index) -> team == 0 ? 0 : gfx.w() - gfx.margin()*2 - PET_ICON_SIZE;
    BiFunction<Integer, Integer, Integer> lambdaY = (team, index) -> BY + index*PET_TOTAL_SPACING;
    
    TriFunction<Integer, Integer, Integer, Integer> alambdaX = (team, petIndex, abilityIndex) -> {
      if (team == 0)
        return lambdaX.apply(team, petIndex) + (PET_ICON_SIZE + ABILITY_SPACING_FROM_PET + ABILITY_SPACING*abilityIndex);
      else
        return lambdaX.apply(team, petIndex) - ABILITY_SPACING_FROM_PET + ABILITY_SPACING*(abilityIndex-3);
    };

    TriFunction<Integer, Integer, Integer, Integer> alambdaY = (team, petIndex, abilityIndex) ->
      lambdaY.apply(team, petIndex)
    ;
      
    gfx.clear(36,36,36);
    gfx.setMargin(40);

    if (battle != null)
    {
      gfx.drawHealthBar(868, 1440, 200, 50, 100, 20);
      
      gfx.setAbsolute(true);
      gfx.fillRect(30, 30, PET_ICON_SIZE + ABILITY_SPACING*3 + ABILITY_SPACING_FROM_PET + 20, PET_TOTAL_SPACING*3 + 40, new Color(40,0,0));
      gfx.rect(30, 30, PET_ICON_SIZE + ABILITY_SPACING*3 + ABILITY_SPACING_FROM_PET + 20, PET_TOTAL_SPACING*3 + 40, new Color(180,0,0));

      gfx.setAbsolute(false);
      
      gfx.drawTitle("Team 1", 0, 10, 4.0f);
      gfx.drawTitle("Team 2", gfx.w() - gfx.margin()*2 - PET_ICON_SIZE, 10, 4.0f);
      
      for (int t = 0; t < 2; ++t)
      {
        for (int p = 0; p < 3; ++p)
        {
          BattlePet pet = battle.team(t).pet(p);
          int px = lambdaX.apply(t, p), py = lambdaY.apply(t, p);
          
          gfx.drawIcon(pet.icon(), pet.pet().quality().color, PET_ICON_SIZE, px, py);
          gfx.drawFamily(pet.family(), px - 10 + PET_ICON_SIZE, py - 10 + PET_ICON_SIZE);
          listener.addZone(gfx.x(px-1), gfx.y(py-1), PET_ICON_SIZE+2, PET_ICON_SIZE+2, () -> toolTip.setPet(pet.pet()));

          for (int a = 0; a < 3; ++a)
          {
            final int fj = a;
            int x =  alambdaX.apply(t, p, a), y = alambdaY.apply(t, p, a);
            gfx.drawIcon(pet.ability(a).get().icon, null, ABILITY_SIZE, x, y);
            listener.addZone(gfx.x(x), gfx.y(y), ABILITY_SIZE, ABILITY_SIZE, () -> toolTip.setAbility(pet.ability(fj).get()));
          }
        }
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
    if (toolTipEnabler == null)
      return null;
    
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
    repaint();

    return toolTipEnabler;
  }
}
