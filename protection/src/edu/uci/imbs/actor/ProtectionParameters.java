/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.Random;

import org.apache.log4j.Logger;

public class ProtectionParameters
{
	private static Logger logger = Logger.getLogger(ProtectionParameters.class);
	public static long RANDOM_SEED = 0;
	public static boolean ROLE_SHIFTING;
	public static double PROTECTION_PARAMETER_INTERVAL_SIZE;
	public static int PROTECTION_PARAMETER_NUMBER_INTERVALS;
	public static double CONTEST_FUNCTION_GAMMA;
	public static double SURVIVE_THRESHOLD;
	public static double THRIVE_THRESHOLD;
	public static double PAYOFF_DISCREPANCY_TOLERANCE;
	public static double ADJUSTMENT_FACTOR_PERCENTAGE;
	public static int NUMBER_PEASANTS;
	public static int NUMBER_BANDITS;
	public static int RUN_LIMIT;
	public static boolean MIMIC_BETTER_PERFORMING_POPULATION;
	public static Random RANDOM;
	public static int MAX_PEASANTS_TO_PREY_UPON = 1;
	public static boolean MULTIPLE_BANDITS_PREY_ON_MULTIPLE_PEASANTS;
	public static int EQUILIBRIUM_NUMBER_PERIODS_WITHOUT_ADJUSTMENT;
	public static int MAXIMUM_POPULATION_SIZE;
	public static double COST_TO_PREY_ON_SINGLE_PEASANT;
	public static double MATCHING_FUNCTION_ALPHA_EXPONENT;
	public static double MATCHING_FUNCTION_BETA_EXPONENT;
	public static double MATCHING_FUNCTION_MU;
	public static boolean BANDITS_USE_MATCHING_FUNCTION;
	public static boolean NORMAL_INTERACTION_PATTERN;
	public static boolean FORCE_PEASANT_ALLOCATION_TO_HIGH_LOW;
	public static double FORCE_PEASANT_ALLOCATION_HIGH_PROPORTION;
	public static double FORCE_PEASANT_ALLOCATION_LOW_PROPORTION;
	public static int FORCE_PEASANT_ALLOCATION_LOW_INITIAL_PEASANTS;

	static 
	{
		RANDOM = new Random(RANDOM_SEED); 
	}
	
	public static void resetForTesting()
	{
		resetRandomSeedForTesting(); 
		PROTECTION_PARAMETER_INTERVAL_SIZE = .05; 
		PROTECTION_PARAMETER_NUMBER_INTERVALS = 21; 
		CONTEST_FUNCTION_GAMMA = 0.5; 
		ROLE_SHIFTING = false;
		SURVIVE_THRESHOLD = 0.1; 
		THRIVE_THRESHOLD = 0.3; 
		PAYOFF_DISCREPANCY_TOLERANCE = .005; 
		ADJUSTMENT_FACTOR_PERCENTAGE = 0.1;
		NUMBER_PEASANTS = 5; 
		NUMBER_BANDITS = 5; 
		RUN_LIMIT = 3; 
		MIMIC_BETTER_PERFORMING_POPULATION = true; 
		MAX_PEASANTS_TO_PREY_UPON = 10; 
		RANDOM = new Random(RANDOM_SEED); 
		MULTIPLE_BANDITS_PREY_ON_MULTIPLE_PEASANTS = false;
		EQUILIBRIUM_NUMBER_PERIODS_WITHOUT_ADJUSTMENT = 1; 
		MAXIMUM_POPULATION_SIZE = 25000; 
		COST_TO_PREY_ON_SINGLE_PEASANT = 0; 
		MATCHING_FUNCTION_ALPHA_EXPONENT = 1;
		MATCHING_FUNCTION_BETA_EXPONENT = 1;
		MATCHING_FUNCTION_MU = 1; 
		BANDITS_USE_MATCHING_FUNCTION = false; 
		NORMAL_INTERACTION_PATTERN = true; 
		FORCE_PEASANT_ALLOCATION_TO_HIGH_LOW = false; 
		FORCE_PEASANT_ALLOCATION_HIGH_PROPORTION = 0; 
		FORCE_PEASANT_ALLOCATION_LOW_PROPORTION = 0; 
		FORCE_PEASANT_ALLOCATION_LOW_INITIAL_PEASANTS = 0; 
		logger.debug("reset for testing completed."); 
	}

	public static void resetRandomSeedForTesting()
	{
		RANDOM_SEED = 123456789l;
	}

}
