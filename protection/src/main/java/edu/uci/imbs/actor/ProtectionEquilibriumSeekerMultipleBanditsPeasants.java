/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class ProtectionEquilibriumSeekerMultipleBanditsPeasants extends
		ProtectionEquilibriumSeeker
{

	private MultipleBehaviorInteractionPattern pattern;

	public void setMultipleBehaviorInteractionPattern(
			MultipleBehaviorInteractionPattern pattern)
	{
		this.pattern = pattern; 
	}
	@Override
	protected void banditsPreyOnPeasants() throws BehaviorException
	{
		this.pattern.permute(); 
		for (PredationBehavior predationBehavior : pattern.getPredationBehaviors())
		{
			predationBehavior.preyOnTargets(); 
		}
	}
	@Override
	protected void updatePopulationLists()
	{
		this.pattern = new MultipleBehaviorInteractionPattern(banditList, peasantList); 
		protectionStatistics.updatePopulations(banditList, peasantList);
	}
}
