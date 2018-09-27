package com.jack.wow.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
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
import javax.swing.SwingUtilities;

import com.jack.wow.battle.abilities.PassiveEffect;
import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetFamily;
import com.jack.wow.ui.misc.BooleanCellRenderer;
import com.jack.wow.ui.misc.Icons;
import com.jack.wow.ui.misc.SearchTextField;
import com.jack.wow.ui.misc.SimpleTableModel;
import com.jack.wow.ui.misc.TableModelColumn;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.ui.table.FilterableDataSource;

public class AbilityListPanel extends JPanel
{
  private Map<PetAbility, String> mechanics = new HashMap<>();
  private final SearchTextField<PetAbility> search = new SearchTextField<>(30, f -> refresh());
  
  FilterableDataSource<PetAbility> data = FilterableDataSource.empty();
  
  private final JButton openChangelogInWH = new JButton("Changelog");
  
  private final TooltipTable<PetAbility> table;
  private final FamilyFilterPanel<PetAbility> familyFilter;
  private final SimpleTableModel<PetAbility> model;
  
  
  public AbilityListPanel(int width, int height)
  {    
    model = new SimpleTableModel<PetAbility>(() -> data,  
      new TableModelColumn<PetAbility>(Integer.class, "", p -> p.id, 50),
      new TableModelColumn<PetAbility>(ImageIcon.class, "", p -> Icons.getIcon(p.icon, true), 20),
      new TableModelColumn<PetAbility>(ImageIcon.class, "", p -> p.family.getTinyIcon(), 20),
      new TableModelColumn<PetAbility>(String.class, "Name", p -> p.name, 200),
      //new TableModelColumn<PetAbility>(Integer.class, "Rounds", p -> p.rounds),
      new TableModelColumn<PetAbility>(Integer.class, "C", p -> p.cooldown, 30),
      new TableModelColumn<PetAbility>(Boolean.class, "P", p -> p.effectCount() > 0 && p.effects().allMatch(e -> e instanceof PassiveEffect), 20),
      new TableModelColumn<PetAbility>(Integer.class, "Used", p -> { return PetAbility.usage.containsKey(p) ? PetAbility.usage.get(p).size() : 0; }, 40),
      new TableModelColumn<PetAbility>(String.class, "Hit Chance", p -> { float hitChance = p.hitChance(); return hitChance == 0.0f ? "" : ""+(int)(hitChance*100)+"%"; }, 50),
      new TableModelColumn<PetAbility>(Boolean.class, "", p -> p.effectCount() > 0, 20 ),
      new TableModelColumn<PetAbility>(String.class, "Mechanics", p -> mechanics.get(p) )
    );
    
    table = new TooltipTable<>(() -> data, model);
    table.setLambda((tooltip, ability) -> tooltip.setAbility(ability));

    
    table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    for (int i = 0; i < model.getColumnCount(); ++i)
    {
      TableModelColumn<?> c = model.getColumn(i);
      if (c.hasWidthSpecified())
        UIUtils.resizeTableColumn(table.getColumnModel().getColumn(i), c.width);
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
      
      /*List<PetSpec> pets = PetAbility.usage.get(abilities.get(r));
      for (PetSpec pet : pets)
        System.out.println(pet.name);*/
    });
    
    generateSearchPredicates();

    JScrollPane pane = new JScrollPane(table);
    
    pane.setPreferredSize(new Dimension(width, height));
    pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    
    openChangelogInWH.addActionListener(e -> {
      int r = table.getSelectedRow();
      
      if (r > 0)
      {
        r = table.convertRowIndexToModel(r);
        PetAbility a = data.get(r);
        try
        {
          Desktop.getDesktop().browse(new URL("http://www.wowhead.com/pet-ability="+a.id+"#changelog").toURI());
        } catch (IOException | URISyntaxException e1)
        {
          e1.printStackTrace();
        }
      }
    });
    
    familyFilter = new FamilyFilterPanel<>();
    familyFilter.setOnFilterChanged(() -> refresh());
    
    JPanel lowerPanel = new JPanel();
    lowerPanel.add(search);
    //lowerPanel.add(openChangelogInWH);

    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    add(lowerPanel, BorderLayout.SOUTH);
    add(familyFilter, BorderLayout.NORTH);
  }
  
  private void generateSearchPredicates()
  {
    search.setDefaultRule(s -> a -> a.name.toLowerCase().contains(s.toLowerCase()));
    search.addRule("family", s -> a -> a.family.description.toLowerCase().contains(s.toLowerCase()));
    search.addStandaloneRule("hasMechanics", a -> a.effectCount() > 0);
  }

  public void setData(List<PetAbility> abilities)
  {
    data = FilterableDataSource.of(abilities);
    refresh();
  }

  public void refresh()
  {
    Predicate<PetAbility> filter = search.predicate();

    filter = filter.and(familyFilter.predicate()).and(p -> !p.isFiltered);
        
    data.filter(filter);
        
    familyFilter.updateCounts(data.stream());

    data.forEach(a -> {
      mechanics.put(a, a.effects().map(aa -> aa.toString()).collect(Collectors.joining(", ")));
    });
    
    SwingUtilities.invokeLater(() ->
      model.fireTableDataChanged()
    );
  }
}
