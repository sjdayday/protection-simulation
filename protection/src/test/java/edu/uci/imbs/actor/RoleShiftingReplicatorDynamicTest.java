/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class RoleShiftingReplicatorDynamicTest
{
	private List<Peasant> peasants;
	private List<Bandit> bandits;
	private ProtectionPopulation population;
	private ProtectionStatistics stats;
	private Dynamic dynamic;
	@Before
	public void setUp() throws Exception
	{
		Peasant.resetStartingIdForTesting(0);
		peasants = TestBuilder.buildPeasantList();
		bandits = TestBuilder.buildBanditList();
		population = new ProtectionPopulation(bandits, peasants); 
		ProtectionParameters.MIMIC_BETTER_PERFORMING_POPULATION = false; 
	}
	//TODO verifyNewPeasantsAdoptTheStrategyOfTheHighestPerformingPeasantNotARandomStrategy  ....if that's what we want?
	@Test
	public void verifyShiftsBanditsToPeasantsByOne() throws Exception
	{
		//TODO consider dropping support for this.
		stats = new TestingStatistics(bandits, peasants, 1); 
		Dynamic dynamic = new RoleShiftingReplicatorDynamic(stats); 
		dynamic.setPopulation(population); 
		population = dynamic.rebuildPopulation(); 
		assertEquals("shift 1 from bandits",3-1, population.getBandits().size()); 
		assertEquals("...to peasants",4+1, population.getPeasants().size()); 
	}
	@Test
	public void verifyQuoteNormalQuoteMimicryShiftsActorsToTheBetterPerformingPopulation() throws Exception
	{
		ProtectionParameters.MIMIC_BETTER_PERFORMING_POPULATION = true; 
		stats = new TestingStatistics(bandits, peasants, 1); 
		Dynamic dynamic = new RoleShiftingReplicatorDynamic(stats); 
		dynamic.setPopulation(population); 
		population = dynamic.rebuildPopulation(); 
		assertEquals("bandits doing better, so peasants mimic them:  shift 1 from peasants",4-1, population.getPeasants().size()); 
		assertEquals("to bandits",3+1, population.getBandits().size()); 
		
	}
	@Test
	public void verifyNoShiftInPopulation() throws Exception
	{
		stats = new TestingStatistics(bandits, peasants, 0); 
		Dynamic dynamic = new RoleShiftingReplicatorDynamic(stats); 
		dynamic.setPopulation(population); 
		population = dynamic.rebuildPopulation(); 
		assertEquals(bandits, population.getBandits()); 
		assertEquals(peasants, population.getPeasants()); 
	}
	@Test
	public void verifyMultiplePeasantsShiftedToBanditsUpToSizeOfExistingPeasantPopulation() throws Exception
	{
		stats = new TestingStatistics(bandits, peasants, -5); 
		dynamic = new RoleShiftingReplicatorDynamic(stats); 
		dynamic.setPopulation(population); 
		population = dynamic.rebuildPopulation(); 
		assertEquals("all available peasants",4-4, population.getPeasants().size()); 
		assertEquals("shift to bandits",3+4, population.getBandits().size()); 
	}
	@Test
	public void verifyPeasantsRemovedHadLowestPayoff() throws Exception
	{
		stats = new TestingStatistics(bandits, peasants, -2); 
		peasants.get(0).setProtectionProportion(.7d); 
		assertEquals(.49, peasants.get(0).getProtection(), .001); 
		assertEquals(.147, peasants.get(0).getProtectedPayoff(), .001); 
		peasants.get(0).surrenderUnprotectedPayoff();
		assertEquals("high protection, moderately low payoff, so will be kept",
				.147, peasants.get(0).getPayoff(), .001); 
		peasants.get(1).setProtectionProportion(.1d); 
		assertEquals("keeps all 1-x cause not surrendered",.9, peasants.get(1).getPayoff(), .001); 
		peasants.get(1).surrenderUnprotectedPayoff(); 
		assertEquals("low protection, lowest payoff after surrender, so will be dropped",
				.009, peasants.get(1).getPayoff(), .001); 
		peasants.get(2).setProtectionProportion(.3d); 
		assertEquals("low protection, but doesn't surrender so payoff high enough to keep",
				.7, peasants.get(2).getPayoff(), .001); 
		peasants.get(3).setProtectionProportion(.5d); 
		peasants.get(3).surrenderUnprotectedPayoff(); 
		assertEquals("moderate protection, but surrendered, so payoff is low; dropped",
				.125, peasants.get(3).getPayoff(), .001); 

		Dynamic dynamic = new RoleShiftingReplicatorDynamic(stats); 
		dynamic.setPopulation(population); 
		population = dynamic.rebuildPopulation(); 
		for (Peasant peasant : population.getPeasants()) 
		{
			peasant.tick(); 
		}
		assertEquals("second highest payoff before dynamic",0.7, population.getPeasants().get(0).getProtectionProportion(), .001);
		assertEquals(0.3, population.getPeasants().get(0).getPayoff(), .001); 
		assertEquals("highest payoff before dynamic",0.3, population.getPeasants().get(1).getProtectionProportion(), .001);
		assertEquals(0.7, population.getPeasants().get(1).getPayoff(), .001); 
		assertEquals(2, population.getPeasants().size());
	}
	@Test
	public void verifyNoMimicryIfLowerPerformingPopulationIsEmpty() throws Exception
	{
		population = new ProtectionPopulation(bandits, new ArrayList<Peasant>()); 
		stats = new TestingStatistics(bandits, peasants, 1); 
		dynamic = new RoleShiftingReplicatorDynamic(stats); 
		dynamic.setPopulation(population); 
		population = dynamic.rebuildPopulation(); 
		assertEquals("original bandit list size unchanged",3,population.getBandits().size()); 
		assertEquals("no peasants created",0,population.getPeasants().size()); 

		population = new ProtectionPopulation(new ArrayList<Bandit>(), peasants); 
		stats = new TestingStatistics(bandits, peasants, -1); 
		dynamic = new RoleShiftingReplicatorDynamic(stats); 
		dynamic.setPopulation(population); 
		population = dynamic.rebuildPopulation(); 
		assertEquals("original peasant list size unchanged",4,population.getPeasants().size()); 
		assertEquals("no bandits created",0,population.getBandits().size()); 
	}
//	private Dynamic getRoleShiftingReplicatorDynamicWithForcedPayoffs()
//	{
//		//anonymous inner class
//		return new RoleShiftingReplicatorDynamic(stats)
//		{
//			@Override
//			protected Peasant buildNewPeasant(Peasant peasant)
//			{
//				Peasant forced = new Peasant(); 
//				forced.setPayoffForTesting(peasant.getPayoff()); 
//				return forced;
//			}
//		};
//	}
	private class TestingStatistics extends ProtectionStatistics
	{
		
		private int adjust;
		public TestingStatistics(List<Bandit> bandits, List<Peasant> peasants, int adjust)
		{
			super(bandits, peasants);
			this.adjust = adjust;  
		}
		@Override
		public int getActorAdjustment()
		{
			return adjust;
		}
	}
}
