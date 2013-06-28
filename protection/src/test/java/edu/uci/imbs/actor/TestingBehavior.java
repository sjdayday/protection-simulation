/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class TestingBehavior extends AbstractBehavior implements Behavior
{

	private int number;
	private boolean hasTicked;

	public TestingBehavior(Actor actor)
	{
		this();
	}

	public TestingBehavior(int number)
	{
		this();
		this.number = number;
	}
	public TestingBehavior()
	{
		this.hasTicked = false;
	}
	public boolean hasTicked()
	{
		return hasTicked;
	}
	@Override
	public Outcome behave()
	{
		return new TestingOutcome(number);
	}

	public BehaviorEnum getType()
	{
		return BehaviorEnum.TESTING_BEHAVIOR;
	}

	@Override
	public double getPayoff()
	{
		return 0;
	}


	@Override
	public void tick()
	{
		hasTicked = true; 
	}

	@Override
	public void inherit(Heritable heritable)
	{
	}
	
}
