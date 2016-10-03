package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetSpec;

public class PetListPanel extends JPanel
{
  private Predicate<PetSpec> predicate = p -> true;
  private List<PetSpec> opets = new ArrayList<>();
  private final List<PetSpec> pets = new ArrayList<>();
  private final JTextField search = new JTextField();
  private final FamilyFilterButton[] familyFilters;
  
  private static class FamilyFilterButton extends JToggleButton
  {
    public final PetFamily family;
        
    FamilyFilterButton(PetFamily family, ImageIcon icon)
    {
      super(icon);
      this.family = family;
      setPreferredSize(new Dimension(24,24));
      setSelected(true);
    }
  }
  
  private class PetListModel extends AbstractTableModel
  {
    private final String[] columnNames = { "", "", "Name" };
    private final Class<?>[] columnClasses = { ImageIcon.class, ImageIcon.class, String.class };
    
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
        case 1: return pet.family.getTinyIcon();
        case 2: return pet.name;
        default: return null;
      }
    }
  }
  
  private class PetNameCellRenderer extends DefaultTableCellRenderer
  {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      JLabel l = (JLabel)c;
      l.setIcon(pets.get(row).family.getTinyIcon());
      
      return c;
    }
    
    /*@Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (getIcon() != null) {
            int textWidth = getFontMetrics(getFont()).stringWidth(getText());
            Insets insets = getInsets();
            int iconTextGap = width - textWidth - getIcon().getIconWidth() - insets.left - insets.right - 10;
            setIconTextGap(iconTextGap);
        } else {
            setIconTextGap(0);
        }
    }*/
  }
  
  private final JTable table;
  private final PetListModel model;
  
  public PetListPanel(int width, int height)
  {
    model = new PetListModel();
    table = new JTable(model);
    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(0), 20);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(1), 20);

    table.setRowHeight(20);
    table.getTableHeader().setFont(this.getFont().deriveFont(this.getFont().getSize()-2.0f));
    table.setAutoCreateRowSorter(true);
    
    JPanel familyFiltersPanel = new JPanel(new GridLayout(1, PetFamily.count()+1));
    ActionListener familyFilterListener = e -> populate(opets, predicate);
    
    familyFilters = Arrays.stream(PetFamily.values())
                          .map(f -> new FamilyFilterButton(f, f.getTinyIcon()))
                          .map(familyFiltersPanel::add)
                          .toArray(s -> new FamilyFilterButton[s]);
    
    JButton invertFamilyFilter = new JButton("~");
    invertFamilyFilter.setPreferredSize(new Dimension(24,24));
    invertFamilyFilter.setFont(this.getFont().deriveFont(this.getFont().getSize()-2.0f));
    invertFamilyFilter.addActionListener(e -> {
      Arrays.stream(familyFilters).forEach(b -> b.setSelected(!b.isSelected()));
      populate(opets, predicate);
    });
    
    familyFiltersPanel.add(invertFamilyFilter);
   
    
    Arrays.stream(familyFilters).forEach(f -> {
      f.addActionListener(familyFilterListener);
    });
    
    JScrollPane pane = new JScrollPane(table);
    
    pane.setPreferredSize(new Dimension(width, height));
    pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    add(search, BorderLayout.SOUTH);
    add(familyFiltersPanel, BorderLayout.NORTH);
  }
  
  public void populate(List<PetSpec> list, Predicate<PetSpec> filter)
  {
    predicate = filter;
    opets = list;

    filter = filter.and(p -> Arrays.stream(familyFilters).anyMatch(b -> b.isSelected() && b.family == p.family));
    
    pets.clear();
    list.stream().filter(filter).forEach(pets::add);
    model.fireTableDataChanged();
  }
}
