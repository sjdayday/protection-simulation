/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class BehaviorTest
{
	private Behavior behavior;
	private Actor actor;
	
	@Before
	public void setUp() throws Exception
	{
		actor = new Actor(); 
		actor.setId(2); 
		behavior = new TestingBehavior(1); 
	}
	@Test
	public void verifyBehaviorsAlwaysHaveTypesAndAssociatedActors() throws Exception
	{
		assertEquals(BehaviorEnum.TESTING_BEHAVIOR, behavior.getType()); 
		try 
		{
			behavior.getActor().getId(); 
		}
		catch (BehaviorException e)
		{
			assertEquals("BehaviorException: Behavior must have associated actor.", e.getMessage());
		}
		behavior.setActor(actor); 
		assertEquals(2, behavior.getActor().getId()); 
	}
	@Test
	public void verifyBehaviorsGenerateOutcomes() throws Exception
	{	
		behavior.setActor(actor); 
		Outcome outcome = behavior.behave(); 
		assertEquals("TestingOutcome", outcome.getName()); 
	}
	@Test
	public void verifyBehaviorsThrowBehaviorException() throws Exception
	{
		behavior = new TestingBehaviorWithException(); 
		try
		{
			behavior.behave(); 
			fail("should throw");
		}
		catch (BehaviorException e)
		{
		}
		
	}
	private class TestingBehaviorWithException implements Behavior
	{

		@Override
		public Actor getActor() throws BehaviorException
		{
			return null;
		}

		@Override
		public void setActor(Actor actor)
		{
		}

		@Override
		public Outcome behave() throws BehaviorException 
		{
			throw new BehaviorException();
		}

		public BehaviorEnum getType()
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
