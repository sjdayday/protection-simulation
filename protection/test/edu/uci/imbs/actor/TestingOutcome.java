package edu.uci.imbs.actor;

public class TestingOutcome implements Outcome
{
	private int number;

	public TestingOutcome(int number)
	{
		this.number = number; 
	}
	
	@Override
	public String getName()
	{
		return "TestingOutcome";
	}

	public int getNumber()
	{
		return number;
	}

}
