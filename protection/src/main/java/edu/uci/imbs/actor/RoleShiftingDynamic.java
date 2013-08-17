/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

public class RoleShiftingDynamic implements Dynamic
{
	private static Logger logger = Logger.getLogger(RoleShiftingDynamic.class);
	private ProtectionPopulation protectionPopulation;
	private ProtectionStatistics statistics;
	private List<Peasant> peasants;
	private List<Bandit> bandits;

	public RoleShiftingDynamic(ProtectionStatistics stats)
	{
		this.statistics = stats; 
	}
	@Override
	public void setPopulation(ProtectionPopulation protectionPopulation)
	{
		this.protectionPopulation = protectionPopulation;
		this.peasants = protectionPopulation.getPeasants();
		this.bandits = protectionPopulation.getBandits(); 
	}
	@Override
	public ProtectionPopulation rebuildPopulation()
	{
		int adjustment = statistics.getActorAdjustment();
		if (adjustment != 0) adjustPeasantsAndBanditsInOppositeDirections(adjustment); 
		return protectionPopulation;
	}
	protected void adjustPeasantsAndBanditsInOppositeDirections(int adjust)
	{
		sortPeasantsByPayoffAscending(); 
		
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
		Random r  = new Random(ProtectionParameters.RANDOM_SEED); 
		Peasant peasant = null; 
		for (int i = 0; i < adjust; i++)
		{
			if (bandits.size() > 0) 
			{
				bandits.remove(0);
				peasant = buildPeasant(r);
				logger.debug("RoleShiftingReplicatorDynamic.moveBanditsToPeasants: adding new peasant: "+peasant.toString()); 
				peasants.add(peasant);
			}
		}
	}
	protected Peasant buildPeasant(Random r)
	{
		Peasant peasant;
		if (ProtectionParameters.NEW_PEASANT_GETS_BEST_PROTECTION_PROPORTION)
		{	
			peasant = buildPeasantWithBestProtectionProportion(); 
		}
		else
		{
			peasant = Peasant.buildPeasantWithContestFunctionAndRandomProtectionProportion(r);
		}
		return peasant;
	}
	protected Peasant buildPeasantWithBestProtectionProportion()
	{
		int lastPeasantIndex = peasants.size()-1;
//		if (lastPeasantIndex >= 0) 
//		{
			return new Peasant(peasants.get(lastPeasantIndex));
//		}
//		else 
//		{
//			throw new RuntimeException("RoleShiftingReplicatorDynamic.buildPeasant:  Not expecting to need Last Standing logic but attempted to replicate from empty list of Peasants.");
			//TODO delete this once a few scenarios have run
//			//TODO see Peasant; we need a better way
//			Peasant peasant = new Peasant();
//			peasant.inherit(peasant.getLastStanding()); 
//			return peasant; 
//		}
	}
}
