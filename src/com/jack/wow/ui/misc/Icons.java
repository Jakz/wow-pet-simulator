package com.jack.wow.ui.misc;

import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Icons
{
  private static final Map<String, ImageIcon> cache = new HashMap<>();
  
  public static ImageIcon getIcon(String name, boolean small)
  {
    name = name.toLowerCase();

    ImageIcon img = cache.get(name);
    
    if (img == null)
      img = loadIcon(name, small);
    
    return img;
  }
  
  private static ImageIcon loadIcon(String name, boolean small)
  {
    name = "data/icons/" + (small ? "small" : "large") + "/" + name + ".jpg";
    
    File file = new File(name);
    
    if (!Files.exists(file.toPath()))
      return new ImageIcon();
    
    try
    {
      Image image = ImageIO.read(new File(name));
      ImageIcon icon = new ImageIcon(image);
      cache.put(name, icon);          
      return icon;
    } 
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return new ImageIcon();

  }
  
  /*private static ImageIcon loadIcon(String name)
  {
    String fileName = name + ".png";
    
    try (ZipFile file = new ZipFile("data/icons.zip"))
    {
      Enumeration<? extends ZipEntry> entries = file.entries();
      
      while (entries.hasMoreElements())
      {
        ZipEntry entry = entries.nextElement();
        
        if (entry.getName().toLowerCase().endsWith(fileName))
        {
          try (InputStream is = file.getInputStream(entry))
          {          
            Image image = ImageIO.read(is);  
            image = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            
            ImageIcon icon = new ImageIcon(image);
            cache.put(name, icon);          
            return icon;
          }
        }
      }
      
      return getIcon("INV_Misc_QuestionMark");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    return null;
  }*/
  
}
