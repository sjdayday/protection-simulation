/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public enum RunGovernorEnum 
{
	NOT_STOPPED(0, "Run not stopped; either not started or still in progress."),
	PEASANTS_EXTINCT(1,"Number of peasants dropped to zero."),
	BANDITS_EXTINCT(2,"Number of bandits dropped to zero."),
	PEASANTS_EXCEEDED_MAX(3,"Number of peasants exceeded maximum population size."),
	BANDITS_EXCEEDED_MAX(4,"Number of bandits exceeded maximum population size."),
	STATIC_EQUILIBRIUM_REACHED(5, "Equilibrium reached for specified periods, with no change in population in at least the last two periods."),
	EQUILIBRIUM_REACHED(6,"Equilibrium reached for specified periods, with some change in population in at least the last two periods"),
	RUN_LIMIT_REACHED(7,"Run limit reached."); 

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
