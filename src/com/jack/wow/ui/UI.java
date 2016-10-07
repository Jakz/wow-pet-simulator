package com.jack.wow.ui;

import com.jack.wow.ui.misc.UIUtils;
import com.jack.wow.ui.misc.WrapperFrame;

public class UI
{
  public static final WrapperFrame<PetInfoPanel> infoFrame;
  public static final WrapperFrame<PetListPanel> petListFrame;
  public static final WrapperFrame<BattlePanel> battleFrame;
  
  static
  {
    infoFrame = UIUtils.buildFrame(new PetInfoPanel(), "Pet Info");
    petListFrame = UIUtils.buildFrame(new PetListPanel(600, 800), "Pet List");
    battleFrame = UIUtils.buildFrame(new BattlePanel(), "Battle View");
  }
  
  public static void init()
  {
    petListFrame.setVisible(true);
    infoFrame.setVisible(true);
    //battleFrame.setVisible(true);
  }
}
