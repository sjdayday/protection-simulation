package edu.uci.imbs.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.log4j.Logger;

public class VariablePopulationProtectionStatistics extends
		ProtectionStatistics
{
	private static Logger logger = Logger.getLogger(VariablePopulationProtectionStatistics.class);
	private Map<Double, Integer> distribution;
	private HashMap<Double, Integer> fullDistribution;
	private boolean initialized = false;
	private double[] bins;
	private double[] counts;
	private double averagePeasantProtectionProportion;
	private double medianPeasantProtectionProportion;
	private double modePeasantProtectionProportion;
	private List<VariablePopulationStatisticsRecord> variableStatisticsRecords;
	private double averageBanditNumberPeasantsToPreyUpon;
	private int modeBanditNumberPeasantsToPreyUpon;
	private double medianBanditNumberPeasantsToPreyUpon;
	private List<Integer> numbersOfPeasantsToPreyUpon;
	private double[] numbersOfPeasantsToPreyUponDoubles;
	private Map<Integer, Integer> numbersToPreyUpon;

	public VariablePopulationProtectionStatistics(
			List<Bandit> bandits, List<Peasant> peasants, int numberBins, double interval)
	{
		super(bandits, peasants);
		variableStatisticsRecords = new ArrayList<VariablePopulationStatisticsRecord>();
		buildPeasantProtectionDistributions(numberBins, interval); 
		initialized  = true; 
		calculateAdditionalStatistics(); 
	}
	@Override
	protected void calculateAdditionalStatistics()
	{
		if (!initialized) return;
		reloadPeasants();
		reloadBandits();
		calculatePeasantDistributions();
		calculateBanditDistributions();
	}
	private void reloadPeasants()
	{
		clearMap(distribution); 
		clearMap(fullDistribution); 
		loadPeasantProportionValuesToDistributions(); 
	}
	private void reloadBandits()
	{
		loadPredationBehaviorsForCalculation(); 
		buildMapOfNumberOfPeasantsToPreyUpon();
	}
	private void calculatePeasantDistributions()
	{
		if (peasants.size() == 0)
		{
			averagePeasantProtectionProportion = 0;
			modePeasantProtectionProportion = 0; 
			medianPeasantProtectionProportion = 0;
		}
		else
		{
			calculateAveragePeasantProtectionProportion();
			calculateModePeasantProtectionProportion();
			calculateMedianPeasantProtectionProportion();
		}
	}
	private void calculateBanditDistributions()
	{
		
		if (bandits.size() == 0)
		{
			averageBanditNumberPeasantsToPreyUpon = 0;
			medianBanditNumberPeasantsToPreyUpon = 0;
			modeBanditNumberPeasantsToPreyUpon = 0;
		}
		else
		{
			calculateAverageBanditNumberPeasantsToPreyUpon();
			calculateMedianBanditNumberPeasantsToPreyUpon();
			calculateModeBanditNumberPeasantsToPreyUpon();
		}
	}
	private void buildPeasantProtectionDistributions(int numberBins, double interval)
	{
		distribution = new HashMap<Double, Integer>();
		fullDistribution = new HashMap<Double, Integer>();
		for (int i = 0; i < numberBins; i++)
		{
			fullDistribution.put(Util.roundDoubleToTwoDecimalPlaces(interval * i), 0); 
		}
		loadPeasantProportionValuesToDistributions();
	}
	private void loadPeasantProportionValuesToDistributions()
	{
		Integer temp = null;
		Double bin = null; 
		for (Peasant peasant : peasants)
		{
			bin = peasant.getRoundedProtectionProportion(); 
			temp = (distribution.get(bin) == null ? 1 : distribution.get(bin)+1) ; 
			distribution.put(bin, temp);
			fullDistribution.put(bin, temp);
		}
	}
	private void loadPredationBehaviorsForCalculation()
	{
		numbersOfPeasantsToPreyUpon = new ArrayList<Integer>(); 
		for (Bandit bandit : bandits)
		{
			numbersOfPeasantsToPreyUpon.add(((BehaviorBanditPreysOnMultiplePeasants) bandit.getPredationBehavior()).getNumberOfPeasantsToPreyUpon()); // Fails whenever we add another PredationBehavior
		}
		numbersOfPeasantsToPreyUponDoubles = convertIntegerCollectionToDoublesArray(numbersOfPeasantsToPreyUpon);
	}
	private void calculateModeBanditNumberPeasantsToPreyUpon()
	{
		Set<Entry<Integer, Integer>> entries = numbersToPreyUpon.entrySet(); 
		Iterator<Entry<Integer, Integer>> it = entries.iterator();
		Entry<Integer, Integer> topEntry = it.next();
		Entry<Integer, Integer> currentEntry = null; 
		while (it.hasNext())
		{
			currentEntry = it.next();
			if (currentEntry.getValue() > topEntry.getValue()) topEntry = currentEntry; 
		}
		modeBanditNumberPeasantsToPreyUpon = topEntry.getKey(); 
	}
	private void calculateMedianBanditNumberPeasantsToPreyUpon()
	{
		Median median = new Median();
		medianBanditNumberPeasantsToPreyUpon = median.evaluate(numbersOfPeasantsToPreyUponDoubles); 
	}
	private void calculateAverageBanditNumberPeasantsToPreyUpon()
	{
		Mean mean = new Mean(); 
		averageBanditNumberPeasantsToPreyUpon = mean.evaluate(numbersOfPeasantsToPreyUponDoubles); 
	}
	private void clearMap(Map<Double, Integer> map)
	{
		for (Double key : map.keySet())
		{
			map.put(key, 0); 
		}
	}
	private void calculateAveragePeasantProtectionProportion()
	{
		Mean mean = new Mean();
		Double[] valuesDouble = new Double[distribution.keySet().size()];
		bins = unboxDoubleArray(distribution.keySet().toArray(valuesDouble)); 
		counts = convertIntegerCollectionToDoublesArray(distribution.values()); 
		averagePeasantProtectionProportion = mean.evaluate(bins, counts);
	}
	private double[] convertIntegerCollectionToDoublesArray(Collection<Integer> integers)
	{
		Iterator<Integer> it = integers.iterator(); 
		double[] doubles = new double[integers.size()];
		for (int i = 0; i < doubles.length; i++)
		{
			doubles[i] = it.next().doubleValue();
		}
		return doubles;
	}
	private double[] unboxDoubleArray(Double[] doubles)
	{
		double[] unboxedDoubles = new double[doubles.length];  
		for (int i = 0; i < doubles.length; i++)
		{
			unboxedDoubles[i] = doubles[i];
		}
		return unboxedDoubles; 
	}
	private void calculateMedianPeasantProtectionProportion()
	{
		double[] proportions = buildSortedArrayOfProtectionProportions(); 
		Median median = new Median();
		medianPeasantProtectionProportion = median.evaluate(proportions); 
	}
	private double[] buildSortedArrayOfProtectionProportions()
	{
		List<Double> proportionsList = new ArrayList<Double>();
		for (Peasant peasant : peasants)
		{
			proportionsList.add(peasant.getRoundedProtectionProportion()); 
		}
		Double[] proportionsArray = new Double[peasants.size()]; 
		proportionsList.toArray(proportionsArray); 
		double[] proportions = unboxDoubleArray(proportionsArray);
		Arrays.sort(proportions);
		return proportions;
	}
	private void calculateModePeasantProtectionProportion()
	{
		Set<Entry<Double, Integer>> entries = distribution.entrySet(); 
		Iterator<Entry<Double, Integer>> it = entries.iterator();
		Entry<Double, Integer> topEntry = it.next();
		Entry<Double, Integer> currentEntry = null; 
		while (it.hasNext())
		{
			currentEntry = it.next();
			if (currentEntry.getValue() > topEntry.getValue()) topEntry = currentEntry; 
		}
		modePeasantProtectionProportion = topEntry.getKey(); 
	}
	private void buildMapOfNumberOfPeasantsToPreyUpon()
	{
		numbersToPreyUpon = new HashMap<Integer, Integer>();
		for (int i = 0; i < ProtectionParameters.MAX_PEASANTS_TO_PREY_UPON; i++)
		{
			numbersToPreyUpon.put(i+1, 0); 
		}
		loadMapForEachBandit();
	}
	private void loadMapForEachBandit()
	{
		int subtotal = 0; 
		for (Integer numberOfPeasantsToPreyUpon : numbersOfPeasantsToPreyUpon)
		{
			subtotal = numbersToPreyUpon.get(numberOfPeasantsToPreyUpon); 
			subtotal++;
			numbersToPreyUpon.put(numberOfPeasantsToPreyUpon, subtotal); 
		}
	}
	@Override
	protected void buildStatisticsRecord()
	{
		variableStatisticsRecords.add(new VariablePopulationStatisticsRecord(period, numberBandits, numberPeasants, averageBanditPayoff, averagePeasantPayoff, banditPeasantPayoffDelta, actorAdjustment, 
				averagePeasantProtectionProportion, medianPeasantProtectionProportion, modePeasantProtectionProportion, averageBanditNumberPeasantsToPreyUpon, medianBanditNumberPeasantsToPreyUpon, modeBanditNumberPeasantsToPreyUpon)); 
		logger.debug("statistics record added: "+variableStatisticsRecords.size());
	}
	public String printPeasantProtectionProportionDistribution()
	{
		StringBuffer sb = new StringBuffer(); 
		sb.append(Constants.CRLF);
		Set<Entry<Double, Integer>> entries = fullDistribution.entrySet(); 
		Set<Entry<Double, Integer>> treeSet = new TreeSet<Entry<Double, Integer>>(new EntryComparator());
		treeSet.addAll(entries); 
		Iterator<Entry<Double, Integer>> it = treeSet.iterator(); 
		Entry<Double, Integer> currentEntry = null;
		String oneDigit = null; 
		while (it.hasNext())
		{
			currentEntry = it.next();
			oneDigit = currentEntry.getKey().toString();
			sb.append(oneDigit);
			if (oneDigit.length() == 3) sb.append(" ");
			sb.append(": ");
			for (int i = 0; i < currentEntry.getValue(); i++)
			{
				sb.append("x");
			}
			sb.append(Constants.CRLF);
		}
		return sb.toString();
	}
	public String printBanditPredationEffortDistribution()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(Constants.CRLF);
		for (int i = 0; i < ProtectionParameters.MAX_PEASANTS_TO_PREY_UPON; i++)
		{
			sb.append(i+1); 
			sb.append(" : ");
			numberPeasants = numbersToPreyUpon.get(i+1);
			for (int j = 0; j < numberPeasants; j++)
			{
				sb.append("y");
			}
			sb.append(Constants.CRLF);
		}
		return sb.toString();
	}
	public double averagePeasantProtectionProportion()
	{
		return averagePeasantProtectionProportion;
	}
	public double modePeasantProtectionProportion()
	{
		return modePeasantProtectionProportion;
	}
	public double medianPeasantProtectionProportion()
	{
		return medianPeasantProtectionProportion;
	}
    @Override
    public List<? extends StatisticsRecord> getStatisticsRecords()
    {
    	return variableStatisticsRecords;
    }
	public List<VariablePopulationStatisticsRecord> getVariableStatisticsRecords()
	{
		return variableStatisticsRecords;
	}

	public double averageBanditNumberPeasantsToPreyUpon()
	{
		return averageBanditNumberPeasantsToPreyUpon;
	}
	public int modeBanditNumberPeasantsToPreyUpon()
	{
		return modeBanditNumberPeasantsToPreyUpon;
	}
	public double medianBanditNumberPeasantsToPreyUpon()
	{
		return medianBanditNumberPeasantsToPreyUpon;
	}
	private class EntryComparator implements Comparator<Entry<Double, Integer>>
	{
		@Override
		public int compare(Entry<Double, Integer> paramT1,
				Entry<Double, Integer> paramT2)
		{
			
			return paramT1.getKey().compareTo(paramT2.getKey());
		}
	}
}
