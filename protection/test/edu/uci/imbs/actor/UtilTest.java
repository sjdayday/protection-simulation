package edu.uci.imbs.actor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class UtilTest
{
	@Test
	public void verifyDoubleRoundedToTwoDecimalPlaces() throws Exception
	{
		assertEquals("0.35", Util.roundDoubleToTwoDecimalPlaces(0.05d * 7).toString()); 
		assertEquals("0.1", Util.roundDoubleToTwoDecimalPlaces(0.05d * 2).toString()); 
		
	}
}
