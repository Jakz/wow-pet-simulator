package com.jack.wow.files.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetSpec;
import com.jack.wow.data.interfaces.Iconed;
import com.pixbits.lib.functional.StreamException;

public class DatabaseBuilder
{
  private final int MAX_ABILITY_ID = 2500;
  private final int MAX_PET_ID = 2000;
  private final int THREADS = 10;
  
  public List<PetAbility> fetchAbilitiesFromAPI() throws InterruptedException
  {
    final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(THREADS);
    
    List<Callable<ApiAbility>> tasks = 
      IntStream.range(0, MAX_ABILITY_ID)
      .boxed()
      .map(i -> (Callable<ApiAbility>)( () -> ApiFetcher.fetchAbility(i) ) )
      .collect(Collectors.toList());
    
    new Thread(() -> {
      while (executor.getCompletedTaskCount() < MAX_ABILITY_ID)
      {
        System.out.println(String.format("Fetching abilities %2.1f%%..", ((executor.getCompletedTaskCount()/(float)MAX_ABILITY_ID)*100)));
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
      }
    }).start();
  
    return executor.invokeAll(tasks).stream()
      .map(StreamException.rethrowFunction(f -> f.get()))
      .filter(Objects::nonNull)
      .map(PetAbility::new)
      .collect(Collectors.toList());
  }
  
  public List<PetSpec> fetchPetsFromAPI() throws InterruptedException
  {
    final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(THREADS);
    
    List<Callable<ApiSpecie>> tasks = 
      IntStream.range(0, MAX_PET_ID)
      .boxed()
      .map(i -> (Callable<ApiSpecie>)( () -> ApiFetcher.fetchSpecie(i) ) )
      .collect(Collectors.toList());
    
    new Thread(() -> {
      while (executor.getCompletedTaskCount() < MAX_PET_ID)
      {
        System.out.println(String.format("Fetching pets %2.1f%%..", ((executor.getCompletedTaskCount()/(float)MAX_PET_ID)*100)));
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
      }
    }).start();
  
    return executor.invokeAll(tasks).stream()
      .map(StreamException.rethrowFunction(f -> f.get()))
      .filter(Objects::nonNull)
      .map(p -> new PetSpec(p))
      .collect(Collectors.toList());
  }
  
  public List<WowHeadFetcher.TooltipInfo> fetchTooltipsFromWowHead() throws InterruptedException
  {
    final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(THREADS);

    List<Callable<WowHeadFetcher.TooltipInfo>> tasks = 
        IntStream.range(0, MAX_ABILITY_ID)
        .boxed()
        .map(i -> (Callable<WowHeadFetcher.TooltipInfo>)( () -> WowHeadFetcher.parseAbilityTooltip(i) ) )
        .collect(Collectors.toList());
    
    new Thread(() -> {
      while (executor.getCompletedTaskCount() < MAX_ABILITY_ID)
      {
        System.out.println(String.format("Fetching tooltips %2.1f%%..", ((executor.getCompletedTaskCount()/(float)MAX_ABILITY_ID)*100)));
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
      }
    }).start();
    
    return executor.invokeAll(tasks).stream()
        .map(StreamException.rethrowFunction(f -> f.get()))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
  
  public void fetchIconsFromWowHead(List<? extends Iconed> data) throws InterruptedException, IOException
  {
     Files.createDirectories(Paths.get("data/icons/small"));
     Files.createDirectories(Paths.get("data/icons/large"));
         
     final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(THREADS);

     List<Callable<Void>> smallIconsTasks = 
         data.stream()
         .filter(e -> !Files.exists(e.iconPath(true)))
         .map(e -> (Callable<Void>)(() -> { WowHeadFetcher.downloadIcon(e.iconName(), true); return null;} ))
         .collect(Collectors.toList());
     
     List<Callable<Void>> largeIconsTasks = 
         data.stream()
         .filter(e -> !Files.exists(e.iconPath(false)))
         .map(e -> (Callable<Void>)(() -> { WowHeadFetcher.downloadIcon(e.iconName(), false); return null; }))
         .collect(Collectors.toList());
     
     smallIconsTasks.addAll(largeIconsTasks);
     
     System.out.println("Attempting to download " + smallIconsTasks.size() + " icons..");
     
     new Thread(() -> {
       while (executor.getCompletedTaskCount() < smallIconsTasks.size())
       {
         System.out.println(String.format("Downloading icons %2.1f%%..", ((executor.getCompletedTaskCount()/(float)smallIconsTasks.size())*100)));
         try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
       }
     }).start();
     
     executor.invokeAll(smallIconsTasks)
       .forEach(StreamException.rethrowConsumer(f -> f.get()));
  }
}
