package com.jack.wow.ui.misc;

import java.util.function.Function;

public class TableModelColumn<T>
{
  public final Class<?> clazz;
  public final String name;
  public final Function<T, Object> lambda;
  
  public TableModelColumn(Class<?> clazz, String name, Function<T, Object> lambda)
  {
    this.clazz = clazz;
    this.name = name;
    this.lambda = lambda;
  }
  
}
