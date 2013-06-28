/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.List;

import org.apache.log4j.Logger;

public class DieSurviveThriveDynamic
{
	private static Logger logger = Logger.getLogger(DieSurviveThriveDynamic.class);
	private FitnessFunction fitnessFunction;

	public void setFitnessFunction(FitnessFunction fitnessFunction)
	{
		this.fitnessFunction = fitnessFunction; 
	}
	public <H> void replicate(Class<H> clazz, List<? extends Heritable> actors, List<H> newActors)
	{
		logger.debug("About to replicate actors");
		for (Heritable heritable : actors)
		{
			FitnessEnum fitness = fitnessFunction.evaluate(heritable.getPayoff()); 
			switch (fitness)
			{
			case DIE:
			{
				logger.debug("Actor about to die: "+heritable.toString()); 
				// nothing replicated to new list
			}
				break;
			case SURVIVE: 
			{
				logger.debug("Actor about to survive: "+heritable.toString()); 
				newActors.add(replicate(clazz, heritable));
			}	
				break;
			case THRIVE:  
			{
				logger.debug("Actor about to thrive: "+heritable.toString()); 
				newActors.add(replicate(clazz, heritable));
				newActors.add(replicate(clazz, heritable));
			}	
				break;

			default:
				break;
			}
		}
	}

	private <H> H replicate(Class<H> clazz, Heritable oldHeritable)
	{
		H heritable = null; 
		try
		{
			heritable = clazz.newInstance();
			((Heritable) heritable).inherit(oldHeritable); //TODO smartguys: can generics do away with this cast? 
			logger.debug("New actor replicated:"+heritable.toString()+" inheriting from: "+oldHeritable.toString()); 
		}
		catch (Exception e)
		{
			String msg = "ReplicatorDynamic.replicate:  Exception instantiating or casting to Heritable.  Details: \n"+e.getMessage(); 
			throw new RuntimeException(msg);
		}
		return heritable;
	}

}
