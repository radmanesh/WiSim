package org.frankStyle.wiSim.model;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Arman
 * 
 */
public class ComponentPlot {
	private String plotName = "plot";
	// private LinkedList listConnectedPort;
	private final LinkedList listProperty;

	public ComponentPlot(String name) {
		plotName = name;
		listProperty = new LinkedList();
		// listConnectedPort = new LinkedList();
	}

	public String getPlotName() {
		return plotName;
	}

	public void setPlotName(String plotName) {
		this.plotName = plotName;
	}

	public ListIterator propertiesIterator() {
		return listProperty.listIterator();
	}

	public void addProperty(ComponentProperty2 property) {
		listProperty.add(property);
	}

	public void removeProperty(ComponentProperty2 property) {
		listProperty.remove(property);
	}

	public String getPropertyValue(String strName) {
		ListIterator iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = (ComponentProperty2) iter.next();
			if (p.getName().equals(strName))
				return p.getValue();
		}
		return null;
	}

	public ComponentProperty2 getProperty(String strName) {
		ListIterator iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = (ComponentProperty2) iter.next();
			if (p.getName().equals(strName))
				return p;
		}
		return null;
	}

	// true means we did changed the old value
	public boolean setPropertyValue(String strName, String strValue) {
		boolean bDirty = false;
		ListIterator iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = (ComponentProperty2) iter.next();
			if (p.getName().equals(strName)) {
				if (!p.getValue().equals(strValue)) {
					p.setValue(strValue);
					bDirty = true;
				}
			}
		}
		return bDirty;
	}

	public boolean addPropertyArg(String name, String arg) {
		boolean bDirty = false;
		ListIterator iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = (ComponentProperty2) iter.next();
			if (p.getName().equals(name)) {
				p.addArg(arg);
				bDirty = true;
			}
		}
		return bDirty;
	}

	public boolean removePropertyArg(String name, String arg) {
		boolean bDirty = false;
		ListIterator iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = (ComponentProperty2) iter.next();
			if (p.getName().equals(name)) {
				p.removeArg(arg);
				bDirty = true;
			}
		}
		return bDirty;
	}

}
