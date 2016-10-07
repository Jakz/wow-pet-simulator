package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.Icons;

public class PetInfoPanel extends JPanel
{
  JLabel petName = new JLabel();
  
  JLabel[] abilities = new JLabel[6];
  
  PetInfoPanel()
  {
    petName.setPreferredSize(new Dimension(300, 60));
    petName.setFont(petName.getFont().deriveFont(petName.getFont().getSize()+5.0f));
        
    JPanel abilitiesPanel = new JPanel(new GridLayout(2,3));
    for (int i = 0; i < abilities.length; ++i)
    {
      abilities[i] = new JLabel();
      abilities[i].setPreferredSize(new Dimension(60,60));
      abilitiesPanel.add(abilities[i]);
    }
    
    
    setLayout(new BorderLayout());
    add(petName, BorderLayout.CENTER);
    add(abilitiesPanel, BorderLayout.SOUTH);
    
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }
  
  public void update(PetSpec pet)
  {
    petName.setText(pet.name);
    petName.setIcon(Icons.getIcon(pet.icon, false));
    
    if (pet.canBattle)
    {
      for (int i = 0; i < abilities.length; ++i)
      {
        PetOwnedAbility ability = pet.abilities[i];
        abilities[i].setIcon(ability != null ? Icons.getIcon(ability.get().name, false) : null);
      }
    }
    else
      Arrays.stream(abilities).forEach(b -> b.setIcon(null));
  }
}
