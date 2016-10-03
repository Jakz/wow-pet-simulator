package com.jack.wow.ui;

import javax.swing.table.TableColumn;

public class UIUtils
{
  public static void resizeColumn(TableColumn column, int width)
  {
    column.setMinWidth(width);
    column.setMaxWidth(width);
    column.setPreferredWidth(width);
  }
}
