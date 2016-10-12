package com.jack.wow.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FilteredData
{
  private final static Set<String> filteredAbilities = new HashSet<String>(Arrays.asList( new String[]
  {
    "alexander's hand",
    "heal back line test",
    "damage test",
    "multieffect test",
    "multi turn test",
    "multieffect and multi turn test",
    "strong trap",
    "apply timed dot test",
    "aura damage10 test",
    "apply aura test",
    "aura bomb 75 test",
    "apply dot until cancel test",
    "apply pad aura test",
    "padauratest",
    "apply stun test",
    "stun aura test",
    "apply swap lock",
    "damage shield 2.0test",
    "stun test 2.0",
    "stun aura test 2.0",
    "custom scripted effect test",
    "aura - damage shield 2.0",
    "pristine trap",
    "heal test 2.0",
    "custom scripted turn test",
    "jacob's test ability",
    "jacob's test aura",
    "jacob's test aura 2",
    "jacob's test aura 3",
    "gm stun",
    "gm heal",
    "gm trap",
    "gm kill",
    "gm suicide",
    "gm revive",
    "gm unkillable",
    "gm pass",
    "gm execute range",
    "gm kill all enemy"
  }));
  
  private final static Set<String> filteredPets = new HashSet<String>(Arrays.asList(new String[]
  {
      
  }));
  
  public static boolean isFiltered(PetAbility ability) { return filteredAbilities.contains(ability.name.toLowerCase()); }
  public static boolean isFiltered(PetSpec pet) { return filteredPets.contains(pet.name.toLowerCase()); }
}
