package com.jack.wow.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.table.TableModel;

import com.pixbits.lib.ui.table.DataSource;

public class TooltipTable<T> extends JTable
{
  private final Supplier<DataSource<T>> data;
  private BiConsumer<CustomToolTip, T> lambda;
  private CustomToolTip tooltip;

  TooltipTable(Supplier<DataSource<T>> data, TableModel model)
  {
    super(model);
    this.data = data;
    ToolTipManager.sharedInstance().registerComponent(this);
    ToolTipManager.sharedInstance().setEnabled(true);
    ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
    ToolTipManager.sharedInstance().setInitialDelay(0);
  }
  
  public void setLambda(BiConsumer<CustomToolTip, T> lambda)
  {
    this.lambda = lambda;
  }
  
  @Override public Point getToolTipLocation(MouseEvent e)
  {
    Point p = e.getPoint();
    
    Dimension tsize = tooltip.getSize();
    
    JFrame root = (JFrame)SwingUtilities.getRoot(this);
    
    final double margin = 5;
    final double minX = margin;
    final double maxX = root.getSize().getWidth() - margin;
    final double maxY = root.getSize().getHeight() - margin;
    
    double left = p.getX();
    double right = left + tsize.getWidth();
    double bottom = p.getY() + 15 + tsize.getHeight();
    
    
    if (right > maxX)
      p.x -= (right - maxX);
    if (left < minX)
      p.x += (minX - left);
    
    if (bottom > maxY)
      p.y -= 15 + tsize.getHeight();
    else 
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
    
    if (r >= 0 && r < data.get().size())
    {
      r = convertRowIndexToModel(r);
      T element = data.get().get(r);
      lambda.accept(tooltip, element);
      return "";
    }
    
    return null;
  }
  
}