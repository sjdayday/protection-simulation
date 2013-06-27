/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public  class ProtectionEquilibriumSeeker
{
	private static Logger logger = Logger.getLogger(ProtectionEquilibriumSeeker.class);
	protected List<Peasant> peasantList; 
	protected List<Bandit> banditList; 
	protected InteractionPattern<Bandit, Peasant> interactionPattern;
	protected ProtectionStatistics protectionStatistics;
	protected boolean done;
	private List<Dynamic> dynamics;
	private RunGovernor governor; 

	public ProtectionEquilibriumSeeker()
	{
		done = false; 
		dynamics = new ArrayList<Dynamic>(); 
		governor = new RunGovernor(this); 
	}
	public RunGovernorEnum runToEquilibriumOrLimit() throws BehaviorException
	{
//			while (!governor.stop())
		while (governor.stop().equals(RunGovernorEnum.NOT_STOPPED))
		{
			banditsPreyOnPeasants();
			protectionStatistics.tick();  // dynamics need the calculated stats; we cut the record here 
			runDynamicsAgainstPopulation(); // population level changes here	
			updatePopulationLists();  // prior to updating the populations
			tickActors();  // perhaps periods and 
			governor.tick(); 
		}
		return governor.stop(); 
//		protectionStatistics.tick(); 
	}
	protected void banditsPreyOnPeasants() throws BehaviorException
	{
		InteractionPair<Bandit, Peasant> pair = null;
		while (interactionPattern.hasNext())
		{
			pair = interactionPattern.next();
			pair.getSource().prey(pair.getTarget());
		}
	}
	protected void tickActors()
	{
		for (Actor peasant : peasantList)
		{
			peasant.tick();
		}
		for (Bandit bandit: banditList)
		{
			bandit.tick();
		}
	}
	public void setPopulation(ProtectionPopulation protectionPopulation)
	{
		this.banditList = protectionPopulation.getBandits(); 
		this.peasantList = protectionPopulation.getPeasants();
	}
	public void addDynamic(Dynamic dynamic)
	{
		dynamics.add(dynamic); 
	}
	public void runDynamicsAgainstPopulation()
	{
		ProtectionPopulation protectionPopulation = new ProtectionPopulation(banditList, peasantList); 
		for (Dynamic dynamic : dynamics)
		{
			logger.debug("ProtectionEquilibriumSeeker.runDynamicsAgainstPopulation: invoking dynamic "+dynamic.toString()); 
			dynamic.setPopulation(protectionPopulation);
			protectionPopulation = dynamic.rebuildPopulation(); 
		}
		banditList = protectionPopulation.getBandits(); 
		peasantList = protectionPopulation.getPeasants(); 
	}
	protected void updatePopulationLists()
	{
		logger.debug("ProtectionEquilibriumSeeker.updatePopulationLists: updating populations: bandits: "+banditList.hashCode()+" size: "+banditList.size()+" peasants: "+peasantList.hashCode()+" size: "+peasantList.size()); 
		interactionPattern.updatePopulations(banditList, peasantList);
		interactionPattern.permute(); 
		protectionStatistics.updatePopulations(banditList, peasantList);
	}
	public List<Peasant> getPeasantList()
	{
		return peasantList;
	}
	public void setPeasantList(List<Peasant> peasantList)
	{
		this.peasantList = peasantList;
	}
	public List<Bandit> getBanditList()
	{
		return banditList;
	}
	public void setBanditList(List<Bandit> banditList)
	{
		this.banditList = banditList;
	}
	public InteractionPattern<Bandit, Peasant> getInteractionPattern()
	{
		return interactionPattern;
	}
	public void setInteractionPattern(
			InteractionPattern<Bandit, Peasant> interactionPattern)
	{
		this.interactionPattern = interactionPattern;
	}
	public ProtectionStatistics getProtectionStatistics()
	{
		return protectionStatistics;
	}
	public void setProtectionStatistics(ProtectionStatistics protectionStatistics)
	{
		this.protectionStatistics = protectionStatistics;
	}
	public void setRunLimit(int limit)
	{
		ProtectionParameters.RUN_LIMIT = limit;
	}
	public List<Dynamic> getDynamics()
	{
		return dynamics;
	}
	public boolean hasAdjustedThisPeriod()
	{
		if (protectionStatistics.getActorAdjustment() != 0) return true;  
		else return false; 
	}
	public boolean populationSizesUnchanged()
	{
		return protectionStatistics.populationSizesUnchanged();
	}

}
