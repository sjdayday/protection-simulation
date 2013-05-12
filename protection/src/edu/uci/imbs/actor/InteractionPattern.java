package edu.uci.imbs.actor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class InteractionPattern<S, T>
{
	protected List<S> source;
	protected List<T> target;
	private Iterator<S> sourceIterator;
	private Iterator<T> targetIterator;
	protected Random random;

	public InteractionPattern(List<S> source, List<T> target)
	{
		updateSourceAndTarget(source, target);
		buildIterators(); 
		//TODO pattern should permute on first invocation
	}
	protected void updateSourceAndTarget(List<S> source, List<T> target)
	{
		this.source = source; 
		this.target = target;
	}
	public InteractionPattern(InteractionPattern<S, T> pattern)
	{
		this(pattern.source, pattern.target);
		this.random = pattern.random; 
	}
	private void buildIterators()
	{
		sourceIterator = source.iterator(); 
		targetIterator = target.iterator(); 
	}
	public List<S> getSourceList()
	{
		return source;
	}
	public List<T> getTargetList()
	{
		return target;
	}
	public void permute()
	{
		buildRandom();
		Collections.shuffle(source, random); 
		Collections.shuffle(target, random); 
		buildIterators(); 
	}
	protected void buildRandom()
	{
		if (random == null) random = new Random(ProtectionParameters.RANDOM_SEED); //TODO consider removing this dependency by passing in the seed
	}
	public boolean hasNext()
	{
		return (sourceIterator.hasNext() && targetIterator.hasNext());
	}
	public InteractionPair<S, T> next()
	{
		InteractionPair<S, T> pair = null; 
		if (hasNext())
		{	
			pair = new InteractionPair<S, T>(sourceIterator.next(), targetIterator.next()); 
		}
		return pair;
	}
	public void setRandomForTesting(Random random)
	{
		this.random = random; 
	}
	public Random getRandomForTesting()
	{
		return random; 
	}
	public void updatePopulations(List<S> source, List<T> target)
	{
		updateSourceAndTarget(source, target); 
		buildIterators();
	}
}
