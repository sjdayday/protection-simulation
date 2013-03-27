package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class VariablePopulationProtectionStatisticsTest
{
	private VariablePopulationProtectionStatistics statistics;
	private List<Peasant> peasants;
	private List<Bandit> bandits;
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
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(10, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); // only one function for all peasants, initially 
		bandits = TestBuilder.buildBanditList(5); 
		statistics = new VariablePopulationProtectionStatistics(bandits, peasants, 21, 0.05); 
	}
	@Test
	public void verifyStatisticsGatheredForProtectionProportionDistribution() throws Exception
	{
		assertEquals(.39, statistics.averagePeasantProtectionProportion(), .001);
		assertEquals("if multiple bins have same number of entries, the first bin is returned as mode",
				0.0, statistics.modePeasantProtectionProportion(), .001);
		assertEquals(.375, statistics.medianPeasantProtectionProportion(), .001);
		assertEquals("\n" +
					 "0.0 : xx\n"+
				     "0.05: xx\n"+
				     "0.1 : \n"+
				     "0.15: \n"+
				     "0.2 : \n"+
				     "0.25: \n"+
				     "0.3 : \n"+
				     "0.35: x\n"+
				     "0.4 : x\n"+
				     "0.45: \n"+
				     "0.5 : x\n"+
				     "0.55: \n"+
				     "0.6 : \n"+
				     "0.65: x\n"+
				     "0.7 : \n"+
				     "0.75: \n"+
				     "0.8 : \n"+
				     "0.85: \n"+
				     "0.9 : x\n"+
				     "0.95: \n"+
				     "1.0 : x\n", statistics.printPeasantProtectionProportionDistribution());
	}
	@Test
	public void verifyStatisticsGatheredForPredationEffortDistribution() throws Exception
	{
		assertEquals(3.4, statistics.averageBanditNumberPeasantsToPreyUpon(), .001);
		assertEquals(4, statistics.medianBanditNumberPeasantsToPreyUpon(), .001);
		assertEquals(1, statistics.modeBanditNumberPeasantsToPreyUpon());
		assertEquals("\n" +
				"1 : yy\n"+
				"2 : \n"+
				"3 : \n"+
				"4 : y\n"+
				"5 : y\n"+
				"6 : y\n"+
				"7 : \n"+
				"8 : \n"+
				"9 : \n"+
				"10 : \n", statistics.printBanditPredationEffortDistribution());
	}
	@Test
	public void verifyBanditDistributionsUpdatedIfBanditPopulationUpdated() throws Exception
	{
//		fail(); 
	}
	@Test
	public void verifyStatisticsRecordContainsVariablePopulationFields() throws Exception
	{
		assertEquals(0, statistics.getStatisticsRecords().size()); 
		statistics.buildStatisticsRecord(); 
		assertEquals(1, statistics.getStatisticsRecords().size()); 
		VariablePopulationStatisticsRecord record = (VariablePopulationStatisticsRecord) statistics.getStatisticsRecords().get(0); 
		assertEquals(10, record.numberPeasants); 
		assertEquals(0.39, record.averagePeasantProtectionProportion, .001); 
		assertEquals(0.375, record.medianPeasantProtectionProportion, .001); 
	}
	@Test
	public void verifyDivisionByZeroAvoided() throws Exception
	{
		try
		{
			peasants = new ArrayList<Peasant>();  
			bandits = new ArrayList<Bandit>(); 
			statistics = new VariablePopulationProtectionStatistics(bandits, peasants, 21, 0.05); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("should not throw: "+e.getMessage());
		}
		
	}
}
