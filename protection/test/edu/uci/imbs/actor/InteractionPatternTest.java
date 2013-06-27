/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;


public class InteractionPatternTest
{
	private List<Peasant> peasants;
	private List<Bandit> bandits;
	private InteractionPattern<Bandit, Peasant> pattern;
	@Before
	public void setUp() throws Exception
	{
		peasants = TestBuilder.buildPeasantList(); 
		bandits = TestBuilder.buildBanditList(); 
		pattern = new InteractionPattern<Bandit, Peasant>(bandits, peasants); 
	}
	
	@Test
	public void verifyInteractionPatternMaintainsSourceAndTargetLists() throws Exception
	{
		assertEquals(3, pattern.getSourceList().size());
		assertEquals(4, pattern.getTargetList().size());
		assertEquals(1, pattern.getSourceList().get(0).getId());
		assertEquals(2, pattern.getTargetList().get(1).getId());
	}
	@Test
	public void verifyListsArePermutedKeepingSameSizeButAlteringOrder() throws Exception
	{
		pattern.setRandomForTesting(new Random(123)); 
		pattern.permute(); 
//		printList(pattern.getSourceList());
//		printList(pattern.getTargetList());
		assertEquals(3, pattern.getSourceList().size());
		assertEquals(4, pattern.getTargetList().size());
		assertEquals("was at position 1", 2, pattern.getSourceList().get(0).getId());
		assertEquals("was at position 1", 2, pattern.getTargetList().get(0).getId());
		//Bandit 2
		//Bandit 1
		//Bandit 3
		//Peasant 2
		//Peasant 1
		//Peasant 3
		//Peasant 4
	}
	@Test
	public void verifyPairedActorsAreReturnedAtNextInteraction() throws Exception
	{
		assertTrue(pattern.hasNext()); 
		InteractionPair<Bandit, Peasant> pair = pattern.next(); 
		assertEquals(1, pair.getSource().getId());
		assertEquals("Bandit", pair.getSource().getName());
		assertEquals("Peasant", pair.getTarget().getName());
	}
	@Test
	public void verifyNextInteractionTraversesEntireList() throws Exception
	{
		assertNotNull(pattern.next());
		assertNotNull(pattern.next());
		assertTrue("2 down, 1 to go",pattern.hasNext()); 
		assertNotNull(pattern.next());
		assertTrue(!pattern.hasNext()); 
		assertNull(pattern.next());
	}
	@Test
	public void verifyIterationStopsWhenEitherListEnds() throws Exception
	{
		peasants.remove(3);
		peasants.remove(2); // shorten the target list
		assertEquals(2, peasants.size()); 
		pattern = new InteractionPattern<Bandit, Peasant>(bandits, peasants); 

		assertNotNull(pattern.next());
		assertTrue("1 down, 1 to go in target list",pattern.hasNext()); 
		assertNotNull(pattern.next());
		assertTrue(!pattern.hasNext()); 
		assertNull(pattern.next());
	}
	@Test
	public void verifyConcurrentModificationAvoidedByUpdatingPopulationsAfterListsAreModified() throws Exception
	{
		pattern.next();
		bandits.remove(0);
		peasants.remove(0); 
		copyPopulationsToNewLists();
		pattern.updatePopulations(bandits, peasants); // = new InteractionPattern<Bandit, Peasant>(pattern); 
		pattern.next();
	}
	private void copyPopulationsToNewLists()
	{
		ArrayList<Bandit> newBandits = new ArrayList<Bandit>(); 
		ArrayList<Peasant> newPeasants = new ArrayList<Peasant>(); 
		for (Bandit bandit : bandits)
		{
			newBandits.add(new Bandit(bandit)); 
		}
		for (Peasant peasant : peasants)
		{
			newPeasants.add(new Peasant(peasant)); 
		}
		assertEquals(2,newBandits.size());
		assertEquals(3,newPeasants.size()); 
		bandits = newBandits;
		peasants = newPeasants;
	}

	@Test
	public void verifyCopyConstructorIncludesRandomButBothOldAndNewSharingSameRandom() throws Exception
	{
		pattern = new InteractionPattern<Bandit, Peasant>(bandits, peasants); 
		pattern.setRandomForTesting(new Random(123)); 
		InteractionPattern<Bandit, Peasant> newPattern = new InteractionPattern<Bandit, Peasant>(pattern); 
		assertEquals(pattern.getRandomForTesting(), newPattern.getRandomForTesting());
		assertEquals(pattern.getSourceList(), newPattern.getSourceList());
		assertEquals(pattern.getTargetList(), newPattern.getTargetList());
		assertTrue("both old and new pointing to same Random, so old interaction pattern should be discarded",
				!(pattern.getRandomForTesting().nextLong() == newPattern.getRandomForTesting().nextLong())); 
	}
	@SuppressWarnings("unused")
	private void printList(List<? extends Actor> sourceList)
	{
		for (Actor actor : sourceList)
		{
			System.out.println(actor.toString());
		}
	}
}

