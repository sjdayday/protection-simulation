/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ActorTest
{
	private Actor actor;
	private Actor actor2;
	
	@Before
	public void setUp() throws Exception
	{
		Actor.resetStartingIdForTesting(1);
		actor = new TestingActor();
		actor2 = new TestingActor(); 
	}
	@Test
	public void verifyActorsAreEqualById() throws Exception
	{
		actor.setId(1);
		assertEquals(1, actor.getId()); 
		actor2.setId(0); 
		assertFalse(actor.equals(actor2));
		actor2.setId(1); 
		assertTrue(actor.equals(actor2)); 
		assertFalse(actor.equals(null));
		assertFalse(actor.equals(new Object()));
	}
	@Test
	public void verifyTwoActorsWithSameIdHaveSameHashcode() throws Exception
	{
		actor.setId(1);
		assertFalse((actor.hashCode() == actor2.hashCode())); 
		actor2.setId(1); 
		assertTrue((actor.hashCode() == actor2.hashCode())); 
	}
	@Test
	public void verifyIdIncrementsAutomatically() throws Exception
	{
		Actor.resetStartingIdForTesting(1); 
		actor = new TestingActor();
		actor2 = new TestingActor(); 
		assertEquals(1, actor.getId());
		assertEquals(2, actor2.getId());
	}
	@Test
	public void verifyActorsHaveBehaviors() throws Exception
	{
		assertEquals(0,actor.getBehaviors().size()); 
		actor.addBehavior(new TestingBehavior(1)); 
		assertEquals(1,actor.getBehaviors().size()); 
	}
	@Test
	public void verifyTickPropagatesToBehaviors() throws Exception
	{
		TestingBehavior testBehavior = new TestingBehavior(); 
		assertFalse(testBehavior.hasTicked());
		actor.addBehavior(testBehavior);
		actor.tick();
		assertTrue(testBehavior.hasTicked()); 
	}
	@Test
	public void verifyActorsBehaveGeneratingOutcomes() throws Exception
	{
		actor.addBehavior(new TestingBehavior(1)); 
		actor.addBehavior(new TestingBehavior(2)); 
		List<Outcome> outcomes = actor.behave(); 
		assertEquals(2, outcomes.size()); 
		assertEquals(1, ((TestingOutcome) outcomes.get(0)).getNumber() );
		assertEquals(2, ((TestingOutcome) outcomes.get(1)).getNumber() );
	}
	@Test
	public void verifyHeritableTypesAreVerified() throws Exception
	{
		Actor actor = new Actor(); 
		Actor actor2 = new Actor(); 
		//should we really have to do this?
		checkHeritableIsVerified(actor2, null);
		checkHeritableIsVerified(new Peasant(), new Bandit());  
		checkHeritableIsVerified(new Bandit(), actor);  
		actor2.inherit(actor);   
		actor2.inherit(new Bandit());   
	}
	public void checkHeritableIsVerified(Actor parent, Heritable heritable)
	{
		try
		{
			parent.inherit(heritable); 
			fail("should throw"); 
		}
		catch (IllegalArgumentException e)
		{
//			assertEquals("Actor:  "+parent.getClass().getName()+" may only inherit from another "+parent.getClass().getName()+"; attempted inheritance from: ",e.getMessage());
			assertTrue(e.getMessage().startsWith("Actor:  "+parent.getClass().getName()+" may only inherit from another "+parent.getClass().getName()+"; attempted inheritance from: "));
		}
	}
	private class TestingActor extends Actor 
	{
		public TestingActor()
		{
			super(); 
		}
		@Override
		public String getName()
		{
			return "TestingActor";
		}

		@Override
		public void tick()
		{
			super.tick();
		}
		@Override
		public boolean equals(Object obj)
		{
			return super.equals(obj);
		}
	}
}
