package com.jack.wow.files;

import java.awt.Frame;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    for (String icon : icons)
    {
      if (!Files.exists(fileName(icon, true)))
        pool.submit(new DownloaderTask(icon, true));
      if (!Files.exists(fileName(icon, false)))
        pool.submit(new DownloaderTask(icon, false));
    }

    pool.shutdown();
        
    if (!pool.getQueue().isEmpty())
    {
      ProgressDialog.init(parent, "Icon Downloader", () -> { pool.shutdownNow(); started = false; });
      
      new Thread( () -> {
        try
        {
          pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
          
          if (!pool.isShutdown())
            ProgressDialog.finished();
        }
        catch (InterruptedException e)
        {
          // cancelled by user
        }
        
      }).start();
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

      } catch (MalformedURLException e)
      {
        e.printStackTrace();
      }
    }
    
    @Override
    public Boolean call()
    {
      try
      {
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        channel.transferFrom(rbc, 0, 1 << 24);
        channel.close();
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
