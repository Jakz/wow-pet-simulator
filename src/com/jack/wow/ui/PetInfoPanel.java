package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.Icons;

public class PetInfoPanel extends JPanel
{
  JLabel petName = new JLabel();
  
  PetInfoPanel()
  {
    petName.setPreferredSize(new Dimension(300, 60));
    petName.setFont(petName.getFont().deriveFont(petName.getFont().getSize()+5.0f));
    
    setLayout(new BorderLayout());
    add(petName, BorderLayout.CENTER);
    
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }
  
  public void update(PetSpec pet)
  {
    petName.setText(pet.name);
    petName.setIcon(Icons.getIcon(pet.icon, false));
  }
}
