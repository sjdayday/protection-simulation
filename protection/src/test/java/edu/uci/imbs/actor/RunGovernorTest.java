/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class RunGovernorTest
{
	private RunGovernor governor;
	private TestingSeeker seeker;

	@BeforeClass
	public static void setUpLog4J() throws Exception
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
	}
	
	@Before
	public void setUp() throws Exception
	{
		ProtectionParameters.resetForTesting(); 
		ProtectionParameters.RUN_LIMIT = 10; 	
		seeker = new TestingSeeker(); 
		governor = new RunGovernor(seeker);
		seeker.setBanditList(TestBuilder.buildBanditList()); 
		seeker.setPeasantList(TestBuilder.buildPeasantList()); 
	}
	@Test
	public void verifyStopsWhenRunLimitReached() throws Exception
	{
		assertEquals(RunGovernorEnum.NOT_STOPPED, governor.stop()); 
		ProtectionParameters.RUN_LIMIT = 2; 	
		governor.tick(); 
		governor.tick(); 
		assertEquals(RunGovernorEnum.RUN_LIMIT_REACHED, governor.stop()); 
	}
	@Test
	public void verifyStopsWhenEquilibriumReachedForSpecifiedNumberOfConsecutivePeriodsWithPopulationChange() throws Exception
	{
		boolean populationSizeUnchanged = false; 
		RunGovernorEnum stopCode = checkGeneralEquilibriumTracksConsecutivePeriodsWithNoAdjustment(populationSizeUnchanged); 
		assertEquals("2nd consecutive period so stop now",RunGovernorEnum.EQUILIBRIUM_REACHED, stopCode);
	}
	@Test
	public void verifyStopsWhenEquilibriumReachedForSpecifiedNumberOfConsecutivePeriodsWithStaticPopulation() throws Exception
	{
		boolean populationSizeUnchanged = true; 
		RunGovernorEnum stopCode = checkGeneralEquilibriumTracksConsecutivePeriodsWithNoAdjustment(populationSizeUnchanged); 
		assertEquals("2nd consecutive period, and population unchanged so stop now with static equilibrium",
				RunGovernorEnum.STATIC_EQUILIBRIUM_REACHED, stopCode);
	}
	private RunGovernorEnum checkGeneralEquilibriumTracksConsecutivePeriodsWithNoAdjustment(boolean populationSizeUnchanged)
	{
		ProtectionParameters.EQUILIBRIUM_NUMBER_PERIODS_WITHOUT_ADJUSTMENT = 2; 
		seeker.setHasAdjustedThisPeriodForTesting(false); 
		governor.tick(); 
		assertEquals("need 2 periods",RunGovernorEnum.NOT_STOPPED, governor.stop()); 
		seeker.setHasAdjustedThisPeriodForTesting(true); 
		governor.tick(); 
		assertEquals("counter reset if we adjust",RunGovernorEnum.NOT_STOPPED, governor.stop()); 
		seeker.setHasAdjustedThisPeriodForTesting(false); 
		governor.tick(); 
		assertEquals("1...",RunGovernorEnum.NOT_STOPPED, governor.stop());
		seeker.setArePopulationSizesUnchangedForTesting(populationSizeUnchanged); 
		governor.tick(); 
		return governor.stop(); 
	}
	@Test
	public void verifyStopsWhenBanditPopulationDropsToZero() throws Exception
	{
		verifyPopulationSize(Population.BANDITS, 0, RunGovernorEnum.BANDITS_EXTINCT);
	}
	@Test
	public void verifyStopsWhenPeasantPopulationDropsToZero() throws Exception
	{
		verifyPopulationSize(Population.PEASANTS, 0, RunGovernorEnum.PEASANTS_EXTINCT);
	}
	@Test
	public void verifyStopsWhenPeasantPopulationExceedsUpperLimit() throws Exception
	{
		ProtectionParameters.MAXIMUM_POPULATION_SIZE = 10; 
		verifyPopulationSize(Population.PEASANTS, 13, RunGovernorEnum.PEASANTS_EXCEEDED_MAX);
	}
	@Test
	public void verifyStopsWhenBanditPopulationExceedsUpperLimit() throws Exception
	{
		
		ProtectionParameters.MAXIMUM_POPULATION_SIZE = 10; 
		verifyPopulationSize(Population.BANDITS, 12, RunGovernorEnum.BANDITS_EXCEEDED_MAX);
	}
	private void verifyPopulationSize(Population population, int size, RunGovernorEnum reason)
	{
		governor.tick(); 
		assertEquals(RunGovernorEnum.NOT_STOPPED, governor.stop()); 
		if (population.equals(Population.BANDITS)) seeker.setBanditList(TestBuilder.buildBanditList(size)); 
		else seeker.setPeasantList(TestBuilder.buildPeasantListWithRandomProtectionProportions(size, new DummyFunction())); 
		assertEquals(reason, governor.stop()); 
	}
	private enum Population
	{
		BANDITS,
		PEASANTS;
	}
	private class TestingSeeker extends ProtectionEquilibriumSeeker
	{
		
		private boolean adjusted = true;
		private boolean unchanged;
		public TestingSeeker()
		{
			
		}
		
		public void setArePopulationSizesUnchangedForTesting(boolean b)
		{
			this.unchanged = b; 
		}

		public void setHasAdjustedThisPeriodForTesting(boolean b)
		{
			this.adjusted = b; 
		}
		@Override
		public boolean populationSizesUnchanged()
		{
			return unchanged;
		}
		@Override
		public boolean hasAdjustedThisPeriod()
		{
			return adjusted;
		}

	}
}
