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

/**
 * @author Arman
 * 
 */
public class SensorNode extends drcl.comp.Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3118991577894627291L;

	public Component sens;

	public String sensorName;
	public int nid;

	private SensorApp senApp;
	private CPUAvr CpuModel;
	private LL ll;
	private ARP arp;
	// RT senRT;
	private SensorAgent senAgent;
	private SensorPhy senPhy;
	private WirelessAgent wirelessAgent;
	private WirelessPhy wirelessPhy;
	private drcl.inet.mac.Mac_802_11 mac;
	private SensorMobilityModel senMobilityModel;

	private double threshold = 1000.0;
	private double xmin = 0.0;
	private double xmax = 30.0;
	private double ymin = 0.0;
	private double ymax = 100.0;
	private double dx = 30.0;
	private double dy = 100.0;

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

	public void cunstructNode(int nid_, int sink_id, Environment env,
			int AppType_, double xCoord_, double yCoord_, double zCoord_) {
		System.out.println("Creating Sensor " + nid); // Creation du senseur
														// "+j);
		sens = new Component("s" + nid);

		if (senApp == null) {
			senApp = new OneHopApp();
			senApp.setID("OneHopApp");
			senApp.setIs_uAMPS(false);
		}

		sens.addComponent(senApp);
		senApp.setNid(nid_);
		senApp.setSinkNid(sink_id); // the sinks ID
		senApp.setSinkLocation(0.0, 0.0, 0.0); // the sinks location
		senApp.setCoherentThreshold(1000.0); // FIXME: threshold at which the
												// sensor could no longer
												// comprehend signals

		if ((AppType_ == SensorNode.LEACH) || (AppType_ == SensorNode.MH_CSMA)) {
			senApp.setNn_(env.getNn_());
		}

		/*
		 * If running multi-hop mode then we must connect the application layer
		 * and nodetracker components together
		 */
		if ((AppType_ == 3) || (AppType_ == 4)) {
			// must connect application and nodetracker for Multi-Hop modes
			// so that neighbors can be located.
			((MultiHopApp) senApp).SensorNodePositionPort
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
				.connectTo(senApp.getPort(".fromSensorAgent"/*
															 * app.
															 * FROM_SENSOR_AGENT_PORT_ID
															 */));

		env.chan.attachPort(nid,
				phy.getPort(".channel"/* phy.CHANNEL_PORT_ID */));

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

		senApp.downPort.connectTo(wireless_agent.upPort);
		wireless_agent.getPort(".toSensorApp").connectTo(
				senApp.getPort(".fromWirelessAgent"/*
													 * app.
													 * FROM_WIRELESS_AGENT_PORT_ID
													 */));

		LL ll = new LL();
		ll.setID("ll");
		sens.addComponent(ll);

		ARP arp = new ARP();
		arp.setID("arp");
		this.addComponent(arp);

		FIFO queue = new FIFO();
		queue.setID("queue");
		sens.addComponent(queue);

		Mac mac;
		if ((AppType_ == SensorNode.OH_CSMA)
				|| (AppType_ == SensorNode.OH_TDMA)
				|| (AppType_ == SensorNode.MH_CSMA)
				|| (AppType_ == SensorNode.LEACH)) {
			mac = new Mac_CSMA();
			mac.setID("MacSensor");
			mac.setMacAddress(sink_id);
			mac.setNode_num_(sink_id);
			if (AppType_ == SensorNode.LEACH) {
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

		if (AppType_ == SensorNode.LEACH) {
			// must connect Application layer and MAC layer together
			// connect mac/.sensorApp@ -and app/.macSensor@
			((LEACHApp) senApp).macSensorPort.connect(((Mac_CSMA) mac).AppPort);

		}

		WirelessPhy wphy = new WirelessPhy();
		wphy = new WirelessPhy();
		// wphy.setRxThresh( 0.0);
		// wphy.setCSThresh( 0.0);
		wphy.setID("wphy");
		if ((AppType_ == SensorNode.OH_CSMA)
				|| (AppType_ == SensorNode.OH_TDMA)
				|| (AppType_ == SensorNode.MH_CSMA)
				|| (AppType_ == SensorNode.LEACH)) {
			wphy.setMIT_uAMPS(true);
			wphy.MacSensorPort.connect(((Mac_CSMA) mac).radioModePort);

			if ((AppType_ == SensorNode.OH_CSMA)
					|| (AppType_ == SensorNode.OH_TDMA)) {
				wphy.setOneHopMode(true);
			} else if (AppType_ == SensorNode.OH_TDMA) {
				wphy.setOneHopModeTDMA(true);
			} else if (AppType_ == SensorNode.MH_CSMA) {
				wphy.setMultiHopMode(true);
			} else if (AppType_ == SensorNode.LEACH) {
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
		// Error Possibility
		pktdispatcher.bind(rt);
		pktdispatcher.bind(id);

		int[] num_interfaces = { 0 };

		BitSet ifs = new BitSet(num_interfaces);
		RTEntry base_entry = new RTEntry(ifs);
		RTKey key = new RTKey(nid, 0, -1);
		rt.add(key, base_entry);

		senApp.setRoutePort.connect(rt.getPort(".service_rt"));

		senApp.wirelessPhyPort.connect(wphy.appPort);

		CPUAvr cpu = new CPUAvr();
		sens.addComponent(cpu);

		senApp.cpuPort.connect(cpu.reportCPUModePort);
		cpu.batteryPort.connect(wphy.cpuEnergyPort);

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

		mobility.getPort(".report").connect(env.tracker.getPort(".node"));
		wphy.downPort.connectTo(env.channel.getPort(".node"));
		env.channel.attachPort(nid, wphy.getPort(".channel" /*
															 * wphy.CHANNEL_PORT_ID
															 */));
		mac.disable_MAC_TRACE_ALL();

		wireless_agent.downPort.connect(pktdispatcher.addPort("up", "1111"));

		env.root.addComponent(sens);
		setTopo(env);

		mobility.getPort(".report_sensor"/* mobility.REPORT_SENSOR_PORT_ID */)
				.connect(env.nodetracker.getPort(".node"/*
														 * env.nodetracker.
														 * NODE_PORT_ID
														 */));
		phy.getPort(".mobility"/* phy.MOBILITY_PORT_ID */).connect(
				mobility.getPort(".query"));

	}

	/**
	 * Create a sensor
	 * 
	 * @param nid
	 * @param sink_id
	 * @param env
	 */
	public SensorNode(int nid_, int sink_id, Environment env, int AppType_,
			double xCoord_, double yCoord_, double zCoord_) {
		super("sensor" + nid_);
		cunstructNode(nid_, sink_id, env, AppType_, xCoord_, yCoord_, zCoord_);

	}

	public Component getSens() {
		return sens;
	}

	public void setSens(Component sens) {
		this.sens = sens;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public int getNid() {
		return nid;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public SensorApp getSenApp() {
		return senApp;
	}

	public void setSenApp(SensorApp senApp) {
		this.senApp = senApp;
	}

	public CPUAvr getCpuModel() {
		return CpuModel;
	}

	public void setCpuModel(CPUAvr cpuModel) {
		CpuModel = cpuModel;
	}

	public LL getLl() {
		return ll;
	}

	public void setLl(LL ll) {
		this.ll = ll;
	}

	public ARP getArp() {
		return arp;
	}

	public void setArp(ARP arp) {
		this.arp = arp;
	}

	public SensorAgent getSenAgent() {
		return senAgent;
	}

	public void setSenAgent(SensorAgent senAgent) {
		this.senAgent = senAgent;
	}

	public SensorPhy getSenPhy() {
		return senPhy;
	}

	public void setSenPhy(SensorPhy senPhy) {
		this.senPhy = senPhy;
	}

	public WirelessAgent getWirelessAgent() {
		return wirelessAgent;
	}

	public void setWirelessAgent(WirelessAgent wirelessAgent) {
		this.wirelessAgent = wirelessAgent;
	}

	public WirelessPhy getWirelessPhy() {
		return wirelessPhy;
	}

	public void setWirelessPhy(WirelessPhy wirelessPhy) {
		this.wirelessPhy = wirelessPhy;
	}

	public drcl.inet.mac.Mac_802_11 getMac() {
		return mac;
	}

	public void setMac(drcl.inet.mac.Mac_802_11 mac) {
		this.mac = mac;
	}

	public SensorMobilityModel getSenMobilityModel() {
		return senMobilityModel;
	}

	public void setSenMobilityModel(SensorMobilityModel senMobilityModel) {
		this.senMobilityModel = senMobilityModel;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getXmin() {
		return xmin;
	}

	public void setXmin(double xmin) {
		this.xmin = xmin;
	}

	public double getXmax() {
		return xmax;
	}

	public void setXmax(double xmax) {
		this.xmax = xmax;
	}

	public double getYmin() {
		return ymin;
	}

	public void setYmin(double ymin) {
		this.ymin = ymin;
	}

	public double getYmax() {
		return ymax;
	}

	public void setYmax(double ymax) {
		this.ymax = ymax;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public SensorMobilityModel getMobility() {
		return mobility;
	}

	public void setMobility(SensorMobilityModel mobility) {
		this.mobility = mobility;
	}
}