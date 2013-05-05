package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class ProtectionEquilibriumSeekerVariablePopulationTest
{
	private List<Peasant> peasants;
	private List<Bandit> bandits;
	private InteractionPattern<Bandit, Peasant> pattern;
	private ProtectionStatistics statistics;
	private ProtectionEquilibriumSeeker seeker;
	private FitnessFunction fitnessFunction;
	private ReplicatorDynamic replicatorDynamic;
	private boolean roleShifting;
	@Before
	public void setUp() throws Exception
	{
		BasicConfigurator.configure(); 
		Logger.getRootLogger().setLevel(Level.ERROR); 
		ProtectionParameters.resetForTesting();
		roleShifting = false; 
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(5, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
//		printPeasants(peasants);
		bandits = TestBuilder.buildBanditList(5); 
		fitnessFunction = new FitnessFunction(); 
		fitnessFunction.setSurviveThreshold(0.1); 
		seekerSetup(); 
	}
	public void seekerSetup()
	{
		pattern = TestBuilder.buildPermutedRepeatableInteractionPattern(bandits, peasants); 
		statistics = new VariablePopulationProtectionStatistics(bandits, peasants, 21, .05); 
		statistics.setPayoffDiscrepancyTolerance(.005);
		statistics.setAdjustmentFactorPercentage(0.1); 
		seeker = new ProtectionEquilibriumSeeker(); 
		seeker.setPeasantList(peasants); 
		seeker.setBanditList(bandits); 
		seeker.setInteractionPattern(pattern); 
		seeker.setProtectionStatistics(statistics);
		replicatorDynamic = new ReplicatorDynamic();
		replicatorDynamic.setFitnessFunction(fitnessFunction); 
//		seeker.setReplicatorDynamic(replicatorDynamic); 
		seeker.addDynamic(new ProtectionReplicatorDynamic(replicatorDynamic)); 
		if (roleShifting) seeker.addDynamic(new RoleShiftingReplicatorDynamic(statistics)); 

	}
//	@Test
	public void verifyEachRunCausesListsToBeUpdated() throws Exception
	{
		assertEquals(peasants, seeker.getPeasantList()); 
		assertEquals(bandits, seeker.getBanditList());
		seeker.setRunLimit(1); 
		seeker.runToEquilibriumOrLimit(); 
		assertNotSame(peasants, seeker.getPeasantList()); 
		assertNotSame(bandits, seeker.getBanditList());
		
	}
//	@Test
	public void verifyRunsToPeriodLimitIfDiscrepanciesNeverWithinThreshold() throws Exception
	{
		statistics.setPayoffDiscrepancyTolerance(.01);
		seeker.setRunLimit(10); 
		seeker.runToEquilibriumOrLimit(); 
		assertEquals(10, statistics.getStatisticsRecords().size()); 
	}
	@Test
	public void verifyStatisticsUpdatedWithNewDistributionFollowingRun() throws Exception
	{
		seeker.setRunLimit(1); 
		seeker.runToEquilibriumOrLimit(); 
		assertEquals(1, seeker.getPeasantList().size()); 
//		printPeasants(peasants);
		assertEquals(3, seeker.getBanditList().size()); 
//		printRecords(); 
//		assertEquals(1, seeker.getProtectionStatistics().getStatisticsRecords().size()); //FIXME 2 
//		assertEquals(1, seeker.getProtectionStatistics().numberPeriods());  //FIXME 3(!)
//		assertEquals(3, seeker.getProtectionStatistics().numberPeriods()); 

	}
	@Test
	public void runScenario() throws Exception
	{
		stabilizationAfterEliminationOfHighAndLowProtectionProportions();
//		threePeasantsBothPopulationsGrowButBanditsOvertakeAndPopulationsStabilizeAfterEliminationOfHighAndLowProtectionProportions();
//		sixPeasantsBothPopulationsGrowWithoutApparentLimitAfterEliminationOfHighProtectionProportions();
//		slowGrowthInBothPopulationsWithSteadyShiftingAndOneFavoredProtectionProportionAveragePayoffDiverging();
//		threePeasantEquilibriumWithHigherThriveThresholdAfterEliminationOfMidToHighProtectionProportionsModeZero();
//		threePeasantsBothPopulationsGrowIndefinitelyWithHighPayoffsAndGrowingRoleShiftingAfterEliminationOfHighProtectionProportions();
//		sixPeasantsBothPopulationsGrowIndefinitelyWithHighPayoffsAndGrowingRoleShiftingAfterEliminationOfMidToHighProtectionProportions();
	}
	private void stabilizationAfterEliminationOfHighAndLowProtectionProportions() throws BehaviorException
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(100, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(100); 
		fitnessFunction.setThriveThreshold(0.3); 
		runWithLimit(4); 
//		assertEquals(4, seeker.getProtectionStatistics().numberPeriods()); //FIXME periods > run limit
		assertEquals(0.2433, seeker.getProtectionStatistics().averageBanditPayoff(), .001); 
//		run();
	}
	@SuppressWarnings("unused")
	private void threePeasantsBothPopulationsGrowButBanditsOvertakeAndPopulationsStabilizeAfterEliminationOfHighAndLowProtectionProportions() throws BehaviorException
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(300, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(100); 
//		seeker.addDynamic(new RoleShiftingReplicatorDynamic(statistics)); 
		fitnessFunction.setThriveThreshold(0.3); 
		run();
	}
	@SuppressWarnings("unused")
	private void sixPeasantsBothPopulationsGrowWithoutApparentLimitAfterEliminationOfHighProtectionProportions() throws BehaviorException
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(600, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(100); 
		fitnessFunction.setThriveThreshold(0.3); 
		runWithLimit(12);
	}
	@SuppressWarnings("unused")
	private void slowGrowthInBothPopulationsWithSteadyShiftingAndOneFavoredProtectionProportionAveragePayoffDiverging() throws BehaviorException
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(100, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(100); 
		roleShifting = true; 
		fitnessFunction.setThriveThreshold(0.3); 
		run();
	}
	@SuppressWarnings("unused")
	private void threePeasantEquilibriumWithHigherThriveThresholdAfterEliminationOfMidToHighProtectionProportionsModeZero() throws BehaviorException
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(300, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(100); 
		roleShifting = true; 
		fitnessFunction.setThriveThreshold(0.4); 
		runWithLimit(15);
	}
	@SuppressWarnings("unused")
	private void threePeasantsBothPopulationsGrowIndefinitelyWithHighPayoffsAndGrowingRoleShiftingAfterEliminationOfHighProtectionProportions() throws BehaviorException
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(300, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(100); 
		roleShifting = true; 
		fitnessFunction.setThriveThreshold(0.3); 
		run();
	}
	@SuppressWarnings("unused")
	private void sixPeasantsBothPopulationsGrowIndefinitelyWithHighPayoffsAndGrowingRoleShiftingAfterEliminationOfMidToHighProtectionProportions() throws BehaviorException
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(600, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(100); 
		roleShifting = true; 
		fitnessFunction.setThriveThreshold(0.3); 
		run();
	}
	public void run() throws BehaviorException
	{
		runWithLimit(10);
	}
	public void runWithLimit(int limit) throws BehaviorException
	{
		seekerSetup(); 
//		System.out.println(((VariablePopulationProtectionStatistics) statistics).printPeasantProtectionProportionDistribution());
		seeker.setRunLimit(limit); 
		seeker.runToEquilibriumOrLimit(); 

//		printRecords(); 
//		System.out.println(((VariablePopulationProtectionStatistics) statistics).printPeasantProtectionProportionDistribution());
	}
	@SuppressWarnings("unused")
	private void printRecords()
	{
		List<? extends StatisticsRecord> records = statistics.getStatisticsRecords(); 
		for (StatisticsRecord statisticsRecord : records)
		{
			System.out.println(statisticsRecord.toString()+" peasant adjustment: "+statisticsRecord.actorAdjustment); 
		}
	}
	public void printPeasants(List<Peasant> peasants)
	{
		for (Peasant peasant : peasants)
		{
			System.out.println(peasant+" surrendered amount: "+peasant.getUnprotectedPayoff()); 
		}
	}
}
//Peasant: 0  Protection Proportion: 0.05  Protection Function: Contest: 0.5  Payoff: 0.95  Surrendered: false surrendered amount: 0.9047619047619048
//Peasant: 1  Protection Proportion: 0.0  Protection Function: Contest: 0.5  Payoff: 1.0  Surrendered: false surrendered amount: 1.0
//Peasant: 2  Protection Proportion: 1.0  Protection Function: Contest: 0.5  Payoff: 0.0  Surrendered: false surrendered amount: 0.0
//Peasant: 3  Protection Proportion: 0.5  Protection Function: Contest: 0.5  Payoff: 0.5  Surrendered: false surrendered amount: 0.33333333333333337
//Peasant: 4  Protection Proportion: 0.9  Protection Function: Contest: 0.5  Payoff: 0.09999999999999998  Surrendered: false surrendered amount: 0.052631578947368404
//prey payoff: 1.0 peasant: Peasant: 1  Protection Proportion: 0.0  Protection Function: Contest: 0.5  Payoff: 0.0  Surrendered: true surrendered amount: 0.0
//preying: Bandit: 6  Payoff: 1.0
//prey payoff: 0.33333333333333337 peasant: Peasant: 3  Protection Proportion: 0.5  Protection Function: Contest: 0.5  Payoff: 0.16666666666666666  Surrendered: true surrendered amount: 0.0
//preying: Bandit: 8  Payoff: 0.33333333333333337
//prey payoff: 0.052631578947368404 peasant: Peasant: 4  Protection Proportion: 0.9  Protection Function: Contest: 0.5  Payoff: 0.047368421052631574  Surrendered: true surrendered amount: 0.0
//preying: Bandit: 9  Payoff: 0.052631578947368404
//prey payoff: 0.0 peasant: Peasant: 2  Protection Proportion: 1.0  Protection Function: Contest: 0.5  Payoff: 0.0  Surrendered: true surrendered amount: 0.0
//preying: Bandit: 5  Payoff: 0.0
//prey payoff: 0.9047619047619048 peasant: Peasant: 0  Protection Proportion: 0.05  Protection Function: Contest: 0.5  Payoff: 0.04523809523809523  Surrendered: true surrendered amount: 0.0
//preying: Bandit: 7  Payoff: 0.9047619047619048
