package com.jack.wow.ui;

import com.jack.wow.ui.misc.UIUtils;
import com.jack.wow.ui.misc.WrapperFrame;

public class UI
{
  public static final WrapperFrame<PetInfoPanel> infoFrame;
  public static final WrapperFrame<PetListPanel> petListFrame;
  
  static
  {
    infoFrame = UIUtils.buildFrame(new PetInfoPanel(), "Pet Info");
    petListFrame = UIUtils.buildFrame(new PetListPanel(300, 800), "Pet List");
  }
  
  public static void init()
  {
    petListFrame.setVisible(true);
    infoFrame.setVisible(true);
  }
}
