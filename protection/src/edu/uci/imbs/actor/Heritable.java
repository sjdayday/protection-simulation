/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public interface Heritable 
{
	public void inherit(Heritable heritable);

	public double getPayoff();

}
