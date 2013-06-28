/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class MatchingFunction extends AbstractProtectionFunction
{

	public static final int ALPHA = 0;
	public static final int BETA = 1;
	public static final int MU = 2;
	private double mu;
	private double beta;
	private double alpha;
	

	public MatchingFunction(double exponents)
	{
//		super(exponents);
		this(new double[] {exponents, exponents, 1});
	}

	public MatchingFunction(double[] parameters)
	{
		super(parameters); 
		validateParameters(parameters);
	}
	public void validateParameters(double[] parameters)
	{
		int length = parameters.length; 
		if (length == 1) {validateParameters(new double[]{parameters[0], parameters[0], 1}); return;}
		if (length == 2) {validateParameters(new double[]{parameters[0], parameters[1], 1}); return;}
		setParameters(parameters); 
		if ((parameters[ALPHA] < 0) || (parameters[ALPHA] > 1) || 
				(parameters[BETA] < 0) || (parameters[BETA] > 1) ) 
			throw new IllegalArgumentException("MatchingFunction: Matching function only accepts Alpha and Beta in the range of 0 to 1; received "+parameters[ALPHA]+" and "+parameters[BETA]);
		this.alpha = parameters[ALPHA]; 
		this.beta = parameters[BETA]; 
		this.mu = parameters[MU]; 
	}
	@Override
	public double evaluate(double[] inputs)
	{
		validateInputs(inputs);
		return mu*Math.pow(inputs[0], alpha)*Math.pow(inputs[1], beta);
	}
	private void validateInputs(double[] inputs)
	{
		if (!(inputs.length == 2))
			throw new IllegalArgumentException("MatchingFunction: Matching function requires exactly two inputs:  number of elements searching for a match, and number of elements available to be matched.");
	}

	@Override
	public double evaluate(double input)
	{
		validateInputs(new double[]{input});
		return 0;
	}

	@Override
	public ProtectionFunction newInstance()
	{
		return ProtectionFunctionEnum.MATCHING.buildFunction(parameters);
	}
	@Override
	public String toString()
	{
		return "Matching: alpha="+alpha+" beta="+beta+" mu="+mu;
	}

}
