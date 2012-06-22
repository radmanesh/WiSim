package org.frankStyle.wiSim.model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;

import org.frankStyle.wiSim.controller.ComponentController;
import org.frankStyle.wiSim.controller.JSimTclController;


// The main reason we have this class is to interact the domNode
/**
 * @author Arman
 * 
 */
public class ComponentSensor implements Comparable<ComponentSensor> {
	static final int ELEMENT_TYPE = 1;

	LinkedList<ComponentProperty2> listProperty;

	// Component ID
	// TODO: Arman unique id creator needed!!!!
	private int id = -1;
	private String name = "sensor";
	// Component class
	private String className = "drcl.comp.Component";
	private Point senLoc = new Point(JSimTclController.defaultNodeLocation);
	private double radius = ComponentController.defaultSensorRadius;
	private String app = "drcl.inet.sensorsim.LEACH.LEACHApp";
	private String phy = "drcl.inet.sensorsim.SensorPhy";
	private String agent = "drcl.inet.sensorsim.SensorAgent";
	private String cpuModel = "drcl.inet.sensorsim.CPUAvr";
	private String wiPhy = "drcl.inet.mac.WirelessPhy";
	private String mac = "drcl.inet.mac.CSMA.Mac_CSMA";
	private String arp = "drcl.inet.mac.ARP";
	private String ll = "drcl.inet.mac.LL";
	private String senMobilityModel = "drcl.inet.sensorsim.SensorMobilityModel";
	private String wiPropagationModel = "drcl.inet.mac.FreeSpaceModel";
	private String wiAgent = "drcl.inet.sensorsim.LEACH.WirelessLEACHAgent";

	public ComponentSensor(String strName, String strClassName, Point point) {
		if (strName != null)
			name = strName;
		if (strClassName != null)
			className = strClassName;
		if (point != null)
			senLoc = point;
		listProperty = new LinkedList<ComponentProperty2>();

	}

	public ComponentSensor(ComponentSensor node) {
		clone(node);
	}

	public void clone(ComponentSensor node) {
		listProperty = new LinkedList<ComponentProperty2>();

		name = new String(node.getName());
		className = new String(node.getClassName());
		senLoc = new Point(node.getLocation());

		// Duplicate property
		ListIterator<ComponentProperty2> iter = node.propertiesIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = iter.next();
			listProperty.add(new ComponentProperty2(p));
		}

	}

	public void removeProperty(ComponentProperty2 p) {
		listProperty.remove(p);
	}

	public void addProperty(ComponentProperty2 p) {
		listProperty.add(p);
	}

	public ListIterator<ComponentProperty2> propertyIterator() {
		return listProperty.listIterator();
	}

	public String getPropertyValue(String strName) {
		ListIterator<ComponentProperty2> iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = iter.next();
			if (p.getName().equals(strName))
				return p.getValue();
		}
		return null;
	}

	public ComponentProperty2 getProperty(String strName) {
		ListIterator<ComponentProperty2> iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = iter.next();
			if (p.getName().equals(strName))
				return p;
		}
		return null;
	}

	// true means we did changed the old value
	public boolean setPropertyValue(String strName, String strValue) {
		boolean bDirty = false;
		ListIterator<ComponentProperty2> iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = iter.next();
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
		ListIterator<ComponentProperty2> iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = iter.next();
			if (p.getName().equals(name)) {
				p.addArg(arg);
				bDirty = true;
			}
		}
		return bDirty;
	}

	public boolean removePropertyArg(String name, String arg) {
		boolean bDirty = false;
		ListIterator<ComponentProperty2> iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = iter.next();
			if (p.getName().equals(name)) {
				p.removeArg(arg);
				bDirty = true;
			}
		}
		return bDirty;
	}

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
		if (name.length() == 0 || name.charAt(0) != '#')
			s += name;
		s += " [" + className + "]";
		s += " (" + senLoc.x + "," + senLoc.y + ")";
		return s;
	}

	public Point getLocation() {
		return senLoc;
	}

	public void setLocation(Point p) {
		System.out.println("sensor loc updated");
		senLoc = p;
	}

	public boolean isPropertyExist(String strName) {
		ListIterator<ComponentProperty2> iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 p = iter.next();
			if (p.getName().equals(strName))
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(ComponentSensor objTarget) {
		if (objTarget == this)
			return 0;
		ComponentSensor nodeTarget = objTarget;
		String strID = getName();
		String strIDTarget = nodeTarget.getName();
		return strID.compareTo(strIDTarget);
	}

	// TODO
	@Override
	public boolean equals(Object objTarget) {
		return false;
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

	public String getAttributes() {
		return "getAttributes no longer exists in"
				+ "the new version of ComponentNode.java";
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getAttribute(String strAttributeName) {
		if (strAttributeName.equals("name"))
			return name;
		else if (strAttributeName.equals("class"))
			return className;
		else {
			java.lang.System.out.println("ComponentNode::getAttribute()"
					+ " Unknown attribute requested!" + " Attribute:"
					+ strAttributeName);
			return null;
		}
	}

	public void setAttribute(String strAttributeName, String strAttributeValue) {
		if (strAttributeName.equals("name"))
			name = strAttributeValue;
		else if (strAttributeName.equals("class"))
			className = strAttributeValue;
		else {
			java.lang.System.out.println("ComponentNode::setAttribute()"
					+ " Unknow attribute requested!" + " Attribute:"
					+ strAttributeName + " Value:" + strAttributeValue);
		}
	}

	public void updateLocation(int nPosX, int nPosY) {
		senLoc = new Point(nPosX, nPosY);
	}

	public ListIterator<ComponentProperty2> propertiesIterator() {
		return listProperty.listIterator();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Point getSenLoc() {
		return senLoc;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getPhy() {
		return phy;
	}

	public void setPhy(String phy) {
		this.phy = phy;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getCpuModel() {
		return cpuModel;
	}

	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	public String getWiPhy() {
		return wiPhy;
	}

	public void setWiPhy(String wiPhy) {
		this.wiPhy = wiPhy;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getArp() {
		return arp;
	}

	public void setArp(String arp) {
		this.arp = arp;
	}

	public String getLl() {
		return ll;
	}

	public void setLl(String ll) {
		this.ll = ll;
	}

	public String getSenMobilityModel() {
		return senMobilityModel;
	}

	public void setSenMobilityModel(String senMobilityModel) {
		this.senMobilityModel = senMobilityModel;
	}

	public String getWiPropagationModel() {
		return wiPropagationModel;
	}

	public void setWiPropagationModel(String wiPropagationModel) {
		this.wiPropagationModel = wiPropagationModel;
	}

	public String getWiAgent() {
		return wiAgent;
	}

	public void setWiAgent(String wiAgent) {
		this.wiAgent = wiAgent;
	}

}
