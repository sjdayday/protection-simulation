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
	@Override
	public void setLastStanding(Heritable heritable)
	{
	}
	public Heritable getLastStanding()
	{
		return null;
	}
}
