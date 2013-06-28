/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.*; 

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AllInteractionPatternTest
{

	private List<Bandit> bandits;
	private List<Peasant> peasants;
	private AllInteractionPattern pattern;
	@BeforeClass
	public static void setUpLog4J() throws Exception
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
	}
	@Before
	public void setUp() throws Exception
	{
		bandits = TestBuilder.buildBanditList(); 
		peasants = TestBuilder.buildPeasantList(); 
		ProtectionParameters.resetForTesting();
		pattern = new AllInteractionPattern(bandits, peasants);  
	}
	@Test
	public void verifyBanditsAndPeasantsCombinedIntoOneList()
	{
		assertEquals(7, pattern.getCombinedList().size()); 
		assertTrue("list order by trial and error",pattern.getCombinedList().get(0) instanceof Peasant); 
		assertEquals(2,pattern.getCombinedList().get(0).getId()); 
		assertTrue(pattern.getCombinedList().get(1) instanceof Bandit); 
		assertEquals(3,pattern.getCombinedList().get(1).getId()); 
		assertTrue(pattern.getCombinedList().get(2) instanceof Peasant); 
		assertEquals(3,pattern.getCombinedList().get(2).getId()); 
		assertTrue(pattern.getCombinedList().get(3) instanceof Peasant); 
		assertEquals(4,pattern.getCombinedList().get(3).getId()); 
		assertTrue(pattern.getCombinedList().get(4) instanceof Peasant); 
		assertEquals(1,pattern.getCombinedList().get(4).getId()); 
		assertTrue(pattern.getCombinedList().get(5) instanceof Bandit); 
		assertEquals(1,pattern.getCombinedList().get(5).getId()); 
		assertTrue(pattern.getCombinedList().get(6) instanceof Bandit); 
		assertEquals(2,pattern.getCombinedList().get(6).getId()); 
	}
	@Test
	public void verifyBanditsAndPeasantsRandomlyMatched() throws Exception
	{
		InteractionPair<Bandit, Peasant> pair = null;
		assertTrue(pattern.hasNext());
		pair = pattern.next(); 
		assertEquals(3, pair.getSource().getId()); 
		assertEquals(2, pair.getTarget().getId()); 
		assertTrue(pattern.hasNext());
		pair = pattern.next(); 
		assertEquals(1, pair.getSource().getId()); 
		assertEquals(1, pair.getTarget().getId()); 
		assertFalse("only two pairs",pattern.hasNext());
	}
	@Test
	public void verifyPopulationUpdatedRebuildingCombinedAndInteractionPairLists() throws Exception
	{
		pattern.updatePopulations(TestBuilder.buildBanditList(5), peasants);
		assertEquals(9, pattern.getCombinedList().size());
		assertEquals("no interaction pairs prior to permute",0, pattern.getInteractionPairs().size()); 
		pattern.permute(); 
		assertEquals("should shuffle list, but not change its size",9, pattern.getCombinedList().size()); 
		assertEquals("random shuffle pairs 3 bandits and peasants",3, pattern.getInteractionPairs().size()); 
		pattern.updatePopulations(TestBuilder.buildBanditList(5), peasants);
		assertEquals(9, pattern.getCombinedList().size());
		assertEquals("resets interaction pairs to empty",0, pattern.getInteractionPairs().size()); 
		pattern.permute(); 
//		for (Actor actor : pattern.getCombinedList())
//		{  // eyeball it to see which actors will be paired up
//			System.out.println(actor);
//		}
		assertEquals(9, pattern.getCombinedList().size()); 
		assertEquals("next shuffle gives 1 ",1, pattern.getInteractionPairs().size()); 

	}
}
