/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;



public class ProtectionReplicatorDynamicTest
{
	private FitnessFunction fitnessFunction;
	private DieSurviveThriveDynamic dstDynamic;
	private ProtectionPopulation protectionPopulation;
	@Before
	public void setUp() throws Exception
	{
		fitnessFunction = new FitnessFunction(); 
		fitnessFunction.setSurviveThreshold(.25); 
		fitnessFunction.setThriveThreshold(.30); 
		dstDynamic = new DieSurviveThriveDynamic();
		dstDynamic.setFitnessFunction(fitnessFunction); 
		protectionPopulation = new ProtectionPopulation(TestBuilder.buildBanditList(), TestBuilder.buildPeasantList());
	}
	@Test
	public void verifyNewPopulationGeneratedWithExpectedLevelsOfPeasantsAndBandits() throws Exception
	{
		Dynamic dynamic = new ProtectionReplicatorDynamic(dstDynamic);
		dynamic.setPopulation(protectionPopulation); 
		protectionPopulation = dynamic.rebuildPopulation(); 
		assertEquals("was 3, but all dropped",0, protectionPopulation.getBandits().size());
		assertEquals("was 4, but all thrived",8, protectionPopulation.getPeasants().size());
	}
}

//newPeasants = new ArrayList<Peasant>(); 
//replicatorDynamic.replicate(Peasant.class, peasants, newPeasants); 
//assertEquals("all peasants replicate because over survive threshold", 3, newPeasants.size());
//verifyOnePeasantInheritedValuesCorrectly(); 
//
//newPeasants = new ArrayList<Peasant>(); 
//replicatorDynamic.replicate(Peasant.class, peasants, newPeasants); 
//assertEquals("all peasants replicate twice because over thrive threshold", 6, newPeasants.size());
//verifyOnePeasantInheritedValuesCorrectly(); 

//pattern = TestBuilder.buildPermutedRepeatableInteractionPattern(bandits, peasants); 
//statistics = new VariablePopulationProtectionStatistics(bandits, peasants, 21, .05); 
//statistics.setPayoffDiscrepancyTolerance(.03);
//seeker = new VariablePopulationProtectionEquilibriumSeeker(); 
//seeker.setPeasantList(peasants); 
//seeker.setBanditList(bandits); 
//seeker.setInteractionPattern(pattern); 
//seeker.setReplicatorDynamic(replicatorDynamic); 
