/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.Iterator;
import java.util.List;

public class VariablePopulationStatisticsRecord extends StatisticsRecord
{
	public double averagePeasantProtectionProportion;
	public double medianPeasantProtectionProportion;
	public double modePeasantProtectionProportion;
	public double averageBanditNumberPeasantsToPreyUpon;
	public double medianBanditNumberPeasantsToPreyUpon;
	public int modeBanditNumberPeasantsToPreyUpon;
	public List<PeasantProportionRecordEntry> protectionProportions;

	public VariablePopulationStatisticsRecord(int period, int numberBandits,
			int numberPeasants, double averageBanditPayoff,
			double averagePeasantPayoff, double banditPeasantPayoffDelta, int peasantAdjustment)
	{
		super(period, numberBandits, numberPeasants, averageBanditPayoff,
				averagePeasantPayoff, banditPeasantPayoffDelta, peasantAdjustment);
		
	}
	public VariablePopulationStatisticsRecord(int period, int numberBandits,
			int numberPeasants, double averageBanditPayoff, 
			double averagePeasantPayoff, double banditPeasantPayoffDelta, int peasantAdjustment, double averagePeasantProtectionProportion, 
			double medianPeasantProtectionProportion, double modePeasantProtectionProportion, 
			double averageBanditNumberPeasantsToPreyUpon, double medianBanditNumberPeasantsToPreyUpon, int modeBanditNumberPeasantsToPreyUpon, List<PeasantProportionRecordEntry> protectionProportions)
	{
		this(period, numberBandits, numberPeasants, averageBanditPayoff,
				averagePeasantPayoff, banditPeasantPayoffDelta, peasantAdjustment); 
		this.averagePeasantProtectionProportion = averagePeasantProtectionProportion; 
		this.medianPeasantProtectionProportion = medianPeasantProtectionProportion; 
		this.modePeasantProtectionProportion = modePeasantProtectionProportion;
		this.averageBanditNumberPeasantsToPreyUpon = averageBanditNumberPeasantsToPreyUpon; 
		this.medianBanditNumberPeasantsToPreyUpon = medianBanditNumberPeasantsToPreyUpon; 
		this.modeBanditNumberPeasantsToPreyUpon = modeBanditNumberPeasantsToPreyUpon; 
		this.protectionProportions = protectionProportions; 
	}
    @Override
    protected void printAdditionalFields(StringBuffer sb)
    {
		sb.append(", Average Protection Proportion=");
		sb.append(averagePeasantProtectionProportion);
		sb.append(", Median Protection Proportion=");
		sb.append(medianPeasantProtectionProportion);
		sb.append(", Mode Protection Proportion=");
		sb.append(modePeasantProtectionProportion);
		sb.append(", Average Number Peasants To Prey Upon=");
		sb.append(averageBanditNumberPeasantsToPreyUpon);
		sb.append(", Median Number Peasants To Prey Upon=");
		sb.append(medianBanditNumberPeasantsToPreyUpon);
		sb.append(", Mode Number Peasants To Prey Upon=");
		sb.append(modeBanditNumberPeasantsToPreyUpon);
		Iterator<PeasantProportionRecordEntry> it = protectionProportions.iterator(); 
		PeasantProportionRecordEntry entry = null; 
		while (it.hasNext())
		{
			entry = it.next(); 
			sb.append(", "+entry.numberHeading+"="); 
			sb.append(entry.number);
			sb.append(", "+entry.proportionHeading+"="); 
			sb.append(entry.proportion);
		}
				
    }

}
