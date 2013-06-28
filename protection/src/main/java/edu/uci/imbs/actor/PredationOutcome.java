/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public interface PredationOutcome extends Outcome
{

	public abstract double getPayoff();

	public abstract String getDetails();

}
