package com.jack.wow.ui.misc;

import java.awt.Dimension;

import javax.swing.JLabel;

public class RenderLabel extends JLabel
{
  private final StringBuilder buffer;
  private String htmlPreamble;
  
  public RenderLabel()
  {
    buffer = new StringBuilder();
  }
  
  public void setHTMLPreamble(String preamble)
  {
    this.htmlPreamble = preamble;
  }
  
  public void clearText()
  {
    buffer.delete(0, buffer.length());
  }
  
  public void appendLine(String string)
  {
    buffer.append(string).append("<br>");
  }
  
  public Dimension finalizeText()
  {
    buffer.insert(0, htmlPreamble);
    setText(buffer.toString());
    setSize(getPreferredSize());
    return getSize();
  }
}
