package edu.uci.imbs.actor;

public interface Behavior extends Heritable
{

	public Actor getActor() throws BehaviorException;

	public void setActor(Actor actor);

	public Outcome behave() throws BehaviorException;

	public abstract BehaviorEnum getType();

	public void tick();
}
