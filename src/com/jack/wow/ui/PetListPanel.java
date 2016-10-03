package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.jack.wow.data.PetSpec;

public class PetListPanel extends JPanel
{
  private final List<PetSpec> pets = new ArrayList<>();
  private final JTextField search = new JTextField();
  
  private class PetListModel extends AbstractTableModel
  {
    private final String[] columnNames = { "", "Name" };
    private final Class<?>[] columnClasses = { ImageIcon.class, String.class };
    
    @Override public int getColumnCount() { return columnClasses.length; }
    @Override public int getRowCount() { return pets.size(); }
    @Override public Class<?> getColumnClass(int c) { return columnClasses[c]; }
    @Override public String getColumnName(int c) { return columnNames[c]; }

    @Override public Object getValueAt(int r, int c)
    {
      PetSpec pet = pets.get(r);
      
      switch (c)
      {
        case 0: return Icons.getIcon(pet.icon, true);
        case 1: return pet.name;
        default: return null;
      }
    }
  }
  
  private final JTable table;
  private final PetListModel model;
  
  public PetListPanel(int width, int height)
  {
    model = new PetListModel();
    table = new JTable(model);
    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(0), 20);
    table.setRowHeight(20);
    table.getTableHeader().setFont(this.getFont().deriveFont(this.getFont().getSize()-2.0f));
    
    JScrollPane pane = new JScrollPane(table);
    
    pane.setPreferredSize(new Dimension(width, height));
    pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    add(search, BorderLayout.SOUTH);
  }
  
  public void populate(List<PetSpec> list, Predicate<PetSpec> filter)
  {
    pets.clear();
    list.stream().filter(filter).forEach(pets::add);
    model.fireTableDataChanged();
  }
}
