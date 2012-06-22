package org.frankStyle.wiSim.model;

import java.util.LinkedList;
import java.util.ListIterator;

//all components :
// WirelessChannel	SensorChannel	SensorPropagationModel	NodePositionTracker	SensorNodePositionTracker
/**
 * @author Arman
 * 
 */
public class ComponentEnviorment {
	double width, height;
	double ratio = 1000; // default
	private String projectName = "Untitiled";

	LinkedList listProperty;

	public ComponentEnviorment(double width, double height) {
		this.width = width;
		this.height = height;
		listProperty = new LinkedList();
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
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

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
