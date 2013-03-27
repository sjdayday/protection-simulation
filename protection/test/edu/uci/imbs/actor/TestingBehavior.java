package edu.uci.imbs.actor;

public class TestingBehavior extends AbstractBehavior implements Behavior
{

	private int number;
	private boolean hasTicked;

	public TestingBehavior(Actor actor)
	{
		this();
	}

	public TestingBehavior(int number)
	{
		this();
		this.number = number;
	}
	public TestingBehavior()
	{
		this.hasTicked = false;
	}
	public boolean hasTicked()
	{
		return hasTicked;
	}
	@Override
	public Outcome behave()
	{
		return new TestingOutcome(number);
	}

	public BehaviorEnum getType()
	{
		return BehaviorEnum.TESTING_BEHAVIOR;
	}

	@Override
	public void inherit(Heritable heritable)
	{
	}

	@Override
	public double getPayoff()
	{
		return 0;
	}

	@Override
	public void setLastStanding(Heritable heritable)
	{
	}

	@Override
	public void tick()
	{
		hasTicked = true; 
	}
	
}
