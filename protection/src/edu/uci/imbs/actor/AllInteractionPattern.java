/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import java.util.ArrayList;      
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class AllInteractionPattern extends InteractionPattern<Bandit, Peasant>
{
	//TODO consider parameterizing this to follow InteractionPattern
	private static Logger logger = Logger.getLogger(AllInteractionPattern.class);
	private List<Actor> combinedList;
	private List<InteractionPair<Bandit,Peasant>> interactionPairs;
	private Iterator<InteractionPair<Bandit, Peasant>> pairIterator;
	public AllInteractionPattern(List<Bandit> bandits, List<Peasant> peasants)
	{
		super(bandits, peasants); 
		buildCombinedListAndIterator(); 
		buildRandom(); 
		permute(); 
	}
	private void buildCombinedListAndIterator()
	{	
		combinedList = new ArrayList<Actor>();
		combinedList.addAll(source); 
		combinedList.addAll(target);
		logger.debug("AllInteractionPattern.buildCombinedListAndIterator: source size: "+source.size()+" target size: "+target.size()+" combined size: "+(source.size()+target.size()));
		interactionPairs = new ArrayList<InteractionPair<Bandit, Peasant>>(); 
	}
	@Override
	public boolean hasNext()
	{
		return pairIterator.hasNext();
	}
	@Override
	public InteractionPair<Bandit, Peasant> next()
	{
		return pairIterator.next();
	}
	@Override
	public void updatePopulations(List<Bandit> source, List<Peasant> target)
	{
		updateSourceAndTarget(source, target); 
		buildCombinedListAndIterator(); 
	}
	@Override
	public void permute()
	{
		Collections.shuffle(combinedList, random); 
		buildInteractionPairList();
	}
	private void buildInteractionPairList()
	{
		Iterator<Actor> iterator = combinedList.iterator(); 
		Actor actor1 = null;
		Actor actor2 = null; 
		while (iterator.hasNext())
		{
			actor1 = iterator.next(); 
			if (iterator.hasNext())
			{
				actor2 = iterator.next(); 
				logger.debug("AllInteractionPattern.buildInteractionPairList: Actor1: "+actor1.toString()+" Actor2: "+actor2.toString() );
				buildInteractionPair(actor1, actor2);  // one way 
				buildInteractionPair(actor2, actor1);  // or the other (or maybe neither)
			}
		}
		logger.debug("AllInteractionPattern.buildInteractionPairList: interactionPairs size: "+interactionPairs.size());
		pairIterator = interactionPairs.iterator(); 
	}
	private void buildInteractionPair(Actor actor1, Actor actor2)
	{
		InteractionPair<Bandit, Peasant> pair = null; 
		if ((actor1 instanceof Bandit) && (actor2 instanceof Peasant)) 
		{
			pair = new InteractionPair<Bandit, Peasant>((Bandit) actor1, (Peasant) actor2); 
			interactionPairs.add(pair);
			logger.debug("AllInteractionPattern.buildInteractionPair: added "+pair.toString()); 
		}
	}
	protected List<Actor> getCombinedList()
	{
		return combinedList;
	}
	protected List<InteractionPair<Bandit, Peasant>> getInteractionPairs()
	{
		return interactionPairs;
	}
}
