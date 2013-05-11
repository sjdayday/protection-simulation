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
		List<Bandit> newBandits = new ArrayList<Bandit>(); 
		replicatorDynamic.replicate(Bandit.class, protectionPopulation.getBandits(), newBandits);  
		return new ProtectionPopulation(newBandits, newPeasants);
	}

	@Override
	public void setPopulation(ProtectionPopulation protectionPopulation)
	{
		this.protectionPopulation = protectionPopulation; 
	}

}
