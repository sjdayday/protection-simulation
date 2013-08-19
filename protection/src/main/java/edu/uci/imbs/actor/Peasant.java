/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.Random;


public class Peasant extends Actor implements Heritable
{
	private double protectionProportion;
	private double workProportion;
	private double protection;
	private double protectedPayoff;
	private double unprotectedPayoff;
	private double surrenderedPayoff;
	private double payoff;
	private boolean surrendered;
	private ProtectionFunction function;
	private ProtectionBehavior protectionBehavior;
	public Peasant()
	{
		super(); 
	}
	public Peasant(Peasant peasant)
	{	
		this(); 
		inheritPeasant(peasant);
	}
	public void inheritPeasant(Peasant peasant)
	{
		setProtectionProportion(peasant.getProtectionProportion()); 
		setFunction(peasant.getFunction().newInstance());
	}
	@Override
	public void inherit(Heritable heritable)
	{
		validateHeritable(Peasant.class, heritable); 
		inheritPeasant((Peasant) heritable); 
	}
	public void setProtectionProportion(double protectionProportion)
	{
		if (protectionProportion < 0) protectionProportion = 0; 
		if (protectionProportion > 1) protectionProportion = 1; 
		this.protectionProportion = protectionProportion;
		calculate(); 
	}
	private void calculate()
	{
		workProportion = 1d-protectionProportion; 
		if (function != null) protection = function.evaluate(getProtectionProportion());
		protectedPayoff = protection * workProportion; 
		if (surrendered) 
		{	
			surrenderedPayoff = unprotectedPayoff; 
			unprotectedPayoff = 0; 
		}
		else 
		{
			surrenderedPayoff = 0; 
			unprotectedPayoff = workProportion - protectedPayoff;
		}
		payoff = protectedPayoff + unprotectedPayoff;
	}
	public double getProtectionProportion()
	{
		validate(); 
		return protectionProportion;
	}
	
	public double getWorkProportion()
	{
		validate(); 
		return workProportion;
	}
	public double getProtection()
	{
		validate(); 
		return protection;
	}
	@Override
	public double getPayoff()
	{
		validate(); 
		return payoff; 
	}  
	protected double getProtectedPayoff()
	{
		validate(); 
		return protectedPayoff; 
	}
	protected double getUnprotectedPayoff()
	{
		validate(); 
		return unprotectedPayoff; 
	}
	protected double getSurrenderedPayoff()
	{
		validate(); 
		return surrenderedPayoff;
	}
	public double surrenderUnprotectedPayoff()
	{
		validate(); 
		double surrenderedPayoff = unprotectedPayoff; 
		surrendered = true;
		calculate(); 
		return surrenderedPayoff;
	}
	@Override
	public String getName()
	{
		return "Peasant";
	}
	public boolean hasSurrendered()
	{
		return surrendered;
	}
	private void validate()
	{
		if (function == null) throw new IllegalStateException("Peasant:  No processing available until setProtectionProportion() and setFunction() have been called."); 
	}
	@Override
	public void tick()
	{
		validate(); 
		surrendered = false; 
		protectionBehavior = null; 
		calculate(); 
	}
	public void setFunction(ProtectionFunction function)
	{
		this.function = function; 
		calculate(); 
	}
	public ProtectionFunction getFunction()
	{
		validate(); 
		return function;
	}
	@Override
	public String toString()
	{
		validate(); 
		StringBuffer sb = new StringBuffer(); 
		sb.append("Peasant: "); 
		sb.append(getId()); 
		sb.append("  Protection Proportion: "); 
		sb.append(getProtectionProportion()); 
		sb.append("  Protection Function: "); 
		sb.append(getFunction().toString()); 
		sb.append("  Payoff: "); 
		sb.append(getPayoff()); 
		sb.append("  Unprotected Payoff: "); 
		sb.append(getUnprotectedPayoff()); 
		sb.append("  Surrendered Payoff: "); 
		sb.append(getSurrenderedPayoff()); 
		sb.append("  Surrendered: "); 
		sb.append(hasSurrendered()); 
		return sb.toString();
	}
	public Double getRoundedProtectionProportion()
	{
		return Util.roundDoubleToTwoDecimalPlaces(getProtectionProportion());
	}
	public void setPayoffForTesting(double forcedPayoff)
	{
		function = new DummyFunction(); 
		payoff = forcedPayoff; 
	}
	public ProtectionBehavior getProtectionBehavior()
	{
		if (this.protectionBehavior == null) protectionBehavior = (ProtectionBehavior) BehaviorEnum.PEASANT_DEFENDS_AGAINST_MULTIPLE_BANDITS.build(this); 
		return protectionBehavior; 
	}
	public static Peasant buildPeasantWithContestFunctionAndRandomProtectionProportion(Random random) 
	{
		Peasant peasant = new Peasant(); 
		peasant.setFunction(ProtectionFunctionEnum.CONTEST.buildFunction(ProtectionParameters.CONTEST_FUNCTION_GAMMA)); 
		peasant.setProtectionProportion(ProtectionParameters.PROTECTION_PROPORTION_INTERVAL_SIZE * random.nextInt(ProtectionParameters.PROTECTION_PROPORTION_NUMBER_INTERVALS));
		return peasant; 
	}
}
