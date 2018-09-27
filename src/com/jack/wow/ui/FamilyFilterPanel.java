package com.jack.wow.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jack.wow.data.PetFamily;
import com.jack.wow.data.interfaces.Familied;

public class FamilyFilterPanel<T extends Familied> extends JPanel
{
  private final FamilyFilterButton[] familyFilters;
  
  private Runnable onFilterChanged;

  public FamilyFilterPanel()
  {
    super(new GridLayout(1, PetFamily.count()+1));
    
    familyFilters = Arrays.stream(PetFamily.values())
        .map(f -> new FamilyFilterButton(f, f.getTinyIcon()))
        .map(this::add)
        .toArray(s -> new FamilyFilterButton[s]); 
    
    JButton invertFamilyFilter = new JButton("~");
    invertFamilyFilter.setPreferredSize(new Dimension(24,24));
    invertFamilyFilter.setFont(this.getFont().deriveFont(this.getFont().getSize()-2.0f));
    invertFamilyFilter.addActionListener(e -> {
      Arrays.stream(familyFilters).forEach(b -> b.setSelected(!b.isSelected()));
      onFilterChanged.run();
    });

    this.add(invertFamilyFilter);
  
    Arrays.stream(familyFilters).forEach(f -> {
      f.addActionListener(e -> onFilterChanged.run());
    });
  }
  
  public void setOnFilterChanged(Runnable onFilterChanged)
  {
    this.onFilterChanged = onFilterChanged;
  }
  
  public void updateCounts(Stream<T> data)
  {
    Map<PetFamily, Long> countByFamily = data.collect(Collectors.groupingBy(a -> a.family(), Collectors.counting()));       
    Arrays.stream(familyFilters).forEach(b -> b.setText(Long.toString(countByFamily.getOrDefault(b.family, 0L))));
  }
  
  public Predicate<T> predicate()
  {
    Predicate<T> filter = t -> true;
    filter = filter.and(p -> Arrays.stream(familyFilters).anyMatch(b -> b.isSelected() && b.family == p.family()));
    return filter;
  }
  
}
