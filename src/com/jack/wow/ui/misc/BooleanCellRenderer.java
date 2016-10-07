package com.jack.wow.ui.misc;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class BooleanCellRenderer extends DefaultTableCellRenderer
{
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    JLabel l = (JLabel)c;
    
    if ((boolean)value)
    {
      l.setForeground(new Color(0,180,0));
      l.setText("\u2713");
    }
    else
    {
       l.setForeground(new Color(180,0,0));
       l.setText("\u2717");
    }
    
    return c;
  }
}