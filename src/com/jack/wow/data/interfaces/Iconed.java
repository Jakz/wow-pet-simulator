package com.jack.wow.data.interfaces;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface Iconed
{
  public String iconName();
  default public Path iconPath(boolean small) { return pathForIcon(iconName(), small); }
  
  public static Path pathForIcon(String iconName, boolean small)
  {
    return Paths.get("data/icons/" + (small ? "small" : "large") + "/" + iconName + ".jpg");
  }
}
