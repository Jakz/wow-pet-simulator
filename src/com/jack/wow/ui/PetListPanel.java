package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.BooleanCellRenderer;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.SearchTextField;
import com.jack.wow.ui.misc.SimpleTableModel;
import com.jack.wow.ui.misc.TableModelColumn;
import com.jack.wow.ui.misc.UIUtils;

public class PetListPanel extends JPanel
{
  private Predicate<PetSpec> predicate = p -> true;
  private List<PetSpec> opets = new ArrayList<>();
  private final List<PetSpec> pets = new ArrayList<>();
  private final JTextField search = new JTextField(30); //new SearchTextField<>(30, f -> searchUpdated(f));
  private final FamilyFilterButton[] familyFilters;
  
  private final JTable table;
  private final SimpleTableModel<PetSpec> model;
  
  public PetListPanel(int width, int height)
  {
    model = new SimpleTableModel<PetSpec>(pets,  
      new TableModelColumn<PetSpec>(Integer.class, "", p -> p.id),
      new TableModelColumn<PetSpec>(ImageIcon.class, "", p -> Icons.getIcon(p.icon, true)),
      new TableModelColumn<PetSpec>(ImageIcon.class, "", p -> p.family.getTinyIcon()),
      new TableModelColumn<PetSpec>(String.class, "Name", p -> p.name),
      new TableModelColumn<PetSpec>(Boolean.class, "Can battle", p -> p.canBattle),
      new TableModelColumn<PetSpec>(Boolean.class, "Tamable", p -> p.usable)
    );
    
    table = new JTable(model);
    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(0), 50);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(1), 20);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(2), 20);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(4), 80);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(5), 80);

    final Font smallerFont = this.getFont().deriveFont(this.getFont().getSize()-2.0f);
    
    table.setRowHeight(20);
    table.getTableHeader().setFont(smallerFont);
    //table.getColumnModel().getColumn(0).setHeaderRenderer(..);
    table.setAutoCreateRowSorter(true);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
    
    table.getSelectionModel().addListSelectionListener(e -> {
      int r = table.getSelectedRow();
      
      if (r != -1)
      {
        r = table.convertRowIndexToModel(r);
        UI.infoFrame.panel().update(pets.get(r));
      }
      
    });
    
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
  
  /*private void searchUpdated(Predicate<PetAbility> filter)
  {
    populate(oabilities, filter);
  }*/
  
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
