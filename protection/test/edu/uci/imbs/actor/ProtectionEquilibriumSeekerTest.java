package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProtectionEquilibriumSeekerTest
{
	private ProtectionEquilibriumSeeker seeker;
	@BeforeClass
	public static void setUpLog4J() throws Exception
	{
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
	}
	@Test
	public void verifyMultipleDynamicsAreProcessed() throws Exception
	{
		seeker = new ProtectionEquilibriumSeeker(); 
		seeker.setPopulation(new ProtectionPopulation(TestBuilder.buildBanditList(), TestBuilder.buildPeasantList())); 
		assertEquals(0, seeker.getDynamics().size()); 
		seeker.runDynamicsAgainstPopulation(); 
		assertEquals("empty Dynamics produces no effect",4, seeker.getPeasantList().size()); 
		seeker.addDynamic(new TestingDynamic()); 
		seeker.addDynamic(new TestingDynamic()); 
		assertEquals(2, seeker.getDynamics().size()); 
		assertEquals(4, seeker.getPeasantList().size()); 
		seeker.runDynamicsAgainstPopulation(); 
		assertEquals("each invocation of TestingDynamic removes 1 from the peasant list",2, seeker.getPeasantList().size()); 
	}
	private class TestingDynamic implements Dynamic
	{
		private ProtectionPopulation protectionPopulation;

		@Override
		public ProtectionPopulation rebuildPopulation()
		{
			protectionPopulation.getPeasants().remove(0);
			return new ProtectionPopulation(protectionPopulation.getBandits(), protectionPopulation.getPeasants());
		}

		@Override
		public void setPopulation(ProtectionPopulation protectionPopulation)
		{
			this.protectionPopulation = protectionPopulation; 
		}
		
	}
	
}
