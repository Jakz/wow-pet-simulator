package com.jack.wow.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

import com.jack.wow.data.Formulas;
import com.jack.wow.data.PetBreed;
import com.jack.wow.data.PetQuality;
import com.jack.wow.data.PetStats;

public class FormulaTest
{
  private static PetStats crawdadStats;
  private static PetStats willyStats;
  private static PetStats qirajiStats;
  
  @BeforeClass
  static public void setup()
  {
    crawdadStats = new PetStats(9, 8, 7);
    willyStats = new PetStats(8, 8, 8);
    qirajiStats = new PetStats(8, 8, 8);
  }
  
  @Test
  public void testStatsFormulas()
  {
    Assert.assertArrayEquals(new int[] {1888, 260, 228}, Formulas.adjustedStats(crawdadStats, PetBreed.HH, 25, PetQuality.RARE).toIntArray());
    Assert.assertArrayEquals(new int[] {1481, 276, 276}, Formulas.adjustedStats(willyStats, PetBreed.BB, 25, PetQuality.RARE).toIntArray());
  }
  
  @Test
  public void testBreedFormulas()
  {
    final int[][] expecteds = new int[][] {
      new int[] { 1481, 276, 276 }, 
      new int[] { 1400, 325, 260 }, 
      new int[] { 1400, 260, 325 }, 
      new int[] { 1725, 260, 260 }, 
      new int[] { 1546, 289, 260 }, 
      new int[] { 1400, 289, 289 }, 
      new int[] { 1546, 260, 289 }, 
      new int[] { 1465, 289, 273 }, 
      new int[] { 1465, 273, 289 }, 
      new int[] { 1546, 273, 273 }
    };
    
    for (int i = 0; i < PetBreed.count(); ++i)
    {
      PetBreed breed = PetBreed.values()[i];
      int[] expected = expecteds[i];
      
      Assert.assertArrayEquals(expected, Formulas.adjustedStats(qirajiStats, breed, 25, PetQuality.RARE).toIntArray());
    }
  }
  
  @Test
  public void testAttackDamageFormulas()
  {
    Assert.assertEquals(Formulas.standardDamage(20, 227), 247); // bite adder
    //Assert.assertEquals(Formulas.standardDamage(9, 322), 153); // flock ikky
  }
}
