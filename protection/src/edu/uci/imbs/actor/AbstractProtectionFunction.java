/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class AbstractProtectionFunction implements ProtectionFunction
{
	protected double[] parameters;
	public AbstractProtectionFunction(double[] parameters)
	{
		setParameters(parameters);
	}
	public AbstractProtectionFunction(double parm)
	{
	}
	@Override
	public double evaluate(double input)
	{
		return 0;
	}
	@Override
	public ProtectionFunction newInstance()
	{
		return null;
	}
	public void validate(double input)
	{
		if ((input < 0) || (input > 1)) throw new IllegalArgumentException("AbstractProtectionFunction.evaluate:  Inputs to evaluate must be in range [0,1]; received "+input);
	}
	@Override
	public void setParameters(double[] parameters)
	{
		if (parameters.length == 0) throw new IllegalArgumentException("AbstractProtectionFunction:  Array of parameters must have at least one entry.");
		this.parameters = parameters; 
	}
	@Override
	public double[] getParameters()
	{
		return parameters;
	}
	@Override
	public double evaluate(double[] inputs)
	{
		return evaluate(inputs[0]);
	}
}
