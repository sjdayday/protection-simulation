/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ContestFunctionTest
{
	private ProtectionFunction contestFunction;
	@Before
	public void setUp() throws Exception
	{
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(0.5); 
	}
	@Test
	public void verifyProtectionFunctionEnumReturnsContestFunction() throws Exception
	{
		assertTrue(contestFunction instanceof ContestFunction); 
		assertEquals(0.5, contestFunction.getParameters()[ContestFunction.GAMMA], .001); 
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(new double[]{0.5}); 
		assertTrue(contestFunction instanceof ContestFunction); 
		assertEquals(0.5, contestFunction.getParameters()[0], .001); 
	}
	@Test
	public void verifyNewInstancesReturnsSameFunction() throws Exception
	{
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(new double[] {0.5, 1.25}); 
		ProtectionFunction newFunction = contestFunction.newInstance(); 
		assertTrue(newFunction instanceof ContestFunction);
		assertEquals(0.5, newFunction.getParameters()[ContestFunction.GAMMA], .001);
		assertEquals(1.25, newFunction.getParameters()[ContestFunction.WEIGHT], .001);
		assertEquals(contestFunction.toString(), newFunction.toString());
	}
	@Test
	public void verifyGammaWithinRange() throws Exception
	{
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(0.5); 
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(1); 
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(.75); 
		try 
		{
			contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(.45); 
			fail("should throw IllegalArgumentException"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("ContestFunction: Contest function only accepts gamma in the range of 0.5 to 1")); 
		}
		try 
		{
			contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(1.05); 
			fail("should throw IllegalArgumentException"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("ContestFunction: Contest function only accepts gamma in the range of 0.5 to 1")); 
		}
	}
	@Test
	public void verifyContestFunctionCalculatesCorrectlyGivenXandGamma() throws Exception
	{
		assertEquals("gamma * x / [(gamma) * x + (1-gamma)(1) ; for gamma = 0.5, reduces to x / x+1 = .4 / 1.4", 0.285, contestFunction.evaluate(.4), .001);
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(0.75); 
		assertEquals("(.75 * .4) / [(.75 * .4) + (1-.75)(1) = .3 / .3 + .25 = .545  ", 0.545, contestFunction.evaluate(.4), .001);
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(new double[]{0.5}); 
		assertEquals("equivalent constructor", 0.285, contestFunction.evaluate(.4), .001);
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(new double[]{0.75}); 
		assertEquals("equivalent constructor", 0.545, contestFunction.evaluate(.4), .001);
	}
	@Test
	public void verifyZeroInputAlwaysForcedToEvaluateToZeroAndOneInputAlwaysForcedToEvaluateToOne() throws Exception
	{
		assertEquals("0.5 * 0 / [(0.5 * 0) + (1-0.5)(1)] = 0",0, contestFunction.evaluate(0), .001);
		assertEquals("0.5 * 1 / [(0.5 * 1) + (1-0.5)(1)] = 0.5 but must be forced to 1",1, contestFunction.evaluate(1), .001);
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(1); 
		assertEquals("1 * 0 / [(1 * 0) + (1-1)(1)] = 0/0 = NaN, so forced to 0",0, contestFunction.evaluate(0), .001);
		assertEquals("1 * 1 / [(1 * 1) + (1-1)(1)] = 1 but forced to 1 anyway",1, contestFunction.evaluate(1), .001);
	}
	@Test
	public void verifyContestFunctionUpdatedWithSumOfAttackingEfforts() throws Exception
	{
		contestFunction.setParameters(new double[]{0.5, 1.5d}); 
		assertEquals("Contest: gamma=0.5 weight=1.5", contestFunction.toString()); 
		assertEquals("gamma * x / [(gamma) * x + (1-gamma)(sum of attacking efforts) ; for gamma = 0.5, reduces to x / x+(sum) = .4 / 1.9", 0.210, contestFunction.evaluate(.4), .001);
		// verify the border cases...
		assertEquals("0.5 * 0 / [(0.5 * 0) + (1-0.5)(1.5)] = 0",0, contestFunction.evaluate(0), .001);
		assertEquals("0.5 * 1 / [(0.5 * 1) + (1-0.5)(1.5)] = 0.4 but must be forced to 1",1, contestFunction.evaluate(1), .001);
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(new double[]{1d, 1.5d}); 
		assertEquals("1 * 0 / [(1 * 0) + (1-1)(1.5)] = 0/0 = NaN, so forced to 0",0, contestFunction.evaluate(0), .001);
		assertEquals("1 * 1 / [(1 * 1) + (1-1)(1.5)] = 1 but forced to 1 anyway",1, contestFunction.evaluate(1), .001);
	}
	@Test
	public void verifyWeightDefaultsToOneWhenSingleParameterPassed() throws Exception
	{
		contestFunction = ProtectionFunctionEnum.CONTEST.buildFunction(new double[]{0.75}); 
		assertEquals("(.75 * .4) / [(.75 * .4) + (1-.75)(1) = .3 / .3 + .25 = .545  ", 0.545, contestFunction.evaluate(.4), .001);
		assertEquals(1, contestFunction.getParameters()[ContestFunction.WEIGHT], .001); 
	}
}
