/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class StatisticsRecord
{

	public final int period;
	public final int numberBandits;
	public final int numberPeasants;
	public int numberBanditsAfterReplication;
	public int numberPeasantsAfterReplication;
	public final double averageBanditPayoff;
	public final double averagePeasantPayoff;
	public final int actorAdjustment;
	private double banditPeasantPayoffDelta;

	public StatisticsRecord(int period, int numberBandits, int numberPeasants, double averageBanditPayoff, double averagePeasantPayoff, double banditPeasantPayoffDelta, int actorAdjustment)
	{
		this.period = period;  
		this.numberBandits = numberBandits; 
		this.numberPeasants = numberPeasants; 
		this.averageBanditPayoff = averageBanditPayoff; 
		this.averagePeasantPayoff = averagePeasantPayoff;
		this.banditPeasantPayoffDelta = banditPeasantPayoffDelta; 
		this.actorAdjustment = actorAdjustment; 
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer(); 
		sb.append("Period=");
		sb.append(period);
		sb.append(", Number Bandits=");
		sb.append(numberBandits);
		sb.append(", Number Peasants=");
		sb.append(numberPeasants);
		sb.append(", Number Bandits After Replication=");
		sb.append(numberBanditsAfterReplication);
		sb.append(", Number Peasants After Replication=");
		sb.append(numberPeasantsAfterReplication);
		sb.append(", Average Bandit Payoff=");
		sb.append(averageBanditPayoff);
		sb.append(", Average Peasant Payoff=");
		sb.append(averagePeasantPayoff);
		sb.append(", Bandit-Peasant Payoff Delta=");
		sb.append(banditPeasantPayoffDelta);
		sb.append(", Actor Adjustment=");
		sb.append(actorAdjustment);
		printAdditionalFields(sb); 
		return sb.toString();
	}
	protected void printAdditionalFields(StringBuffer sb)
	{
	}
}
