package com.jack.wow.files;

import java.awt.Frame;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.jack.wow.ui.misc.ProgressDialog;


public class IconDownloader
{
  public ThreadPoolExecutor pool;
  int totalTasks;
  int missingTasks;
  boolean started;
  
  Frame parent;
  
  Set<String> icons;
  
  private Path fileName(String name, boolean small)
  {
    String suffix = small ? "small" : "large";
    return Paths.get("data/icons/", suffix, name+".jpg");
  }

  public IconDownloader(Frame parent, Set<String> icons)
  {
    this.icons = icons;
  }
  
  public void start()
  {
    try
    {
      Files.createDirectories(Paths.get("data/icons/small"));
      Files.createDirectories(Paths.get("data/icons/large"));

    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }

    if (started)
      return;
    
    pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    started = true;
    
    List<Callable<Boolean>> tasks = icons.stream()
      .filter(icon -> !Files.exists(fileName(icon, true)))
      .map(icon -> new DownloaderTask(icon, true))
      .collect(Collectors.toList());
    
    icons.stream()
    .filter(icon -> !Files.exists(fileName(icon, false)))
    .map(icon -> new DownloaderTask(icon, false))
    .forEach(tasks::add);
    
    try
    {
      if (!tasks.isEmpty())
      {
        ProgressDialog.init(parent, "Icon Downloader", () -> { pool.shutdownNow(); started = false; });
        List<Future<Boolean>> results = pool.invokeAll(tasks);
        ProgressDialog.finished();

      }
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
  
  public class DownloaderTask implements Callable<Boolean>
  {
    URL url;
    Path path;
    
    private static final String baseURL = "http://wow.zamimg.com/images/wow/icons/";
    
    public DownloaderTask(String iconName, boolean isSmall)
    {  
      String suffix = isSmall ? "small" : "large";

      try
      {
        this.url = new URL(baseURL + suffix + "/" + iconName + ".jpg");
        this.path = fileName(iconName, isSmall);
      } 
      catch (MalformedURLException e)
      {
        e.printStackTrace();
      }
    }
    
    @Override
    public Boolean call()
    {
      try (InputStream is = url.openStream())
      {
        try (ReadableByteChannel rbc = Channels.newChannel(is))
        {
          FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
          channel.transferFrom(rbc, 0, 1 << 24);
        }
      }
      catch (FileNotFoundException e)
      {
        return false;
      }
      catch (java.nio.channels.ClosedByInterruptException e)
      {
        try
        {
          if (Files.exists(path))
            Files.delete(path);
        }
        catch (IOException ee)
        {
          ee.printStackTrace();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();

        return false;
      }

      if (pool != null)
      {
        long completed = pool.getCompletedTaskCount();
        long total = pool.getTaskCount(); 
      
        ProgressDialog.update(completed/(float)total, (completed+1)+" of "+total);
      }

      return true;
    }
  }
}
