/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public enum ProtectionFunctionEnum
{
	POWER { public ProtectionFunction buildFunction(double power) { return new PowerFunction(power); } 
		public ProtectionFunction buildFunction(double[] parameters) { return new PowerFunction(parameters); } }, 
	CONTEST { public ProtectionFunction buildFunction(double gamma) { return new ContestFunction(gamma); } 
	    public ProtectionFunction buildFunction(double[] parameters) { return new ContestFunction(parameters); } },
	MATCHING { public ProtectionFunction buildFunction(double exponents) { return new MatchingFunction(exponents); }
	public ProtectionFunction buildFunction(double[] parameters) { return new MatchingFunction(parameters); } }; 

	public abstract ProtectionFunction buildFunction(double parm);

	public abstract ProtectionFunction buildFunction(double[] parameters);
}
