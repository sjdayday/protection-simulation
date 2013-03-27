package edu.uci.imbs.actor;

public interface ProtectionFunction
{

	public  double evaluate(double input);

	public ProtectionFunction newInstance();

	public void setParameters(double[] doubles);

	public double[] getParameters();

	public double evaluate(double[] inputs);

}