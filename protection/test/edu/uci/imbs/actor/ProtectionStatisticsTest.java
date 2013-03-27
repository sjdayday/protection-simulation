package edu.uci.imbs.actor;

import static org.junit.Assert.*; 

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ProtectionStatisticsTest
{
	
	private List<Peasant> peasants;
	private List<Bandit> bandits;
	private InteractionPattern<Bandit, Peasant> pattern;
	private ProtectionStatistics statistics;
	@Before
	public void setUp() throws Exception
	{
		peasants = TestBuilder.buildPeasantList(); 
		bandits = TestBuilder.buildBanditList(); 
		pattern = TestBuilder.buildPermutedRepeatableInteractionPattern(bandits, peasants); 
		statistics = new ProtectionStatistics(bandits, peasants); 
	}
	@Test
	public void verifyAveragePayoffsBeforeAndAfterInteractions() throws Exception
	{
		assertEquals(4, statistics.numberPeasants()); 
		assertEquals(0.5, statistics.averagePeasantPayoff(), .001); 
		assertEquals(3, statistics.numberBandits()); 
		assertEquals(0, statistics.averageBanditPayoff(), .001); 
		banditsPreyOnPeasants();
		statistics.tick(); 
		assertEquals("3 have protected payoffs of .125; 4th was not preyed on, still has payoff .5; total .875 / 4",0.218, statistics.averagePeasantPayoff(), .001); 
		assertEquals("3 have payoff .375",.375, statistics.averageBanditPayoff(), .001); 
	}
	@Test
	public void verifyAdjustmentFactorCalculatedAsPercentageOfPopulationSize() throws Exception
	{
		
		assertEquals("factor not set defaults to 1", 1, statistics.getAdjustmentFactor()); 
		verifyExpectedErrorReceived("numbers out of percentage range forced to 1", -.5d); 
		verifyExpectedErrorReceived("numbers out of percentage range forced to 1", 1.5d); 
		statistics.setAdjustmentFactorPercentage(0);
		assertEquals("zero forced to 1", 1, statistics.getAdjustmentFactor()); 
		statistics.setAdjustmentFactorPercentage(.1);
		assertEquals("low numbers rounded up to 1",1, statistics.getAdjustmentFactor()); 
		statistics.setAdjustmentFactorPercentage(.25);
		assertEquals("1 of 4 = 1",1, statistics.getAdjustmentFactor()); 
		statistics.setAdjustmentFactorPercentage(.49);
		assertEquals("1.99 of 4 = 1",1, statistics.getAdjustmentFactor()); 
		statistics.setAdjustmentFactorPercentage(.5);
		assertEquals("2 of 4 = 2",2, statistics.getAdjustmentFactor()); 
		statistics.setAdjustmentFactorPercentage(.99);
		assertEquals("3.99 of 4 = 3",3, statistics.getAdjustmentFactor()); 
		statistics.setAdjustmentFactorPercentage(1.0);
		assertEquals("4 of 4 = 4",4, statistics.getAdjustmentFactor()); 
	}
	private void verifyExpectedErrorReceived(String msg, double percentage)
	{
		try 
		{
			statistics.setAdjustmentFactorPercentage(percentage);
			fail("should throw"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(msg, e.getMessage().startsWith("ProtectionStatistics.setAdjustmentFactorPercentage:  valid percentages are from 0.0 to 1.0.  Received: "));
		}
	}
	@Test
	public void verifyAdjustmentToNumberOfPeasantsIfAveragePayoffDiscrepanciesOutsideOfDiscrepancyTolerance() throws Exception
	{
		statistics.setPayoffDiscrepancyTolerance(.01);
		banditsPreyOnPeasants();
		statistics.tick(); 
		assertEquals("average bandit payoff is higher", 1, statistics.getPeasantAdjustment());
		adjustPeasantsAndBanditsInOppositeDirectionsAndTick(1); 
		assertEquals(2, bandits.size());
		assertEquals(5, peasants.size());
		banditsPreyOnPeasants();
		statistics.tick(); 
		assertEquals("2 have .125, 3 have .5, total 1.75 / 5", .35, statistics.averagePeasantPayoff(), .001);
		assertEquals("2 have payoff .375",.375, statistics.averageBanditPayoff(), .001); 
		assertEquals("average bandit payoff is higher", 1, statistics.getPeasantAdjustment());
		statistics.setPayoffDiscrepancyTolerance(.03);
		statistics.calculateForTesting(); 
		assertEquals("average bandit payoff is higher but within the new tolerance", 0, statistics.getPeasantAdjustment());
		adjustPeasantsAndBanditsInOppositeDirectionsAndTick(1); 
		assertEquals(1, bandits.size());
		assertEquals(6, peasants.size());
		banditsPreyOnPeasants();
		statistics.tick(); 
		assertEquals("1 have .125, 5 have .5, total 2.625 / 6", .437, statistics.averagePeasantPayoff(), .001);
		assertEquals("1 have payoff .375",.375, statistics.averageBanditPayoff(), .001); 
		assertEquals("average peasant payoff is higher", -1, statistics.getPeasantAdjustment());
	}
	@Test
	public void verifyOneStatisticsRecordAccumulatedPerTimePeriod() throws Exception
	{
		assertEquals(1, statistics.numberPeriods()); 
		assertEquals(0, statistics.getStatisticsRecords().size()); 
		statistics.setPayoffDiscrepancyTolerance(.01);
		banditsPreyOnPeasants();
		statistics.tick(); 
		assertEquals(2, statistics.numberPeriods()); 
		assertEquals(1, statistics.getStatisticsRecords().size()); 
		StatisticsRecord record = statistics.getStatisticsRecords().get(0); 
		assertEquals(1, record.period); 
		assertEquals(3, record.numberBandits); 
		assertEquals(4, record.numberPeasants); 
		assertEquals(0.375, record.averageBanditPayoff, .001); 
		assertEquals(0.218, record.averagePeasantPayoff, .001); 
		assertEquals(1, record.peasantAdjustment); 
		adjustPeasantsAndBanditsInOppositeDirectionsAndTick(statistics.getPeasantAdjustment()); 
		banditsPreyOnPeasants();
		statistics.tick(); 
		assertEquals(3, statistics.numberPeriods()); 
		assertEquals(2, statistics.getStatisticsRecords().size()); 
		record = statistics.getStatisticsRecords().get(1); 
		assertEquals(2, record.period); 
		assertEquals(2, record.numberBandits); 
		assertEquals(5, record.numberPeasants); 
		assertEquals(0.375, record.averageBanditPayoff, .001); 
		assertEquals(0.35, record.averagePeasantPayoff, .001); 
		assertEquals(1, record.peasantAdjustment); 
	}
	@Test
	public void verifyStatisticsRecordPrinting() throws Exception
	{
		statistics.setPayoffDiscrepancyTolerance(.01);
		banditsPreyOnPeasants();
		statistics.tick(); 
		assertEquals("Period=1, Number Bandits=3, Number Peasants=4, Number Bandits After Replication=0, Number Peasants After Replication=0, Average Bandit Payoff=0.375, Average Peasant Payoff=0.21875, Bandit-Peasant Payoff Delta=0.15625, Peasant Adjustment=1",
				statistics.getStatisticsRecords().get(0).toString());
	}
	@Test
	public void verifyDivisionByZeroAvoidedDuringCalculation() throws Exception
	{
		peasants = new ArrayList<Peasant>(); 
		bandits = new ArrayList<Bandit>(); 
//		pattern = TestBuilder.buildPermutedRepeatableInteractionPattern(bandits, peasants); 
//		statistics = new ProtectionStatistics(pattern); 
		statistics = new ProtectionStatistics(bandits, peasants); 
		assertEquals(0, statistics.averageBanditPayoff, .001); 
		assertEquals(0, statistics.averagePeasantPayoff, .001); 
		
	}
	private void adjustPeasantsAndBanditsInOppositeDirectionsAndTick(int adjust)
	{
		if (adjust == 1)
		{
			bandits.remove(0); 
			peasants.add(TestBuilder.buildPeasant(peasants.size()+1));
		}
		pattern = new InteractionPattern<Bandit, Peasant>(pattern);
//		pattern.reset();
		for (Actor peasant : peasants)
		{
			peasant.tick();
		}
		for (Bandit bandit: bandits)
		{
			bandit.tick();
		}
	}
	private void banditsPreyOnPeasants()
	{
		InteractionPair<Bandit, Peasant> pair = null;
		while (pattern.hasNext())
		{
			pair = pattern.next();
			pair.getSource().prey(pair.getTarget()); 
		}
	}
}

//average will be affected by number of preyed on peasants vs. bandits.  
//distribution of protection proportions (across both preyed-on and not preyed-on) 
//what defines equilibrium: payoffs same
//define bins in a map, number of occurrences in each bin gives mode, median, average, could also graph 
//suggests one statistics per equilibrium seekers
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

//post interaction: 
//Peasant: 1  Protection Proportion: 0.0  Protection Function: Contest: 0.5  Payoff: 0.0  Surrendered: true surrendered amount: 0.0
//Peasant: 3  Protection Proportion: 0.5  Protection Function: Contest: 0.5  Payoff: 0.16666666666666666  Surrendered: true surrendered amount: 0.0
//Peasant: 4  Protection Proportion: 0.9  Protection Function: Contest: 0.5  Payoff: 0.047368421052631574  Surrendered: true surrendered amount: 0.0
//Peasant: 2  Protection Proportion: 1.0  Protection Function: Contest: 0.5  Payoff: 0.0  Surrendered: true surrendered amount: 0.0
//Peasant: 0  Protection Proportion: 0.05  Protection Function: Contest: 0.5  Payoff: 0.04523809523809523  Surrendered: true surrendered amount: 0.0
