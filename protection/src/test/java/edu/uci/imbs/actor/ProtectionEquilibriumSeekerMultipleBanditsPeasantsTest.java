/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ProtectionEquilibriumSeekerMultipleBanditsPeasantsTest
{
	private List<Peasant> peasants;
	private List<Bandit> bandits;
	private FitnessFunction fitnessFunction;
	private boolean roleShifting;
	private MultipleBehaviorInteractionPattern pattern;
	private VariablePopulationProtectionStatistics statistics;
	private ProtectionEquilibriumSeekerMultipleBanditsPeasants seeker;
	private DieSurviveThriveDynamic dstDynamic;
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
		roleShifting = false; 
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(10, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(3); 
		fitnessFunction = new FitnessFunction(); 
		seekerSetup(); 
	}
	public void seekerSetup()
	{
		pattern = new MultipleBehaviorInteractionPattern(bandits, peasants); 
		statistics = new VariablePopulationProtectionStatistics(bandits, peasants, 21, .05); 
		statistics.setPayoffDiscrepancyTolerance(.005);
		statistics.setAdjustmentFactorPercentage(0.1); 
		seeker = new ProtectionEquilibriumSeekerMultipleBanditsPeasants(); 
		seeker.setPeasantList(peasants); 
		seeker.setBanditList(bandits); 
		seeker.setMultipleBehaviorInteractionPattern(pattern); 
		seeker.setProtectionStatistics(statistics);
		dstDynamic = new DieSurviveThriveDynamic();
		dstDynamic.setFitnessFunction(fitnessFunction); 
		seeker.addDynamic(new ProtectionReplicatorDynamic(dstDynamic)); 
		if (roleShifting) seeker.addDynamic(new RoleShiftingReplicatorDynamic(statistics)); 
	}
	@Test
	public void verifyMultiplePeasantsAndBanditsReplicate() throws Exception
	{
		fitnessFunction.setSurviveThreshold(0.1); 
		fitnessFunction.setThriveThreshold(0.9); 
//		assertEquals(peasants, seeker.getPeasantList()); 
//		assertEquals(bandits, seeker.getBanditList());
		seeker.setRunLimit(2); 
		seeker.runToEquilibriumOrLimit(); 
//		for (Bandit bandit : seeker.getBanditList()) {
//			System.out.println(bandit);
//		}
//		for (Peasant peasant : seeker.getPeasantList()) {
//			System.out.println(peasant);
//		}
		assertEquals(2, seeker.getProtectionStatistics().getStatisticsRecords().size()); 
		assertEquals(3, seeker.getProtectionStatistics().numberPeriods()); 
		assertEquals(6, seeker.getBanditList().size());
		assertEquals(4, seeker.getPeasantList().size()); 
		StatisticsRecord record = seeker.getProtectionStatistics().getStatisticsRecords().get(0);
		assertEquals(3, record.numberBandits); 
		assertEquals(10, record.numberPeasants); 
		assertEquals(5, record.numberBanditsAfterReplication); 
		assertEquals(7, record.numberPeasantsAfterReplication); 
		record = seeker.getProtectionStatistics().getStatisticsRecords().get(1);
		assertEquals(5, record.numberBandits); 
		assertEquals(7, record.numberPeasants); 
		assertEquals(6, record.numberBanditsAfterReplication); 
		assertEquals(4, record.numberPeasantsAfterReplication); 
//		assertNotSame(peasants, seeker.getPeasantList()); 
//		assertNotSame(bandits, seeker.getBanditList());
//  From MultipleBehaviorInteractionPatternTest		
//		assertEquals(1.349, bandits.get(0).getPayoff(), .001);  thrive:  y = .166
//		assertEquals(0.857, bandits.get(1).getPayoff(), .001); 
//		assertEquals(0.994, bandits.get(2).getPayoff(), .001);  thrive   y = .25
//		assertEquals(0.95, peasants.get(0).getPayoff(), .001);  thrive   x = .05
//		assertEquals(0.0, peasants.get(1).getPayoff(), .001);  die 
//		assertEquals(0.0, peasants.get(2).getPayoff(), .001);  die
//		assertEquals(0.5, peasants.get(3).getPayoff(), .001);  
//		assertEquals(0.068, peasants.get(4).getPayoff(), .001); die
//		assertEquals(0.649, peasants.get(5).getPayoff(), .001); 
//		assertEquals(0.0, peasants.get(6).getPayoff(), .001);  die
//		assertEquals(0.158, peasants.get(7).getPayoff(), .001); 
//		assertEquals(0.278, peasants.get(8).getPayoff(), .001); 
//		assertEquals(0.293, peasants.get(9).getPayoff(), .001); 
//		10 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - About to replicate actors
//		10 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to thrive: Peasant: 0  Protection Proportion: 0.05  Protection Function: Contest: gamma=0.5 weight=1.0  Payoff: 0.95  Unprotected Payoff: 0.9047619047619048  Surrendered Payoff: 0.0  Surrendered: false
//		11 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to die: Peasant: 1  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=1.1666666666666667  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true
//		11 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to die: Peasant: 2  Protection Proportion: 1.0  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 0.0  Surrendered: true
//		11 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to survive: Peasant: 3  Protection Proportion: 0.5  Protection Function: Contest: gamma=0.5 weight=1.0  Payoff: 0.5  Unprotected Payoff: 0.33333333333333337  Surrendered Payoff: 0.0  Surrendered: false
//		11 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to die: Peasant: 4  Protection Proportion: 0.9  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.06835443037974683  Unprotected Payoff: 0.0  Surrendered Payoff: 0.03164556962025315  Surrendered: true
//		11 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to survive: Peasant: 5  Protection Proportion: 0.35000000000000003  Protection Function: Contest: gamma=0.5 weight=1.0  Payoff: 0.6499999999999999  Unprotected Payoff: 0.4814814814814814  Surrendered Payoff: 0.0  Surrendered: false
//		12 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to die: Peasant: 6  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true
//		12 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to survive: Peasant: 7  Protection Proportion: 0.05  Protection Function: Contest: gamma=0.5 weight=0.25  Payoff: 0.15833333333333335  Unprotected Payoff: 0.0  Surrendered Payoff: 0.7916666666666666  Surrendered: true
//		12 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to survive: Peasant: 8  Protection Proportion: 0.65  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.2785714285714285  Unprotected Payoff: 0.0  Surrendered Payoff: 0.07142857142857145  Surrendered: true
//		12 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to survive: Peasant: 9  Protection Proportion: 0.4  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.2938775510204082  Unprotected Payoff: 0.0  Surrendered Payoff: 0.3061224489795918  Surrendered: true
//		12 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - About to replicate actors
//		13 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to thrive: Bandit: 10  Payoff: 1.3493929217256524
//		13 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to survive: Bandit: 11  Payoff: 0.8571428571428571
//		13 [main] DEBUG edu.uci.imbs.actor.ReplicatorDynamic  - Actor about to thrive: Bandit: 12  Payoff: 0.9943274778265736
	}
}
