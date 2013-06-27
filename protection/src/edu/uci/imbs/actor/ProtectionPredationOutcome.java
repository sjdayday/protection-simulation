/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

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
