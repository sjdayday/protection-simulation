/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class DieSurviveThriveDynamicTest
{
	private DieSurviveThriveDynamic replicatorDynamic;
	private List<Peasant> peasants;
	private FitnessFunction fitnessFunction;
	private Peasant peasant;
	private List<Peasant> newPeasants;
	// equilibrium seeker sets up lists of bandit and peasants, runs til done.
	// after bandits prey on peasants, both lists passed to replicator dynamic, which returns updated lists
	// what does stats report? when are we done? 
	// fix contest function at 0.5, vary x randomly through the peasant population
	// if x sufficiently high, bandit gets little, but peasant keeps little since so much spent on defense
	// interpret contest function as a share, or as a probability of success (if the latter, how much is taken if defense fails?)
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
		peasants = new ArrayList<Peasant>(); 
		peasants.add(TestBuilder.buildPeasant(1)); 
		peasants.add(TestBuilder.buildPeasant(2)); 
		peasants.add(TestBuilder.buildPeasant(3)); 
		fitnessFunction = new FitnessFunction(); 
		replicatorDynamic = new DieSurviveThriveDynamic();
		replicatorDynamic.setFitnessFunction(fitnessFunction); 
	}
	@Test
	public void verifyNewListShrinksStaysSameGrowsAsActorsDieSurviveThrive() throws Exception
	{
		assertEquals(3, peasants.size()); 
		fitnessFunction.setSurviveThreshold(.75); 
		newPeasants = new ArrayList<Peasant>(); 
		replicatorDynamic.replicate(Peasant.class, peasants, newPeasants);  
		assertEquals("no peasants replicate because under survive threshold", 0, newPeasants.size());

		fitnessFunction.setSurviveThreshold(.25); 
		newPeasants = new ArrayList<Peasant>(); 
		replicatorDynamic.replicate(Peasant.class, peasants, newPeasants); 
		assertEquals("all peasants replicate because over survive threshold", 3, newPeasants.size());
		verifyOnePeasantInheritedValuesCorrectly(); 
		
		fitnessFunction.setThriveThreshold(.30); 
		newPeasants = new ArrayList<Peasant>(); 
		replicatorDynamic.replicate(Peasant.class, peasants, newPeasants); 
		assertEquals("all peasants replicate twice because over thrive threshold", 6, newPeasants.size());
		verifyOnePeasantInheritedValuesCorrectly(); 
	}
	@Test
	public void verifyPopulationReplicatesToZeroWithoutActorToCopyFrom() throws Exception
	{
		assertEquals(3, peasants.size()); 
		fitnessFunction.setSurviveThreshold(.75); 
		newPeasants = new ArrayList<Peasant>(); 

		List<Bandit> bandits = TestBuilder.buildBanditList();  
		List<Bandit> newBandits = new ArrayList<Bandit>(); 
		verifyTargetNumbers(bandits); 
	
		replicatorDynamic.replicate(Peasant.class, peasants, newPeasants);  
		assertEquals("no peasants replicate because under survive threshold", 0, newPeasants.size());
		
		fitnessFunction = new FitnessFunction(); 
		replicatorDynamic = new DieSurviveThriveDynamic();
		replicatorDynamic.setFitnessFunction(fitnessFunction); 
		fitnessFunction.setSurviveThreshold(0.0); 
		replicatorDynamic.replicate(Bandit.class, bandits, newBandits);  
		assertEquals(3, newBandits.size());
		verifyTargetNumbers(newBandits); 
//		ProtectionPopulation protectionPopulation = new ProtectionPopulation(banditList, peasantList); 
//		for (Dynamic dynamic : dynamics)
//		{
//			dynamic.setPopulation(protectionPopulation);
//			protectionPopulation = dynamic.rebuildPopulation(); 
//		}
//		banditList = protectionPopulation.getBandits(); 
//		peasantList = protectionPopulation.getPeasants(); 

	}
	public void verifyOnePeasantInheritedValuesCorrectly()
	{
		peasant = newPeasants.get(0);
		assertEquals(0.5, peasant.getProtectionProportion(), .001); 
		assertEquals(2, peasant.getFunction().getParameters()[PowerFunction.POWER], .001); 
		assertTrue(peasant.getFunction() instanceof PowerFunction);
	}
	@Test
	public void verifyClassThatDoesntImplementHeritableWillThrowUsefulException() throws Exception
	{
		TestingActorNotInstantiable actor = new TestingActorNotInstantiable(true); 
		List<TestingActorNotInstantiable> actors = new ArrayList<TestingActorNotInstantiable>(); 
		actors.add(actor); 
		List<TestingActorNotInstantiable> newActors = new ArrayList<TestingActorNotInstantiable>(); 
		try
		{
			replicatorDynamic.replicate(TestingActorNotInstantiable.class, actors, newActors);  
			fail("should throw"); 
		}
		catch (Exception e)
		{
			assertTrue(e.getMessage().startsWith("ReplicatorDynamic.replicate:  Exception instantiating or casting to Heritable.  Details: ")); 
		}
	}
	@Test
	public void verifyBanditBehaviorsReplicateCorrectly() throws Exception
	{
		List<Bandit> bandits = TestBuilder.buildBanditList();  
		verifyTargetNumbers(bandits); 
		fitnessFunction = new FitnessFunction(); 
		replicatorDynamic = new DieSurviveThriveDynamic();
		replicatorDynamic.setFitnessFunction(fitnessFunction); 
		fitnessFunction.setSurviveThreshold(0.0); 
		ArrayList<Bandit> newBandits = new ArrayList<Bandit>(); 
		replicatorDynamic.replicate(Bandit.class, bandits, newBandits);  
		assertEquals(3, newBandits.size());
		verifyTargetNumbers(newBandits); 
	}
	private void verifyTargetNumbers(List<Bandit> bandits)
	{
		assertEquals(6, ((BehaviorBanditPreysOnMultiplePeasants) bandits.get(0).getPredationBehavior()).getNumberOfPeasantsToPreyUpon()); 
		assertEquals(1, ((BehaviorBanditPreysOnMultiplePeasants) bandits.get(1).getPredationBehavior()).getNumberOfPeasantsToPreyUpon()); 
		assertEquals(4, ((BehaviorBanditPreysOnMultiplePeasants) bandits.get(2).getPredationBehavior()).getNumberOfPeasantsToPreyUpon()); 
		
	}
}
