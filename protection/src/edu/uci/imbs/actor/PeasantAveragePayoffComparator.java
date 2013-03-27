package edu.uci.imbs.actor;

import java.util.Comparator;

public class PeasantAveragePayoffComparator implements Comparator<Peasant>
{

	@Override
	public int compare(Peasant peasant1, Peasant peasant2)
	{
		Double first = peasant1.getPayoff(); 
		Double second = peasant2.getPayoff(); 
		return first.compareTo(second);
	}

}
