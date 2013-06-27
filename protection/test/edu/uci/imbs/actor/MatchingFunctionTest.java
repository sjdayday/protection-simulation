/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class MatchingFunctionTest
{
	private ProtectionFunction matchingFunction;
	@Before
	public void setUp() throws Exception
	{
		matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(new double[]{0.5, 0.4, 3}); 
	}
	@Test
	public void verifyProtectionFunctionEnumReturnsMatchingFunction() throws Exception
	{
		matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(0.5);
		assertTrue(matchingFunction instanceof MatchingFunction); 
		assertEquals(0.5, matchingFunction.getParameters()[MatchingFunction.ALPHA], .001); 
		assertEquals("Beta defaults to same as Alpha",0.5, matchingFunction.getParameters()[MatchingFunction.BETA], .001); 
		assertEquals("Mu defaults to 1",1, matchingFunction.getParameters()[MatchingFunction.MU], .001); 
		matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(new double[]{0.5, 0.4, 3}); 
		assertTrue(matchingFunction instanceof MatchingFunction); 
		assertEquals(0.5, matchingFunction.getParameters()[0], .001); 
		assertEquals(0.4, matchingFunction.getParameters()[1], .001); 
		assertEquals(3, matchingFunction.getParameters()[2], .001); 
		assertEquals("Matching: alpha=0.5 beta=0.4 mu=3.0", matchingFunction.toString()); 
		matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(new double[]{0.5, 0.4}); 
		assertEquals("Mu defaults to 1 if not specified",1, matchingFunction.getParameters()[2], .001); 
		try
		{
			matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(new double[]{}); 
			fail("should throw from parent"); 
		}
		catch (IllegalArgumentException e)
		{
		}
	}
	@Test
	public void verifyNewMatchingFunctionInstanceReturnsSameFunction() throws Exception
	{
		ProtectionFunction newFunction = matchingFunction.newInstance(); 
		assertTrue(newFunction instanceof MatchingFunction);
		assertEquals(0.5, matchingFunction.getParameters()[0], .001); 
		assertEquals(0.4, matchingFunction.getParameters()[1], .001); 
		assertEquals(3, matchingFunction.getParameters()[2], .001); 
		assertEquals(matchingFunction.toString(), newFunction.toString());
	}
	@Test
	public void verifyAlphaAndBetaWithinRange() throws Exception
	{
		matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(0); 
		matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(1); 
		matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(.5); 
		try 
		{
			matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(-0.5); 
			fail("should throw IllegalArgumentException for Alpha & Beta < 1"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("MatchingFunction: Matching function only accepts Alpha and Beta in the range of 0 to 1")); 
		}
		try 
		{
			matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(1.05); 
			fail("should throw IllegalArgumentException for Alpha & Beta > 1"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("MatchingFunction: Matching function only accepts Alpha and Beta in the range of 0 to 1")); 
		}
		try 
		{
			matchingFunction = ProtectionFunctionEnum.MATCHING.buildFunction(new double[]{0.5, 1.1, 1000}); 
			fail("should throw IllegalArgumentException for Beta > 1"); 
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(e.getMessage().startsWith("MatchingFunction: Matching function only accepts Alpha and Beta in the range of 0 to 1")); 
		}
	}
	@Test
	public void verifyMatchingFunctionCalculatesCorrectlyGivenAlphaBetaAndTwoInputs() throws Exception
	{
		assertEquals("mu*(input[0]^alpha)*(input[1]^beta) = 3*(100^0.5)*(25^0.4)", 108.716, matchingFunction.evaluate(new double[]{100, 25}), .001);
	}
	@Test
	public void verifyMatchingFunctionAcceptsExactlyTwoInputs() throws Exception
	{
		try
		{
			matchingFunction.evaluate(100); 
			fail("should throw."); 
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("MatchingFunction: Matching function requires exactly two inputs:  number of elements searching for a match, and number of elements available to be matched.", e.getMessage());
		}
		try
		{
			matchingFunction.evaluate(new double[]{100}); 
			fail("should throw."); 
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("MatchingFunction: Matching function requires exactly two inputs:  number of elements searching for a match, and number of elements available to be matched.", e.getMessage());
		}
		try
		{
			matchingFunction.evaluate(new double[]{100, 100, 100}); 
			fail("should throw."); 
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("MatchingFunction: Matching function requires exactly two inputs:  number of elements searching for a match, and number of elements available to be matched.", e.getMessage());
		}

	}
}
