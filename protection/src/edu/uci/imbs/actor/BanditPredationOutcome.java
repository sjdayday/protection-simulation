package edu.uci.imbs.actor;

public class BanditPredationOutcome implements PredationOutcome
{

	private double payoff;
	private String details;

	public BanditPredationOutcome(double payoff)
	{
		this(payoff, "");
	}

	public BanditPredationOutcome(double payoff, String details)
	{
		this.payoff = payoff; 
		this.details = details; 
	}

	@Override
	public String getName()
	{
		return "PeasantProtectionOutcome";
	}

	@Override
	public double getPayoff()
	{
		return payoff;
	}

	@Override
	public String getDetails()
	{
		return details;
	}
	

}
