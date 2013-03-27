package edu.uci.imbs.actor;

public class ProtectionPredationOutcome implements PredationOutcome
{


	@Override
	public double getPayoff()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return "ProtectionPredationOutcome";
	}

	public String getDetails()
	{
		return null;
	}

}
