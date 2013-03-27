package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

//TODO RoleShiftingReplicatorDynamic implements Dynamic
public class DynamicTest
{
	private List<Bandit> bandits;
	private List<Peasant> peasants;
	@Before
	public void setUp() throws Exception
	{
		bandits = TestBuilder.buildBanditList(); 
		peasants = TestBuilder.buildPeasantList(); 
	}
	@Test
	public void verifyDynamicReturnsNewPopulation() throws Exception
	{
		Dynamic dynamic = new TestingDynamic(); 
		dynamic.setPopulation(new ProtectionPopulation(bandits, peasants)); 
		ProtectionPopulation  population = dynamic.rebuildPopulation(); 
		assertNotSame(bandits, population.getBandits()); 
		assertNotSame(peasants, population.getPeasants()); 
	}
	private class TestingDynamic implements Dynamic
	{
		@Override
		public ProtectionPopulation rebuildPopulation()
		{
			return new ProtectionPopulation(TestBuilder.buildBanditList(), TestBuilder.buildPeasantList());
		}
		@Override
		public void setPopulation(ProtectionPopulation protectionPopulation)
		{
		}
	}
}
