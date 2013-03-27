package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MultipleBehaviorInteractionPatternTest
{
	private List<Bandit> bandits;
	private List<Peasant> peasants;
	private MultipleBehaviorInteractionPattern pattern;
	private List<PredationBehavior> predationBehaviors;
	private List<ProtectionBehavior> protectionBehaviors;
	@BeforeClass
	public static void setUpLog4J() throws Exception
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
	}
	@Before
	public void setUp() throws Exception
	{
		ProtectionParameters.resetForTesting();
		bandits = TestBuilder.buildBanditList();
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(10, ProtectionFunctionEnum.CONTEST.buildFunction(0.5));
		assertEquals(6, ((BehaviorBanditPreysOnMultiplePeasants) bandits.get(0).getPredationBehavior()).getNumberOfPeasantsToPreyUpon());
		assertEquals(1, ((BehaviorBanditPreysOnMultiplePeasants) bandits.get(1).getPredationBehavior()).getNumberOfPeasantsToPreyUpon());
		assertEquals(4, ((BehaviorBanditPreysOnMultiplePeasants) bandits.get(2).getPredationBehavior()).getNumberOfPeasantsToPreyUpon());
	}
	@Test
	public void verifyMultipleBehaviorsMatched() throws Exception
	{
		pattern = new MultipleBehaviorInteractionPattern(bandits, peasants); 
		predationBehaviors = pattern.getPredationBehaviors(); 
		assertEquals(3, predationBehaviors.size()); 
		protectionBehaviors = pattern.getProtectionBehaviors();
		assertEquals(10, protectionBehaviors.size()); 
		pattern.permute(); 
		assertEquals(6, pattern.getPredationBehaviors().get(0).getTargets().size());
		assertEquals(1, pattern.getPredationBehaviors().get(1).getTargets().size());
		assertEquals(4, pattern.getPredationBehaviors().get(2).getTargets().size());
		for (PredationBehavior predationBehavior : pattern.getPredationBehaviors())
		{
			predationBehavior.preyOnTargets(); 
		}
		verifyExpectedPayoffs();
	}
	@Test
	public void verifyBehaviorForEmptyList() throws Exception
	{
		peasants = new ArrayList<Peasant>(); 
		pattern = new MultipleBehaviorInteractionPattern(bandits, peasants); 
		predationBehaviors = pattern.getPredationBehaviors(); 
		assertEquals(3, predationBehaviors.size()); 
		protectionBehaviors = pattern.getProtectionBehaviors();
		assertEquals(0, protectionBehaviors.size()); 
		pattern.permute(); 
		assertEquals(0, pattern.getPredationBehaviors().get(0).getTargets().size());
		assertEquals(0, pattern.getPredationBehaviors().get(1).getTargets().size());
		assertEquals(0, pattern.getPredationBehaviors().get(2).getTargets().size());
	}
	@Test
	public void verifyBehaviorForShortList() throws Exception
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(2, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); 
		pattern = new MultipleBehaviorInteractionPattern(bandits, peasants); 
		predationBehaviors = pattern.getPredationBehaviors(); 
		assertEquals(3, predationBehaviors.size()); 
		protectionBehaviors = pattern.getProtectionBehaviors();
		assertEquals(2, protectionBehaviors.size()); 
		pattern.permute(); 
		assertEquals(2, pattern.getPredationBehaviors().get(0).getTargets().size());
		assertEquals(1, pattern.getPredationBehaviors().get(1).getTargets().size());
		assertEquals(2, pattern.getPredationBehaviors().get(2).getTargets().size());
	}
	@Test // (expected=IllegalArgumentException.class)
	public void verifyNeitherInputListIsNull() throws Exception
	{
		verifyListNotNull(bandits, null); 
		verifyListNotNull(null, peasants); 
		verifyListNotNull(null, null); 
	}
	@Test
	public void verifyCalculateSuccessfulPredationPreyMatchesAsPercentageOfNumberOfBandits() throws Exception
	{
		ProtectionParameters.MATCHING_FUNCTION_ALPHA_EXPONENT = 0.33;
		ProtectionParameters.MATCHING_FUNCTION_BETA_EXPONENT = 0.33;
		buildPatternWithBanditsPeasants(10, 10); 
		assertEquals("default is 1.0", 1.0, pattern.percentSuccessfulPredationPreyMatches(), .001); 
		ProtectionParameters.BANDITS_USE_MATCHING_FUNCTION = true;
		assertEquals("1*10^.33*10^.33 = 4.57 / 10 bandits = .457", .457, pattern.percentSuccessfulPredationPreyMatches(), .001); 
		buildPatternWithBanditsPeasants(10, 3); 
		assertEquals("fewer peasants than bandits: 1*3^.33*10^.33 = 3.07 / 10 bandits = .307", .307, pattern.percentSuccessfulPredationPreyMatches(), .001); 
		buildPatternWithBanditsPeasants(3, 10); 
		assertEquals("fewer bandits than peasants: 1*3^.33*10^.33 = 3.07 / 3 bandits = 1+, so round to 1.0 (max)", 
				1.0, pattern.percentSuccessfulPredationPreyMatches(), .001); 
	}
	@Test
	public void verifyMatchingFunctionReducesNumberOfBehaviorsMatched() throws Exception
	{
		ProtectionParameters.MATCHING_FUNCTION_ALPHA_EXPONENT = 0.33;
		ProtectionParameters.MATCHING_FUNCTION_BETA_EXPONENT = 0.33;
		ProtectionParameters.BANDITS_USE_MATCHING_FUNCTION = true; 
		buildPatternWithBanditsPeasants(10, 10); 
		assertEquals("1*10^.33*10^.33 = 4.57 / 10 = .457", .457, pattern.percentSuccessfulPredationPreyMatches(), .001); 
		predationBehaviors = pattern.getPredationBehaviors(); 
		assertEquals(10, predationBehaviors.size()); 
		protectionBehaviors = pattern.getProtectionBehaviors();
		assertEquals(10, protectionBehaviors.size()); 
		pattern.permute(); 
		assertEquals("5 * .457 rounded down", 2, pattern.getPredationBehaviors().get(0).getTargets().size());
		assertEquals("1 * .457 rounded down", 0, pattern.getPredationBehaviors().get(1).getTargets().size());
		assertEquals("5 * .457 rounded down", 2, pattern.getPredationBehaviors().get(2).getTargets().size());

	}
	private void buildPatternWithBanditsPeasants(int numberBandits, int numberPeasants)
	{
		peasants = TestBuilder.buildPeasantListWithRandomProtectionProportions(numberPeasants, ProtectionFunctionEnum.CONTEST.buildFunction(0.5)); 
		bandits = TestBuilder.buildBanditList(numberBandits); 
		pattern = new MultipleBehaviorInteractionPattern(bandits, peasants); 
	}
	private void verifyListNotNull(List<Bandit> bandits, List<Peasant> peasants)
	{
		try 
		{
			pattern = new MultipleBehaviorInteractionPattern(bandits, peasants); 
			fail("should throw");
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("MultipleBehaviorInteractionPattern: input lists of bandits and peasants must not be null.", e.getMessage());
		}
	}
	private void verifyExpectedPayoffs()
	{
		assertEquals(1.349, bandits.get(0).getPayoff(), .001); 
		assertEquals(0.857, bandits.get(1).getPayoff(), .001); 
		assertEquals(0.994, bandits.get(2).getPayoff(), .001); 
		assertEquals(0.95, peasants.get(0).getPayoff(), .001); 
		assertEquals(0.0, peasants.get(1).getPayoff(), .001); 
		assertEquals(0.0, peasants.get(2).getPayoff(), .001); 
		assertEquals(0.5, peasants.get(3).getPayoff(), .001); 
		assertEquals(0.068, peasants.get(4).getPayoff(), .001); 
		assertEquals(0.649, peasants.get(5).getPayoff(), .001); 
		assertEquals(0.0, peasants.get(6).getPayoff(), .001); 
		assertEquals(0.158, peasants.get(7).getPayoff(), .001); 
		assertEquals(0.278, peasants.get(8).getPayoff(), .001); 
		assertEquals(0.293, peasants.get(9).getPayoff(), .001); 
	}
}
//Bandit: 1  Payoff: 1.3493929217256524
//Bandit: 2  Payoff: 0.8571428571428571
//Bandit: 3  Payoff: 0.9943274778265736
//Peasant: 3  Protection Proportion: 0.05  Protection Function: Contest: gamma=0.5 weight=1.0  Payoff: 0.95  Unprotected Payoff: 0.9047619047619048  Surrendered Payoff: 0.0  Surrendered: false
//Peasant: 4  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=1.1666666666666667  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true
//Peasant: 5  Protection Proportion: 1.0  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 0.0  Surrendered: true
//Peasant: 6  Protection Proportion: 0.5  Protection Function: Contest: gamma=0.5 weight=1.0  Payoff: 0.5  Unprotected Payoff: 0.33333333333333337  Surrendered Payoff: 0.0  Surrendered: false
//Peasant: 7  Protection Proportion: 0.9  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.06835443037974683  Unprotected Payoff: 0.0  Surrendered Payoff: 0.03164556962025315  Surrendered: true
//Peasant: 8  Protection Proportion: 0.35000000000000003  Protection Function: Contest: gamma=0.5 weight=1.0  Payoff: 0.6499999999999999  Unprotected Payoff: 0.4814814814814814  Surrendered Payoff: 0.0  Surrendered: false
//Peasant: 9  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true
//Peasant: 10  Protection Proportion: 0.05  Protection Function: Contest: gamma=0.5 weight=0.25  Payoff: 0.15833333333333335  Unprotected Payoff: 0.0  Surrendered Payoff: 0.7916666666666666  Surrendered: true
//Peasant: 11  Protection Proportion: 0.65  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.2785714285714285  Unprotected Payoff: 0.0  Surrendered Payoff: 0.07142857142857145  Surrendered: true
//Peasant: 12  Protection Proportion: 0.4  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.2938775510204082  Unprotected Payoff: 0.0  Surrendered Payoff: 0.3061224489795918  Surrendered: true

