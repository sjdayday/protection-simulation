/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;


public class TestingActorNotInstantiable extends Actor implements Heritable
{
	public int id = 0; 
	public TestingActorNotInstantiable()
	{
		throw new RuntimeException("Force an error during instantiation");
	}
	public TestingActorNotInstantiable(boolean b)
	{
		// any old constructur to avoid nullary
	}
	@Override
	public String getName()
	{
		return null;
	}
	@Override
	public void tick()
	{
	}
	@Override
	public void inherit(Heritable heritable)
	{
	}
	@Override
	public double getPayoff()
	{
		return 1;
	}
}