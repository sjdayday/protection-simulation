package edu.uci.imbs.actor;

public class Bandit extends Actor implements Heritable
{
	protected static Heritable LAST_STANDING;
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
		return "Bandit: "+getId()+"  Payoff: "+getPayoff();
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
	@Override
	public void setLastStanding(Heritable peasant)
	{
		LAST_STANDING = peasant; 
	}
	@Override
	public Heritable getLastStanding()
	{
		return LAST_STANDING;
	}
}
