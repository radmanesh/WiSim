package org.frankStyle.wiSim.view;

import javax.swing.JLabel;

/**
 * @author Arman
 * 
 */
public abstract class NodeIconAbstract extends JLabel {
	/**
	 * 
	 */
	private final int type = -1; // Required
	final static int SENSOR = 1;
	final static int TARGET = 2;
	final static int SINK = 3;

	public void debug() {
		System.out.println("No debug information for this component.\n"
				+ "gComponent:" + this);
	}

	public abstract String getEmbededClass();

	public int getType() {
		return type;
	}

	public abstract void updateLocationFromComp();

	abstract public double getRadius();

}
