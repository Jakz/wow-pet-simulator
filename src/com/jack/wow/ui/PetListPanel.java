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
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.FilterableDataSource;

public class PetListPanel extends JPanel
{
  private final SearchTextField<PetSpec> search = new SearchTextField<>(30, f -> refresh());
  private FilterableDataSource<PetSpec> data = FilterableDataSource.empty();
  private final FamilyFilterPanel<PetSpec> familyFilter;
  
  private final JTable table;
  private final SimpleTableModel<PetSpec> model;
  
  public PetListPanel(int width, int height)
  {
    model = new SimpleTableModel<PetSpec>(() -> data,  
      new TableModelColumn<PetSpec>(Integer.class, "", p -> p.id),
      new TableModelColumn<PetSpec>(ImageIcon.class, "", p -> Icons.getIcon(p.icon, true)),
      new TableModelColumn<PetSpec>(ImageIcon.class, "", p -> p.family.getTinyIcon()),
      new TableModelColumn<PetSpec>(String.class, "Name", p -> p.name),
      new TableModelColumn<PetSpec>(Boolean.class, "Can battle", p -> p.canBattle),
      new TableModelColumn<PetSpec>(Boolean.class, "Tamable", p -> p.usable)
    );
    
    table = new JTable(model);
    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    UIUtils.resizeTableColumn(table.getColumnModel().getColumn(0), 50);
    UIUtils.resizeTableColumn(table.getColumnModel().getColumn(1), 20);
    UIUtils.resizeTableColumn(table.getColumnModel().getColumn(2), 20);
    UIUtils.resizeTableColumn(table.getColumnModel().getColumn(4), 80);
    UIUtils.resizeTableColumn(table.getColumnModel().getColumn(5), 80);

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
        UI.infoFrame.panel().update(data.get(r));
      }
      
    });
   
    
    JScrollPane pane = new JScrollPane(table);
    
    pane.setPreferredSize(new Dimension(width, height));
    pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    
    generateSearchPredicates();
    familyFilter = new FamilyFilterPanel<>();
    familyFilter.setOnFilterChanged(() -> refresh());

    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    add(search, BorderLayout.SOUTH);
    add(familyFilter, BorderLayout.NORTH);
  }
  
  private void generateSearchPredicates()
  {
    search.setDefaultRule(s -> a -> a.name.toLowerCase().contains(s.toLowerCase()));
    search.addRule("family", s -> a -> a.family.description.toLowerCase().contains(s.toLowerCase()));
  }
  
  public void setData(List<PetSpec> data)
  {
    this.data = FilterableDataSource.of(data);
  }
  
  public void refresh()
  {
    Predicate<PetSpec> filter = search.predicate();
    filter = filter.and(familyFilter.predicate());
    
    data.filter(filter);
    familyFilter.updateCounts(data.stream());
    
    model.fireTableDataChanged();
  }
}
