/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;


public class PowerFunctionTest
{
	private ProtectionFunction powerFunction;

	@Test
	public void verifyProtectionFunctionEnumReturnsPowerFunction() throws Exception
	{
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(2); 
		assertTrue(powerFunction instanceof PowerFunction); 
	}
	@Test
	public void verifyNewInstancesReturnsSameFunction() throws Exception
	{
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(2); 
		ProtectionFunction newFunction = powerFunction.newInstance(); 
		assertTrue(newFunction instanceof PowerFunction);
		assertEquals(2, newFunction.getParameters()[PowerFunction.POWER], .001);
		assertEquals(powerFunction.toString(), newFunction.toString());
	}
	@Test
	public void verifyNonNegativePowerFunctionsAccepted() throws Exception
	{
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(0); 
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(0.5); 
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(1); 
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(100); 
		try 
		{
			powerFunction = ProtectionFunctionEnum.POWER.buildFunction(-0.5); 
			fail("should throw IllegalArgumentException"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("ProtectionFunction: Power function only accepts non-negative exponent")); 
		}
	}
	@Test
	public void verifyPowerFunctionEvaluatesCorrectly() throws Exception
	{
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(0); 
		assertEquals("1 ^ 0", 1, powerFunction.evaluate(0.5), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(1); 
		assertEquals("0.5 ^ 1", 0.5, powerFunction.evaluate(0.5), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(0.5); 
		assertEquals("sqrt(.25)",0.5, powerFunction.evaluate(0.25), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(2); 
		assertEquals("0.5 ^ 2",0.25, powerFunction.evaluate(0.5), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(new double[] {0.5}); 
		assertEquals("equivalent constructor",0.5, powerFunction.evaluate(0.25), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(new double[] {2}); 
		assertEquals("equivalent constructor",0.25, powerFunction.evaluate(0.5), .001);
	}
	// TODO test for NaN, other Math.pow() cases  
	@Test
	public void verifyZeroInputEvaluatesToZeroAndOneInputEvaluatesToOneForAllSupportedProtectionFunctions() throws Exception
	{
		//Note:  power function meets requirement without modification, but other functions might have to be forced at the 0, 1 boundaries
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(2); 
		assertEquals("0 ^ 2",0, powerFunction.evaluate(0), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(0.5); 
		assertEquals("0 ^ 0.5",0, powerFunction.evaluate(0), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(2); 
		assertEquals("1 ^ 2",1, powerFunction.evaluate(1), .001);
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(0.5); 
		assertEquals("1 ^ 0.5",1, powerFunction.evaluate(1), .001);
	}
	@Test
	public void verifyParameterArrayTreatedSameAsSingleDoubleConstructor() throws Exception
	{
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(new double[]{2}); 
		assertTrue(powerFunction instanceof PowerFunction); 
		powerFunction = ProtectionFunctionEnum.POWER.buildFunction(new double[]{0.5}); 
		assertEquals("sqrt(.25)",0.5, powerFunction.evaluate(0.25), .001);
	}
}
