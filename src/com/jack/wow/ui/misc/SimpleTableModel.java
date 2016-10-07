package com.jack.wow.ui.misc;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import com.jack.wow.data.PetSpec;

@SuppressWarnings("serial")
public class SimpleTableModel<T> extends AbstractTableModel
{
  private final List<T> data;
  private final TableModelColumn<?>[] columns;
  
  @SafeVarargs
  public SimpleTableModel(List<T> data, TableModelColumn<T>... columns)
  {
    this.data = data;
    this.columns = columns;
  }
  
  @Override public int getColumnCount() { return columns.length; }
  @Override public int getRowCount() { return data.size(); }
  @Override public Class<?> getColumnClass(int c) { return columns[c].clazz; }
  @Override public String getColumnName(int c) { return columns[c].name; }

  @SuppressWarnings("unchecked")
  @Override public Object getValueAt(int r, int c)
  {
    T pet = data.get(r);
    return ((TableModelColumn<T>)columns[c]).lambda.apply(pet);
  }
}