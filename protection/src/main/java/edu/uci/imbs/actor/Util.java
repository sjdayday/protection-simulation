/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

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
