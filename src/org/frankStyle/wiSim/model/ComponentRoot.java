package org.frankStyle.wiSim.model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.crimson.tree.XmlDocument;
import org.frankStyle.wiSim.controller.JSimTclController;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * @author Arman
 * 
 */
public class ComponentRoot {

	static final int ELEMENT_TYPE = 1;

	// TODO: maybe its better to make these static
	ComponentEnviorment env;
	LinkedList<ComponentSensor> listSensorNode;
	LinkedList<ComponentTargetNode> listTargetNode;
	ComponentSinkNode sinkNode;
	LinkedList<ComponentPlot> listPlot;
	LinkedList<ComponentProperty2> listProperty;

	String m_strName = new String("Untitled");
	// Component class
	String m_strClassName = new String("drcl.comp.Component");
	// Simulation time
	String m_strTiming = new String("");
	// Simulation route setup
	String m_strRoutes = new String("");
	// Simulation command
	String m_strCommand = new String("");
	// Log File
	String m_logFile = new String("log.txt");

	/*
	 * public ComponentRoot(String name) { if (name != null) m_strName = name;
	 * listSensorNode = new LinkedList(); listTargetNode = new LinkedList();
	 * listPlot = new LinkedList(); listProperty = new LinkedList(); //TODO:
	 * wrong env = new ComponentEnviorment(EditorFrame.windowWidth - 50,
	 * EditorFrame.windowHeight - 50); sinkNode = new
	 * ComponentSinkNode("SinkNode", "wsnGUI.v2.SinkNode", new Point(
	 * ScenarioPanel.defaultNodeLocation)); }
	 */
	public ComponentRoot(org.w3c.dom.Node domNode) {
		listSensorNode = new LinkedList<ComponentSensor>();
		listTargetNode = new LinkedList<ComponentTargetNode>();
		listPlot = new LinkedList<ComponentPlot>();
		listProperty = new LinkedList<ComponentProperty2>();
		// System.out.println("root, localName: "+domNode.getLocalName()+" ; type: "+
		// domNode.getNodeType()+" ;name: "+domNode.getNodeName());

		NamedNodeMap map;
		if (domNode == null) {
			env = new ComponentEnviorment(JSimTclController.defaultEnvWidth,
					JSimTclController.defaultEnvHeight);
			sinkNode = new ComponentSinkNode("sink0", "drcl.comp.Component",
					new Point(0, 0));
			return;
		}
		if (domNode.getNodeType() == org.w3c.dom.Node.DOCUMENT_NODE)
			if (domNode.hasChildNodes()) {
				domNode = domNode.getFirstChild();
			} else {
				env = new ComponentEnviorment(
						JSimTclController.defaultEnvWidth,
						JSimTclController.defaultEnvHeight);
				sinkNode = new ComponentSinkNode("sink0",
						"drcl.comp.Component", new Point(0, 0));
				return;
			}

		map = domNode.getAttributes();
		if (map != null) {
			if (map.getNamedItem("projectName") != null)
				m_strName = map.getNamedItem("projectName").getNodeValue();
			if (map.getNamedItem("class") != null)
				m_strClassName = map.getNamedItem("class").getNodeValue();
			if (map.getNamedItem("timing") != null)
				m_strTiming = map.getNamedItem("timing").getNodeValue();
			if (map.getNamedItem("routes") != null)
				m_strRoutes = map.getNamedItem("routes").getNodeValue();
			if (map.getNamedItem("command") != null)
				m_strCommand = map.getNamedItem("command").getNodeValue();
			if (map.getNamedItem("logfile") != null)
				m_logFile = map.getNamedItem("logfile").getNodeValue();
		}

		org.w3c.dom.Node domNodeTemp;
		for (int i = 0; i < domNode.getChildNodes().getLength(); i++) {
			domNodeTemp = domNode.getChildNodes().item(i);
			// System.out.println("localName: "+domNodeTemp.getLocalName()+" ; type: "+
			// domNodeTemp.getNodeType()+" ;name: "+domNodeTemp.getNodeName());
			map = domNodeTemp.getAttributes();

			// CunstructSensor
			if (domNodeTemp.getNodeType() == ComponentRoot.ELEMENT_TYPE
					&& domNodeTemp.getNodeName().equals("sensor")) {

				Point sPos = null;
				String sName = null;
				String sClassName = null;
				String sRadius = null;

				if (map.getNamedItem("name") != null)
					sName = map.getNamedItem("name").getNodeValue();
				if (map.getNamedItem("class") != null)
					sClassName = map.getNamedItem("class").getNodeValue();
				if (map.getNamedItem("radius") != null)
					sRadius = map.getNamedItem("radius").getNodeValue();

				// TODO: add try
				if (map.getNamedItem("posX") != null
						&& map.getNamedItem("posY") != null)
					sPos = new Point(Integer.parseInt(map.getNamedItem("posX")
							.getNodeValue()), Integer.parseInt(map
							.getNamedItem("posY").getNodeValue()));
				else
					sPos = new Point(JSimTclController.defaultNodeLocation);
				ComponentSensor newSensor = new ComponentSensor(sName,
						sClassName, sPos);
				if (sRadius != null)
					try {
						newSensor.setRadius(Double.valueOf(sRadius)
								.doubleValue());
					} catch (Exception e) {
						e.printStackTrace();
					}

				// Add prop
				for (int j = 0; j < domNodeTemp.getChildNodes().getLength(); j++) {
					Node domNodeProp = domNodeTemp.getChildNodes().item(j);
					NamedNodeMap mapProperty = domNodeProp.getAttributes();
					if (domNodeProp.getNodeType() == ComponentRoot.ELEMENT_TYPE
							&& domNodeProp.getNodeName().equals("property")) {
						String propName = (mapProperty.getNamedItem("name") != null) ? mapProperty
								.getNamedItem("name").getNodeValue() : "";
						String propVal = (mapProperty.getNamedItem("value") != null) ? mapProperty
								.getNamedItem("value").getNodeValue() : "";
						ComponentProperty2 prop = new ComponentProperty2(
								propName, propVal);
						// TODO include args!!!!!!
						newSensor.addProperty(prop);

					}
				}
				listSensorNode.add(newSensor);
			}
			// Construct Targets
			if (domNodeTemp.getNodeType() == ComponentRoot.ELEMENT_TYPE
					&& domNodeTemp.getNodeName().equals("target")) {

				Point sPos;
				String sName = null;
				String sClassName = null;
				String sRadius = null;

				if (map.getNamedItem("name") != null)
					sName = map.getNamedItem("name").getNodeValue();
				if (map.getNamedItem("class") != null)
					sClassName = map.getNamedItem("class").getNodeValue();
				if (map.getNamedItem("radius") != null)
					sRadius = map.getNamedItem("radius").getNodeValue();
				if (map.getNamedItem("posX") != null
						&& map.getNamedItem("posY") != null)
					sPos = new Point(Integer.parseInt(map.getNamedItem("posX")
							.getNodeValue()), Integer.parseInt(map
							.getNamedItem("posY").getNodeValue()));
				else
					sPos = new Point(JSimTclController.defaultNodeLocation);
				ComponentTargetNode newTarget = new ComponentTargetNode(sName,
						sClassName, sPos);
				if (sRadius != null)
					try {
						newTarget.setRadius(Double.valueOf(sRadius)
								.doubleValue());
					} catch (Exception e) {
						e.printStackTrace();
					}

				// Add prop
				for (int j = 0; j < domNodeTemp.getChildNodes().getLength(); j++) {
					Node domNodeProp = domNodeTemp.getChildNodes().item(j);
					NamedNodeMap mapProperty = domNodeProp.getAttributes();
					if (domNodeProp.getNodeType() == ComponentRoot.ELEMENT_TYPE
							&& domNodeProp.getNodeName().equals("property")) {
						String propName = (mapProperty.getNamedItem("name") != null) ? mapProperty
								.getNamedItem("name").getNodeValue() : "";
						String propVal = (mapProperty.getNamedItem("value") != null) ? mapProperty
								.getNamedItem("value").getNodeValue() : "";
						ComponentProperty2 prop = new ComponentProperty2(
								propName, propVal);
						// TODO include args!!!!!!
						newTarget.addProperty(prop);

					}
				}
				listTargetNode.add(newTarget);
			}

			// Construct Sink
			if (domNodeTemp.getNodeType() == ComponentRoot.ELEMENT_TYPE
					&& domNodeTemp.getNodeName().equals("sink")) {

				Point sPos;
				String sName = "";
				String sClassName = "";
				String sRadius = null;

				if (map.getNamedItem("name") != null)
					sName = map.getNamedItem("name").getNodeValue();
				if (map.getNamedItem("class") != null)
					sClassName = map.getNamedItem("class").getNodeValue();
				if (map.getNamedItem("radius") != null)
					sRadius = map.getNamedItem("radius").getNodeValue();
				if (map.getNamedItem("posX") != null
						&& map.getNamedItem("posY") != null)
					sPos = new Point(Integer.parseInt(map.getNamedItem("posX")
							.getNodeValue()), Integer.parseInt(map
							.getNamedItem("posY").getNodeValue()));
				else
					sPos = new Point(JSimTclController.defaultNodeLocation);
				ComponentSinkNode newSink = new ComponentSinkNode(sName,
						sClassName, sPos);
				if (sRadius != null)
					try {
						newSink.setRadius(Double.valueOf(sRadius).doubleValue());
					} catch (Exception e) {
						e.printStackTrace();
					}

				// Add prop
				for (int j = 0; j < domNodeTemp.getChildNodes().getLength(); j++) {
					Node domNodeProp = domNodeTemp.getChildNodes().item(j);
					NamedNodeMap mapProperty = domNodeProp.getAttributes();
					if (domNodeProp.getNodeType() == ComponentRoot.ELEMENT_TYPE
							&& domNodeProp.getNodeName().equals("property")) {
						String propName = (mapProperty.getNamedItem("name") != null) ? mapProperty
								.getNamedItem("name").getNodeValue() : "";
						String propVal = (mapProperty.getNamedItem("value") != null) ? mapProperty
								.getNamedItem("value").getNodeValue() : "";
						ComponentProperty2 prop = new ComponentProperty2(
								propName, propVal);
						// TODO include args!!!!!!
						newSink.addProperty(prop);

					}
				}
				sinkNode = newSink;
			}

			// Construct Environment
			if (domNodeTemp.getNodeType() == ComponentRoot.ELEMENT_TYPE
					&& domNodeTemp.getNodeName().equals("env")) {

				double envWidth, envHeight;
				try {
					envWidth = (Double.valueOf((map.getNamedItem("width")
							.getNodeValue()))).doubleValue();
				} catch (Exception e) {
					System.out
							.println("error loading ev width, using default value");
					envWidth = JSimTclController.defaultEnvWidth;
				}
				try {
					envHeight = (Double.valueOf((map.getNamedItem("height")
							.getNodeValue()))).doubleValue();
				} catch (Exception e) {
					System.out
							.println("error loading ev height, using default value");
					envHeight = JSimTclController.defaultEnvHeight;
				}
				env = new ComponentEnviorment(envWidth, envHeight);
				try {
					env.setRatio(Double.parseDouble(map.getNamedItem("ratio")
							.getNodeValue()));
				} catch (Exception e) {
				}

				// Add prop
				for (int j = 0; j < domNodeTemp.getChildNodes().getLength(); j++) {
					Node domNodeProp = domNodeTemp.getChildNodes().item(j);
					NamedNodeMap mapProperty = domNodeProp.getAttributes();
					if (domNodeProp.getNodeType() == ComponentRoot.ELEMENT_TYPE
							&& domNodeProp.getNodeName().equals("property")) {
						String propName = (mapProperty.getNamedItem("name") != null) ? mapProperty
								.getNamedItem("name").getNodeValue() : "";
						String propVal = (mapProperty.getNamedItem("value") != null) ? mapProperty
								.getNamedItem("value").getNodeValue() : "";
						ComponentProperty2 prop = new ComponentProperty2(
								propName, propVal);
						// TODO include args!!!!!!
						env.addProperty(prop);

					}
				}
			}

			// construct plots
			if (domNodeTemp.getNodeType() == ComponentRoot.ELEMENT_TYPE
					&& domNodeTemp.getNodeName().equals("plot")) {

				String sName = "";

				if (map.getNamedItem("name") != null)
					sName = map.getNamedItem("name").getNodeValue();
				ComponentPlot plot = new ComponentPlot(sName);

				// Add prop and ports
				for (int j = 0; j < domNodeTemp.getChildNodes().getLength(); j++) {
					Node domNodePlot = domNodeTemp.getChildNodes().item(j);
					NamedNodeMap mapPlot = domNodePlot.getAttributes();
					// property
					if (domNodePlot.getNodeType() == ComponentRoot.ELEMENT_TYPE
							&& domNodePlot.getNodeName().equals("property")) {
						String propName = (mapPlot.getNamedItem("name") != null) ? mapPlot
								.getNamedItem("name").getNodeValue() : "";
						String propVal = (mapPlot.getNamedItem("value") != null) ? mapPlot
								.getNamedItem("value").getNodeValue() : "";
						ComponentProperty2 prop = new ComponentProperty2(
								propName, propVal);
						// TODO include args!!!!!!
						plot.addProperty(prop);
					}
					// ConnectedPort
					if (domNodePlot.getNodeType() == ComponentRoot.ELEMENT_TYPE
							&& domNodePlot.getNodeName()
									.equals("connectedPort")) {
						for (int k = 0; k < domNodePlot.getChildNodes()
								.getLength(); k++) {
							Node domNodeProp = domNodePlot.getChildNodes()
									.item(k);
							NamedNodeMap mapProp = domNodeProp.getAttributes();
							if (domNodeProp.getNodeType() == ComponentRoot.ELEMENT_TYPE
									&& domNodeProp.getNodeName().equals(
											"property")) {
								String propName = (mapProp.getNamedItem("name") != null) ? mapProp
										.getNamedItem("name").getNodeValue()
										: "";
								String propVal = (mapProp.getNamedItem("value") != null) ? mapProp
										.getNamedItem("value").getNodeValue()
										: "";
								ComponentProperty2 prop = new ComponentProperty2(
										propName, propVal);
								// TODO include args!!!!!!
								// conPort.addProperty(prop);
							}
						}
						// plot.addConnectedPort(conPort);
					}
				}
			}

		}
		// TODO: wrong env
		if (env == null) {
			System.out.println("error loading env, creating a default");
			env = new ComponentEnviorment(JSimTclController.defaultEnvWidth,
					JSimTclController.defaultEnvHeight);
		}
		if (sinkNode == null) {
			sinkNode = new ComponentSinkNode("sink0", "drcl.comp.Component",
					JSimTclController.defaultNodeLocation);
			System.out.println("error loading sink node, creating a default");
		}

	}

