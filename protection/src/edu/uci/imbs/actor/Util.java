package edu.uci.imbs.actor;

import java.text.DecimalFormat;

public class Util
{

	public static Double roundDoubleToTwoDecimalPlaces(double d)
	{
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));

	}

}
