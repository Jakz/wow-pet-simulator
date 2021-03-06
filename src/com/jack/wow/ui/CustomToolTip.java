package com.jack.wow.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import com.jack.wow.battle.BattlePet;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetSpec;
import com.jack.wow.data.PetStats;
import com.jack.wow.data.interfaces.Abilited;
import com.jack.wow.data.interfaces.Qualitied;
import com.jack.wow.data.interfaces.Specced;
import com.jack.wow.data.interfaces.Statsed;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.MyGfx;
import com.jack.wow.ui.misc.RenderLabel;

class CustomToolTip extends JToolTip
{
  private final JComponent parent;

  PetAbility ability;
  Pet pet;
  PetSpec petSpec;
  BattlePet battlePet;
  Object common;
  
  private int WIDTH = 250;
  private int HEIGHT = 400;
  
  private int ICON_SIZE = 36;
  private final int BIG_ICON_SIZE = 50;
  private final int ABILITY_SIZE = 40;
  private final int ABILITY_GRID_SPACING = 5;
  private final int ABILITY_GRID_BASE = BIG_ICON_SIZE + 10 + 4;
  private final int ABILITY_ROWS = 3;
  private final int INLINE_TOOLTIP_BASE = ABILITY_GRID_BASE + (ABILITY_ROWS-1)*(ABILITY_SIZE+ABILITY_GRID_SPACING) + 5;
  
  private final String htmlPreamble = "<html><body style='width: %d; padding: 5px; color: white; padding-top: 40px; font-weight: bold;'>";
  private final String htmlPreambleInline = "<html><body style='width: %d; padding: 5px; color: white; padding-top: %dpx; font-weight: bold;'>";
  
  private final String YELLOW = "#FFD200";
  
  private final RenderLabel renderLabel;
  
  CustomToolTip(JComponent parent)
  {
    this.parent = parent;
    this.setOpaque(false);
    
    renderLabel = new RenderLabel();

    ability = null;
  }
  
  private int width() { return this.getSize().width; }
  
  MyGfx g;
  
  public void drawBackground()
  {
    g.clear(new Color(8, 13, 33, 220));
    
    g.setAbsolute(true);
    g.line(0, 0, g.w()-1, 0, 127, 127, 127);
    g.line(0, g.h()-1, g.w()-1, g.h()-1, 127, 127, 127);
    g.line(0, 0, 0, g.h()-1, 119, 119, 119);
    g.line(g.w()-1, 0, g.w()-1, g.h()-1, 119, 119, 119);
    g.setAbsolute(false);
  }
    
  public void drawStrongAndWeak(PetFamily strong, PetFamily weak)
  {
    float base = g.w() - 90;
    g.image(strong.getTinyIcon().getImage(), base + 18, 0);
    g.image(Icons.Misc.STRONG.image(), base, -1, 20, 20);
    g.image(weak.getTinyIcon().getImage(), base + 58, 0);
    g.image(Icons.Misc.WEAK.image(), base + 40, -1, 20, 20);
  }
  
  public void drawAbilityGrid(Abilited pet)
  {
    g.restoreFont();
    
    BattlePet bpet = pet instanceof BattlePet ? ((BattlePet)pet) : null;
    
    for (int i = 0; i < 2; ++i)
    {
      for (int j = 0; j < ABILITY_ROWS; ++j)
      {
        PetAbility ability = pet.getAbility(j, i);
        
        if (ability != null)
        {
          float ix = i * (width() / 2);
          float iy = ABILITY_GRID_BASE + j*(ABILITY_SIZE + ABILITY_GRID_SPACING);
          
          g.image(Icons.getImage(ability.icon), ix, iy , ABILITY_SIZE, ABILITY_SIZE);
          g.string(ability.name, ix + ABILITY_SIZE + ABILITY_GRID_SPACING, iy + ABILITY_SIZE/2 + g.fontHeight()/2, Color.WHITE);
          g.image(ability.family.getTinyIcon().getImage(), ix + ABILITY_SIZE + ABILITY_GRID_SPACING, iy);
          
          if (bpet != null)
          {
            if (bpet.isAbilitySelected(ability))
              g.rect(ix, iy, ABILITY_SIZE, ABILITY_SIZE, 255, 255, 255);
            else
              g.fillRect(ix, iy, ABILITY_SIZE, ABILITY_SIZE, MyGfx.GRAYED_OUT_COLOR);  
          }
        }
      }
    }
  }
  
