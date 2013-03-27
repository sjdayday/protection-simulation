package edu.uci.imbs.actor;

import java.util.List;

public interface PredationBehavior extends Behavior
{

	public double getPredationEffort();

	public PredationOutcome preyOnTargets() throws BehaviorException;

	public void addTarget(ProtectionBehavior protectionBehavior);

	public List<ProtectionBehavior> getTargets();

	
}
