/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class BehaviorBanditPreysOnMultiplePeasants extends AbstractBehavior
		implements PredationBehavior
{
	private static Logger logger = Logger.getLogger(BehaviorBanditPreysOnMultiplePeasants.class);
	private int numberPeasantsToPreyUpon;
	private double predationEffort;
	private List<ProtectionBehavior> targets;
	private double totalPayoff;


	public BehaviorBanditPreysOnMultiplePeasants(Actor bandit)
	{	
		setActor(bandit); 
		this.numberPeasantsToPreyUpon = ProtectionParameters.RANDOM.nextInt(ProtectionParameters.MAX_PEASANTS_TO_PREY_UPON)+1; 
		this.predationEffort = 1d / new Double(numberPeasantsToPreyUpon); 
		resetTargets(); 
	}
	private void resetTargets()
	{
		targets = new ArrayList<ProtectionBehavior>();
	}
	@Override
	public Outcome behave() throws BehaviorException
	{
		return null;
	}

	@Override
	public BehaviorEnum getType()
	{
		return BehaviorEnum.BANDIT_PREYS_ON_MULTIPLE_PEASANTS;
	}

	public int getNumberOfPeasantsToPreyUpon()
	{
		return numberPeasantsToPreyUpon;
	}

	@Override
	public double getPredationEffort()
	{
		return predationEffort;
	}
	@Override
	public void addTarget(ProtectionBehavior protectionBehavior)
	{
		targets.add(protectionBehavior); 
		protectionBehavior.addThreat(this); 
	}

	@Override
	public PredationOutcome preyOnTargets() throws BehaviorException
	{
		double payoff = 0; 
		totalPayoff = 0; 
		for (ProtectionBehavior protectionBehavior : targets)
		{
			payoff = protectionBehavior.surrenderPayoff(this).getPayoff(); 
			totalPayoff += payoff;
			logger.debug("PredationBehavior for "+getActor().toString()+" : payoff "+payoff+" received from target "+protectionBehavior.getActor().toString());
		}
		decreasePayoffsByCost();
		((Bandit) getActor()).setPayoff(totalPayoff); 
		return new BanditPredationOutcome(totalPayoff);
	}
	private void decreasePayoffsByCost()
	{
		if (ProtectionParameters.COST_TO_PREY_ON_SINGLE_PEASANT > 0)
		{
				totalPayoff -= numberPeasantsToPreyUpon*ProtectionParameters.COST_TO_PREY_ON_SINGLE_PEASANT; 
				if (totalPayoff < 0) totalPayoff = 0; 
		}
	}
	@Override
	public List<ProtectionBehavior> getTargets()
	{
		return targets;
	}


	@Override
	public void inherit(Heritable heritable)
	{
		validateHeritable(this.getClass(), heritable);
		numberPeasantsToPreyUpon = ((BehaviorBanditPreysOnMultiplePeasants) heritable).getNumberOfPeasantsToPreyUpon();
	}


	@Override
	public double getPayoff()
	{
		return 0;
	}


	@Override
	public void tick()
	{
		resetTargets(); 
	}
	@Override
	public String toString() {
		return " number of peasants to prey upon: "+numberPeasantsToPreyUpon+"; predation effort: "+predationEffort+"; total payoff: "+totalPayoff;
	}
}
