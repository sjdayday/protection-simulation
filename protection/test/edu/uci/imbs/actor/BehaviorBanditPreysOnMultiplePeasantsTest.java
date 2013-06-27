/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class BehaviorBanditPreysOnMultiplePeasantsTest
{
	private Bandit bandit;
	private BehaviorBanditPreysOnMultiplePeasants behavior;
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
		bandit = new Bandit();
		behavior = (BehaviorBanditPreysOnMultiplePeasants) BehaviorEnum.BANDIT_PREYS_ON_MULTIPLE_PEASANTS.build(bandit); 
	}
	@Test
	public void verifyBehaviorAssociatedWithBandit() throws Exception
	{
		assertEquals(BehaviorEnum.BANDIT_PREYS_ON_MULTIPLE_PEASANTS, behavior.getType());
		bandit.addBehavior(behavior); 
		assertEquals(bandit, behavior.getActor()); 
	}
	@Test
	public void verifyBanditPreysOnUpToTenPeasantsDividingEffortProportionally() throws Exception
	{
		assertEquals(6, behavior.getNumberOfPeasantsToPreyUpon()); 
		PredationBehavior predationBehavior = (PredationBehavior) behavior;
		assertEquals(.166, predationBehavior.getPredationEffort(), .001);
		List<ProtectionBehavior> behaviors = buildSixProtectionBehaviors();  
		for (ProtectionBehavior protectionBehavior : behaviors)
		{
			behavior.addTarget(protectionBehavior); 
			assertTrue(((TestingProtectionBehavior) protectionBehavior).getThreatAdded()); 
		}
		PredationOutcome outcome = behavior.preyOnTargets(); 
		assertEquals(2.1, outcome.getPayoff(), .001); 
	}
	@Test
	public void verifyPredationBehaviorUpdatesBanditBayoff() throws Exception
	{
		addSixTargetProtectionBehaviors(); 
		behavior.preyOnTargets();
		assertEquals(2.1, bandit.getPayoff(), .001); 
	}
	@Test
	public void verifyPredationCostsProportionatelyMoreAsNumberOfPeasantsIncreases() throws Exception
	{
		ProtectionParameters.COST_TO_PREY_ON_SINGLE_PEASANT = .01; 
		addSixTargetProtectionBehaviors(); 
		behavior.preyOnTargets();
		assertEquals("2.1 -6c = 2.1-.06 = 2.04 c=.01",2.04, bandit.getPayoff(), .001); 
	}
	@Test
	public void verifyPredationCostsCantDriveTotalPayoffBelowZero() throws Exception
	{
		ProtectionParameters.COST_TO_PREY_ON_SINGLE_PEASANT = .5; 
		addSixTargetProtectionBehaviors(); 
		behavior.preyOnTargets();
		assertEquals("2.1 -6c = 2.1-3.0 = -.9 forced to 0 c=.5",0, bandit.getPayoff(), .001); 
	}
	@Test
	public void verifyTickResetsTargets() throws Exception
	{
		addSixTargetProtectionBehaviors(); 
		assertEquals(6, behavior.getTargets().size());
		behavior.tick(); 
		assertEquals(0, behavior.getTargets().size()); 
	}
	private void addSixTargetProtectionBehaviors()
	{
		List<ProtectionBehavior> behaviors = buildSixProtectionBehaviors();  
		for (ProtectionBehavior protectionBehavior : behaviors)
		{
			behavior.addTarget(protectionBehavior); 
		}
	}
	private List<ProtectionBehavior> buildSixProtectionBehaviors()
	{
		List<ProtectionBehavior> behaviors = new ArrayList<ProtectionBehavior>();
		behaviors.add(new TestingProtectionBehavior(.1)); 
		behaviors.add(new TestingProtectionBehavior(.2)); 
		behaviors.add(new TestingProtectionBehavior(.3)); 
		behaviors.add(new TestingProtectionBehavior(.4)); 
		behaviors.add(new TestingProtectionBehavior(.5)); 
		behaviors.add(new TestingProtectionBehavior(.6)); 
		
		return behaviors;
	}
}
