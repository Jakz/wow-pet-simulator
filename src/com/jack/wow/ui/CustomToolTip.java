package com.jack.wow.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JToolTip;

import com.jack.wow.battle.BattlePet;
import com.jack.wow.data.Pet;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.Gfx;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.RenderLabel;

class CustomToolTip extends JToolTip
{
  private final BattlePanel battlePanel;

  PetAbility ability;
  Pet pet;
  PetSpec petSpec;
  BattlePet battlePet;
  
  private final int WIDTH = 250;
  
  private final int ICON_BASE = 5;
  private final int ICON_SIZE = 32;
  
  private final String YELLOW = "#FFD200";
  
  private final RenderLabel renderLabel;
  
  CustomToolTip(BattlePanel battlePanel)
  {
    this.battlePanel = battlePanel;
    this.setOpaque(false);
    
    renderLabel = new RenderLabel();
    renderLabel.setHTMLPreamble("<html><body style='width: "+WIDTH+"; padding: 5px; color: white; padding-top: 40px; font-weight: bold;'>");

    ability = null;
  }
  
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
  
  public void drawIcon(String icon)
  {
    Image img = Icons.getIcon(icon, false).getImage();
    g.fillRect(0, 0, ICON_SIZE+2, ICON_SIZE+2, 140, 140, 140);
    g.image(img, 1, 1, ICON_SIZE, ICON_SIZE);
  }
  
  public void drawTitle(String title)
  {
    g.setFont(g.font(4.0f, Font.BOLD));
    g.string(title, ICON_SIZE + 5, ICON_SIZE, Color.WHITE);
  }
  
  public void drawFamily(PetFamily family)
  {
    Image fimg = family.getTinyIcon().getImage();
    g.image(fimg, ICON_SIZE + 5, 0);
  }
  
  public void drawStrongAndWeak(PetFamily strong, PetFamily weak)
  {
    float base = g.w() - 90;
    g.image(strong.getTinyIcon().getImage(), base + 18, 0);
    g.image(Icons.Misc.STRONG.image(), base, -1, 20, 20);
    g.image(weak.getTinyIcon().getImage(), base + 58, 0);
    g.image(Icons.Misc.WEAK.image(), base + 40, -1, 20, 20);
  }
  
  @Override public void paintComponent(Graphics gg)
  {
    Graphics2D g2d = (Graphics2D)gg;
    
    this.battlePanel.repaint();

    g = new Gfx(g2d, true);
    g.setMargin(5);

    drawBackground();
    
    if (ability != null)
    {
      drawIcon(ability.icon);
      drawTitle(ability.name);
      drawFamily(ability.family);
      drawStrongAndWeak(ability.family.getStrongAttacking(), ability.family.getWeakAttacking());

      renderLabel.paint(gg);
    }
    else if (pet != null || petSpec != null)
    {
      PetSpec spec = petSpec != null ? petSpec : pet.spec();
      
      drawIcon(spec.icon);
      drawTitle(spec.name);
      drawFamily(spec.family);
      drawStrongAndWeak(spec.family.getStrongDefending(), spec.family.getWeakDefending());
    }
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
    this.setPreferredSize(new Dimension(WIDTH, 400));

  }
}