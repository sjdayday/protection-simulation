package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ProtectionStatistics
{
	protected static final int PEASANT = 1;
	protected static final int BANDIT = 2;
	private static Logger logger = Logger.getLogger(ProtectionStatistics.class);
	private double payoffDiscrepancyTolerance;
	public  int period;
	public  int numberBandits;
	public  int numberPeasants;
	public  double averageBanditPayoff;
	public  double averagePeasantPayoff;
	public  int actorAdjustment;
	public double banditPeasantPayoffDelta;

	protected List<StatisticsRecord> statisticsRecords;
	protected List<Peasant> peasants;
	protected List<Bandit> bandits;
	protected double adjustmentPercentage;
	private boolean populationSizesUnchanged;

	public ProtectionStatistics(List<Bandit> bandits, List<Peasant> peasants)
	{
		this.bandits = bandits; 
		this.peasants = peasants; 
		period = 1; 
		statisticsRecords = new ArrayList<StatisticsRecord>(); 
		calculate(); 
	}
	protected void calculateForTesting()
	{
		calculate(); 
	}
	private void calculate()
	{
		numberBandits = bandits.size(); 
		numberPeasants = peasants.size();
//		numberBandits = pattern.getSourceList().size(); 
//		numberPeasants = pattern.getTargetList().size();
		calculateAverageBanditPayoff(); 
		calculateAveragePeasantPayoff(); 
		calculateBanditPeasantPayoffDelta(); 
		calculatePeasantAdjustment();
		calculateAdditionalStatistics(); 
	}
	private void calculateBanditPeasantPayoffDelta()
	{
		banditPeasantPayoffDelta = (averageBanditPayoff - averagePeasantPayoff); 
	}
	protected void calculateAdditionalStatistics()
	{
		//TODO  refactor to abstract
	}
	private void calculateAverageBanditPayoff()
	{
		double sumPayoffs = 0; 
		for (Bandit bandit : bandits)
		{
			sumPayoffs += bandit.getPayoff();  
		}
		averageBanditPayoff = (numberBandits() > 0) ? sumPayoffs / numberBandits() : 0;
	}
	private void calculateAveragePeasantPayoff()
	{
		double sumPayoffs = 0; 
//		for (Peasant peasant : pattern.getTargetList())
		for (Heritable peasant : peasants)
		{
			sumPayoffs += peasant.getPayoff();  
		}
		averagePeasantPayoff = (numberPeasants() > 0) ? sumPayoffs / numberPeasants() : 0;
	}
	private void calculatePeasantAdjustment()
	{
		int adjustment = 0; 
		if (Math.abs(averageBanditPayoff - averagePeasantPayoff) <= payoffDiscrepancyTolerance) adjustment = 0; 
		else  adjustment = (averageBanditPayoff > averagePeasantPayoff) ? (getActorAdjustmentFactor(PEASANT)) : (getActorAdjustmentFactor(BANDIT));
		actorAdjustment = adjustment; 
	}
	public void updatePopulations(List<Bandit> bandits,
			List<Peasant> peasants)
	{
		setPeasants(peasants);
		setBandits(bandits);
		updateLastRecordAndCheckForStaticPopulation(bandits, peasants); 
		logger.debug("populations updated.");
	}
	private void updateLastRecordAndCheckForStaticPopulation(
			List<Bandit> bandits, List<Peasant> peasants)
	{
		StatisticsRecord record = getLastStatisticsRecord(); 
		if (record != null)
		{
			record.numberBanditsAfterReplication = bandits.size(); 
			record.numberPeasantsAfterReplication = peasants.size(); 
			checkPopulationStatic(record);
		logger.debug("populations after replications updated.");
		}
	}
	private void checkPopulationStatic(StatisticsRecord record)
	{
		if ((record.numberBandits == record.numberBanditsAfterReplication) &&
			(record.numberPeasants == record.numberPeasantsAfterReplication) &&
			(record.numberBandits != 0) &&
			(record.numberPeasants != 0))
		{
			populationSizesUnchanged = true;
		}
		else
		{
			populationSizesUnchanged = false; 
		}
	}
	protected StatisticsRecord getLastStatisticsRecord()
	{
		int last = getStatisticsRecords().size()-1; 
		return (last >= 0) ? getStatisticsRecords().get(last) : null;
	}
	protected int getActorAdjustmentFactor(int actor)
	{
		Double factor = 0d; 
		if (actor == PEASANT) 
		{
			factor = numberPeasants * adjustmentPercentage;
			if ((factor) < 1d) return 1;  
		}
		else 
		{
			factor = -1 * numberBandits * adjustmentPercentage;
			if ((factor) > -1d) return -1;  
		}
		return factor.intValue();
	}
	public void setAdjustmentFactorPercentage(double percentage)
	{
		if ((percentage < 0) || (percentage > 1.0d)) throw new IllegalArgumentException("ProtectionStatistics.setAdjustmentFactorPercentage:  valid percentages are from 0.0 to 1.0.  Received: "+percentage);  
		this.adjustmentPercentage = percentage; 
	}
	public void tick()
	{
		calculate(); 
		buildStatisticsRecord(); 
		period++; 
		logger.debug("tick completed.");
	}
	public int numberPeasants()
	{
		return numberPeasants; 
	}
	public double averagePeasantPayoff()
	{
		return averagePeasantPayoff;
	}
	public int numberBandits()
	{
		return numberBandits; 
	}
	public double averageBanditPayoff()
	{ 
		return averageBanditPayoff; 
	}
	public void setPayoffDiscrepancyTolerance(double payoffDiscrepancyTolerance)
	{
		this.payoffDiscrepancyTolerance = payoffDiscrepancyTolerance; 
	}
	public double getPayoffDiscrepancyTolerance()
	{
		return payoffDiscrepancyTolerance;
	}
	public int getActorAdjustment()
	{
		return actorAdjustment;
	}
	public int numberPeriods()
	{
		return period;
	}
	protected void buildStatisticsRecord()
	{
		statisticsRecords.add(new StatisticsRecord(period, numberBandits, numberPeasants, averageBanditPayoff, averagePeasantPayoff, banditPeasantPayoffDelta, actorAdjustment)); 
		logger.debug("statistics record added: "+statisticsRecords.size());  
	}
	public List<? extends StatisticsRecord> getStatisticsRecords()
	{
		return statisticsRecords;
	}
	public List<Peasant> getPeasants()
	{
		return peasants;
	}
	public void setPeasants(List<Peasant> peasants)
	{
		this.peasants = peasants;
	}
	public List<Bandit> getBandits()
	{
		return bandits;
	}
	public void setBandits(List<Bandit> bandits)
	{
		this.bandits = bandits;
	}
	public boolean populationSizesUnchanged() 
	{
		return populationSizesUnchanged;
	}
}
