/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class BanditTest
{
	private Bandit bandit;
	private Peasant peasant;
	private PredationBehavior behavior;
	
	@BeforeClass
	public static void setUpLog4J() throws Exception
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
	}
	
	@Before
	public void setUp() throws Exception
	{
		peasant = new Peasant(); 
		peasant.setProtectionProportion(0.5);
		peasant.setFunction(ProtectionFunctionEnum.POWER.buildFunction(2));  
		bandit = new Bandit(); 
		ProtectionParameters.resetForTesting();
	}
	@Test
	public void verifyBanditPreysOnPeasantReceivingUnprotectedRemainderOfWorkProportion() throws Exception
	{
		assertEquals("peasant's work proportion (1-x) 0.5 - peasant's payoff (p(x)) 0.125; [1 - p(x)](1-x) ",0.375, bandit.prey(peasant), .001); 
		assertEquals(0.375, bandit.getPayoff(), .001); 
	}
	@Test
	public void verifyTickResetsPayoffsInPreparationForNextPeriod() throws Exception
	{
		assertEquals(0, bandit.getPayoff(), .001); 
		bandit.prey(peasant);
		assertEquals(0.375, bandit.getPayoff(), .001); 
		bandit.tick(); 
		assertEquals(0, bandit.getPayoff(), .001); 
	}
	@Test
	public void verifyTickResetsBehaviors() throws Exception
	{
		behavior = bandit.getPredationBehavior();
		bandit.tick(); 
		assertNotSame(bandit.getPredationBehavior(), behavior);
	}

	@Test
	public void verifyBanditPreysOnPeasantWithContestFunction() throws Exception
	{
		peasant.setFunction(ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); 
		assertEquals("unprotected payoff under Contest = .333", .333, bandit.prey(peasant), .001 ); 
	}
	@Test
	public void verifyBanditHasPredatoryBehavior() throws Exception
	{
		assertEquals(0, bandit.getBehaviors().size());
		behavior = bandit.getPredationBehavior(); 
		assertTrue(behavior instanceof BehaviorBanditPreysOnMultiplePeasants);
		assertEquals(1, bandit.getBehaviors().size());
		assertEquals(behavior, bandit.getBehaviors().get(0));
	}
	@Test
	public void verifyBanditInheritsPredationBehavior() throws Exception
	{
		behavior = bandit.getPredationBehavior(); 
		behavior.addTarget(new TestingProtectionBehavior(0.5));
		assertEquals(1, behavior.getTargets().size()); 
		assertEquals(6, ((BehaviorBanditPreysOnMultiplePeasants) behavior).getNumberOfPeasantsToPreyUpon()); 
		Bandit newBandit = new Bandit(); 
		assertEquals(0, newBandit.getBehaviors().size());
		PredationBehavior newBehavior = newBandit.getPredationBehavior(); 
		assertEquals(1, newBandit.getBehaviors().size());
		assertNotSame(behavior, newBehavior);
		assertEquals("randomly assigned new number of targets",1, ((BehaviorBanditPreysOnMultiplePeasants) newBehavior).getNumberOfPeasantsToPreyUpon()); 
		assertEquals("new behavior will have no new targets",0, newBehavior.getTargets().size()); 
		newBandit.inherit(bandit);
		assertEquals("now inherit number of targets from old bandit's behavior",6, ((BehaviorBanditPreysOnMultiplePeasants) newBehavior).getNumberOfPeasantsToPreyUpon()); 
		assertEquals("...but not the old behavior's targets",0, newBehavior.getTargets().size()); 
	}
}

