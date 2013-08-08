/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PeasantTest
{
	private Peasant peasant;

	@Before
	public void setUp() throws Exception
	{
		peasant = new Peasant(); 
		peasant.setProtectionProportion(0.5);
		peasant.setFunction(ProtectionFunctionEnum.POWER.buildFunction(2));  
	}
	@Test
	public void verifyPeasantIsActor() throws Exception
	{
		peasant.setId(1); 
		Actor peasant2 = new Peasant();
		peasant2.setId(1); 
		assertEquals(peasant, peasant2); 
	}
	@Test
	public void verifyPeasantResourceDividedBetweenProtectionAndWork() throws Exception
	{
		peasant.setProtectionProportion(1);
		assertEquals(1, peasant.getProtectionProportion(), .001); 
		assertEquals(0, peasant.getWorkProportion(), .001);
		peasant.setProtectionProportion(0);
		assertEquals(0, peasant.getProtectionProportion(), .001); 
		assertEquals(1, peasant.getWorkProportion(), .001);
		peasant.setProtectionProportion(.75f);
		assertEquals(.75f, peasant.getProtectionProportion(), .001); 
		assertEquals(.25f, peasant.getWorkProportion(), .001);
	}
	@Test
	public void verifyResourceStaysBetweenZeroAndOne() throws Exception
	{
		peasant.setProtectionProportion(-1f);
		assertEquals(0, peasant.getProtectionProportion(), .001);
		peasant.setProtectionProportion(2f);
		assertEquals(1, peasant.getProtectionProportion(), .001);
	}
	@Test
	public void verifyProtectionFunctionAppliedToProtectionProportionResultingInProtectedOutput() throws Exception
	{
		assertEquals(0.25, peasant.getProtection(), .001);
	}
	@Test
	public void verifyPeasantProtectedPayoffCalculatedAsProtectionTimesWorkProportion() throws Exception
	{
		assertEquals("protection p(x) = .25 [0.5 ^ 2] * workProportion (1-x) = 0.5", 0.125, peasant.getProtectedPayoff(), .001); 
	}
	@Test
	public void verifyPeasantUnprotectedPayoffIsRemainderOfWorkProportion() throws Exception
	{
		assertEquals("peasant's work proportion (1-x) 0.5 - peasant's payoff (p(x)) 0.125; [1 - p(x)](1-x) ",0.375, peasant.getUnprotectedPayoff(), .001); 
	}
	@Test
	public void verifyTotalPayoffIsSumOfProtectedAndUnprotectedPayoffs() throws Exception
	{
		assertEquals("protected payoff: .125 + unprotected payoff: .375 = .5", 0.5, peasant.getPayoff(), .001); 
	}
	@Test
	public void verifyUnprotectedPayoffAvailableBeforeSurrenderButNotAfterAndTotalPayoffAdjustedAccordingly() throws Exception
	{
		assertEquals(0.375, peasant.getUnprotectedPayoff(), .001); 
		assertEquals(0.5, peasant.getPayoff(), .001); 
		assertFalse(peasant.hasSurrendered()); 
		assertEquals(0.375, peasant.surrenderUnprotectedPayoff(), .001); 
		assertTrue(peasant.hasSurrendered()); 
		assertEquals(0, peasant.getUnprotectedPayoff(), .001); 
		assertEquals(0.125, peasant.getPayoff(), .001); 
	}
	@Test
	public void verifyPayoffUnderContestFunction() throws Exception
	{
		peasant.setFunction(ProtectionFunctionEnum.CONTEST.buildFunction(0.5));  
		assertEquals("with gamma 0.5, reduces to x / x+1 = 0.5 / 1.5 = .333",0.333, peasant.getProtection(), .001);
		assertEquals("protection p(x) = .333 * workProportion (1-x) = 0.5", 0.166, peasant.getProtectedPayoff(), .001); 
		assertEquals("peasant's work proportion (1-x) 0.5 - peasant's payoff (p(x)) 0.166; [1 - p(x)](1-x) ",0.333, peasant.getUnprotectedPayoff(), .001); 
		assertEquals("protected payoff: .166 + unprotected payoff: .333 = .5", 0.5, peasant.getPayoff(), .001); 
		assertEquals(0.333, peasant.surrenderUnprotectedPayoff(), .001); 
		assertTrue(peasant.hasSurrendered()); 
		assertEquals(0, peasant.getUnprotectedPayoff(), .001); 
		assertEquals(0.166, peasant.getPayoff(), .001); 
	}
	@Test
	public void verifyTickResetsPayoffsAndSurrenderedFlagInPreparationForNextPeriod() throws Exception
	{
		assertEquals(0.375, peasant.surrenderUnprotectedPayoff(), .001); 
		assertTrue(peasant.hasSurrendered()); 
		assertEquals(0, peasant.getUnprotectedPayoff(), .001); 
		assertEquals(0.375, peasant.getSurrenderedPayoff(), .001); 
		assertEquals(0.125, peasant.getPayoff(), .001); 
		assertEquals("can't surrender more until next tick",0, peasant.surrenderUnprotectedPayoff(), .001); 
		
		peasant.tick(); 
		assertEquals(0.375, peasant.getUnprotectedPayoff(), .001); 
		assertEquals(0, peasant.getSurrenderedPayoff(), .001); 
		assertEquals(0.5, peasant.getPayoff(), .001); 
		assertFalse(peasant.hasSurrendered()); 
	}
	@Test
	public void verifyTickResetsBehaviors() throws Exception
	{
		ProtectionBehavior behavior = peasant.getProtectionBehavior(); 
		peasant.tick(); 
		assertNotSame(peasant.getProtectionBehavior(), behavior);
	}
	@Test
	public void verifyPeasantStateSetBeforeGetsAllowed() throws Exception
	{
		peasant = new Peasant(); 
		try 
		{
			peasant.getPayoff();
			fail("should throw IllegalStateException");
		}
		catch (IllegalStateException e)
		{
			assertEquals("Peasant:  No processing available until setProtectionProportion() and setFunction() have been called.", e.getMessage());
		}
		// same validation done but not tested for remaining getXxxx() functions. 
	}
	@Test
	public void verifyCopiedPeasantHasHigherIdSameProtectionProportionSameProtectionFunction() throws Exception
	{
		Peasant.resetStartingIdForTesting(5);
		peasant.setId(4);
		peasant.surrenderUnprotectedPayoff(); 
		Peasant newPeasant = new Peasant(peasant); 
		assertEquals(5, newPeasant.getId());
		assertEquals(0.5, newPeasant.getProtectionProportion(), .001); 
		assertEquals(2, newPeasant.getFunction().getParameters()[PowerFunction.POWER], .001); 
		assertTrue(newPeasant.getFunction() instanceof PowerFunction); 
		assertEquals(0.5, newPeasant.getPayoff(), .001);
		assertFalse(newPeasant.hasSurrendered()); 
		assertNotSame(peasant.getProtectionBehavior(), newPeasant.getProtectionBehavior());
		// see BehaviorPeasantDefendsAgainstMultipleBanditsTest for Behavior verification after inheritance
	}
	@Test
	public void verifyPeasantPrintsState() throws Exception
	{
		Peasant peasant = TestBuilder.buildPeasant(3); 
		assertEquals("Peasant: 3  Protection Proportion: 0.5  Protection Function: Power: 2.0  Payoff: 0.5  Unprotected Payoff: 0.375  Surrendered Payoff: 0.0  Surrendered: false", peasant.toString()); 
		peasant.surrenderUnprotectedPayoff(); 
		assertEquals("Peasant: 3  Protection Proportion: 0.5  Protection Function: Power: 2.0  Payoff: 0.125  Unprotected Payoff: 0.0  Surrendered Payoff: 0.375  Surrendered: true", peasant.toString()); 
	}
	@Test
	public void verifyPeasantProportionPrintsRoundedToTwoDecimalPlaces() throws Exception
	{
		peasant.setProtectionProportion(0.05d * 7); 
		assertEquals("0.35", peasant.getRoundedProtectionProportion().toString()); 
		peasant.setProtectionProportion(0.05d * 2); 
		assertEquals("0.1", peasant.getRoundedProtectionProportion().toString()); 
	}
	@Test
	public void verifyCanForcePayoffForTesting() throws Exception
	{
		peasant.setPayoffForTesting(.222d); 
		assertEquals(.222d, peasant.getPayoff(), .001); 
	}
	@Test
	public void verifyAppropriateDefensiveBehaviorElicitedByPredatoryBehavior() throws Exception
	{
		ProtectionBehavior behavior = peasant.getProtectionBehavior(); //BehaviorEnum.BANDIT_PREYS_ON_MULTIPLE_PEASANTS.build(new Bandit())); 
		assertEquals(BehaviorEnum.PEASANT_DEFENDS_AGAINST_MULTIPLE_BANDITS, behavior.getType());
		assertEquals(peasant, behavior.getActor()); 
	}
	@Test
	public void verifyOnlyOneDefensiveBehaviorPerPeriod() throws Exception
	{
		ProtectionBehavior currentBehavior = peasant.getProtectionBehavior(); 
		assertSame(currentBehavior, peasant.getProtectionBehavior());
		peasant.tick();
		assertNotSame("new behavior created each period",currentBehavior, peasant.getProtectionBehavior());
		
	}
	
}
