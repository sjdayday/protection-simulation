package edu.uci.imbs.actor;

public enum RunGovernorEnum 
{
	
	NOT_STOPPED(0, "Run not stopped; either not started or still in progress."),
	EQUILIBRIUM_REACHED(1,"Equilibrium reached for specified periods."),
	PEASANTS_EXTINCT(2,"Number of peasants dropped to zero."),
	BANDITS_EXTINCT(3,"Number of bandits dropped to zero."),
	PEASANTS_EXCEEDED_MAX(4,"Number of peasants exceeded maximum population size."),
	BANDITS_EXCEEDED_MAX(5,"Number of bandits exceeded maximum population size."),
	RUN_LIMIT_REACHED(6,"Run limit reached.");

	private int reason;
	private String reasonDescription;
	private RunGovernorEnum(int reason, String reasonDescription)
	{
		this.reason = reason;
		this.reasonDescription = reasonDescription; 
	}
	public int getReason() 
	{
		return reason;
	}
	public String getReasonDescription() 
	{
		return reasonDescription;
	}
}
