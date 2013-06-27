/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestBuilder
{

	public static List<Peasant> buildPeasantList()
	{
		List<Peasant> peasants = new ArrayList<Peasant>(); 
		peasants.add(buildPeasant(1));  
		peasants.add(buildPeasant(2));  
		peasants.add(buildPeasant(3));  
		peasants.add(buildPeasant(4));  
		return peasants;
	}

	public static List<Bandit> buildBanditList()
	{
		List<Bandit> bandits = new ArrayList<Bandit>(); 
		bandits.add(buildBandit(1));  
		bandits.add(buildBandit(2));  
		bandits.add(buildBandit(3));  
		return bandits;
	}
	public static Bandit buildBandit(int id)
	{
		Bandit bandit = new Bandit(); 
		bandit.setId(id); 
		bandit.getPredationBehavior(); 
		return bandit;
	}
	public static Peasant buildPeasant(int id)
	{
		return buildFullPeasant(id, 0.5, ProtectionFunctionEnum.POWER.buildFunction(2));
	}
	public static Peasant buildFullPeasant(int id, double protectionProportion, ProtectionFunction protectionFunction)
	{
		Peasant peasant = new Peasant(); 
		peasant.setId(id); 
		peasant.setProtectionProportion(protectionProportion);
		peasant.setFunction(protectionFunction);  
		return peasant;
	}

	public static InteractionPattern<Bandit, Peasant> buildPermutedRepeatableInteractionPattern(
			List<Bandit> bandits, List<Peasant> peasants)
	{
		InteractionPattern<Bandit, Peasant> pattern = new InteractionPattern<Bandit, Peasant>(bandits, peasants);
		pattern.setRandomForTesting(new Random(123)); 
		pattern.permute(); 
		return pattern;
	}

	public static List<Peasant> buildPeasantListWithRandomProtectionProportions(
			int numberPeasants, ProtectionFunction protectionFunction)
	{
		ProtectionParameters.resetRandomSeedForTesting(); 
		Random r  = new Random(ProtectionParameters.RANDOM_SEED); 
		List<Peasant> peasants = new ArrayList<Peasant>(); 
		Peasant peasant = null; 
		for (int i = 0; i < numberPeasants; i++)
		{
			peasant = new Peasant(); 
			peasant.setFunction(protectionFunction);
			peasant.setProtectionProportion(ProtectionParameters.PROTECTION_PARAMETER_INTERVAL_SIZE * r.nextInt(ProtectionParameters.PROTECTION_PARAMETER_NUMBER_INTERVALS));
			peasants.add(peasant);
		}
		return peasants;
	}

	public static List<Bandit> buildBanditList(int numberBandits)
	{
		List<Bandit> bandits = new ArrayList<Bandit>(); 
		Bandit bandit = null;
		for (int i = 0; i < numberBandits; i++)
		{
			bandit = new Bandit();
			bandits.add(bandit);
		}
		return bandits;
	}

	
}
