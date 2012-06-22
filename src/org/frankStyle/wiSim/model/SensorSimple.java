package org.frankStyle.wiSim.model;

import drcl.comp.Component;
import drcl.data.BitSet;
import drcl.inet.core.Identity;
import drcl.inet.core.PktDispatcher;
import drcl.inet.core.RT;
import drcl.inet.core.queue.FIFO;
import drcl.inet.data.RTEntry;
import drcl.inet.data.RTKey;
import drcl.inet.mac.ARP;
import drcl.inet.mac.FreeSpaceModel;
import drcl.inet.mac.LL;
import drcl.inet.mac.Mac;
import drcl.inet.mac.Mac_802_11;
import drcl.inet.mac.WirelessPhy;
import drcl.inet.mac.CSMA.Mac_CSMA;
import drcl.inet.sensorsim.CPUAvr;
import drcl.inet.sensorsim.SensorAgent;
import drcl.inet.sensorsim.SensorApp;
import drcl.inet.sensorsim.SensorMobilityModel;
import drcl.inet.sensorsim.SensorPhy;
import drcl.inet.sensorsim.WirelessAgent;
import drcl.inet.sensorsim.LEACH.LEACHApp;
import drcl.inet.sensorsim.LEACH.WirelessLEACHAgent;
import drcl.inet.sensorsim.MultiHop.MultiHopApp;
import drcl.inet.sensorsim.OneHop.OneHopApp;
import drcl.inet.sensorsim.OneHopTDMA.OneHopAppTDMA;

public class SensorSimple {

	public Component sens;
	public CPUAvr cpu;

	double threshold = 1000.0;
	double xmin = 0.0;
	double xmax = 30.0;
	double ymin = 0.0;
	double ymax = 100.0;
	double dx = 30.0;
	double dy = 100.0;

	// The various Routing Combinations Possible
	public static final int OH_80211 = 0;
	public static final int OH_CSMA = 1;
	public static final int OH_TDMA = 2;
	public static final int MH_CSMA = 3;
	public static final int MH_80211 = 4;
	public static final int LEACH = 5;

	protected SensorMobilityModel mobility;

	/**
	 * Creates Topology
	 * 
	 * @param xmin_
	 * @param xmax_
	 * @param ymin_
	 * @param ymax_
	 * @param dx_
	 * @param dy_
	 *            TODO: prendre en compte la 3eme dim
	 */
	void setTopo(double xmin_, double xmax_, double ymin_, double ymax_,
			double dx_, double dy_) {
		xmin = xmin_;
		xmax = xmax_;
		ymin = ymin_;
		ymax = ymax_;
		dx = dx_;
		dy = dy_;

		mobility.setTopologyParameters(xmax, ymax, 0.0, xmin, ymin, 0.0, dx,
				dy, 0.0);
	}

	/**
	 * Creates Topology
	 * 
	 * @param env
	 */
	void setTopo(Environment env) {
		mobility.setTopologyParameters(env.xmax, env.ymax, 0.0, env.xmin,
				env.ymin, 0.0, env.dx, env.dy, 0.0);
	}

