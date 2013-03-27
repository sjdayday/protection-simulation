package edu.uci.imbs.actor;

import org.apache.log4j.Logger;

public class RunGovernor
{
	private static Logger logger = Logger.getLogger(RunGovernor.class);
	private int period;
	private ProtectionEquilibriumSeeker seeker;
	private int numberPeriodsWithEquilibrium;

	public RunGovernor(ProtectionEquilibriumSeeker seeker)
	{
		this.seeker = seeker; 
		period = 0; 
		numberPeriodsWithEquilibrium = 0; 
	}

	public boolean stop()
	{
		return (runLimitReached() || equilibriumReachedForSpecifiedPeriods() ||  eitherPopulationDroppedToZero() || eitherPopulationExceededMaximum());
	}

	private boolean eitherPopulationExceededMaximum()
	{
		return (banditsExceededMaximum() || peasantsExceededMaximum());
	}
	private boolean banditsExceededMaximum()
	{
		return stopping((seeker.getBanditList().size() > ProtectionParameters.MAXIMUM_POPULATION_SIZE), 
				"number of bandits ("+seeker.getBanditList().size()+") exceeded maximum population size."); 
	}
	private boolean peasantsExceededMaximum()
	{
		return stopping((seeker.getPeasantList().size() > ProtectionParameters.MAXIMUM_POPULATION_SIZE), 
				"number of peasants ("+seeker.getPeasantList().size()+") exceeded maximum population size."); 
	}
	private boolean eitherPopulationDroppedToZero()
	{
		return (banditsDroppedToZero() || peasantsDroppedToZero());
	}
	private boolean peasantsDroppedToZero()
	{
		return stopping((seeker.getPeasantList().size() == 0), "number of peasants dropped to zero."); 
	}
	private boolean banditsDroppedToZero()
	{
		return stopping((seeker.getBanditList().size() == 0), "number of bandits dropped to zero."); 
	}
	private boolean equilibriumReachedForSpecifiedPeriods()
	{
		return stopping((numberPeriodsWithEquilibrium == ProtectionParameters.EQUILIBRIUM_NUMBER_PERIODS_WITHOUT_ADJUSTMENT), 
				"equilibrium reached for specified periods."); 
	}
	private boolean runLimitReached()
	{
		return stopping((period >= ProtectionParameters.RUN_LIMIT), "run limit reached.");
	}
	private boolean stopping(boolean condition, String reason)
	{
		boolean stopping = condition;
		if (stopping) logger.info("Stopping because "+reason); 
		return  stopping;
	}

	public void tick()
	{
		this.period++; 
		calculateConsecutivePeriodsWithoutAdjustment(); 
		logger.debug("tick completed for period: "+period);
	}

	private void calculateConsecutivePeriodsWithoutAdjustment()
	{
		if (seeker.hasAdjustedThisPeriod())
		{
			numberPeriodsWithEquilibrium = 0; 
		}
		else 
		{
			numberPeriodsWithEquilibrium++; 
		}
		logger.debug("Number periods with equilibrium: "+numberPeriodsWithEquilibrium); 
	}
}
