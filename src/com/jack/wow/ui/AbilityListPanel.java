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

import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.BooleanCellRenderer;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.SimpleTableModel;
import com.jack.wow.ui.misc.TableModelColumn;
import com.jack.wow.ui.misc.UIUtils;

public class AbilityListPanel extends JPanel
{
  private Predicate<PetAbility> predicate = p -> true;
  private List<PetAbility> oabilities = new ArrayList<>();
  private final List<PetAbility> abilities = new ArrayList<>();
  private final JTextField search = new JTextField();
  private final FamilyFilterButton[] familyFilters;
  
  private final JTable table;
  private final SimpleTableModel<PetAbility> model;
  
  public AbilityListPanel(int width, int height)
  {
    model = new SimpleTableModel<PetAbility>(abilities,  
      new TableModelColumn<PetAbility>(Integer.class, "", p -> p.id),
      new TableModelColumn<PetAbility>(ImageIcon.class, "", p -> Icons.getIcon(p.icon, true)),
      new TableModelColumn<PetAbility>(ImageIcon.class, "", p -> p.family.getTinyIcon()),
      new TableModelColumn<PetAbility>(String.class, "Name", p -> p.name),
      new TableModelColumn<PetAbility>(Integer.class, "Rounds", p -> p.rounds),
      new TableModelColumn<PetAbility>(Integer.class, "Cooldown", p -> p.cooldown),
      new TableModelColumn<PetAbility>(Boolean.class, "Passive", p -> p.isPassive),
      new TableModelColumn<PetAbility>(Boolean.class, "Hide Hints", p -> p.hideHints),
      new TableModelColumn<PetAbility>(Integer.class, "Used", p -> { return PetAbility.usage.containsKey(p) ? PetAbility.usage.get(p).size() : 0; })
    );
    
    table = new JTable(model);
    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(0), 50);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(1), 20);
    UIUtils.resizeColumn(table.getColumnModel().getColumn(2), 20);

    final Font smallerFont = this.getFont().deriveFont(this.getFont().getSize()-2.0f);
    
    table.setRowHeight(20);
    table.getTableHeader().setFont(smallerFont);
    //table.getColumnModel().getColumn(0).setHeaderRenderer(..);
    table.setAutoCreateRowSorter(true);
    
    table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
    
    table.getSelectionModel().addListSelectionListener(e -> {
      int r = table.getSelectedRow();
      
      if (r != -1)
      {
        r = table.convertRowIndexToModel(r);
      }
      
    });
    
    JPanel familyFiltersPanel = new JPanel(new GridLayout(1, PetFamily.count()+1));
    ActionListener familyFilterListener = e -> populate(oabilities, predicate);
    
    familyFilters = Arrays.stream(PetFamily.values())
                          .map(f -> new FamilyFilterButton(f, f.getTinyIcon()))
                          .map(familyFiltersPanel::add)
                          .toArray(s -> new FamilyFilterButton[s]);
    
    JButton invertFamilyFilter = new JButton("~");
    invertFamilyFilter.setPreferredSize(new Dimension(24,24));
    invertFamilyFilter.setFont(this.getFont().deriveFont(this.getFont().getSize()-2.0f));
    invertFamilyFilter.addActionListener(e -> {
      Arrays.stream(familyFilters).forEach(b -> b.setSelected(!b.isSelected()));
      populate(oabilities, predicate);
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
  
  public void populate(List<PetAbility> list, Predicate<PetAbility> filter)
  {
    predicate = filter;
    oabilities = list;

    filter = filter.and(p -> Arrays.stream(familyFilters).anyMatch(b -> b.isSelected() && b.family == p.family));
    
    abilities.clear();
    list.stream().filter(filter).forEach(abilities::add);
    model.fireTableDataChanged();
  }
}
