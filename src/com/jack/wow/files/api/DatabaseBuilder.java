package com.jack.wow.files.api;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jack.wow.data.PetAbility;
import com.jack.wow.data.PetSpec;
import com.jack.wow.files.StreamException;

public class DatabaseBuilder
{
  private final int MAX_ABILITY_ID = 2000;
  private final int MAX_PET_ID = 2000;
  
  public List<PetAbility> fetchAbilitiesFromAPI() throws InterruptedException
  {
    final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    
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
    final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    
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
    final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);

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
}
