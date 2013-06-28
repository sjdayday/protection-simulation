/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class PowerFunction extends AbstractProtectionFunction // extends ProtectionFunction
{

	public static final int POWER = 0;
	private double power;

	public PowerFunction(double power)
	{
		super(power); 
		validatePower(power);
	}
	public void validatePower(double power)
	{
		if (power < 0) throw new IllegalArgumentException("ProtectionFunction: Power function only accepts non-negative exponent; received "+power); 
		this.power = power;
		if (parameters == null) parameters = new double[]{power}; 
	}
	public PowerFunction(double[] parameters)
	{
		super(parameters); 
		validatePower(parameters[POWER]);	
	}
	@Override
	public double evaluate(double input)
	{
		validate(input); 
		return Math.pow(input, power);
	}
	@Override
	public ProtectionFunction newInstance()
	{
		return ProtectionFunctionEnum.POWER.buildFunction(parameters);
	}
	@Override
	public String toString()
	{
		return "Power: "+power;
	}
}
