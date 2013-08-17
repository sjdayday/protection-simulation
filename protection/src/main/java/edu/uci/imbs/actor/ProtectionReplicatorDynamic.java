/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.List;

public class ProtectionReplicatorDynamic implements Dynamic
{

	private DieSurviveThriveDynamic replicatorDynamic;
	private ProtectionPopulation protectionPopulation;

	public ProtectionReplicatorDynamic(DieSurviveThriveDynamic replicatorDynamic)
	{
		this.replicatorDynamic = replicatorDynamic; 
	}

	@Override
	public ProtectionPopulation rebuildPopulation()
	{
		List<Peasant> newPeasants = new ArrayList<Peasant>(); 
		replicatorDynamic.replicate(Peasant.class, protectionPopulation.getPeasants(), newPeasants);  
//		System.out.println("ProtectionReplicatorDynamic.rebuildPopulation Number of peasants just replicated: "+newPeasants.size());
		List<Bandit> newBandits = new ArrayList<Bandit>(); 
		replicatorDynamic.replicate(Bandit.class, protectionPopulation.getBandits(), newBandits);  
//		System.out.println("ProtectionReplicatorDynamic.rebuildPopulation Number of bandits just replicated: "+newBandits.size());
		return new ProtectionPopulation(newBandits, newPeasants);
	}

	@Override
	public void setPopulation(ProtectionPopulation protectionPopulation)
	{
		this.protectionPopulation = protectionPopulation; 
	}

}
