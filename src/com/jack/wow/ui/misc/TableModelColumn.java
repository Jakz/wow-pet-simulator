package com.jack.wow.ui.misc;

import java.util.function.Function;

public class TableModelColumn<T>
{
  public final Class<?> clazz;
  public final String name;
  public final Function<T, Object> lambda;
  public final int width;
  
  public TableModelColumn(Class<?> clazz, String name, Function<T, Object> lambda)
  {
    this(clazz, name, lambda, -1);
  }
  
  public TableModelColumn(Class<?> clazz, String name, Function<T, Object> lambda, int width)
  {
    this.clazz = clazz;
    this.name = name;
    this.lambda = lambda;
    this.width = width;
  }
  
  public boolean hasWidthSpecified() { return width != -1; }
  public int width() { return width; }
}
