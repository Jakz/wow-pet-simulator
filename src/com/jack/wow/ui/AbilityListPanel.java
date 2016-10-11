package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.table.TableModel;

import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.data.PetSpec;
import com.jack.wow.ui.misc.BooleanCellRenderer;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.SearchTextField;
import com.jack.wow.ui.misc.SimpleTableModel;
import com.jack.wow.ui.misc.TableModelColumn;
import com.jack.wow.ui.misc.UIUtils;

public class AbilityListPanel extends JPanel
{
  private Predicate<PetAbility> predicate = p -> true;
  private List<PetAbility> oabilities = new ArrayList<>();
  private Map<PetAbility, String> mechanics = new HashMap<>();
  private final List<PetAbility> abilities = new ArrayList<>();
  private final SearchTextField<PetAbility> search = new SearchTextField<>(30, f -> searchUpdated(f));
  private final FamilyFilterButton[] familyFilters;
  
  private final JButton openChangelogInWH = new JButton("Changelog");
  
  private final TooltipTable table;
  private final SimpleTableModel<PetAbility> model;
  
  
  public class TooltipTable extends JTable
  {
    private CustomToolTip tooltip;
 
    TooltipTable(TableModel model)
    {
      super(model);
      ToolTipManager.sharedInstance().registerComponent(this);
      ToolTipManager.sharedInstance().setEnabled(true);
      ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
      ToolTipManager.sharedInstance().setInitialDelay(0);
    }
    
    @Override public Point getToolTipLocation(MouseEvent e)
    {
      Point p = e.getPoint();
      p.y += 15;
      return p;
    }
    
    @Override public JToolTip createToolTip()
    {
      if (tooltip == null)
        tooltip = new CustomToolTip(this);
      //toolTip.setAbility(PetAbility.forName("bombing run"));

      return tooltip;
    }
    
    @Override public String getToolTipText(MouseEvent e)
    {
      java.awt.Point p = e.getPoint();
      int r = rowAtPoint(p);
      
      if (tooltip == null)
        createToolTip();
      
      if (r >= 0 && r < abilities.size())
      {
        r = convertRowIndexToModel(r);
        tooltip.setAbility(abilities.get(r));
        return "";
      }
      
      return null;
    }
    
  }
  
  public AbilityListPanel(int width, int height)
  {    
    model = new SimpleTableModel<PetAbility>(abilities,  
      new TableModelColumn<PetAbility>(Integer.class, "", p -> p.id, 50),
      new TableModelColumn<PetAbility>(ImageIcon.class, "", p -> Icons.getIcon(p.icon, true), 20),
      new TableModelColumn<PetAbility>(ImageIcon.class, "", p -> p.family.getTinyIcon(), 20),
      new TableModelColumn<PetAbility>(String.class, "Name", p -> p.name, 200),
      //new TableModelColumn<PetAbility>(Integer.class, "Rounds", p -> p.rounds),
      new TableModelColumn<PetAbility>(Integer.class, "C", p -> p.cooldown, 20),
      new TableModelColumn<PetAbility>(Boolean.class, "P", p -> p.effectCount() > 0 && p.stream().allMatch(e -> e instanceof PassiveEffect), 20),
      new TableModelColumn<PetAbility>(Integer.class, "Used", p -> { return PetAbility.usage.containsKey(p) ? PetAbility.usage.get(p).size() : 0; }, 40),
      new TableModelColumn<PetAbility>(String.class, "Hit Chance", p -> { return p.hitChance.isPresent() ? Integer.valueOf((int)(float)p.hitChance.get())+"%" : ""; }, 50),
      new TableModelColumn<PetAbility>(Boolean.class, "", p -> p.effectCount() > 0, 20 ),
      new TableModelColumn<PetAbility>(String.class, "Mechanics", p -> mechanics.get(p) )
    );
    
    table = new TooltipTable(model);
    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    for (int i = 0; i < model.getColumnCount(); ++i)
    {
      TableModelColumn<?> c = model.getColumn(i);
      if (c.hasWidthSpecified())
        UIUtils.resizeColumn(table.getColumnModel().getColumn(i), c.width);
    }
  
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
    
    generateSearchPredicates();
    
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
    
    openChangelogInWH.addActionListener(e -> {
      int r = table.getSelectedRow();
      
      if (r > 0)
      {
        r = table.convertRowIndexToModel(r);
        PetAbility a = abilities.get(r);
        try
        {
          Desktop.getDesktop().browse(new URL("http://www.wowhead.com/pet-ability="+a.id+"#changelog").toURI());
        } catch (IOException | URISyntaxException e1)
        {
          e1.printStackTrace();
        }
      }
    });
    
    JPanel lowerPanel = new JPanel();
    lowerPanel.add(search);
    //lowerPanel.add(openChangelogInWH);

    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    add(lowerPanel, BorderLayout.SOUTH);
    add(familyFiltersPanel, BorderLayout.NORTH);
  }
  
  private void generateSearchPredicates()
  {
    search.setDefaultRule(s -> a -> a.name.toLowerCase().contains(s.toLowerCase()));
    search.addRule("family", s -> a -> a.family.description.toLowerCase().contains(s.toLowerCase()));
    search.addStandaloneRule("hasMechanics", a -> a.effectCount() > 0);
  }
  
  private void searchUpdated(Predicate<PetAbility> filter)
  {
    populate(oabilities, filter);
  }

  public void populate(List<PetAbility> list, Predicate<PetAbility> filter)
  {
    predicate = filter;
    oabilities = list;

    filter = filter.and(p -> Arrays.stream(familyFilters).anyMatch(b -> b.isSelected() && b.family == p.family));
        
    abilities.clear();
    list.stream().filter(filter).forEach(abilities::add);
    
    Map<PetFamily, Long> countByFamily = abilities.stream().collect(Collectors.groupingBy(a -> a.family, Collectors.counting()));
    Arrays.stream(familyFilters).forEach(b -> b.setText(Long.toString(countByFamily.getOrDefault(b.family, 0L))));
    
    abilities.forEach(a -> {
      mechanics.put(a, a.stream().map(aa -> aa.toString()).collect(Collectors.joining(", ")));
    });
    
    SwingUtilities.invokeLater(() ->
      model.fireTableDataChanged()
    );
  }
}
