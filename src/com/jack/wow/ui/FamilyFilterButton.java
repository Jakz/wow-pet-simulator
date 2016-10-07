package com.jack.wow.ui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import com.jack.wow.data.PetFamily;

class FamilyFilterButton extends JToggleButton
{
  public final PetFamily family;
      
  FamilyFilterButton(PetFamily family, ImageIcon icon)
  {
    super(icon);
    this.family = family;
    setPreferredSize(new Dimension(24,24));
    setSelected(true);
  }
}