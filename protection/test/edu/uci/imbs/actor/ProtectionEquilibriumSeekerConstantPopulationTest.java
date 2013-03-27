package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ProtectionEquilibriumSeekerConstantPopulationTest
{
	private List<Peasant> peasants;
	private List<Bandit> bandits;
	private InteractionPattern<Bandit, Peasant> pattern;
	private ProtectionStatistics statistics;
	private ProtectionEquilibriumSeeker seeker;
	@Before
	public void setUp() throws Exception
	{
		peasants = TestBuilder.buildPeasantList(); 
		bandits = TestBuilder.buildBanditList(); 
		ProtectionParameters.MIMIC_BETTER_PERFORMING_POPULATION = false; 
		seekerSetup(); 
	}
	public void seekerSetup()
	{
		pattern = TestBuilder.buildPermutedRepeatableInteractionPattern(bandits, peasants); 
		statistics = new ProtectionStatistics(bandits, peasants); 
		statistics.setPayoffDiscrepancyTolerance(.03);
		seeker = new ProtectionEquilibriumSeeker(); 
		seeker.setPeasantList(peasants); 
		seeker.setBanditList(bandits); 
		seeker.setInteractionPattern(pattern); 
		seeker.setProtectionStatistics(statistics);
		seeker.addDynamic(new RoleShiftingReplicatorDynamic(statistics)); 
	}
	@Test
	public void verifyRunsMultiplePeriodsUntilDiscrepancyWithinThreshold() throws Exception
	{
		seeker.setRunLimit(5); // avoid looping forever if we have a bug 
		seeker.runToEquilibriumOrLimit(); 
//		printRecords(); 
//		assertEquals(3, seeker.getProtectionStatistics().numberPeriods()); 
	}
	@Test
	public void verifyEachRunCausesListsToBeUpdated() throws Exception
	{
		assertEquals(peasants, seeker.getPeasantList()); 
		assertEquals(bandits, seeker.getBanditList());
		seeker.setRunLimit(1); 
		seeker.runToEquilibriumOrLimit(); 
		assertNotSame(peasants, seeker.getPeasantList()); 
		assertNotSame(bandits, seeker.getBanditList());
	}

	@Test
	public void verifyRunsToPeriodLimitIfDiscrepanciesNeverWithinThreshold() throws Exception
	{
		statistics.setPayoffDiscrepancyTolerance(.01);
		seeker.setRunLimit(10); 
		seeker.runToEquilibriumOrLimit(); 
		assertEquals(10, seeker.getProtectionStatistics().getStatisticsRecords().size()); 
//		printRecords(); 
	}
//	@Test
	public void runTrial() throws Exception
	{
		setupTrial();
	}
	private void setupTrial() throws BehaviorException
	{
		bandits = new ArrayList<Bandit>(); 
		peasants = new ArrayList<Peasant>(); 
		for (int i = 0; i < 50; i++)
		{
			bandits.add(TestBuilder.buildBandit(i)); 
			peasants.add(TestBuilder.buildFullPeasant(i, 0.5, ProtectionFunctionEnum.POWER.buildFunction(2)));
		}
		seekerSetup();
		statistics.setPayoffDiscrepancyTolerance(.01);
		seeker.setRunLimit(100); 
		seeker.runToEquilibriumOrLimit(); 
		printRecords(); 
	}
//	@SuppressWarnings("unused")
	private void printRecords()
	{
		List<? extends StatisticsRecord> records = statistics.getStatisticsRecords(); 
		for (StatisticsRecord statisticsRecord : records)
		{
			System.out.println(statisticsRecord);
		}
	}
}
