package com.jack.wow.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Icons
{
  private static final Map<String, BufferedImage> cache = new HashMap<>();
  
  public static ImageIcon getIcon(String name)
  {
    name = name.toLowerCase();

    BufferedImage img = cache.get(name);
    
    if (img == null)
      img = loadIcon(name);
    
    return new ImageIcon(img);
  }
  
  private static BufferedImage loadIcon(String name)
  {
    try (ZipFile file = new ZipFile("data/icons.zip"))
    {
      Enumeration<? extends ZipEntry> entries = file.entries();
      
      while (entries.hasMoreElements())
      {
        ZipEntry entry = entries.nextElement();
        
        if (entry.getName().toLowerCase().equals(name))
        {
          try (InputStream is = file.getInputStream(entry))
          {          
            BufferedImage image = ImageIO.read(is);              
            cache.put(name, image);          
            return image;
          }
        }
      }   
    
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    return null;
  }
  
}
