package edu.uci.imbs.actor;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class BehaviorPeasantDefendsAgainstMultipleBandits extends AbstractBehavior implements ProtectionBehavior
{
	private static Logger logger = Logger.getLogger(BehaviorPeasantDefendsAgainstMultipleBandits.class);
	private Peasant peasant;
	private double totalPayoffToSurrender;
	private boolean payoffLoaded;
	private double weight;
	private Map<PredationBehavior, Double> predationBehaviorMap;

	public BehaviorPeasantDefendsAgainstMultipleBandits(Actor peasant)
	{
		setActor(peasant); 
		payoffLoaded = false; 
		predationBehaviorMap = new HashMap<PredationBehavior, Double>(); 
	}
	public void validate(Actor peasant)
	{
		if (!(peasant instanceof Peasant)) 
		{
			String obj = "null"; 
			if (peasant != null) obj = peasant.getClass().getName();
			throw new IllegalArgumentException("BehaviorPeasantDefendsAgainstMultipleBandits:  must be initialized with a Peasant; was "+obj);
		}
	}
	@Override
	public Actor getActor() throws BehaviorException
	{
		return peasant;
	}
	@Override
	public void setActor(Actor peasant)
	{
		validate(peasant); 
		this.peasant = (Peasant) peasant; 
	}
	@Override
	public PredationOutcome behave() throws BehaviorException
	{
		return new BanditPredationOutcome(peasant.surrenderUnprotectedPayoff());
	}
	@Override
	public BehaviorEnum getType()
	{
		return BehaviorEnum.PEASANT_DEFENDS_AGAINST_MULTIPLE_BANDITS;
	}
	@Override
	public void addThreat(PredationBehavior predationBehavior)
	{
		predationBehaviorMap.put(predationBehavior, predationBehavior.getPredationEffort()); 
		updateContestFunctionWeight(); 
	}
	private void updateContestFunctionWeight()
	{
		if (!(peasant.getFunction() instanceof ContestFunction)) throw new IllegalArgumentException("BehaviorPeasantDefendsAgainstMultipleBandits:  Peasant must have contest function; was: "+peasant.getFunction().toString());
		weight = 0; 
		for (Double weight : predationBehaviorMap.values())
		{
			this.weight += weight;  
		}
		double gamma = peasant.getFunction().getParameters()[ContestFunction.GAMMA]; 
		peasant.setFunction(ProtectionFunctionEnum.CONTEST.buildFunction(new double[]{gamma, weight})); 
	}
	@Override
	public Map<PredationBehavior, Double> getThreatMap()
	{
		return predationBehaviorMap;
	}
	@Override
	public PredationOutcome surrenderPayoff(PredationBehavior predationBehavior)
			throws BehaviorException
	{
		if (!payoffLoaded)
		{
			this.totalPayoffToSurrender = peasant.surrenderUnprotectedPayoff();  
			payoffLoaded = true; 
		}
		return allocateProportionalPayoffToPredationBehavior(predationBehavior); 
	}
	private PredationOutcome allocateProportionalPayoffToPredationBehavior(
			PredationBehavior predationBehavior) throws BehaviorException
	{
		if (predationBehaviorMap.containsKey(predationBehavior))
		{
			predationBehaviorMap.remove(predationBehavior);
			double payoffPortionToSurrender = (predationBehavior.getPredationEffort() / weight) * totalPayoffToSurrender;
			logger.debug("ProtectionBehavior for "+getActor().toString()+"; payoff "+payoffPortionToSurrender+" surrendered to threat "+predationBehavior.getActor().toString());
			return new BanditPredationOutcome(payoffPortionToSurrender);
		}
		else 
		{
			String details = ""; 
			if (predationBehavior != null) details =  predationBehavior.getActor().toString(); 
			return new BanditPredationOutcome(0, "BehaviorPeasantDefendsAgainstMultipleBandits: PredationBehavior not previously added as target or attempted to prey multiple times: "+details);
		}
	}
	@Override
	public void inherit(Heritable heritable)
	{
	}
	@Override
	public double getPayoff()
	{
		return 0;
	}
	@Override
	public void setLastStanding(Heritable heritable)
	{
	}
	@Override
	public void tick()
	{
	}


}
