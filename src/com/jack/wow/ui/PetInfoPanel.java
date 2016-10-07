package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jack.wow.data.Pet;
import com.jack.wow.data.PetBreed;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetQuality;
import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.BreedComboBox;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.LevelSliderBar;
import com.jack.wow.ui.misc.QualityComboBox;

public class PetInfoPanel extends JPanel
{  
  private final CustomToolTip tip;
  private final LevelSliderBar level;
  private final QualityComboBox quality;
  private final BreedComboBox breed;
  
  private Pet pet;
  
  private final int TOP_HEIGHT = 40;
  
  PetInfoPanel(int width, int height)
  {
    this.setBackground(new Color(36,36,36));
    //this.setPreferredSize(new Dimension(width, height));
    
    tip = new CustomToolTip(this);
    tip.setSize(width, height - TOP_HEIGHT);
    tip.setMainIconSize(50);
    
    level = new LevelSliderBar();
    level.setOpaque(false);

    quality = new QualityComboBox(false);
    quality.setOpaque(false);
    
    breed = new BreedComboBox(false);
    breed.setOpaque(false);
    
    level.setCallback(i -> { if (pet != null) { pet.setLevel(i); tip.repaint(); } });
    quality.setCallback(q -> { if (pet != null) { pet.setQuality(q); tip.repaint(); } });
    breed.setCallback(q -> { if (pet != null) { pet.setBreed(q); tip.repaint(); } });


    setLayout(new BorderLayout());
    
    JPanel topPanel = new JPanel();
    topPanel.setBorder(null);
    topPanel.setOpaque(false);
    topPanel.setPreferredSize(new Dimension(width, TOP_HEIGHT));
    topPanel.add(quality);
    topPanel.add(breed);
    topPanel.add(level);
    
    add(topPanel, BorderLayout.SOUTH);
    add(tip, BorderLayout.CENTER);
    
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }
  
  public void update(PetSpec spec)
  {
    this.pet = new Pet(spec, breed.getSelectedValue(), quality.getSelectedValue(), level.getValue());
    
    tip.setPet(this.pet);
  }
}
