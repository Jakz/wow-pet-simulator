package com.jack.wow.ui.misc;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class WrapperFrame<T extends JPanel> extends JFrame
{
  private final T panel;
  
  WrapperFrame(T panel)
  {
    this.panel = panel;
  }
  
  public T panel() 
  {
    return panel;
  }
}
