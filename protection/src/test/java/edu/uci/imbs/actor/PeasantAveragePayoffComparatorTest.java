/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;



public class PeasantAveragePayoffComparatorTest
{
	private List<TestingPeasant> peasants;

	@Before
	public void setUp() throws Exception
	{
		peasants = new ArrayList<TestingPeasant>(); 
		peasants.add(new TestingPeasant(.5d)); 
		peasants.add(new TestingPeasant(.75d)); 
		peasants.add(new TestingPeasant(0d)); 
		peasants.add(new TestingPeasant(.25d)); 
	}
	@Test
	public void verifySortsPeasantsByPayoffAscending() throws Exception
	{
		Comparator<Peasant> comparator = new PeasantAveragePayoffComparator(); 
		Collections.sort(peasants, comparator); 
		assertEquals(0d, peasants.get(0).getPayoff(), .001); 
		assertEquals(.25d, peasants.get(1).getPayoff(), .001); 
		assertEquals(.5d, peasants.get(2).getPayoff(), .001); 
		assertEquals(.75d, peasants.get(3).getPayoff(), .001); 
	}
	private class TestingPeasant extends Peasant
	{
		private double payoff;
		public TestingPeasant(double payoff)
		{
			this.payoff = payoff; 
		}
		@Override
		public double getPayoff()
		{
			return payoff;
		}
	}
}
