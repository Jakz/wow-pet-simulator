package com.jack.wow.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jack.wow.data.Database;
import com.jack.wow.data.PetOwnedAbility;
import com.jack.wow.data.PetSpec;
import com.jack.wow.data.PetStats;
import com.jack.wow.json.ImplicitContextedAdapter;

public class Json
{
  public static Gson build()
  {
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    
    builder.registerTypeAdapter(PetSpec.class, new ImplicitContextedAdapter<PetSpec>(PetSpec.class));
    builder.registerTypeAdapter(PetOwnedAbility.class, new ImplicitContextedAdapter<PetOwnedAbility>(PetOwnedAbility.class));
    builder.registerTypeAdapter(PetStats.class, new ImplicitContextedAdapter<PetStats>(PetStats.class));
    builder.registerTypeAdapter(Database.class, new ImplicitContextedAdapter<Database>(Database.class));

    return builder.create();
  }
}
