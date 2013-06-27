/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

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
	public void verifyHeadingsReturnedForPeasantProportionNumberAndPercentages() throws Exception
	{
		assertEquals(42, statistics.getPeasantProportionHeadings().size()); 
		assertEquals("X0.0#", statistics.getPeasantProportionHeadings().get(0));
		assertEquals("X0.0%", statistics.getPeasantProportionHeadings().get(1));
		assertEquals("X1.0#", statistics.getPeasantProportionHeadings().get(40));
		assertEquals("X1.0%", statistics.getPeasantProportionHeadings().get(41));
	}
	@Test
	public void verifyPeasantProportionEntriesList() throws Exception
	{
		assertEquals(21,statistics.getPeasantProportionRecordEntries().size()); 
		checkPeasantProportionRecordEntry(0,"X0.0#",2,"X0.0%",0.2); 
		checkPeasantProportionRecordEntry(1,"X0.05#",2,"X0.05%",0.2); 
		checkPeasantProportionRecordEntry(2,"X0.1#",0,"X0.1%",0.0); 
		checkPeasantProportionRecordEntry(20,"X1.0#",1,"X1.0%",0.1); 
	}
	private void checkPeasantProportionRecordEntry(int index, String numberHeading, int number,
			String proportionHeading, double proportion)
	{
		PeasantProportionRecordEntry entry = statistics.getPeasantProportionRecordEntries().get(index); 
		assertEquals(numberHeading, entry.numberHeading); 
		assertEquals(number, entry.number); 
		assertEquals(proportionHeading, entry.proportionHeading); 
		assertEquals(proportion, entry.proportion, .001); 
		
	}
	@Test
	public void verifyStatisticsRecordHasProportionEntries() throws Exception
	{
		statistics.tick(); 
		assertEquals(21,statistics.getVariableStatisticsRecords().get(0).protectionProportions.size());
	}
	@Test
	public void verifyStatisticsGatheredForProtectionProportionDistribution() throws Exception
	{
		assertEquals(.39, statistics.averagePeasantProtectionProportion(), .001);
		assertEquals("if multiple bins have same number of entries, the first bin is returned as mode",
				0.0, statistics.modePeasantProtectionProportion(), .001);
		assertEquals(.375, statistics.medianPeasantProtectionProportion(), .001);
		assertEquals(""+Constants.CRLF +
					 "0.0 : xx"+Constants.CRLF+
				     "0.05: xx"+Constants.CRLF+
				     "0.1 : "+Constants.CRLF+
				     "0.15: "+Constants.CRLF+
				     "0.2 : "+Constants.CRLF+
				     "0.25: "+Constants.CRLF+
				     "0.3 : "+Constants.CRLF+
				     "0.35: x"+Constants.CRLF+
				     "0.4 : x"+Constants.CRLF+
				     "0.45: "+Constants.CRLF+
				     "0.5 : x"+Constants.CRLF+
				     "0.55: "+Constants.CRLF+
				     "0.6 : "+Constants.CRLF+
				     "0.65: x"+Constants.CRLF+
				     "0.7 : "+Constants.CRLF+
				     "0.75: "+Constants.CRLF+
				     "0.8 : "+Constants.CRLF+
				     "0.85: "+Constants.CRLF+
				     "0.9 : x"+Constants.CRLF+
				     "0.95: "+Constants.CRLF+
				     "1.0 : x"+Constants.CRLF, statistics.printPeasantProtectionProportionDistribution());
	}
	@Test
	public void verifyStatisticsGatheredForPredationEffortDistribution() throws Exception
	{
		assertEquals(3.4, statistics.averageBanditNumberPeasantsToPreyUpon(), .001);
		assertEquals(4, statistics.medianBanditNumberPeasantsToPreyUpon(), .001);
		assertEquals(1, statistics.modeBanditNumberPeasantsToPreyUpon());
		assertEquals(""+Constants.CRLF +
				"1 : yy"+Constants.CRLF+
				"2 : "+Constants.CRLF+
				"3 : "+Constants.CRLF+
				"4 : y"+Constants.CRLF+
				"5 : y"+Constants.CRLF+
				"6 : y"+Constants.CRLF+
				"7 : "+Constants.CRLF+
				"8 : "+Constants.CRLF+
				"9 : "+Constants.CRLF+
				"10 : "+Constants.CRLF, statistics.printBanditPredationEffortDistribution());
	}
//	@Test
	public void verifyBanditDistributionsUpdatedIfBanditPopulationUpdated() throws Exception
	{
//TODO verifyBanditDistributionsUpdatedIfBanditPopulationUpdated do we need this? 
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
			fail("should not throw: "+e.getMessage());
		}
	}
}
