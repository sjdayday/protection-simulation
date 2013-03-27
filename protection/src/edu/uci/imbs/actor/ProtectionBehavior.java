package edu.uci.imbs.actor;

import java.util.Map;

public interface ProtectionBehavior extends Behavior
{

	public void addThreat(PredationBehavior predationBehavior);

	public Map<PredationBehavior, Double> getThreatMap(); 
	
	@Override
	public PredationOutcome behave() throws BehaviorException;

	public PredationOutcome surrenderPayoff(PredationBehavior predationBehavior) throws BehaviorException;

}
