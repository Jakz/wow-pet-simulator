package com.jack.wow.ui.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.JTextField;

import com.pixbits.lib.parser.SimpleParser;

public class SearchTextField<T> extends JTextField
{
  final private SimpleParser parser;
  final private Map<String, Function<String, Predicate<T>>> options;
  final private Map<String, Predicate<T>> standaloneOptions;
  private Optional<Function<String, Predicate<T>>> defaultOption;
  
  private Predicate<T> predicate;
  final private Consumer<Predicate<T>> callback;
  
  private String text;
  
  public SearchTextField(int width, Consumer<Predicate<T>> callback)
  {
    super(width);
    
    options = new HashMap<>();
    standaloneOptions = new HashMap<>();
    defaultOption = Optional.empty();
    
    parser = new SimpleParser();
    parser.addWhiteSpace(' ');
    parser.addQuote('\"');
    
    predicate = t -> true;
    
    this.addCaretListener(e -> fieldUpdated());
    
    this.callback = callback;
  }
  
  public Predicate<T> predicate()
  {
    return predicate;
  }
  
  public void addRule(String preamble, Function<String, Predicate<T>> lambda)
  {
    options.put(preamble, lambda);
  }
  
  public void addStandaloneRule(String preamble, Predicate<T> lambda)
  {
    standaloneOptions.put(preamble, lambda);
  }
  
  public void setDefaultRule(Function<String, Predicate<T>> lambda)
  {
    defaultOption = Optional.of(lambda);
  }
  
  private void fieldUpdated()
  {    
    predicate = p -> true;

    String ntext = this.getText();
    if (ntext.equals(text))
      return;
    else
      text = ntext;

    List<String> tokens = new ArrayList<>();
    Consumer<String> callback = s -> tokens.add(s); 
  
    parser.setCallback(callback);
    parser.reset(new java.io.ByteArrayInputStream(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    
    try
    {
      parser.parse();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    for (String tk : tokens)
    {
      final String token;
      boolean negated = false;
      
      if (tk.startsWith("!"))
      {
        negated = true;
        token = tk.substring(1);
      }
      else
        token = tk;

      Predicate<T> standalone = standaloneOptions.get(token);
      if (standalone != null)
      {
        predicate = predicate.and(negated ? standalone.negate() : standalone);
        continue;
      }
           
      String[] otk = token.split(":");
         
      if (otk.length == 2)
      {
        Function<String, Predicate<T>> currentPredicateBuilder = options.getOrDefault(otk[0], s -> (p -> true));
        Predicate<T> pred = currentPredicateBuilder.apply(otk[1]);
        predicate = predicate.and(negated ? pred.negate() : pred);
      }
      else
      {
        Predicate<T> pred = defaultOption.orElse(s -> (p -> true)).apply(token);
        predicate = predicate.and(negated ? pred.negate() : pred);
      }
    }
    
    this.callback.accept(predicate);
  }
  

}
