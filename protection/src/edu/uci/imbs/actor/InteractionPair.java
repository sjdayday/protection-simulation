package edu.uci.imbs.actor;

public class InteractionPair<S, T>
{

	private S source;
	private T target;

	public InteractionPair(S source, T target)
	{
		this.source = source;
		this.target = target; 
	}

	public S getSource()
	{
		return source;
	}

	public T getTarget()
	{
		return target;
	}

}
