package edu.uci.imbs.actor;

public enum BehaviorEnum
{
	BANDIT_PREYS_ON_MULTIPLE_PEASANTS { public PredationBehavior build(Actor actor) {return new BehaviorBanditPreysOnMultiplePeasants(actor);} }, 
	TESTING_BEHAVIOR  { public Behavior build(Actor actor) {return new TestingBehavior(actor); }  }, 
	PEASANT_DEFENDS_AGAINST_MULTIPLE_BANDITS { public ProtectionBehavior build(Actor actor) {return new BehaviorPeasantDefendsAgainstMultipleBandits(actor);} };

	public abstract Behavior build(Actor actor); 

}
