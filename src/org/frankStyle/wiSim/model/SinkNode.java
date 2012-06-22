package org.frankStyle.wiSim.model;

import drcl.comp.Component;
import drcl.inet.core.Identity;
import drcl.inet.core.PktDispatcher;
import drcl.inet.core.RT;
import drcl.inet.core.queue.FIFO;
import drcl.inet.mac.ARP;
import drcl.inet.mac.FreeSpaceModel;
import drcl.inet.mac.LL;
import drcl.inet.mac.Mac;
import drcl.inet.mac.Mac_802_11;
import drcl.inet.mac.MobilityModel;
import drcl.inet.mac.WirelessPhy;
import drcl.inet.mac.CSMA.Mac_CSMA;
import drcl.inet.sensorsim.SensorApp;
import drcl.inet.sensorsim.WirelessAgent;
import drcl.inet.sensorsim.LEACH.SinkAppLEACH;
import drcl.inet.sensorsim.LEACH.WirelessLEACHAgent;
import drcl.inet.sensorsim.MultiHop.SinkAppMH;
import drcl.inet.sensorsim.OneHop.SinkAppOH;
import drcl.inet.sensorsim.OneHopTDMA.SinkAppTDMA;

/**
 * @author Arman
 * 
 */
public class SinkNode {

	protected double threshold = 1000.0;

	protected double xmin = 0.0;
	protected double xmax = 30.0;
	protected double ymin = 0.0;
	protected double ymax = 100.0;
	protected double dx = 100.0;
	protected double dy = 100.0;

	public Component sink;
	protected MobilityModel mobility;
	public SensorApp app;

	protected LL ll;
	protected ARP arp;
	protected WirelessAgent wireless_agent;
	protected FIFO queue;
	protected Mac mac;
	protected WirelessPhy wphy;
	protected FreeSpaceModel propagation;
	protected PktDispatcher pktdispatcher;
	protected RT rt;
	protected Identity id;

	// The various Routing Combinations Possible
	public static final int OH_80211 = 0;
	public static final int OH_CSMA = 1;
	public static final int OH_TDMA = 2;
	public static final int MH_CSMA = 3;
	public static final int MH_80211 = 4;
	public static final int LEACH = 5;

	/**
	 * Topology Specifications for the grid system
	 * 
	 * @param xmi
	 * @param xma
	 * @param ymi
	 * @param yma
	 * @param dx_
	 * @param dy_
	 *            TODO: 3D space extension
	 */
	void setTopo(double xmi, double xma, double ymi, double yma, double dx_,
			double dy_) {
		xmin = xmi;
		xmax = xma;
		ymin = ymi;
		ymax = yma;
		dx = dx_;
		dy = dy_;

		mobility.setTopologyParameters(xmax, ymax, 0.0, xmin, ymin, 0.0, dx,
				dy, 0.0);
	}

	void setTopo(Environment env) {
		mobility.setTopologyParameters(env.xmax, env.ymax, 0.0, env.xmin,
				env.ymin, 0.0, env.dx, env.dy, 0.0);
	}

