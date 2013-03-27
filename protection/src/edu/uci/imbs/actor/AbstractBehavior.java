package edu.uci.imbs.actor;

public abstract class AbstractBehavior implements Behavior
{

	protected Actor actor;

	@Override
	public Actor getActor() throws BehaviorException
	{
		if (actor == null) throw new BehaviorException("BehaviorException: Behavior must have associated actor.");
		return actor;
	}

	@Override
	public void setActor(Actor actor)
	{
		this.actor = actor; 
	}

	public BehaviorEnum getType()
	{
		return null;
	}
	protected <T> void validateHeritable(Class<T> class1, Heritable heritable)
	{
		String type = "null";
		String msg = "AbstractBehavior:  "+class1.getName()+" may only inherit from another "+class1.getName()+"; attempted inheritance from: "; 
		if (heritable != null)  
		{
			type = heritable.toString();
			if (!(class1.isAssignableFrom(heritable.getClass()))) throw new IllegalArgumentException(msg+type); 
//			if (!(class1.isInstance(heritable.getClass()))) throw new IllegalArgumentException(msg+type);  //doesn't work for Actor.class.isInstance(actor.getClass())...dunno why 
		}
		else throw new IllegalArgumentException(msg+type);
	}

	@Override
	public void setLastStanding(Heritable heritable)
	{
	}

	@Override
	public Heritable getLastStanding()
	{
		return null;
	}
}
