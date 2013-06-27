/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultipleBehaviorInteractionPattern 
{

	private List<Bandit> bandits;
	private List<Peasant> peasants;
	private List<PredationBehavior> predationBehaviors;
	private ArrayList<ProtectionBehavior> protectionBehaviors;
	private Random random;

//	public InteractionPair<S, T> next()
//	public boolean hasNext()
//	public void permute()

	
	public MultipleBehaviorInteractionPattern(List<Bandit> bandits, List<Peasant> peasants)
	{
		this.bandits = bandits;
		this.peasants = peasants; 
		verifyNotNull();
		predationBehaviors = new ArrayList<PredationBehavior>(); 
		protectionBehaviors = new ArrayList<ProtectionBehavior>();  
		buildBehaviors(); 
	}
	private void verifyNotNull()
	{
		if ((bandits == null) || (peasants == null)) throw new IllegalArgumentException("MultipleBehaviorInteractionPattern: input lists of bandits and peasants must not be null."); 
	}
	private void buildBehaviors()
	{
		for (Bandit bandit : bandits)
		{
			predationBehaviors.add(bandit.getPredationBehavior()); 
		}
		for (Peasant peasant : peasants)
		{
			protectionBehaviors.add(peasant.getProtectionBehavior()); 
		}
	}
	public List<PredationBehavior> getPredationBehaviors()
	{
		return predationBehaviors;
	}
	public List<ProtectionBehavior> getProtectionBehaviors()
	{
		return protectionBehaviors;
	}
	public void permute() throws BehaviorException
	{
		if (random == null) random = new Random(ProtectionParameters.RANDOM_SEED); //TODO consider removing this dependency by passing in the seed
		for (PredationBehavior predationBehavior : predationBehaviors)
		{
			Collections.shuffle(protectionBehaviors, random); 
			allocateProtectionBehaviors(predationBehavior);
		}
	}
	private void allocateProtectionBehaviors(PredationBehavior predationBehavior) throws BehaviorException
	{
		String type = "null"; 
		if (predationBehavior != null)  type = predationBehavior.getClass().getName();
		if (!(predationBehavior instanceof BehaviorBanditPreysOnMultiplePeasants)) throw new BehaviorException("MultipleBehaviorInteractionPattern.allocateProtectionBehaviors: Expected BehaviorBanditPreysOnMultiplePeasants but found "+type); 
		BehaviorBanditPreysOnMultiplePeasants banditBehavior = (BehaviorBanditPreysOnMultiplePeasants) predationBehavior; 
		int predationLimit = predationLimit(banditBehavior);
		for (int i = 0; i < predationLimit; i++)
		{
			banditBehavior.addTarget(protectionBehaviors.get(i)); 
		}
	}
	private int predationLimit(BehaviorBanditPreysOnMultiplePeasants banditBehavior)
	{
		int limit = 0;
		int listSize = protectionBehaviors.size(); 
		int maxTargets = banditBehavior.getNumberOfPeasantsToPreyUpon();  
		limit = maxTargets; 
		if (ProtectionParameters.BANDITS_USE_MATCHING_FUNCTION)
		{
			Double limitDouble = maxTargets * percentSuccessfulPredationPreyMatches();  
			limit = limitDouble.intValue(); 
		}
		if (limit > listSize) limit = listSize; 
		return limit;
	}
	protected double percentSuccessfulPredationPreyMatches()
	{
		if ((!ProtectionParameters.BANDITS_USE_MATCHING_FUNCTION) || (bandits.size() == 0)) return 1.0; 
		else
		{
			ProtectionFunction matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(new double[]{ProtectionParameters.MATCHING_FUNCTION_ALPHA_EXPONENT, ProtectionParameters.MATCHING_FUNCTION_BETA_EXPONENT, 1}); 
			double numberMatches = matchingFunction.evaluate(new double[]{bandits.size(), peasants.size()});
			double percent = numberMatches / bandits.size(); 
			if (percent > 1) percent = 1; 
			return percent;
		}
	}

}
