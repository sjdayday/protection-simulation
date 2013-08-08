/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class Bandit extends Actor implements Heritable
{
	private double payoff;
	private PredationBehavior predationBehavior;
	public Bandit(Bandit bandit)
	{
		this(); 
		inheritBandit(bandit);
	}
	public Bandit()
	{
		super(); 
	}
	public double prey(Peasant peasant)
	{
		payoff+= peasant.surrenderUnprotectedPayoff(); 
		return payoff;  
	}
	@Override
	public String getName()
	{
		return "Bandit";
	}
	@Override
	public String toString()
	{
		return "Bandit: "+getId()+"  Payoff: "+getPayoff()+((predationBehavior != null) ? predationBehavior.toString() : "");
	}
	@Override
	public double getPayoff()
	{
		return payoff;
	}
	public void setPayoff(double payoff)
	{
		this.payoff = payoff;
	}
	@Override
	public void tick()
	{
		super.tick();
		predationBehavior = null; 
		payoff = 0; 
	}
	public void inheritBandit(Bandit bandit)
	{
		if (bandit.predationBehavior != null) 
		{
			getPredationBehavior();
			predationBehavior.inherit(bandit.predationBehavior); 
		}
	}
	@Override
	public void inherit(Heritable heritable)
	{
		validateHeritable(Bandit.class, heritable); 
		inheritBandit((Bandit) heritable); 
	}
	public PredationBehavior getPredationBehavior()
	{
		if (predationBehavior == null) 
		{
			predationBehavior = (PredationBehavior) BehaviorEnum.BANDIT_PREYS_ON_MULTIPLE_PEASANTS.build(this); 
			behaviors.add(predationBehavior);
		}
		return predationBehavior;
	}
}
