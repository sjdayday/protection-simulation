/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoleShiftingReplicatorDynamic implements Dynamic
{

	private ProtectionPopulation protectionPopulation;
	private ProtectionStatistics statistics;
	private List<Peasant> peasants;
	private List<Bandit> bandits;

	public RoleShiftingReplicatorDynamic(ProtectionStatistics stats)
	{
		this.statistics = stats; 
	}
	@Override
	public ProtectionPopulation rebuildPopulation()
	{
		int adjustment = statistics.getActorAdjustment();
		if (adjustment != 0) adjustPeasantsAndBanditsInOppositeDirections(adjustment); 
		return protectionPopulation;
	}
	@Override
	public void setPopulation(ProtectionPopulation protectionPopulation)
	{
		this.protectionPopulation = protectionPopulation;
	}
	protected void adjustPeasantsAndBanditsInOppositeDirections(int adjust)
	{
		refreshListsOfPeasantsAndBandits();
		
		if (eitherPopulationIsEmpty()) return;
		
		if (ProtectionParameters.MIMIC_BETTER_PERFORMING_POPULATION)
		{
			if (adjust > 0) movePeasantsToBandits(adjust); 
			else moveBanditsToPeasants(adjust*-1); 
		}
		else
		{
			if (adjust > 0) moveBanditsToPeasants(adjust);
			else movePeasantsToBandits(adjust*-1); 
		}
		protectionPopulation = new ProtectionPopulation(bandits, peasants);
	}
	private boolean eitherPopulationIsEmpty()
	{
		if ((protectionPopulation.getBandits().size() == 0) || (protectionPopulation.getPeasants().size() == 0)) return true;  
		else return false;
	}
	public void refreshListsOfPeasantsAndBandits()
	{
		peasants = new ArrayList<Peasant>(); 
		bandits = new ArrayList<Bandit>(); 
		for (Peasant peasant : protectionPopulation.getPeasants())
		{
			peasants.add(buildNewPeasant(peasant)); 
		}
		sortPeasantsByPayoffAscending(); 
		for (Bandit bandit : protectionPopulation.getBandits())
		{
			bandits.add(new Bandit(bandit)); 
		}
	}
	public void sortPeasantsByPayoffAscending()
	{
		Comparator<Peasant> comparator = new PeasantAveragePayoffComparator(); 
		Collections.sort(peasants, comparator);
	}
	protected Peasant buildNewPeasant(Peasant peasant)
	{
		return new Peasant(peasant);
	}
	private void movePeasantsToBandits(int adjust)
	{
		for (int i = 0; i < adjust; i++)
		{
			if (peasants.size() > 0) 
			{
				peasants.remove(0); 
				bandits.add(new Bandit());
			}
		}
	}
	private void moveBanditsToPeasants(int adjust)
	{
		for (int i = 0; i < adjust; i++)
		{
			if (bandits.size() > 0) 
			{
				bandits.remove(0); 
				peasants.add(buildPeasant());
			}
		}
	}
	public Peasant buildPeasant()
	{
		int lastPeasantIndex = peasants.size()-1;
		if (lastPeasantIndex >= 0) return new Peasant(peasants.get(lastPeasantIndex));
		else 
		{
			throw new RuntimeException("RoleShiftingReplicatorDynamic.buildPeasant:  Not expecting to need Last Standing logic but attempted to replicate from empty list of Peasants.");
			//TODO delete this once a few scenarios have run
//			//TODO see Peasant; we need a better way
//			Peasant peasant = new Peasant();
//			peasant.inherit(peasant.getLastStanding()); 
//			return peasant; 
		}
	}
}