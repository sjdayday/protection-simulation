/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public interface Behavior extends Heritable
{

	public Actor getActor() throws BehaviorException;

	public void setActor(Actor actor);

	public Outcome behave() throws BehaviorException;

	public abstract BehaviorEnum getType();

	public void tick();
}
