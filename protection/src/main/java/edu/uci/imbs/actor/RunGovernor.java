/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import org.apache.log4j.Logger;

public class RunGovernor
{
	private static Logger logger = Logger.getLogger(RunGovernor.class);
	private int period;
	private ProtectionEquilibriumSeeker seeker;
	private int numberPeriodsWithEquilibrium;
	private RunGovernorEnum reason;
	private boolean populationSizesUnchanged;

	public RunGovernor(ProtectionEquilibriumSeeker seeker)
	{
		this.seeker = seeker; 
		period = 0; 
		numberPeriodsWithEquilibrium = 0; 
		this.reason = RunGovernorEnum.NOT_STOPPED; 
	}

	public boolean isStopping()
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
				RunGovernorEnum.BANDITS_EXCEEDED_MAX);  
	}
	private boolean peasantsExceededMaximum()
	{
		return stopping((seeker.getPeasantList().size() > ProtectionParameters.MAXIMUM_POPULATION_SIZE), 
				RunGovernorEnum.PEASANTS_EXCEEDED_MAX);  
	}
	private boolean eitherPopulationDroppedToZero()
	{
		return (banditsDroppedToZero() || peasantsDroppedToZero());
	}
	private boolean peasantsDroppedToZero()
	{
		return stopping((seeker.getPeasantList().size() == 0), RunGovernorEnum.PEASANTS_EXTINCT); 
	}
	private boolean banditsDroppedToZero()
	{
		return stopping((seeker.getBanditList().size() == 0), RunGovernorEnum.BANDITS_EXTINCT); 
	}
	private boolean equilibriumReachedForSpecifiedPeriods()
	{
		if (numberPeriodsWithEquilibrium == ProtectionParameters.EQUILIBRIUM_NUMBER_PERIODS_WITHOUT_ADJUSTMENT)
		{
			if (populationSizesUnchanged) return stopping(true, RunGovernorEnum.STATIC_EQUILIBRIUM_REACHED);
			else return stopping(true, RunGovernorEnum.EQUILIBRIUM_REACHED); 
		}
		else return stopping(false, RunGovernorEnum.NOT_STOPPED); //TODO maybe refactor stopping()  
	}
	private boolean runLimitReached()
	{
		return stopping((period >= ProtectionParameters.RUN_LIMIT), RunGovernorEnum.RUN_LIMIT_REACHED);
	}
	private boolean stopping(boolean condition, RunGovernorEnum reason)
	{
		boolean stopping = condition;
		if (stopping) 
		{
			this.reason = reason; 
			logger.info("Stopping because: "+reason.getReasonDescription()); 
		}
		else this.reason = RunGovernorEnum.NOT_STOPPED;
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
			populationSizesUnchanged = false; 
		}
		else 
		{
			numberPeriodsWithEquilibrium++; 
			if (seeker.populationSizesUnchanged()) populationSizesUnchanged = true;
			else populationSizesUnchanged = false;
		}
		logger.debug("Number periods with equilibrium: "+numberPeriodsWithEquilibrium); 
	}
	public RunGovernorEnum stop()
	{
		isStopping();
		return reason;
	}
}
