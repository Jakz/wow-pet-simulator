package com.jack.wow.ui.misc;

import java.awt.Dimension;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LevelSliderBar extends JPanel
{
  private final static int MIN = 1;
  private final static int MAX = 25;
  
  private final JTextField field = new JTextField(3);
  private final JSlider bar = new JSlider(JSlider.HORIZONTAL, 1, 25, 25);
  private Consumer<Integer> lambda = i -> {};
  
  private final DocumentListener listener = new DocumentListener() {
    private void event(int v)
    {
      bar.setValue(v);
      lambda.accept(v);
    }
    
    private void revert()
    {
      field.setText(""+bar.getValue());
    }
    
    private void tryParse()
    {
      SwingUtilities.invokeLater(() -> {
        try
        {
          int n = Integer.valueOf(field.getText());
          event(n);
        }
        catch (NumberFormatException e) { revert(); }
      });
    }
    
    @Override public void insertUpdate(DocumentEvent e) { tryParse(); }
    @Override public void removeUpdate(DocumentEvent e) { tryParse(); }
    @Override public void changedUpdate(DocumentEvent e) { tryParse(); } 
  };
  
  public LevelSliderBar(int width, int height)
  {
    this();
    setPreferredSize(new Dimension(width, height));
  }

  public LevelSliderBar()
  {
    bar.setMajorTickSpacing(5);
    bar.setMinorTickSpacing(1);
    
    add(bar);
    add(field);
    
    bar.addChangeListener(e -> {
      SwingUtilities.invokeLater(() -> {
        int v = bar.getValue();
        field.setText(""+v);
        lambda.accept(v);
      });
    });
    
    field.getDocument().addDocumentListener(listener);
    
    update(25);
  }
  
  public int getValue() { return bar.getValue(); }
  
  public void update(int level)
  {
    SwingUtilities.invokeLater(() -> {
      field.setText(""+level);
      bar.setValue(level);
    });
  }
  
  public void setCallback(Consumer<Integer> lambda)
  { 
    Objects.requireNonNull(lambda);
    this.lambda = lambda;
  }
}