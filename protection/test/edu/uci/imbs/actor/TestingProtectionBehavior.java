/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.Map;

public class TestingProtectionBehavior implements ProtectionBehavior
{

	private double payoff;
	public boolean threatAdded = false;

	public TestingProtectionBehavior(double payoff)
	{
		this.payoff = payoff; 
	}
	
	public boolean getThreatAdded()
	{
		return threatAdded;
	}

	@Override
	public Actor getActor() throws BehaviorException
	{
		return new Actor();
	}

	@Override
	public void setActor(Actor actor)
	{
	}

	@Override
	public PredationOutcome behave() throws BehaviorException
	{
		return null;
	}

	@Override
	public BehaviorEnum getType()
	{
		return null;
	}

	@Override
	public void addThreat(PredationBehavior predationBehavior)
	{
		this.threatAdded  = true; 
	}

	@Override
	public PredationOutcome surrenderPayoff(
			PredationBehavior predationBehavior) throws BehaviorException
	{
		return new BanditPredationOutcome(payoff);
	}

	@Override
	public Map<PredationBehavior, Double> getThreatMap()
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
