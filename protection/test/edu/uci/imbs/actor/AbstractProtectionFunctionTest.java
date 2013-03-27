package edu.uci.imbs.actor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;


public class AbstractProtectionFunctionTest
{
	TestingProtectionFunction tf; 
	@Test
	public void verifyProtectionFunctionOnlyAcceptsInputsFromZeroToOne() throws Exception
	{
		tf = new TestingProtectionFunction(); 
		tf.evaluate(0); 
		tf.evaluate(1); 
		tf.evaluate(0.5); 
		verifyEvaluateThrowsIllegalArgumentExceptionForInputOutOfRange(-0.5);
		verifyEvaluateThrowsIllegalArgumentExceptionForInputOutOfRange(1.5);
	}
	private void verifyEvaluateThrowsIllegalArgumentExceptionForInputOutOfRange(double input)
	{
		try 
		{
			tf.evaluate(input); 
			fail("should throw IllegalArgumentException"); 
		} 
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("AbstractProtectionFunction.evaluate:  Inputs to evaluate must be in range [0,1]")); 
		}
	}
	@Test
	public void verifyAtLeastOneParameterIsPresent() throws Exception
	{
		try 
		{
			tf = new TestingProtectionFunction(new double[]{}); 
			fail("should throw IllegalArgumentException"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("AbstractProtectionFunction:  Array of parameters must have at least one entry.")); 
		}
		try 
		{
			tf = new TestingProtectionFunction(); 
			tf.setParameters(new double[]{});
			fail("should throw IllegalArgumentException"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("AbstractProtectionFunction:  Array of parameters must have at least one entry.")); 
		}
	}
	private class TestingProtectionFunction extends AbstractProtectionFunction
	{
		public TestingProtectionFunction(double[] ds)
		{
			super(ds); 
		}
		public TestingProtectionFunction(double parm)
		{
			super(parm); 
		}
		public TestingProtectionFunction()
		{
			this(1);
		}

		@Override
		public double evaluate(double input)
		{
			validate(input);
			return 0;
		}
	}
}
