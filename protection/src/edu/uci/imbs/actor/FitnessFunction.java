/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class FitnessFunction
{
	private double surviveThreshold = 0; 
	private double thriveThreshold = 0;
	public double getSurviveThreshold()
	{
		return surviveThreshold;
	}
	public void setSurviveThreshold(double surviveThreshold)
	{
		this.surviveThreshold = surviveThreshold;
	}
	public double getThriveThreshold()
	{
		return thriveThreshold;
	}
	public void setThriveThreshold(double thriveThreshold)
	{
		this.thriveThreshold = thriveThreshold;
	}
	public FitnessEnum evaluate(double fitness)
	{
//		if ((fitness < 0) || (fitness > 1)) throw new IllegalArgumentException("FitnessFunction.evaluate:  Inputs to evaluate must be in range [0,1]; received "+fitness);
		if ((fitness < 0)) throw new IllegalArgumentException("FitnessFunction.evaluate:  Inputs to evaluate must be greater than 0; received "+fitness);
		if (surviveThreshold == 0) return FitnessEnum.SURVIVE;
		if (fitness < surviveThreshold) return FitnessEnum.DIE;
		if ((fitness >= thriveThreshold) && (thriveThreshold >= surviveThreshold)) return FitnessEnum.THRIVE; 
		return FitnessEnum.SURVIVE; 
	} 
}
