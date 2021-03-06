/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class VariablePopulationStatisticsRecordTest
{
	private List<PeasantProportionRecordEntry> protectionProportions;
	@Before
	public void setUp() throws Exception
	{
		protectionProportions = new ArrayList<PeasantProportionRecordEntry>(); 
		protectionProportions.add(new PeasantProportionRecordEntry("X0.15#",4,"X0.15%",0.13)); 
		protectionProportions.add(new PeasantProportionRecordEntry("X0.2#",1,"X0.2%",0.1)); 
	}
	@Test
	public void verifyStatisticsRecordPrintsAllFields() throws Exception
	{
		VariablePopulationStatisticsRecord record = new VariablePopulationStatisticsRecord(1, 2, 3, 4.0, 5.0, 6.0, -7, 0.8, 0.9, 0.1, 2.0, 3.0, 4, protectionProportions);
		assertEquals("Period=1, Number Bandits=2, Number Peasants=3, Number Bandits After Replication=0, Number Peasants After Replication=0, Average Bandit Payoff=4.0, Average Peasant Payoff=5.0, " +
				"Bandit-Peasant Payoff Delta=6.0, Actor Adjustment=-7, Average Protection Proportion=0.8, Median Protection Proportion=0.9, Mode Protection Proportion=0.1, " +
				"Average Number Peasants To Prey Upon=2.0, Median Number Peasants To Prey Upon=3.0, Mode Number Peasants To Prey Upon=4, " +
				"X0.15#=4, X0.15%=0.13, X0.2#=1, X0.2%=0.1", record.toString());
//		sb.append(", Average Protection Proportion=");
//		sb.append(averagePeasantProtectionProportion);
//		sb.append(", Median Protection Proportion=");
//		sb.append(medianPeasantProtectionProportion);
//		sb.append(", Mode Protection Proportion=");
//		sb.append(modePeasantProtectionProportion);
		  

	}
}
