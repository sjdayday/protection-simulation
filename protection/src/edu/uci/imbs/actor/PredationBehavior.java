/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.List;

public interface PredationBehavior extends Behavior
{

	public double getPredationEffort();

	public PredationOutcome preyOnTargets() throws BehaviorException;

	public void addTarget(ProtectionBehavior protectionBehavior);

	public List<ProtectionBehavior> getTargets();

	
}