  @Override public void paintComponent(Graphics gg)
  {
    Graphics2D g2d = (Graphics2D)gg;
    
    /*if (!parent.isAncestorOf(this))  
      this.parent.repaint();*/

    g = new MyGfx(g2d, true);
    g.setMargin(5);

    drawBackground();
    
    if (ability != null && common == null)
    {
      g.drawIcon(ability.icon, Color.DARK_GRAY, ICON_SIZE, 0, 0);
      g.drawTitle(ability.name, ICON_SIZE + 5, 2, 4.0f);
      g.drawFamily(ability.family, ICON_SIZE - 8, ICON_SIZE - 8);
      drawStrongAndWeak(ability.family.getStrongAttacking(), ability.family.getWeakAttacking());

      renderLabel.paint(gg);
    }
    else
    {      
      if (common != null && common instanceof Specced)
      {
        PetSpec spec = ((Specced)common).spec();
        
        if (common instanceof Statsed)
        {
          PetStats stats = ((Statsed)common).stats();
          g.drawStats(BIG_ICON_SIZE + 10, BIG_ICON_SIZE - 20, (int)stats.health(), (int)stats.power(), (int)stats.speed());
        }

        Color borderColor = Color.DARK_GRAY;
        if (common instanceof Qualitied)
          borderColor = ((Qualitied)common).quality().color;

        g.drawIcon(spec.icon, borderColor, BIG_ICON_SIZE, 0, 0);

        g.drawTitle(spec.name, BIG_ICON_SIZE + 5, 2, 4.0f);
        g.drawFamily(spec.family, BIG_ICON_SIZE - 8, BIG_ICON_SIZE - 8);
        drawStrongAndWeak(spec.family.getStrongDefending(), spec.family.getWeakDefending());
        
        if (common instanceof Abilited)
          drawAbilityGrid((Abilited)common);
        
        if (ability != null)
          renderLabel.paint(gg);
      }
    }

  }
  
  public void setMainIconSize(int size)
  {
    this.ICON_SIZE = size;
  }
  
  public void setInlineAbility(PetAbility ability)
  {
    PetAbility oldAbility = this.ability;
    this.ability = ability;
 
    renderLabel.clearText();
    if (ability != null && ability != oldAbility)
    {
      renderLabel.setHTMLPreamble(String.format(htmlPreambleInline, WIDTH, INLINE_TOOLTIP_BASE));
      renderLabel.appendLine("3 Round Cooldown");
      renderLabel.appendLine("<span style='color: #FFD200;'>100%</span> Hit Chance");
      renderLabel.appendLine("");
      renderLabel.appendLine("<span style='color: #FFD200;'>Deals 147 Mechanical damage and calls in a bombing run.");
      renderLabel.appendLine("");
      renderLabel.appendLine("After 3 rounds the bombs will arrive, dealing 239 Mechanical damage to the current enemy pet.</span>");
      this.setPreferredSize(renderLabel.finalizeText());
    }
    
    if (parent.isAncestorOf(this) && oldAbility != ability)
      repaint();
  }
  
  public void setAbility(PetAbility ability)
  {
    this.ability = ability;
    this.pet = null;
    this.petSpec = null;
    this.common = null;
    
    renderLabel.clearText();
    renderLabel.setHTMLPreamble(String.format(htmlPreamble, WIDTH));
    
    if (ability.cooldown > 0)
      renderLabel.appendLine(ability.cooldown+" Round Cooldown");
    if (ability.hitChance() != 0.0f)
      renderLabel.appendLine("<span style='color: #FFD200;'>"+(int)(float)ability.hitChance()*100+"%</span> Hit Chance");
    renderLabel.appendLine("");
    renderLabel.appendLine("<span style='color: #FFD200;'>"+ability.tooltip+"</span>");
    
    this.setPreferredSize(renderLabel.finalizeText());
  }
  
  public void setPet(Pet pet)
  {
    this.ability = null;
    this.pet = pet;
    this.petSpec = null;
    this.common = pet;

    this.setSize(WIDTH, ABILITY_GRID_BASE + 3*(ABILITY_SIZE + ABILITY_GRID_SPACING) + 5*2);
    
    if (parent.isAncestorOf(this))
      repaint();
  }
  
  public void setBattlePet(BattlePet pet)
  {
    this.ability = null;
    this.battlePet = pet;
    this.pet = null;
    this.petSpec = null;
    this.common = battlePet;
    
    this.setSize(WIDTH, ABILITY_GRID_BASE + 3*(ABILITY_SIZE + ABILITY_GRID_SPACING) + 5*2);

    if (parent.isAncestorOf(this))
      repaint();
  }
    
  public void setPetSpec(PetSpec spec)
  {
    this.ability = null;
    this.battlePet = null;
    this.pet = null;
    this.petSpec = spec;
    this.common = spec;
    
    this.setSize(400, ABILITY_GRID_BASE + 3*(ABILITY_SIZE + ABILITY_GRID_SPACING) + 5*2);
    
    if (parent.isAncestorOf(this))
      repaint();
  }
  
  public void setSize(int width, int height)
  {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    
    if (parent.isAncestorOf(this))
    {
      revalidate();
    }
  }
  
  public void setupListener(Consumer<PetAbility> lambda)
  {
    mouseListener = new MouseAdapter() { 
      @Override public void mouseMoved(MouseEvent e)
      {
        if (common instanceof Abilited)
        {
          int mx = e.getX(), my = e.getY();
          
          for (int i = 0; i < 2; ++i)
          {
            float ix = i * (width() / 2);
            if (mx >= ix && mx <= ix + ABILITY_SIZE && my >= ABILITY_GRID_BASE && my < ABILITY_GRID_BASE + (ABILITY_SIZE+ABILITY_GRID_SPACING)*3)
            {
              int slot = (int)((my - ABILITY_GRID_BASE)/(ABILITY_SIZE+ABILITY_GRID_SPACING));
              int index = i; 
              
              lambda.accept(((Abilited)common).getAbility(slot, index));
              return;
            }
          }

          lambda.accept(null);
        }
      }
    };
    
    this.addMouseMotionListener(mouseListener);
  }
  
  private MouseMotionListener mouseListener;
}