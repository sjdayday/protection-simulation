package edu.uci.imbs.actor;

public class ContestFunction extends AbstractProtectionFunction  // extends ProtectionFunction
{

	public static final int GAMMA = 0;
	public static final int WEIGHT = 1;
	private static final double DEFAULT_WEIGHT = 1;
	private double gamma;
	private double weight;
	
	public ContestFunction(double gamma)
	{
		super(gamma);
		validateGamma(gamma); 
	}
	public void validateGamma(double gamma)
	{
		if ((gamma < 0.5) || (gamma > 1)) throw new IllegalArgumentException("ContestFunction: Contest function only accepts gamma in the range of 0.5 to 1; received "+gamma);
		this.gamma = gamma; 
		if (parameters == null) setParameters(new double[]{gamma});
	}
	public ContestFunction(double[] parameters)
	{
		super(parameters);
		validateGamma(parameters[GAMMA]); 
	}
	@Override
	public double evaluate(double input)
	{
		validate(input); 
		if (input == 1) return 1; // per requirements for Protection Functions; returns wrong answers otherwise
		if (input == 0) return 0; // per requirements for Protection Functions; returns NaN otherwise
		return (gamma * input) / ((gamma * input) + ((1d-gamma) * weight ));
	}
	@Override
	public ProtectionFunction newInstance()
	{
		return ProtectionFunctionEnum.CONTEST.buildFunction(parameters);
	}
	@Override
	public void setParameters(double[] parameters)
	{
		super.setParameters(parameters);
		validateGamma(parameters[GAMMA]); 
		if (parameters.length > 1) this.weight = parameters[WEIGHT]; 
		else 
		{
			super.setParameters(new double[]{gamma, DEFAULT_WEIGHT}); 
			this.weight = DEFAULT_WEIGHT; 
		}
	}
	@Override
	public double[] getParameters()
	{
		return parameters;
	}
	@Override
	public String toString()
	{
		return "Contest: gamma="+gamma+" weight="+weight;
	}
}
