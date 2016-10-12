package com.jack.wow.ui.misc;

import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings({"serial", "unchecked"})
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
  
  public TableModelColumn<T> getColumn(int i) { return ((TableModelColumn<T>)columns[i]); }
  
  @Override public int getColumnCount() { return columns.length; }
  @Override public int getRowCount() { return data.size(); }
  @Override public Class<?> getColumnClass(int c) { return columns[c].clazz; }
  @Override public String getColumnName(int c) { return columns[c].name; }

  @Override public Object getValueAt(int r, int c)
  {
    T pet = data.get(r);
    return ((TableModelColumn<T>)columns[c]).lambda.apply(pet);
  }
}