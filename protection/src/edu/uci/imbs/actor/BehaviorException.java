/* Copyright (c) 2013, Regents of the University of California.  See License.txt for details */

package edu.uci.imbs.actor;

public class BehaviorException extends Exception
{

	private static final long serialVersionUID = 1L;

	public BehaviorException()
	{
	}

	public BehaviorException(String arg0)
	{
		super(arg0);

	}

	public BehaviorException(Throwable arg0)
	{
		super(arg0);

	}

	public BehaviorException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);

	}

}
