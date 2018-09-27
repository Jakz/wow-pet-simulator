package com.jack.wow.ui.misc;

import java.util.List;
import java.util.function.Supplier;

import javax.swing.table.AbstractTableModel;

import com.pixbits.lib.ui.table.DataSource;

@SuppressWarnings({"serial", "unchecked"})
public class SimpleTableModel<T> extends AbstractTableModel
{
  private final Supplier<DataSource<T>> data;
  private final TableModelColumn<?>[] columns;
  
  @SafeVarargs
  public SimpleTableModel(Supplier<DataSource<T>> data, TableModelColumn<T>... columns)
  {
    this.data = data;
    this.columns = columns;
  }
  
  public TableModelColumn<T> getColumn(int i) { return ((TableModelColumn<T>)columns[i]); }
  
  @Override public int getColumnCount() { return columns.length; }
  @Override public int getRowCount() { return data.get().size(); }
  @Override public Class<?> getColumnClass(int c) { return columns[c].clazz; }
  @Override public String getColumnName(int c) { return columns[c].name; }

  @Override public Object getValueAt(int r, int c)
  {
    T pet = data.get().get(r);
    return ((TableModelColumn<T>)columns[c]).lambda.apply(pet);
  }
}