	/**
	 * Create a sink node.
	 * 
	 * @param sink_id
	 * @param env
	 * @param SinkType_
	 */
	public SinkNode(int sink_id, Environment env, int SinkType_) {
		System.out.println("Creating sink " + sink_id);
		sink = new Component("sink" + sink_id);

		// application layer depending on which type of routing you are using.
		switch (SinkType_) {
		case 0:
			app = new SinkAppOH();
			app.setID("SinkAppOH");
			app.setIs_uAMPS(false);
			break;
		case 1:
			app = new SinkAppOH();
			app.setID("SinkAppOH");
			app.setIs_uAMPS(true);
			break;
		case 2:
			app = new SinkAppTDMA();
			app.setID("SinkAppTDMA");
			app.setIs_uAMPS(true);
			app.setNn_(env.getNn_());
			break;
		case 3:
			app = new SinkAppMH();
			app.setID("SinkAppMH");
			app.setIs_uAMPS(true);
			break;
		case 4:
			app = new SinkAppMH();
			app.setID("SinkAppMH");
			app.setIs_uAMPS(false);
			break;
		case 5:
			app = new SinkAppLEACH();
			app.setID("SinkAppLEACH");
			app.setIs_uAMPS(true);
			break;
		default:
			System.out.println("Error: Unknown Sink Type");
			System.exit(-1);
		}

		sink.addComponent(app);
		app.setNid(sink_id);
		app.setSinkNid(sink_id);
		app.setCoherentThreshold(threshold);

		// Wifi agent

		// if we are using LEACH use the new wireless agent specific to that
		// task
		if (SinkType_ == 5) {
			wireless_agent = new WirelessLEACHAgent("wireless_agent");
		} else {
			// otherwise use the commoon wireless agent layer
			wireless_agent = new WirelessAgent("wireless_agent");
		}
		sink.addComponent(wireless_agent);

		// connect the sensor application to the wireless agent
		// so that sensors can send through the wireless network protocol stack
		app.downPort.connectTo(wireless_agent.upPort);

		// connect the wireless agent to the sensor application
		// so that sensors can receive thru the wireless network protocol stack
		wireless_agent.getPort(".toSensorApp").connectTo(
				app.getPort(".fromWirelessAgent"));

		ll = new LL();
		ll.setID("ll");
		sink.addComponent(ll);
		arp = new ARP();
		arp.setID("arp");
		sink.addComponent(arp);
		queue = new FIFO();
		queue.setID("queue");
		sink.addComponent(queue);

		if ((SinkType_ == SinkNode.OH_CSMA) || (SinkType_ == SinkNode.OH_TDMA)
				|| (SinkType_ == SinkNode.MH_CSMA)
				|| (SinkType_ == SinkNode.LEACH)) {
			mac = new Mac_CSMA();
			mac.setID("MacSensor");
			mac.setMacAddress(sink_id);
			mac.setNode_num_(sink_id);
		} else {
			mac = new Mac_802_11();
			mac.setID("mac");
		}
		sink.addComponent(mac);

		wphy = new WirelessPhy();
		wphy.setID("wphy");
		if ((SinkType_ == SinkNode.OH_CSMA) || (SinkType_ == SinkNode.OH_TDMA)
				|| (SinkType_ == SinkNode.MH_CSMA)
				|| (SinkType_ == SinkNode.LEACH)) {
			wphy.setMIT_uAMPS(true);
			wphy.getPort(".channelCheck")
					.connect(mac.getPort(".wphyRadioMode"));
			app.getPort(".energy").connect(wphy.getPort(".appEnergy"));

			if (SinkType_ == SinkNode.MH_CSMA) {
				wphy.setMultiHopMode(true); // turn on MH mode settings
			} else if (SinkType_ == SinkNode.LEACH) {
				wphy.setLEACHMode(true); // turn on LEACH mode settings
			}

		}

		sink.addComponent(wphy);
		wphy.setRxThresh(0.0);
		wphy.setCSThresh(0.0);

		propagation = new FreeSpaceModel();
		propagation.setID("propagation");
		sink.addComponent(propagation);

		mobility = new MobilityModel();
		mobility.setID("mobility");
		sink.addComponent(mobility);

		pktdispatcher = new PktDispatcher("pktdispatcher");
		sink.addComponent(pktdispatcher);
		rt = new RT("rt");
		id = new Identity("id");
		sink.addComponent(rt);
		sink.addComponent(id);
		// TODO: Error Possibility 
		pktdispatcher.bind(rt);
		pktdispatcher.bind(id);

		app.setRoutePort.connect(rt.getPort(".service_rt"));

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
		int nid = sink_id;
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
		mac.setRTSThreshold(10);

		mobility.getPort(".report").connect(env.tracker.getPort(".node"));
		wphy.downPort.connectTo(env.channel.getPort(".node"));
		env.channel.attachPort(sink_id, wphy.getPort(".channel"/*
																 * wphy.
																 * CHANNEL_PORT_ID
																 */));

		mac.disable_MAC_TRACE_ALL();
		mac.disable_PSM();

		wireless_agent.downPort.connect(pktdispatcher.addPort("up", "1111"));

		env.root.addComponent(sink);
		setTopo(env);
	}

}