//  for peasant 12:  p(x) = .4 / .4+.416 = .490 * (1-x) .6 = .294 peasant's protected payoff
//   1-p(x) = .510 * .6 = .306 unprotected payoff; pro-rated for bandit 1:  .122, and for bandit 3:  .189

//0 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 12  Protection Proportion: 0.4  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.2938775510204082  Unprotected Payoff: 0.0  Surrendered Payoff: 0.3061224489795918  Surrendered: true; payoff 0.12244897959183672 surrendered to threat Bandit: 1  Payoff: 0.0
//2 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 1  Payoff: 0.0 : payoff 0.12244897959183672 received from target Peasant: 12  Protection Proportion: 0.4  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.2938775510204082  Unprotected Payoff: 0.0  Surrendered Payoff: 0.3061224489795918  Surrendered: true
//2 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 5  Protection Proportion: 1.0  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 0.0  Surrendered: true; payoff 0.0 surrendered to threat Bandit: 1  Payoff: 0.0
//2 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 1  Payoff: 0.0 : payoff 0.0 received from target Peasant: 5  Protection Proportion: 1.0  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 0.0  Surrendered: true
//3 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 4  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=1.1666666666666667  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true; payoff 0.14285714285714285 surrendered to threat Bandit: 1  Payoff: 0.0
//3 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 1  Payoff: 0.0 : payoff 0.14285714285714285 received from target Peasant: 4  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=1.1666666666666667  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true
//3 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 9  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true; payoff 1.0 surrendered to threat Bandit: 1  Payoff: 0.0
//3 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 1  Payoff: 0.0 : payoff 1.0 received from target Peasant: 9  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true
//3 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 7  Protection Proportion: 0.9  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.06835443037974683  Unprotected Payoff: 0.0  Surrendered Payoff: 0.03164556962025315  Surrendered: true; payoff 0.012658227848101262 surrendered to threat Bandit: 1  Payoff: 0.0
//4 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 1  Payoff: 0.0 : payoff 0.012658227848101262 received from target Peasant: 7  Protection Proportion: 0.9  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.06835443037974683  Unprotected Payoff: 0.0  Surrendered Payoff: 0.03164556962025315  Surrendered: true
//4 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 11  Protection Proportion: 0.65  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.2785714285714285  Unprotected Payoff: 0.0  Surrendered Payoff: 0.07142857142857145  Surrendered: true; payoff 0.07142857142857145 surrendered to threat Bandit: 1  Payoff: 0.0
//4 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 1  Payoff: 0.0 : payoff 0.07142857142857145 received from target Peasant: 11  Protection Proportion: 0.65  Protection Function: Contest: gamma=0.5 weight=0.16666666666666666  Payoff: 0.2785714285714285  Unprotected Payoff: 0.0  Surrendered Payoff: 0.07142857142857145  Surrendered: true
//4 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 4  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=1.1666666666666667  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true; payoff 0.8571428571428571 surrendered to threat Bandit: 2  Payoff: 0.0
//4 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 2  Payoff: 0.0 : payoff 0.8571428571428571 received from target Peasant: 4  Protection Proportion: 0.0  Protection Function: Contest: gamma=0.5 weight=1.1666666666666667  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 1.0  Surrendered: true
//5 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 7  Protection Proportion: 0.9  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.06835443037974683  Unprotected Payoff: 0.0  Surrendered Payoff: 0.03164556962025315  Surrendered: true; payoff 0.018987341772151896 surrendered to threat Bandit: 3  Payoff: 0.0
//5 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 3  Payoff: 0.0 : payoff 0.018987341772151896 received from target Peasant: 7  Protection Proportion: 0.9  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.06835443037974683  Unprotected Payoff: 0.0  Surrendered Payoff: 0.03164556962025315  Surrendered: true
//5 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 5  Protection Proportion: 1.0  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 0.0  Surrendered: true; payoff 0.0 surrendered to threat Bandit: 3  Payoff: 0.0
//5 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 3  Payoff: 0.0 : payoff 0.0 received from target Peasant: 5  Protection Proportion: 1.0  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.0  Unprotected Payoff: 0.0  Surrendered Payoff: 0.0  Surrendered: true
//6 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 12  Protection Proportion: 0.4  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.2938775510204082  Unprotected Payoff: 0.0  Surrendered Payoff: 0.3061224489795918  Surrendered: true; payoff 0.1836734693877551 surrendered to threat Bandit: 3  Payoff: 0.0
//6 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 3  Payoff: 0.0 : payoff 0.1836734693877551 received from target Peasant: 12  Protection Proportion: 0.4  Protection Function: Contest: gamma=0.5 weight=0.41666666666666663  Payoff: 0.2938775510204082  Unprotected Payoff: 0.0  Surrendered Payoff: 0.3061224489795918  Surrendered: true
//6 [main] DEBUG edu.uci.imbs.actor.BehaviorPeasantDefendsAgainstMultipleBandits  - ProtectionBehavior for Peasant: 10  Protection Proportion: 0.05  Protection Function: Contest: gamma=0.5 weight=0.25  Payoff: 0.15833333333333335  Unprotected Payoff: 0.0  Surrendered Payoff: 0.7916666666666666  Surrendered: true; payoff 0.7916666666666666 surrendered to threat Bandit: 3  Payoff: 0.0
//6 [main] DEBUG edu.uci.imbs.actor.BehaviorBanditPreysOnMultiplePeasants  - PredationBehavior for Bandit: 3  Payoff: 0.0 : payoff 0.7916666666666666 received from target Peasant: 10  Protection Proportion: 0.05  Protection Function: Contest: gamma=0.5 weight=0.25  Payoff: 0.15833333333333335  Unprotected Payoff: 0.0  Surrendered Payoff: 0.7916666666666666  Surrendered: true

