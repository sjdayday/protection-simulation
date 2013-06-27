/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class BehaviorPeasantDefendsAgainstMultipleBanditsTest
{
	private ProtectionBehavior protectionBehavior;
	private Peasant peasant;

	@Before
	public void setUp() throws Exception
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR); 
		peasant = new Peasant(); 
		peasant.setProtectionProportion(0.5); 
		peasant.setFunction(ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); 
		protectionBehavior = (ProtectionBehavior) BehaviorEnum.PEASANT_DEFENDS_AGAINST_MULTIPLE_BANDITS.build(peasant); 

	}
	
	@Test
	public void verifyAccumulatesMultipleAttackers() throws Exception
	{
		assertEquals(0, protectionBehavior.getThreatMap().size()); 
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		assertEquals(3, protectionBehavior.getThreatMap().size()); 
	}
	@Test
	public void verifyActorMustBeSetAsPeasantAtInitialization() throws Exception
	{
		checkThrowsIfNotInitializedWithPeasant(null, "null");
		checkThrowsIfNotInitializedWithPeasant(new Actor(), "edu.uci.imbs.actor.Actor");
	}
	@Test
	public void verifyPredationUpdatesPeasantsContestFunctionWeightReducingPeasantPayoff() throws Exception
	{
		assertEquals("default weight is 1",1, peasant.getFunction().getParameters()[ContestFunction.WEIGHT], .001); 
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		assertEquals(0.25, peasant.getFunction().getParameters()[ContestFunction.WEIGHT], .001); 
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		assertEquals(0.75, peasant.getFunction().getParameters()[ContestFunction.WEIGHT], .001); 
	}
	@Test
	public void verifyMultiplePredatorsReducePeasantsPayoffAndPayoffAvailableThroughProtectionOutcome() throws Exception
	{
		assertEquals("peasant's work proportion (1-x) 0.5 - peasant's protection (p(x)) 0.166; equivalently [1 - p(x)](1-x)",0.333, peasant.getUnprotectedPayoff(), .001); 
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		assertEquals("weight changed from default 1 to .25:  p(x)=.5/.5+.25 = .666 * (1-x) = .333 (peasant's protected payoff)"+
				"+ [1 - p(x)](1-x)=.333*.5=.166 (peasant's unprotected payoff); total payoff = protected + unprotected = .5",0.5, peasant.getPayoff(), .001); 
		assertEquals("peasant's work proportion (1-x) 0.5 - peasant's protection (p(x)) 0.333; equivalently [1 - p(x)](1-x) ",0.166, peasant.getUnprotectedPayoff(), .001); 
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		assertEquals("weight changed .25 to .5:  p(x)=.5/.5+.5 = .5 * (1-x) = .250 (peasant's protected payoff)"+
				"+ [1 - p(x)](1-x)=.5*.5=.250 (peasant's unprotected payoff); total payoff = protected + unprotected = .5" +
				"still the same total payoff, but proportion of protected to unprotected has changed.",0.5, peasant.getPayoff(), .001); 
		assertEquals("peasant's work proportion (1-x) 0.5 - peasant's protection (p(x)) 0.250; equivalently [1 - p(x)](1-x) ",0.250, peasant.getUnprotectedPayoff(), .001); 
		
	}
	@Test
	public void verifyUnprotectedPayoutIsDistributedToPredatorsInProportionToTheirEffortAndOnceOnlyPerPredator() throws Exception
	{
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.50));  
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.1));  
		protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 1));  
		assertEquals("weight 1.85:  p(x)=.5/.5+1.85 = .2127 * (1-x) = .106 (peasant's protected payoff)"+
				"+ [1 - p(x)](1-x)=.5*.5=.393 (peasant's unprotected payoff); total payoff = protected + unprotected = 5",0.5, peasant.getPayoff(), .001); 
		assertEquals(0.393, peasant.getUnprotectedPayoff(), .001); 
		PredationOutcome outcome = null;  
		PredationBehavior predationBehavior = null; 
		Map<Double, Double> payoffsForEffort = new HashMap<Double, Double>(); 
		PredationBehavior[] behaviors = new PredationBehavior[protectionBehavior.getThreatMap().size()];
		protectionBehavior.getThreatMap().keySet().toArray(behaviors); // avoids concurrent modification trying to iterate over the keySet
		for (int i = 0; i < behaviors.length; i++)
		{
			predationBehavior = behaviors[i];  
			outcome = predationBehavior.preyOnTargets(); 
			payoffsForEffort.put(predationBehavior.getPredationEffort(), outcome.getPayoff()); 
		}
		assertEquals(.053, payoffsForEffort.get(0.25), .001); 
		assertEquals(.106, payoffsForEffort.get(0.5), .001); 
		assertEquals(.021, payoffsForEffort.get(0.1), .001); 
		assertEquals(.212, payoffsForEffort.get(1.0), .001); 
	}
	@Test
	public void verifyNoDoublePayoffsOrPayoffsForUnknownThreats() throws Exception
	{
		PredationBehavior predationBehavior = new TestingPredationBehavior(protectionBehavior, .25); 
		PredationOutcome outcome = predationBehavior.preyOnTargets(); 
		assertEquals("no payoff to threats not previously recognized by protection behavior",0.0, outcome.getPayoff(), .001); 
		assertTrue(outcome.getDetails().startsWith("BehaviorPeasantDefendsAgainstMultipleBandits: PredationBehavior not previously added as target or attempted to prey multiple times: "));
		protectionBehavior.addThreat(predationBehavior); 
		outcome = predationBehavior.preyOnTargets(); 
		assertEquals(.333, outcome.getPayoff(), .001); 
		assertEquals("",outcome.getDetails()); 
		outcome = predationBehavior.preyOnTargets(); 
		assertEquals("no second payoff from same protection behavior",0.0, outcome.getPayoff(), .001); 
		assertTrue(outcome.getDetails().startsWith("BehaviorPeasantDefendsAgainstMultipleBandits: PredationBehavior not previously added as target or attempted to prey multiple times: "));
	}
	public void verifyProtectionFunctionIsContest() throws Exception
	{
		peasant.setFunction(ProtectionFunctionEnum.POWER.buildFunction(0.5)); 
		try 
		{
			protectionBehavior.addThreat(new TestingPredationBehavior(protectionBehavior, 0.25));  
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("BehaviorPeasantDefendsAgainstMultipleBandits:  Peasant must have contest function; was: "));
		}
	}
	public void checkThrowsIfNotInitializedWithPeasant(Actor actor, String msg)
	{
		try
		{
			protectionBehavior = (ProtectionBehavior) BehaviorEnum.PEASANT_DEFENDS_AGAINST_MULTIPLE_BANDITS.build(actor); 
			fail("should throw"); 
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("BehaviorPeasantDefendsAgainstMultipleBandits:  must be initialized with a Peasant; was "+msg, e.getMessage());
		}
	}
	private class TestingPredationBehavior implements PredationBehavior
	{
		private ProtectionBehavior protectionBehavior;
		private double effort;
		private Bandit actor;

		public TestingPredationBehavior(ProtectionBehavior protectionBehavior, double effort)
		{
			this.protectionBehavior = protectionBehavior; 
			this.effort = effort;
			this.actor = new Bandit(); 
		}

		@Override
		public double getPredationEffort()
		{
			return effort;
		}
		@Override
		public Actor getActor() throws BehaviorException
		{
			return actor;
		}

		@Override
		public void setActor(Actor actor)
		{
		}

		@Override
		public Outcome behave() throws BehaviorException
		{
			return preyOnTargets();
		}

		@Override
		public BehaviorEnum getType()
		{
			return null;
		}


		@Override
		public PredationOutcome preyOnTargets() throws BehaviorException
		{
			return protectionBehavior.surrenderPayoff(this); 
		}

		@Override
		public void addTarget(ProtectionBehavior defensiveBehavior)
		{
		}

		@Override
		public List<ProtectionBehavior> getTargets()
		{
			return null;
		}

		@Override
		public void inherit(Heritable heritable)
		{
		}

		@Override
		public double getPayoff()
		{
			return 0;
		}

		@Override
		public void tick()
		{
		}

	}
}
