package org.frankStyle.wiSim.model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;

import org.frankStyle.wiSim.controller.ComponentController;
import org.frankStyle.wiSim.controller.JSimTclController;


/**
 * @author Arman
 * 
 */
public class ComponentTargetNode {

	private int id = -1; // required to be set
	private Point targetLoc = new Point(JSimTclController.defaultNodeLocation);
	private String name = "target";
	private String className = "drcl.comp.Component";
	private LinkedList listProperty;
	private double radius = ComponentController.defaultSensorRadius;
	private String targetAgent = "drcl.inet.sensorsim.TargetAgent";
	private String phy = "drcl.inet.sensorsim.SensorPhy";
	private String mobility = "drcl.inet.sensorsim.SensorMobilityModel";

	/*
	 * double xmin =100.0; double xmax = 600.0; double ymin = 100.0; double ymax
	 * = 500.0; double dx = 60.0; double dy = 60.0;
	 * 
	 * protected SensorMobilityModel mobility; TargetAgent agent; SensorPhy phy;
	 */

	public ComponentTargetNode(String strName, String strClassName, Point point) {
		name = strName != null ? strName : "";
		className = strClassName != null ? strClassName : "";
		if (point != null)
			targetLoc = point;
		listProperty = new LinkedList();
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Point getLocation() {
		return targetLoc;
	}

	public void setLocation(Point targetLoc) {
		this.targetLoc = targetLoc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ListIterator propertiesIterator() {
		return listProperty.listIterator();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAgent() {
		return targetAgent;
	}

	public void setTargetAgent(String targetAgent) {
		this.targetAgent = targetAgent;
	}

	public String getPhy() {
		return phy;
	}

	public void setPhy(String phy) {
		this.phy = phy;
	}

	public String getMobility() {
		return mobility;
	}

	public void setMobility(String mobility) {
		this.mobility = mobility;
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

	// This toString() decide what is displayed in the JTree.
	@Override
	public String toString() {
		String s = new String();
		if (name.length() == 0 || name.charAt(0) != '#') {
			s += name;
		}
		return s;
	}

	public String toLongString() {
		String s = new String();
		// This block will give a long description in each tree node.
		if (name.length() == 0 || name.charAt(0) != '#')
			s += name;
		s += " [" + className + "]";
		s += " (" + targetLoc.x + "," + targetLoc.y + ")";
		return s;
	}

}
