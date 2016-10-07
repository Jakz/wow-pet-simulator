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

import com.jack.wow.data.PetQuality;

@SuppressWarnings("serial")
public class QualityComboBox extends JPanel
{
  private final JComboBox<PetQuality> combo;
  private Consumer<PetQuality> lambda = q -> {};
  
  private final ItemListener listener;
  
  public QualityComboBox(boolean includeAny)
  {
    combo = new JComboBox<>(PetQuality.values());
    combo.setRenderer(new RarityRenderer());
    
    listener = e -> {
      if (e.getStateChange() == ItemEvent.SELECTED)
        lambda.accept(combo.getItemAt(combo.getSelectedIndex()));
    };
    
    combo.addItemListener(listener);
    
    add(combo);
  }
  
  public QualityComboBox(int width, int height, boolean includeAny)
  {
    this(includeAny);
    combo.setPreferredSize(new Dimension(width, height));
  }
  
  public void setCallback(Consumer<PetQuality> lambda)
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
  
  public PetQuality getSelectedValue()
  {
    return combo.getItemAt(combo.getSelectedIndex());
  }
  
  private class RarityRenderer extends DefaultListCellRenderer
  {
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      PetQuality quality = (PetQuality)value;
      
      label.setText(quality.description);
      label.setIcon(quality.icon(12));
      
      return label;
    }
  }
}