	/**
	 * Create a sensor
	 * 
	 * @param nid
	 * @param sink_id
	 * @param env
	 */
	public SensorSimple(int nid, int sink_id, Environment env, int AppType_,
			double xCoord_, double yCoord_, double zCoord_) {
		// Attention ! j pour les indices de tableaux i pour la nid
		// int j = nid-sink_id-1;
		int j = nid;

		System.out.println("Creating Sensor " + j); // Creation du senseur "+j);
		sens = new Component("s" + nid);

		SensorApp app;
		// application layer depending on which type of routing you are using.
		switch (AppType_) {
		case 0:
			app = new OneHopApp();
			app.setID("OneHopApp");
			app.setIs_uAMPS(false);
			break;
		case 1:
			app = new OneHopApp();
			app.setID("OneHopApp");
			app.setIs_uAMPS(true);
			break;
		case 2:
			app = new OneHopAppTDMA();
			app.setID("OneHopAppTDMA");
			app.setIs_uAMPS(true);
			break;
		case 3:
			app = new MultiHopApp();
			app.setID("MultiHopApp");
			app.setIs_uAMPS(true);
			break;
		case 4:
			app = new MultiHopApp();
			app.setID("MultiHopApp");
			app.setIs_uAMPS(false);
			break;
		case 5:
			app = new LEACHApp();
			app.setID("LEACHApp");
			break;
		default:
			app = new SensorApp();
			app.setID("SensorApp");
			System.out.println("Error: Unknown Sensor Type");
			System.exit(-1);
		}

		sens.addComponent(app);
		app.setNid(nid); // this sensors ID
		app.setSinkNid(sink_id); // the sinks ID
		app.setSinkLocation(0.0, 0.0, 0.0); // the sinks location
		app.setCoherentThreshold(1000.0); // threshold at which the sensor could
											// no longer comprehend signals

		if ((AppType_ == SensorSimple.LEACH)
				|| (AppType_ == SensorSimple.MH_CSMA)) {
			app.setNn_(env.getNn_());
		}

		/*
		 * If running multi-hop mode then we must connect the application layer
		 * and nodetracker components together
		 */
		if ((AppType_ == 3) || (AppType_ == 4)) {
			// must connect application and nodetracker for Multi-Hop modes
			// so that neighbors can be located.
			((MultiHopApp) app).SensorNodePositionPort
					.connect(env.nodetracker.multihopPort);
		}

		// ***********************************************************************
		// Creating the components required for the sensing stack
		// ***********************************************************************

		// SensorAgent Creation
		SensorAgent agent = new SensorAgent();
		agent.setID("agent");
		agent.setDebugEnabled(false);
		sens.addComponent(agent);

		// SensorPhy Creation
		SensorPhy phy = new SensorPhy();
		phy.setID("phy");
		// phy.setRxThresh( 0.0);
		sens.addComponent(phy);

		// SensorMobilityModel Creation
		mobility = new SensorMobilityModel();
		mobility.setID("mobility");
		sens.addComponent(mobility);
		phy.setNid(nid);
		phy.setRadius(100.0);

		// sensorphy physical layer-> sensoragent
		phy.getPort(".toAgent"/* phy.TO_AGENT_PORT_ID */).connectTo(
				agent.getPort(".fromPhy"/* agent.FROM_PHY_PORT_ID */));

		// sensoragent-> sensorapp
		agent.getPort(".toSensorApp"/* agent.TO_SENSOR_APP_PORT_ID */)
				.connectTo(app.getPort(".fromSensorAgent"/*
														 * app.
														 * FROM_SENSOR_AGENT_PORT_ID
														 */));

		// connexion externe : sensor channel -> sensorphy
		env.chan.attachPort(nid,
				phy.getPort(".channel"/* phy.CHANNEL_PORT_ID */));

		// connexion externe : propagation model -> sensor
		phy.getPort(".propagation"/* phy.PROPAGATION_PORT_ID */).connect(
				env.seismic_prop.getPort(".query"));

		mobility.setNid(nid);
		mobility.setPosition(0.0, xCoord_, yCoord_, zCoord_);

		// ***********************************************************************
		// Creating the components required for the wireless Protocol stack
		// ***********************************************************************

		// Creating WirelessAgent
		WirelessAgent wireless_agent;
		// if we are using LEACH use the new wireless agent specific to that
		// task
		if (AppType_ == 5) {
			wireless_agent = new WirelessLEACHAgent("wireless_agent");
		} else {
			// otherwise use the commoon wireless agent layer
			wireless_agent = new WirelessAgent("wireless_agent");
		}
		wireless_agent.setID("wireless_agent");
		sens.addComponent(wireless_agent);

		// connection wireless agent et appli
		app.downPort.connectTo(wireless_agent.upPort);
		wireless_agent.getPort(".toSensorApp").connectTo(
				app.getPort(".fromWirelessAgent"/*
												 * app.FROM_WIRELESS_AGENT_PORT_ID
												 */));

		LL ll = new LL();
		ll.setID("ll");
		sens.addComponent(ll);

		ARP arp = new ARP();
		arp.setID("arp");
		sens.addComponent(arp);

		FIFO queue = new FIFO();
		queue.setID("queue");
		sens.addComponent(queue);

		Mac mac;
		if ((AppType_ == SensorSimple.OH_CSMA)
				|| (AppType_ == SensorSimple.OH_TDMA)
				|| (AppType_ == SensorSimple.MH_CSMA)
				|| (AppType_ == SensorSimple.LEACH)) {
			mac = new Mac_CSMA();
			mac.setID("MacSensor");
			mac.setMacAddress(sink_id);
			mac.setNode_num_(sink_id);
			if (AppType_ == SensorSimple.LEACH) {
				((Mac_CSMA) mac).setLEACHmode(true);
			}
		} else {
			mac = new Mac_802_11();
			mac.setID("mac");
			/*
			 * NICHOLAS: Because of the fact that RTS and CTS are short frames,
			 * it reduces the overhead of collisions, since they are recognized
			 * faster than it would be recognized if the whole packet was to be
			 * transmitted (this is true if the packet is significantly bigger
			 * than the RTS), so the MAC802.11 standard allows for short packets
			 * to be transmitted without the RTS/CTS transaction, and this is
			 * controlled per station by the RTSThreshold. (therefore below any
			 * packet that is 40 bytes or less will not require the use of
			 * RTS/CTS.
			 */
			mac.setRTSThreshold(10);
			mac.disable_PSM();
		}
		sens.addComponent(mac);

		if (AppType_ == SensorSimple.LEACH) {
			// must connect Application layer and MAC layer together
			// connect mac/.sensorApp@ -and app/.macSensor@
			((LEACHApp) app).macSensorPort.connect(((Mac_CSMA) mac).AppPort);

		}

		WirelessPhy wphy = new WirelessPhy();
		wphy = new WirelessPhy();
		// wphy.setRxThresh( 0.0);
		// wphy.setCSThresh( 0.0);
		wphy.setID("wphy");
		if ((AppType_ == SensorSimple.OH_CSMA)
				|| (AppType_ == SensorSimple.OH_TDMA)
				|| (AppType_ == SensorSimple.MH_CSMA)
				|| (AppType_ == SensorSimple.LEACH)) {
			wphy.setMIT_uAMPS(true);
			wphy.MacSensorPort.connect(((Mac_CSMA) mac).radioModePort);

			if ((AppType_ == SensorSimple.OH_CSMA)
					|| (AppType_ == SensorSimple.OH_TDMA)) {
				wphy.setOneHopMode(true);
			} else if (AppType_ == SensorSimple.OH_TDMA) {
				wphy.setOneHopModeTDMA(true);
			} else if (AppType_ == SensorSimple.MH_CSMA) {
				wphy.setMultiHopMode(true);
			} else if (AppType_ == SensorSimple.LEACH) {
				wphy.setLEACHMode(true);
			}

		}
		sens.addComponent(wphy);

		FreeSpaceModel propagation = new FreeSpaceModel();
		propagation.setID("propagation");
		sens.addComponent(propagation);

		PktDispatcher pktdispatcher = new PktDispatcher("pktdispatcher");
		sens.addComponent(pktdispatcher);
		RT rt = new RT("rt");
		Identity id = new Identity("id");
		sens.addComponent(rt);
		sens.addComponent(id);
		// attention tentative
		pktdispatcher.bind(rt);
		pktdispatcher.bind(id);

		/**
		 * Nicholas: Add a route entry directly back to the sink node. create
		 * route configuration request for testing this is to define the
		 * interfaces. So in this case each sensor only has 1 interface (hence
		 * array size 1) and its eth0. another example is (which has 3
		 * interfaces 0, 2, and 4: set ifs [java::new drcl.data.BitSet
		 * [java::new {int[]} 3 {0 2 4}]]
		 */
		int[] num_interfaces = { 0 };

		BitSet ifs = new BitSet(num_interfaces);
		RTEntry base_entry = new RTEntry(ifs);
		RTKey key = new RTKey(nid, 0, -1);
		rt.add(key, base_entry);

		// Nicholas: Connecting application layer and routing table
		// connect app/.setRoute@ -to rt/.service_rt@
		app.setRoutePort.connect(rt.getPort(".service_rt"));

		// Nicholas: connecting application and wirelessPhy layer together
		app.wirelessPhyPort.connect(wphy.appPort);

		CPUAvr cpu = new CPUAvr();
		sens.addComponent(cpu);

		app.cpuPort.connect(cpu.reportCPUModePort);
		cpu.batteryPort.connect(wphy.cpuEnergyPort);

		// lien power saving:
		mac.getPort(".energy").connect(wphy.getPort(".energy"));
		wphy.getPort(".mobility"/* wphy.MOBILITY_PORT_ID */).connect(
				mobility.getPort(".query"));
		wphy.getPort(".propagation"/* wphy.PROPAGATION_PORT_ID */).connect(
				propagation.getPort(".query"));

		mac.downPort.connect(wphy.upPort);
		mac.upPort.connect(queue.getPort("output"/* queue.OUTPUT_PORT_ID */));

		ll.getPort(".mac").connect(mac.getPort(".linklayer"));
		ll.downPort.connect(queue.getPort("up"));
		ll.getPort(".arp").connect(arp.getPort(".arp"));

		pktdispatcher.addPort("down", "0").connect(ll.upPort);

		arp.setAddresses(nid, nid);
		ll.setAddresses(nid, nid);
		mac.setMacAddress(nid);
		wphy.setNid(nid);
		mobility.setNid(nid);
		id.setDefaultID(nid);

		queue.setMode("packet");
		queue.setCapacity(40);

		// disable arp
		arp.setBypassARP(true);

		// connexion externes au composant
		mobility.getPort(".report").connect(env.tracker.getPort(".node"));
		wphy.downPort.connectTo(env.channel.getPort(".node"));
		env.channel.attachPort(nid, wphy.getPort(".channel" /*
															 * wphy.CHANNEL_PORT_ID
															 */));
		mac.disable_MAC_TRACE_ALL();

		wireless_agent.downPort.connect(pktdispatcher.addPort("up", "1111"));

		// ajout au composant principal :
		env.root.addComponent(sens);
		setTopo(env);

		// bonus rajouté à la fin :
		mobility.getPort(".report_sensor"/* mobility.REPORT_SENSOR_PORT_ID */)
				.connect(env.nodetracker.getPort(".node"/*
														 * env.nodetracker.
														 * NODE_PORT_ID
														 */));
		phy.getPort(".mobility"/* phy.MOBILITY_PORT_ID */).connect(
				mobility.getPort(".query"));
	}
}