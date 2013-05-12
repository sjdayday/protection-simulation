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
    @Override
    public String toString()
    {
    	StringBuffer sb = new StringBuffer(); 
    	sb.append("Interaction Pair Source: ");
    	sb.append(source.toString());
    	sb.append(" Target: ");
    	sb.append(target.toString());
    	return sb.toString();
    }
}
