package com.jack.wow.ui.misc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.jack.wow.data.PetBreed;
import com.jack.wow.data.PetQuality;

@SuppressWarnings("serial")
public class BreedComboBox extends JPanel
{
  private final JComboBox<PetBreed> combo;
  private Consumer<PetBreed> lambda = q -> {};
  
  private final ItemListener listener;
  
  public BreedComboBox(boolean includeAny)
  {
    combo = new JComboBox<>(PetBreed.values());
    combo.setRenderer(new RarityRenderer());
    
    listener = e -> {
      if (e.getStateChange() == ItemEvent.SELECTED)
        lambda.accept(combo.getItemAt(combo.getSelectedIndex()));
    };
    
    combo.addItemListener(listener);
    
    add(combo);
  }
  
  public BreedComboBox(int width, int height, boolean includeAny)
  {
    this(includeAny);
    combo.setPreferredSize(new Dimension(width, height));
  }
  
  public void setCallback(Consumer<PetBreed> lambda)
  {
    Objects.requireNonNull(lambda);
    this.lambda = lambda;
  }
  
  public void update(PetQuality quality)
  {
    combo.removeItemListener(listener);
    combo.setSelectedItem(quality);
    combo.addItemListener(listener);
  }
  
  public PetBreed getSelectedValue()
  {
    return combo.getItemAt(combo.getSelectedIndex());
  }
  
  private class RarityRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      PetBreed quality = (PetBreed)value;
      
      label.setText(quality.description);
      label.setIcon(null);
      
      return label;
    }
  }
}