package com.jack.wow.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

import com.jack.wow.battle.Battle;
import com.jack.wow.battle.BattleAbilityStatus;
import com.jack.wow.battle.BattlePet;
import com.jack.wow.battle.BattleTeam;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.MouseRegionHoverListener;
import com.jack.wow.ui.misc.MyGfx;

import com.pixbits.lib.functional.TriFunction;

public class BattlePanel extends JPanel
{
  private Battle battle;
  private final CustomToolTip toolTip;
  private MouseRegionHoverListener listener;
  private String toolTipEnabler = null;
  
  private int MINIMUM_WIDTH = 800, MINIMUM_HEIGHT = 800;
    
  BattlePanel()
  {
    setPreferredSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
    
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
    final int PET_SPACING = 70;
    final int PET_TOTAL_SPACING = PET_ICON_SIZE + PET_SPACING;
    final int BY = 80;
    final int MARGIN = 40;
    
    final int EFFECT_ICON_SIZE = 24;
    final int EFFECT_SPACING = 8;
    final int EFFECT_SPACING_FROM_PET = 5;
    
    final int ABILITY_SIZE = 28;
    final int ABILITY_PAD = 5;
    final int ABILITY_SPACING = ABILITY_SIZE + ABILITY_PAD;
    final int ABILITY_SPACING_FROM_PET = 10;
    
    final int HEALTH_BAR_WIDTH = ABILITY_SIZE*3 + ABILITY_PAD*3;
    final int HEALTH_BAR_HEIGHT = 20;
    
    BiFunction<Integer, Integer, Integer> petPositionX = (team, index) -> team == 0 ? 0 : gfx.w() - gfx.margin()*2 - PET_ICON_SIZE;
    BiFunction<Integer, Integer, Integer> petPositionY = (team, index) -> BY + index*PET_TOTAL_SPACING;
 
    TriFunction<Integer, Integer, Integer, Integer> abilityX = (team, petIndex, abilityIndex) -> {
      if (team == 0)
        return petPositionX.apply(team, petIndex) + (PET_ICON_SIZE + ABILITY_SPACING_FROM_PET + ABILITY_SPACING*abilityIndex);
      else
        return petPositionX.apply(team, petIndex) - ABILITY_SPACING_FROM_PET + ABILITY_SPACING*(abilityIndex-3);
    };

    TriFunction<Integer, Integer, Integer, Integer> abilityY = (team, petIndex, abilityIndex) ->
      petPositionY.apply(team, petIndex) + PET_ICON_SIZE - ABILITY_SIZE;
    ;
    
    BiFunction<Integer, Integer, Integer> hlambday = (team, petIndex) -> petPositionY.apply(team, petIndex);
    
    TriFunction<Integer, Integer, Integer, Integer> elambdax = (team, pet, effect) -> {
      if (team == 0)
        return petPositionX.apply(team, pet) + (EFFECT_ICON_SIZE+EFFECT_SPACING)*effect;
      else
        return petPositionX.apply(team, pet) + PET_ICON_SIZE - EFFECT_ICON_SIZE - (EFFECT_ICON_SIZE+EFFECT_SPACING)*effect; 
    };
    
    TriFunction<Integer, Integer, Integer, Integer> elambday = (team, pet, effect) -> petPositionY.apply(team, pet) + PET_ICON_SIZE + EFFECT_SPACING_FROM_PET;

      
    gfx.clear(36,36,36);
    gfx.setMargin(40);

    if (battle != null)
    {      
      gfx.setAbsolute(true);
      final int BG_WIDTH = PET_ICON_SIZE + ABILITY_SPACING*3 + ABILITY_SPACING_FROM_PET + 20;
      final int BG_HEIGHT = PET_TOTAL_SPACING*3 + 80;
      
      gfx.fillRect(30, 30, BG_WIDTH, BG_HEIGHT, new Color(40,0,0));
      gfx.rect(30, 30, BG_WIDTH, BG_HEIGHT, new Color(180,0,0));
      
      gfx.fillRect(gfx.w() - 30 - BG_WIDTH , 30, BG_WIDTH, BG_HEIGHT, new Color(0,0,40));
      gfx.rect(gfx.w() - 30 - BG_WIDTH, 30, BG_WIDTH, BG_HEIGHT, new Color(0,0,180));

      gfx.setAbsolute(false);
      
      gfx.drawTitle("Team 1", 0, 10, 4.0f);
      gfx.drawTitle("Team 2", gfx.w() - gfx.margin()*2 - PET_ICON_SIZE, 10, 4.0f);
      
      for (int t = 0; t < 2; ++t)
      {
        BattleTeam team = battle.team(t);
        
        AtomicInteger te = new AtomicInteger(0);
        final int tt = t;
        team.effects().forEach(effect -> {
          final int ppy = 30;
          final int ppx = tt == 0 ? (EFFECT_ICON_SIZE+EFFECT_SPACING)*te.get() : (petPositionX.apply(1,0) + PET_ICON_SIZE - EFFECT_ICON_SIZE - (EFFECT_ICON_SIZE+EFFECT_SPACING)*te.get());
          
          gfx.drawEffect(effect, ppx, ppy, EFFECT_ICON_SIZE);       
          listener.addZone(gfx.x(ppx), gfx.y(ppy), EFFECT_ICON_SIZE, EFFECT_ICON_SIZE, () -> toolTip.setAbility(effect.ability()));
          te.getAndIncrement();
        });
        
        for (int p = 0; p < 3; ++p)
        {
          BattlePet pet = team.pet(p);
          int px = petPositionX.apply(t, p), py = petPositionY.apply(t, p);
          
          /* draw pet */
          gfx.drawIcon(pet.icon(), pet.pet().quality().color, PET_ICON_SIZE, px, py); 
          gfx.drawFamily(pet.family(), px - 10 + PET_ICON_SIZE, py - 10 + PET_ICON_SIZE);
          listener.addZone(gfx.x(px-1), gfx.y(py-1), PET_ICON_SIZE+2, PET_ICON_SIZE+2, () -> toolTip.setBattlePet(pet));
          
          if (pet.isDead())
            gfx.fillRect(px, py, PET_ICON_SIZE, PET_ICON_SIZE, MyGfx.DEAD_COLOR);
          else if (pet != battle.activePet(t))
            gfx.fillRect(px, py, PET_ICON_SIZE, PET_ICON_SIZE, MyGfx.GRAYED_OUT_COLOR);

          gfx.drawHealthBar(pet.hitPoints(), pet.maxHitPoints(), abilityX.apply(t, p, 0), hlambday.apply(t, p), HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

          /* draw effects */
          AtomicInteger e = new AtomicInteger(0);
          final int pp = p;
          pet.effects().forEach(effect -> {
            int ppx = elambdax.apply(tt, pp, e.get()), ppy = elambday.apply(tt, pp, e.get());
            gfx.drawEffect(effect, ppx, ppy, EFFECT_ICON_SIZE);       
            e.incrementAndGet();
            listener.addZone(gfx.x(ppx), gfx.y(ppy), EFFECT_ICON_SIZE, EFFECT_ICON_SIZE, () -> toolTip.setAbility(effect.ability()));
          });
          
          /* draw abilities */
          for (int a = 0; a < 3; ++a)
          {
            BattleAbilityStatus status = pet.abilityStatus(a);

            final int fj = a;
            int x =  abilityX.apply(t, p, a), y = abilityY.apply(t, p, a);
            gfx.drawIcon(status.ability().get().icon, null, ABILITY_SIZE, x, y);
            
            if (!status.isReady())
            {
              gfx.fillRect(x, y, ABILITY_SIZE, ABILITY_SIZE, MyGfx.GRAYED_OUT_COLOR);
              gfx.stringCentered(String.valueOf(status.cooldown()), x + ABILITY_SIZE/2, y + ABILITY_SIZE/2, 255, 255, 255);
            }
            
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
