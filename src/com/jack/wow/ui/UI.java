package com.jack.wow.ui;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.UIUtils;
import com.jack.wow.ui.misc.WrapperFrame;

public class UI
{
  public static WrapperFrame<PetInfoPanel> infoFrame;
  public static WrapperFrame<PetListPanel> petListFrame;
  public static WrapperFrame<AbilityListPanel> abilityListFrame;
  public static WrapperFrame<BattlePanel> battleFrame;
  
  static
  {
    SwingUtilities.invokeLater(() -> {    
      infoFrame = UIUtils.buildFrame(new PetInfoPanel(600, 400), "Pet Info");
      petListFrame = UIUtils.buildFrame(new PetListPanel(600, 800), "Pet List");
      battleFrame = UIUtils.buildFrame(new BattlePanel(), "Battle View");
      abilityListFrame = UIUtils.buildFrame(new AbilityListPanel(400, 800), "Ability List");
    });

  }
  
  public static void init()
  {
    SwingUtilities.invokeLater(() -> {    
      petListFrame.setVisible(true);
      abilityListFrame.setVisible(true);
      
      
      
      infoFrame.setVisible(true);
      //battleFrame.setVisible(true);
      
      petListFrame.setLocationRelativeTo(null);
      abilityListFrame.setLocation(petListFrame.getLocationOnScreen().x + petListFrame.getSize().width, petListFrame.getLocationOnScreen().y);
      
      petListFrame.panel().populate(Arrays.asList(PetSpec.data), p -> true);
      abilityListFrame.panel().populate(new ArrayList<>(PetAbility.data.values()), p -> true);
    
      petListFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    });
  }
}
