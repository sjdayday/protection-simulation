package edu.uci.imbs.actor;

import java.util.List;

public class ProtectionPopulation
{

	private List<Bandit> bandits;
	private List<Peasant> peasants;

	public ProtectionPopulation(List<Bandit> bandits, List<Peasant> peasants)
	{
		this.bandits = bandits; 
		this.peasants = peasants; 
	}

	public List<Bandit> getBandits()
	{
		return bandits;
	}

	public List<Peasant> getPeasants()
	{
		return peasants;
	}

}
