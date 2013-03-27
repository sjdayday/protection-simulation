package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public  class ProtectionEquilibriumSeeker
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ProtectionEquilibriumSeeker.class);
	protected List<Peasant> peasantList; 
	protected List<Bandit> banditList; 
	protected InteractionPattern<Bandit, Peasant> interactionPattern;
	protected ProtectionStatistics protectionStatistics;
	protected boolean done;
	private List<Dynamic> dynamics;
	private RunGovernor governor; 
	static 
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.DEBUG);
	}

	public ProtectionEquilibriumSeeker()
	{
		done = false; 
		dynamics = new ArrayList<Dynamic>(); 
		governor = new RunGovernor(this); 
	}
	public void runToEquilibriumOrLimit() throws BehaviorException
	{
		while (!governor.stop())
		{
			banditsPreyOnPeasants();
			protectionStatistics.tick();  // dynamics need the calculated stats; we cut the record here 
			runDynamicsAgainstPopulation(); // population level changes here	
			updatePopulationLists();  // prior to updating the populations
			tickActors();  // perhaps periods and 
			governor.tick(); 
		}
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
			dynamic.setPopulation(protectionPopulation);
			protectionPopulation = dynamic.rebuildPopulation(); 
		}
		banditList = protectionPopulation.getBandits(); 
		peasantList = protectionPopulation.getPeasants(); 
	}
	protected void updatePopulationLists()
	{
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
		if (protectionStatistics.getPeasantAdjustment() != 0) return true;  
		else return false; 
	}

}
