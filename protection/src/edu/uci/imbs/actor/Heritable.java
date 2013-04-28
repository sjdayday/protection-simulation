package edu.uci.imbs.actor;

public interface Heritable 
{
	public void inherit(Heritable heritable);

	public double getPayoff();

}