	public void buildXMLDocument(XmlDocument document) {

		// Creating Root(Project) Node
		Element elementRoot = document.createElement("WSN_project");
		Attr attr = document.createAttribute("projectName");
		attr.setValue(m_strName);
		elementRoot.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("class");
		attr.setValue(m_strClassName);
		elementRoot.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("timing");
		attr.setValue(m_strTiming);
		elementRoot.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("command");
		attr.setValue(m_strCommand);
		elementRoot.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("routes");
		attr.setValue(m_strRoutes);
		elementRoot.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("logFile");
		attr.setValue(m_logFile);
		elementRoot.getAttributes().setNamedItem(attr);

		document.appendChild(elementRoot);

		// Creating Root Properties
		ListIterator iter = listProperty.listIterator();
		while (iter.hasNext()) {
			ComponentProperty2 prop = (ComponentProperty2) iter.next();
			Element elementProperty = document.createElement("property");
			attr = document.createAttribute("name");
			attr.setValue(prop.getName());
			elementProperty.getAttributes().setNamedItem(attr);
			attr = document.createAttribute("value");
			attr.setValue(prop.getValue());
			elementProperty.getAttributes().setNamedItem(attr);

			elementRoot.appendChild(elementProperty);

			// TODO Args has BUG!!!! should make element
			ListIterator argsIter = prop.argsIterator();
			while (argsIter.hasNext()) {
				// String arg = (String) argsIter.next();
				// attr = document.createAttribute("arg");
				// attr.setValue(arg);
				// elementProperty.getAttributes().setNamedItem(attr);
			}
		}

		// Creating Env element
		Element elementEnv = document.createElement("enviorment");
		attr = document.createAttribute("width");
		attr.setValue(String.valueOf(env.getWidth()));
		elementEnv.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("height");
		attr.setValue(String.valueOf(env.getHeight()));
		elementEnv.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("ratio");
		attr.setValue(String.valueOf(env.getRatio()));
		elementEnv.getAttributes().setNamedItem(attr);

		elementRoot.appendChild(elementEnv);

		// Env Properties
		ListIterator propIter = env.propertiesIterator();
		while (propIter.hasNext()) {
			ComponentProperty2 prop = (ComponentProperty2) propIter.next();
			Element elementProperty = document.createElement("property");
			attr = document.createAttribute("name");
			attr.setValue(prop.getName());
			elementProperty.getAttributes().setNamedItem(attr);
			attr = document.createAttribute("value");
			attr.setValue(prop.getValue());
			elementProperty.getAttributes().setNamedItem(attr);

			elementEnv.appendChild(elementProperty);

			// TODO Args has BUG!!!! should make element
			ListIterator argsIter = prop.argsIterator();
			while (argsIter.hasNext()) {
				// String arg = (String) argsIter.next();
				// attr = document.createAttribute("arg");
				// attr.setValue(arg);
				// elementProperty.getAttributes().setNamedItem(attr);
			}
		}

		// Creating Sensors
		iter = listSensorNode.listIterator();
		while (iter.hasNext()) {
			ComponentSensor sen = (ComponentSensor) iter.next();
			Element elementSensor = document.createElement("sensor");
			attr = document.createAttribute("name");
			attr.setValue(sen.getName());
			elementSensor.getAttributes().setNamedItem(attr);
			attr = document.createAttribute("class");
			attr.setValue(sen.getClassName());
			elementSensor.getAttributes().setNamedItem(attr);
			if (sen.getLocation() != null) {
				attr = document.createAttribute("posX");
				attr.setValue(String.valueOf(sen.getLocation().x));
				elementSensor.getAttributes().setNamedItem(attr);
				attr = document.createAttribute("posY");
				attr.setValue(String.valueOf(sen.getLocation().y));
				elementSensor.getAttributes().setNamedItem(attr);
			}
			attr = document.createAttribute("radius");
			attr.setValue(String.valueOf(sen.getRadius()));
			elementSensor.getAttributes().setNamedItem(attr);

			elementRoot.appendChild(elementSensor);

			// Sensor Properties
			propIter = sen.propertiesIterator();
			while (propIter.hasNext()) {
				ComponentProperty2 prop = (ComponentProperty2) propIter.next();
				Element elementProperty = document.createElement("property");
				attr = document.createAttribute("name");
				attr.setValue(prop.getName());
				elementProperty.getAttributes().setNamedItem(attr);
				attr = document.createAttribute("value");
				attr.setValue(prop.getValue());
				elementProperty.getAttributes().setNamedItem(attr);

				elementSensor.appendChild(elementProperty);

				// TODO Args has BUG!!!! should make element
				ListIterator argsIter = prop.argsIterator();
				while (argsIter.hasNext()) {
					// String arg = (String) argsIter.next();
					// attr = document.createAttribute("arg");
					// attr.setValue(arg);
					// elementProperty.getAttributes().setNamedItem(attr);
				}
			}
		}

		// Sink Node
		Element elementSink = document.createElement("sink");
		attr = document.createAttribute("name");
		attr.setValue(sinkNode.getName());
		elementSink.getAttributes().setNamedItem(attr);
		attr = document.createAttribute("class");
		attr.setValue(sinkNode.getClassName());
		elementSink.getAttributes().setNamedItem(attr);
		if (sinkNode.getLocation() != null) {
			attr = document.createAttribute("posX");
			attr.setValue(String.valueOf(sinkNode.getLocation().x));
			elementSink.getAttributes().setNamedItem(attr);
			attr = document.createAttribute("posY");
			attr.setValue(String.valueOf(sinkNode.getLocation().y));
			elementSink.getAttributes().setNamedItem(attr);
		}
		attr = document.createAttribute("radius");
		attr.setValue(String.valueOf(sinkNode.getRadius()));
		elementSink.getAttributes().setNamedItem(attr);

		elementRoot.appendChild(elementSink);

		// Sink Properties
		propIter = sinkNode.propertiesIterator();
		while (propIter.hasNext()) {
			ComponentProperty2 prop = (ComponentProperty2) propIter.next();
			Element elementProperty = document.createElement("property");
			attr = document.createAttribute("name");
			attr.setValue(prop.getName());
			elementProperty.getAttributes().setNamedItem(attr);
			attr = document.createAttribute("value");
			attr.setValue(prop.getValue());
			elementProperty.getAttributes().setNamedItem(attr);

			elementSink.appendChild(elementProperty);

			// TODO Args has BUG!!!! should make element
			ListIterator argsIter = prop.argsIterator();
			while (argsIter.hasNext()) {
				// String arg = (String) argsIter.next();
				// attr = document.createAttribute("arg");
				// attr.setValue(arg);
				// elementProperty.getAttributes().setNamedItem(attr);
			}
		}

		// Target Nodes
		iter = listTargetNode.listIterator();
		while (iter.hasNext()) {
			ComponentTargetNode target = (ComponentTargetNode) iter.next();
			Element elementTarget = document.createElement("target");
			attr = document.createAttribute("name");
			attr.setValue(target.getName());
			elementTarget.getAttributes().setNamedItem(attr);
			attr = document.createAttribute("class");
			attr.setValue(target.getClassName());
			elementTarget.getAttributes().setNamedItem(attr);
			if (target.getLocation() != null) {
				attr = document.createAttribute("posX");
				attr.setValue(String.valueOf(target.getLocation().x));
				elementTarget.getAttributes().setNamedItem(attr);
				attr = document.createAttribute("posY");
				attr.setValue(String.valueOf(target.getLocation().y));
				elementTarget.getAttributes().setNamedItem(attr);
			}
			attr = document.createAttribute("radius");
			attr.setValue(String.valueOf(target.getRadius()));
			elementTarget.getAttributes().setNamedItem(attr);

			elementRoot.appendChild(elementTarget);

			// Target Properties
			propIter = target.propertiesIterator();
			while (propIter.hasNext()) {
				ComponentProperty2 prop = (ComponentProperty2) propIter.next();
				Element elementProperty = document.createElement("property");
				attr = document.createAttribute("name");
				attr.setValue(prop.getName());
				elementProperty.getAttributes().setNamedItem(attr);
				attr = document.createAttribute("value");
				attr.setValue(prop.getValue());
				elementProperty.getAttributes().setNamedItem(attr);

				elementTarget.appendChild(elementProperty);

				// TODO Args has BUG!!!! should make element
				ListIterator argsIter = prop.argsIterator();
				while (argsIter.hasNext()) {
					// String arg = (String) argsIter.next();
					// attr = document.createAttribute("arg");
					// attr.setValue(arg);
					// elementProperty.getAttributes().setNamedItem(attr);
				}
			}
		}

		// Plots
		iter = listPlot.listIterator();
		while (iter.hasNext()) {
			ComponentPlot plot = (ComponentPlot) iter.next();
			Element elementPlot = document.createElement("plot");
			attr = document.createAttribute("name");
			attr.setValue(plot.getPlotName());
			elementPlot.getAttributes().setNamedItem(attr);

			elementRoot.appendChild(elementPlot);

			// Plot Properties
			propIter = plot.propertiesIterator();
			while (propIter.hasNext()) {
				ComponentProperty2 prop = (ComponentProperty2) propIter.next();
				Element elementProperty = document.createElement("property");
				attr = document.createAttribute("name");
				attr.setValue(prop.getName());
				elementProperty.getAttributes().setNamedItem(attr);
				attr = document.createAttribute("value");
				attr.setValue(prop.getValue());
				elementProperty.getAttributes().setNamedItem(attr);

				elementPlot.appendChild(elementProperty);

				// TODO Args has BUG!!!! should make element
				ListIterator argsIter = prop.argsIterator();
				while (argsIter.hasNext()) {
					// String arg = (String) argsIter.next();
					// attr = document.createAttribute("arg");
					// attr.setValue(arg);
					// elementProperty.getAttributes().setNamedItem(attr);
				}
			}
		}
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

	public String getAttribute(String strName) {
		return getProperty(strName).getValue();
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

	public ComponentEnviorment getEnv() {
		return env;
	}

	public void setEnv(ComponentEnviorment env) {
		this.env = env;
	}

	public ComponentSinkNode getSinkNode() {
		return sinkNode;
	}

	public void setSinkNode(ComponentSinkNode sinkNode) {
		this.sinkNode = sinkNode;
	}

	public String getName() {
		return m_strName;
	}

	public void setName(String mStrName) {
		m_strName = mStrName;
	}

	public String getClassName() {
		return m_strClassName;
	}

	public void setClassName(String mStrClassName) {
		m_strClassName = mStrClassName;
	}

	public String getTiming() {
		return m_strTiming;
	}

	public void setTiming(String mStrTiming) {
		m_strTiming = mStrTiming;
	}

	public String getRoutes() {
		return m_strRoutes;
	}

	public void setRoutes(String mStrRoutes) {
		m_strRoutes = mStrRoutes;
	}

	public String getCommand() {
		return m_strCommand;
	}

	public void setCommand(String mStrCommand) {
		m_strCommand = mStrCommand;
	}

	public String getLogFile() {
		return m_logFile;
	}

	public void setLogFile(String mLogFile) {
		m_logFile = mLogFile;
	}

	public ListIterator propertiesIterator() {
		return listProperty.listIterator();
	}

	public ListIterator<ComponentSensor> sensorsIterator() {
		return listSensorNode.listIterator();
	}

	public ListIterator<ComponentTargetNode> targetsIterator() {
		return listTargetNode.listIterator();
	}

	public ListIterator plotssIterator() {
		return listPlot.listIterator();
	}

	public void removeSensorNode(ComponentSensor sensor) {
		listSensorNode.remove(sensor);
	}

	public void removeTargetNode(ComponentTargetNode target) {
		listTargetNode.remove(target);
	}

	public void removePlot(ComponentPlot plot) {
		listPlot.remove(plot);
	}

	public void addSensorNode(ComponentSensor sen) {
		listSensorNode.add(sen);
	}

	public void addTargetNode(ComponentTargetNode target) {
		listTargetNode.add(target);
	}

	public void addPlot(ComponentPlot plot) {
		listPlot.add(plot);
	}

	public int getChildCount() {
		return listSensorNode.size() + listTargetNode.size()
				+ (sinkNode != null ? 1 : 0);
	}

	public int getNodeCount() {
		return listSensorNode.size() + listTargetNode.size();
	}

	public int sensorCount() {
		return listSensorNode.size();
	}

	public int targetCount() {
		return listTargetNode.size();
	}

	public Dimension getEnvironmentDimension() {
		return new Dimension((int) env.getWidth(), (int) env.getHeight());
	}

}