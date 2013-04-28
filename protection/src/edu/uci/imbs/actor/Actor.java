package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.List;

public class Actor implements Heritable
{

	private static int TEST_ID;
	private int id;
	protected List<Behavior> behaviors;

	public static void resetStartingIdForTesting(int id)
	{
		TEST_ID = id; 
	}

	public Actor()
	{
		id = TEST_ID++; 
		behaviors = new ArrayList<Behavior>(); 
	}
	public void setId(int id)
	{
		this.id = id; 
	}
	public int getId()
	{
		return id;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false; 
		if (!(obj instanceof Actor)) return false;
		Actor otherActor = (Actor) obj; 
		if (this.id == otherActor.getId()) return true;
		else return false;
	}
	@Override
	public int hashCode()
	{
		return id;
	}
	@Override
	public String toString()
	{
		return getName()+" "+id;
	}
	public String getName()
	{
		return "Actor";
	}
	public void tick()
	{
		for (Behavior behavior : behaviors)
		{
			behavior.tick(); 
		}
	}

	public void addBehavior(Behavior behavior)
	{
		behaviors.add(behavior); 
	}

	public List<Behavior> getBehaviors()
	{
		return behaviors;
	}

	public List<Outcome> behave() throws BehaviorException
	{
		List<Outcome> outcomes = new ArrayList<Outcome>(); 
		for (Behavior behavior : behaviors)
		{
			outcomes.add(behavior.behave()); 
		}
		return outcomes;
	}

	@Override
	public void inherit(Heritable heritable)
	{
		validateHeritable(Actor.class, heritable); 
	}
	protected <T> void validateHeritable(Class<T> class1, Heritable heritable)
	{
		String type = "null";
		String msg = "Actor:  "+class1.getName()+" may only inherit from another "+class1.getName()+"; attempted inheritance from: "; 
		if (heritable != null)  
		{
			type = heritable.toString();
			if (!(class1.isAssignableFrom(heritable.getClass()))) throw new IllegalArgumentException(msg+type); 
//			if (!(class1.isInstance(heritable.getClass()))) throw new IllegalArgumentException(msg+type);  //doesn't work for Actor.class.isInstance(actor.getClass())...dunno why 
		}
		else throw new IllegalArgumentException(msg+type);
	}

	@Override
	public double getPayoff()
	{
		return 0;
	}
}
