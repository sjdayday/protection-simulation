package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FitnessFunctionTest
{
	private FitnessFunction fitnessFunction;

	@Before
	public void setUp() throws Exception
	{
		fitnessFunction = new FitnessFunction(); 
	}
	@Test
	public void verifyFitnessFunctionReturnsSurviveAtSurviveThreshold() throws Exception
	{
		assertEquals("defaults to zero if not set", 0, fitnessFunction.getSurviveThreshold(), 0.001);
		assertEquals("defaults to zero if not set", 0, fitnessFunction.getThriveThreshold(), 0.001);
		assertEquals("If threshold not set, all values return SURVIVE",FitnessEnum.SURVIVE, fitnessFunction.evaluate(0)); 
		fitnessFunction.setSurviveThreshold(0.25d); 
		assertEquals(0.25, fitnessFunction.getSurviveThreshold(), 0.001);
		assertEquals(FitnessEnum.DIE, fitnessFunction.evaluate(0)); 
		assertEquals(FitnessEnum.DIE, fitnessFunction.evaluate(.249)); 
		assertEquals(FitnessEnum.SURVIVE, fitnessFunction.evaluate(.25)); 
		assertEquals(FitnessEnum.SURVIVE, fitnessFunction.evaluate(.499)); 
		assertEquals(FitnessEnum.SURVIVE, fitnessFunction.evaluate(.5)); 
		assertEquals(FitnessEnum.SURVIVE, fitnessFunction.evaluate(1)); 
	}
	@Test
	public void verifyThriveReturnedIfThriveThresholdSet() throws Exception
	{
		fitnessFunction.setSurviveThreshold(0.25d); 
		fitnessFunction.setThriveThreshold(0.5d); 
		assertEquals(0.5, fitnessFunction.getThriveThreshold(), 0.001);
		assertEquals(FitnessEnum.SURVIVE, fitnessFunction.evaluate(.499)); 
		assertEquals(FitnessEnum.THRIVE, fitnessFunction.evaluate(.5)); 
		assertEquals(FitnessEnum.THRIVE, fitnessFunction.evaluate(1)); 
	}
	@Test
	public void verifyFitnessInputGreaterThanZero() throws Exception
	{
		verifyEvaluateThrowsIllegalArgumentExceptionForInputOutOfRange(-0.5);
//		verifyEvaluateThrowsIllegalArgumentExceptionForInputOutOfRange(1.5);
	}
	private void verifyEvaluateThrowsIllegalArgumentExceptionForInputOutOfRange(double input)
	{
		try 
		{
			fitnessFunction.evaluate(input); 
			fail("should throw IllegalArgumentException"); 
		} 
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("FitnessFunction.evaluate:  Inputs to evaluate must be greater than 0")); 
//			assertTrue(e.getMessage().startsWith("FitnessFunction.evaluate:  Inputs to evaluate must be in range [0,1]")); 
		}
	}

}
