package org.frankStyle.wiSim.model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Arman
 * 
 */
public class ComponentSinkNode {
	// private Point sinkLoc = new Point(0,0);
	// private String name = "sink";
	// private String className = "drcl.comp.Component";
	// private LinkedList<ComponentProperty2> listProperty;
	// private double radius = 50; //default
	// private int id = 0;
	// private String app = "drcl.inet.sensorsim.SensorApp";
	// private String wiAgent = "drcl.inet.sensorsim.WirelessAgent";
	// private String wiPhy="drcl.inet.mac.WirelessPhy";
	// private String mac = "drcl.inet.mac.Mac_802_11";
	// private String arp="drcl.inet.mac.ARP";
	// private String ll = "drcl.inet.mac.LL";
	// private String sinkMobilityModel = "drcl.inet.mac.MobilityModel";
	// private String wiPropagationModel =
	// "drcl.inet.mac.RadioPropagationModel";
	private Point sinkLoc = new Point(0, 0);
	private String name = "sink";
	private String className = "drcl.comp.Component";
	private LinkedList<ComponentProperty2> listProperty;
	private double radius = 50; // default
	private int id = 0;
	private String app = "drcl.inet.sensorsim.LEACH.SinkAppLEACH";
	private String wiAgent = "drcl.inet.sensorsim.LEACH.WirelessLEACHAgent";
	private String wiPhy = "drcl.inet.mac.WirelessPhy";
	private String mac = "drcl.inet.mac.CSMA.Mac_CSMA";
	private String arp = "drcl.inet.mac.ARP";
	private String ll = "drcl.inet.mac.LL";
	private String sinkMobilityModel = "drcl.inet.mac.MobilityModel";
	private String wiPropagationModel = "drcl.inet.mac.FreeSpaceModel";

	/*
	 * private int sink_id, sink_type; MobilityModel mobility; SensorApp app;
	 * 
	 * protected LL ll; protected ARP arp; protected WirelessAgent
	 * wireless_agent; protected FIFO queue ; protected Mac mac ; protected
	 * WirelessPhy wphy; protected FreeSpaceModel propagation ; protected
	 * PktDispatcher pktdispatcher ; protected RT rt; protected Identity id ;
	 */

	public ComponentSinkNode(String strName, String strClassName, Point point) {
		if (strName != null)
			name = strName;
		if (strClassName != null)
			className = strClassName;
		if (point != null)
			sinkLoc = point;
		listProperty = new LinkedList<ComponentProperty2>();
	}

	public Point getLocation() {
		return sinkLoc;
	}

	public void setLocation(Point sinkLoc) {
		this.sinkLoc = sinkLoc;
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

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Point getSinkLoc() {
		return sinkLoc;
	}

	public void setSinkLoc(Point sinkLoc) {
		this.sinkLoc = sinkLoc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getWiAgent() {
		return wiAgent;
	}

	public void setWiAgent(String wiAgent) {
		this.wiAgent = wiAgent;
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

	public String getSinkMobilityModel() {
		return sinkMobilityModel;
	}

	public void setSinkMobilityModel(String senMobilityModel) {
		this.sinkMobilityModel = senMobilityModel;
	}

	public String getWiPropagationModel() {
		return wiPropagationModel;
	}

	public void setWiPropagationModel(String wiPropagationModel) {
		this.wiPropagationModel = wiPropagationModel;
	}

	public void removeProperty(ComponentProperty2 p) {
		listProperty.remove(p);
	}

	public void addProperty(ComponentProperty2 p) {
		listProperty.add(p);
	}

	public ListIterator<ComponentProperty2> propertiesIterator() {
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

	public String toLongString() {
		String s = name + "  " + className + "  x:" + sinkLoc.x + "   y:"
				+ sinkLoc.y;
		return s;
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

}
