package edu.uci.imbs.actor;

public class DummyFunction implements ProtectionFunction
{

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
	@Override
	public void setParameters(double[] doubles)
	{
	}

	@Override
	public double[] getParameters()
	{
		return null;
	}

	@Override
	public double evaluate(double[] inputs)
	{
		return 0;
	}

}
