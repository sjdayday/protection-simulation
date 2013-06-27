/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class PeasantProportionRecordEntry
{
	public String numberHeading;
	public int number;
	public String proportionHeading;
	public double proportion; 
	
	public PeasantProportionRecordEntry(String numberHeading, int number, String proportionHeading, double proportion)
	{
		this.numberHeading = numberHeading; 
		this.number = number; 
		this.proportionHeading = proportionHeading; 
		this.proportion = proportion; 
	}
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(numberHeading);
		sb.append(": "); 
		sb.append(number);
		sb.append(proportionHeading);
		sb.append(": "); 
		sb.append(proportion); 
		return sb.toString();
	}
}
