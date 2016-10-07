package com.jack.wow.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JToolTip;

import com.jack.wow.battle.BattlePet;
import com.jack.wow.data.Abilited;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetSpec;
import com.jack.wow.data.Qualitied;
import com.jack.wow.ui.misc.Gfx;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.RenderLabel;

class CustomToolTip extends JToolTip
{
  private final JPanel parent;

  PetAbility ability;
  Pet pet;
  PetSpec petSpec;
  BattlePet battlePet;
  
  private int WIDTH = 250;
  private int HEIGHT = 400;
  
  private final int ICON_BASE = 5;
  private int ICON_SIZE = 40;
  private final int ABILITY_SIZE = 40;
  
  private final String YELLOW = "#FFD200";
  
  private final RenderLabel renderLabel;
  
  CustomToolTip(JPanel parent)
  {
    this.parent = parent;
    this.setOpaque(false);
    
    renderLabel = new RenderLabel();
    renderLabel.setHTMLPreamble("<html><body style='width: "+WIDTH+"; padding: 5px; color: white; padding-top: 40px; font-weight: bold;'>");

    ability = null;
  }
  
  private int width() { return this.getSize().width; }
  
  Gfx g;
  
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
  
  public void drawIcon(String icon, Color color)
  {
    Image img = Icons.getIcon(icon, false).getImage();
    g.fillRect(0, 0, ICON_SIZE+2, ICON_SIZE+2, color);
    g.image(img, 1, 1, ICON_SIZE, ICON_SIZE);

    Color acolor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 180);
    g.rect(1, 1, ICON_SIZE-1, ICON_SIZE-1, acolor);

    acolor = new Color(acolor.getRed(), acolor.getGreen(), acolor.getBlue(), 128);
    g.rect(2, 2, ICON_SIZE-3, ICON_SIZE-3, acolor);
    
    g.fillOval(ICON_SIZE-15, ICON_SIZE-15, 20, 20, new Color(0,0,0,120));
  }
  
  public void drawTitle(String title)
  {
    g.setFont(g.font(4.0f, Font.BOLD));
    g.string(title, ICON_SIZE + 5, g.fontHeight(), Color.WHITE);
  }
  
  public void drawFamily(PetFamily family)
  {
    Image fimg = family.getTinyIcon().getImage();
    g.image(fimg, ICON_SIZE - 15 + 2, ICON_SIZE - 15 + 2);
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
    
    float base = ICON_SIZE + 10;
    
    for (int i = 0; i < 2; ++i)
    {
      for (int j = 0; j < 3; ++j)
      {
        PetAbility ability = pet.getAbility(j, i);
        
        if (ability != null)
        {
          float ix = i * (width() / 2);
          float iy = base + j*(ABILITY_SIZE+5);
          
          g.image(Icons.getImage(ability.icon), ix, iy , ABILITY_SIZE, ABILITY_SIZE);
          g.string(ability.name, ix + ABILITY_SIZE + 5, iy + ABILITY_SIZE/2 + g.fontHeight()/2, Color.WHITE);
          g.image(ability.family.getTinyIcon().getImage(), ix + ABILITY_SIZE + 5, iy);
        }
      }
    }
  }
  
  @Override public void paintComponent(Graphics gg)
  {
    Graphics2D g2d = (Graphics2D)gg;
    
    this.parent.repaint();

    g = new Gfx(g2d, true);
    g.setMargin(5);

    drawBackground();
    
    if (ability != null)
    {
      drawIcon(ability.icon, Color.DARK_GRAY);
      drawTitle(ability.name);
      drawFamily(ability.family);
      drawStrongAndWeak(ability.family.getStrongAttacking(), ability.family.getWeakAttacking());

      renderLabel.paint(gg);
    }
    else
    {
      Object common = null;
      
      if (pet != null)
        common = pet;
      else if (petSpec != null)
        common = petSpec;
      
      if (common != null)
      {
        PetSpec spec = petSpec != null ? petSpec : pet.spec();
        
        Color borderColor = Color.DARK_GRAY;
        if (common instanceof Qualitied)
          borderColor = ((Qualitied)common).quality().color;

        drawIcon(spec.icon, borderColor);

        drawTitle(spec.name);
        drawFamily(spec.family);
        drawStrongAndWeak(spec.family.getStrongDefending(), spec.family.getWeakDefending());
        
        drawAbilityGrid(spec);
      }
    }

  }
  
  public void setMainIconSize(int size)
  {
    this.ICON_SIZE = size;
  }
  
  public void setAbility(PetAbility ability)
  {
    this.ability = ability;
    this.pet = null;
    this.petSpec = null;
    
    renderLabel.clearText();
    renderLabel.appendLine("3 Round Cooldown");
    renderLabel.appendLine("<span style='color: #FFD200;'>100%</span> Hit Chance");
    renderLabel.appendLine("");
    renderLabel.appendLine("<span style='color: #FFD200;'>Deals 147 Mechanical damage and calls in a bombing run.");
    renderLabel.appendLine("");
    renderLabel.appendLine("After 3 rounds the bombs will arrive, dealing 239 Mechanical damage to the current enemy pet.</span>");
    this.setPreferredSize(renderLabel.finalizeText());
  }
  
  public void setPet(Pet pet)
  {
    this.ability = null;
    this.pet = pet;
    this.petSpec = null;
  }
    
  public void setPetSpec(PetSpec spec)
  {
    this.ability = null;
    this.pet = null;
    this.petSpec = spec;
  }
  
  public void setSize(int width, int height)
  {
    this.WIDTH = width;
    this.HEIGHT = height;
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
  }
}