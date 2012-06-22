package org.frankStyle.wiSim.controller;

import java.awt.Point;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

import org.frankStyle.wiSim.model.ComponentRoot;
import org.frankStyle.wiSim.model.ComponentSensor;
import org.frankStyle.wiSim.model.ComponentSinkNode;
import org.frankStyle.wiSim.model.ComponentTargetNode;
import org.frankStyle.wiSim.model.Environment;
import org.frankStyle.wiSim.model.SensorSimple;
import org.frankStyle.wiSim.model.SinkNode;
import org.frankStyle.wiSim.model.TargetNode;

import drcl.comp.Component;
import drcl.comp.tool.Plotter;
import drcl.inet.mac.Mac_802_11;
import drcl.inet.mac.MobilityModel;
import drcl.inet.mac.WirelessPhy;
import drcl.inet.sensorsim.AliveSensors;
import drcl.inet.sensorsim.OneHop.OneHopApp;
import drcl.inet.sensorsim.OneHop.SinkAppOH;

/**
 * @author Arman
 * 
 */
public class SimulationController implements Runnable {

	static Vector<SensorSimple> sensors = new Vector<SensorSimple>();
	static Vector<TargetNode> targets = new Vector<TargetNode>();
	static Vector<Plotter> plots = new Vector<Plotter>();
	static Environment env;

	// * Copied Exactly from SensorSim
	List<? extends Object> neighbor_list = new Vector<Object>();
	Vector<? extends Object> del_list = new Vector<Object>();
	static boolean EXIT = true;
	static double simulationTime = 0.0;
	drcl.comp.ACARuntime sim;
	long starttime;
	// static int total_nodes;
	// static int total_target_nodes;
	static int total_sensor_nodes;// = total_nodes - total_target_nodes;
	final static int sink_id = 0;

	static Random generator = new Random();
	static SinkNode sink0;
	static int simChoice = 0;

	static Plotter sinkPlot1_ = new Plotter("sinkPlot1_");
	static Plotter sinkPlot2_ = new Plotter("sinkPlot2_");
	static Plotter plot3 = new Plotter("plot3");
	static Plotter plot4 = new Plotter("plot4");
	static AliveSensors liveSensors = new AliveSensors();

	// The various Routing Combinations Possible
	public static final int OH_80211 = 0;
	public static final int OH_CSMA = 1;
	public static final int OH_TDMA = 2;
	public static final int MH_CSMA = 3;
	public static final int MH_80211 = 4;
	public static final int LEACH = 5;

	// */

	public SimulationController(ComponentRoot nodeRoot) {

		SimulationController.env = new Environment(new Component(
				nodeRoot.getName()), nodeRoot.getChildCount(),
				nodeRoot.targetCount());
		System.out.println("Building Env: total child:"
				+ nodeRoot.getChildCount() + " targets: "
				+ nodeRoot.targetCount());
		SimulationController.env.setTopo(0.0, 30.0, 0.0, 100.0, 100.0, 100.0);

		ComponentSinkNode compSink = new ComponentSinkNode("0",
				"wsnGUI.v2.TargetNode", new Point(0, 0));
		nodeRoot.setSinkNode(compSink);
		int sink_id = Integer.valueOf(compSink.getName()).intValue();
		System.out.println("Creating sink:  nid:" + sink_id);
		SimulationController.sink0 = new SinkNode(sink_id,
				SimulationController.env, 0);
		((MobilityModel) SimulationController.sink0.sink
				.getComponent("mobility")).setPosition(0.0, 0.0, 0.0, 0.0);

		ListIterator iter = nodeRoot.sensorsIterator();
		int nid = 1;
		while (iter.hasNext()) {
			ComponentSensor compSensor = (ComponentSensor) iter.next();

			// SensorSimple sens= new
			// SensorSimple(Integer.valueOf(compSensor.getName()).intValue() ,
			// sink_id, env, 0 ,
			// compSensor.getLocation().getX(),compSensor.getLocation().getY(),0.0);
			double posX = compSensor.getLocation().getX() / 30;
			double posY = compSensor.getLocation().getY() / 7;
			System.out.println("Creating SensorSimple:   " + "id:" + nid
					+ "  loc:  x:" + posX + "\t y:" + posY);
			SensorSimple sens = new SensorSimple(nid, sink_id,
					SimulationController.env, 0, posX, posY, 0.0);
			((MobilityModel) sens.sens.getComponent("mobility")).setPosition(
					0.0, posX, posY, 0.0);
			nid++;
			SimulationController.sensors.add(sens);
		}
		SimulationController.total_sensor_nodes = SimulationController.sensors
				.size() + 1;

		iter = nodeRoot.targetsIterator();
		while (iter.hasNext()) {
			ComponentTargetNode compTarget = (ComponentTargetNode) iter.next();
			// TargetNode targ= new
			// TargetNode(Integer.parseInt(compTarget.getName()), env);
			double posX = compTarget.getLocation().getX() / 30;
			double posY = compTarget.getLocation().getY() / 7;

			System.out.println("Creating Target:    " + " id:" + nid
					+ "loc:   x:" + posX + "\t y:" + posY);
			TargetNode target = new TargetNode(nid, SimulationController.env);
			((MobilityModel) target.targ.getComponent("mobility")).setPosition(
					0.0, posX, posY, 0.0);
			nid++;
			SimulationController.targets.add(target);
		}

		SimulationController.env.root
				.addComponent(SimulationController.sinkPlot1_);
		SimulationController.env.root
				.addComponent(SimulationController.sinkPlot2_);
		SimulationController.env.root.addComponent(SimulationController.plot3);
		SimulationController.env.root.addComponent(SimulationController.plot4);
		SimulationController.sink0.app.createSnrPorts(nodeRoot.getChildCount(),
				nodeRoot.targetCount());
		Plotter plot = new Plotter("plot");
		SimulationController.env.root.addComponent(plot);

		((SinkAppOH) SimulationController.sink0.sink.getComponent("SinkAppOH"))
				.getPort(".PacketsReceivedPlot").connectTo(
						SimulationController.sinkPlot1_.addPort("0", "0"));
		((SinkAppOH) SimulationController.sink0.sink.getComponent("SinkAppOH"))
				.getPort(".latencyPlot").connectTo(
						SimulationController.sinkPlot2_.addPort("0", "0"));

		iter = SimulationController.sensors.listIterator(0);
		while (iter.hasNext()) {
			((OneHopApp) ((SensorSimple) iter.next()).sens
					.getComponent("OneHopApp")).getPort(".plotter").connectTo(
					SimulationController.plot3.addPort("0",
							String.valueOf(iter.nextIndex())));
		}

		for (int i = 0; i < SimulationController.targets.size(); i++)
			((SinkAppOH) SimulationController.sink0.sink
					.getComponent("SinkAppOH")).getPort(".snr" + i).connectTo(
					plot.addPort(i + "", i + ""));

		SimulationController.liveSensors.setID("liveSensors");
		SimulationController.env.root
				.addComponent(SimulationController.liveSensors);
		SimulationController.liveSensors.plotterPort
				.connectTo(SimulationController.plot4.addPort("0", "0"));

		long time_ = System.currentTimeMillis();
		SimulationController.simulationTime = 10;
		System.out.println("Simulation begins... at " + time_
				+ "\n Sim Duration: " + SimulationController.simulationTime);

		drcl.comp.ACARuntime sim_ = null;
		sim_ = new drcl.sim.event.SESimulator();

		sim_.takeover(SimulationController.env.root);
		sim_.stop();
		sim_.addRunnable(0.0000001, new SimulationController(sim_, time_));

		sim_.stopAt(SimulationController.simulationTime);

		Object[] object = new Object[1];
		object[0] = SimulationController.env.root;

		drcl.comp.Util.operate(object, "start");
		sim_.resume();

	}

	public SimulationController(drcl.comp.ACARuntime sim_, long starttime_) {
		sim = sim_;
		starttime = starttime_;
	}

	@Override
	public void run() {
		double now_ = sim.getTime();

		/*
		 * If the simulation is not done yet then check for possible events that
		 * may need to run
		 */
		if (now_ < SimulationController.simulationTime) {

			// print the locations of all sensors to the screen
			if (now_ < 0.001) {
				sensorLocPrintOut();
			}

			// Check the logTextArea of all sensors constantly
			if (now_ > 15.0) {
				wsnLoop();
				sim.addRunnable(0.25, this);
			} else { // o.w. just call back this runnable component again in the
						// future
				sim.addRunnable(0.5, this);
			}
		}
	}

	// ***********************************************************
	// Helper Functions
	// ***********************************************************

	/**
	 * Go throught all sensors and prints their (x,y,z) coordinates
	 */
	void sensorLocPrintOut() {
		ListIterator iter = SimulationController.sensors.listIterator(0);
		while (iter.hasNext())
			((OneHopApp) ((SensorSimple) iter.next()).sens
					.getComponent("OneHopApp")).printNodeLoc();

	}

	/**
	 * This method is called upon periodically and checks to see if all sensors
	 * are dead. If so prints off concluding results and provides insight on the
	 * routings efficiency.
	 */
	void wsnLoop() {

		int totalINpackets = 0;
		double avgLatency = 0.0;
		int BSmacCollisions = 0;
		int packets = -1;
		int totalPackets = 0;
		int deadNodes = 0;
		int app_dropped = 0;
		int wphy_dropped = 0;
		int totalDroppedPackets = 0;

		// determine if all the sensors are dead:
		ListIterator iter = SimulationController.sensors.listIterator(0);
		while (iter.hasNext())
			if (((OneHopApp) ((SensorSimple) iter.next()).sens
					.getComponent("OneHopApp")).isSensorDead())
				deadNodes++;

		// ************************************************
		// Update Live Node Graph
		// ************************************************
		SimulationController.liveSensors
				.setLiveNodes(SimulationController.total_sensor_nodes
						- deadNodes);
		SimulationController.liveSensors.updateGraph();

		// if all sensors are dead then show results and halt the simulator
		if ((deadNodes == (SimulationController.total_sensor_nodes - 1))/*
																		 * ||
																		 * (sim
																		 * .
																		 * getTime
																		 * () >
																		 * 11500.0
																		 * )
																		 */) {

			System.out.println("----------------------------");
			System.out.println("All nodes dead at " + sim.getTime());
			System.out.println("Simulations Terminated.");
			System.out.println("Results: ");

			// *****************************************************************
			// 1. Determine how many packets the BS Received and
			// 2. Determine the total # of packets sent by all nodes.
			// 3. Determine the total # of dropped packets at the wirelessPhy
			// layer
			// 4. Determine the total # of dropped packets at the application
			// layer
			// 5. Determine the total # of dropped packets at the MAC layer
			// (only when using MacSensor.java)
			// 6. Determine the avg. Latency
			// *****************************************************************

			if ((SimulationController.simChoice == 0)) {
				totalINpackets = ((SinkAppOH) SimulationController.sink0.sink
						.getComponent("SinkAppOH")).getTotalINPackets();
				avgLatency = ((SinkAppOH) SimulationController.sink0.sink
						.getComponent("SinkAppOH")).getAvgLatency();

				iter = SimulationController.sensors.listIterator(0);
				while (iter.hasNext()) {
					packets = ((OneHopApp) ((SensorSimple) iter.next()).sens
							.getComponent("OneHopApp")).geteID();

					System.out.println("Sensor" + iter.nextIndex()
							+ " sent out " + packets);
					totalPackets = totalPackets + packets;
				}

				app_dropped = ((OneHopApp) SimulationController.sensors
						.get(0).sens.getComponent("OneHopApp"))
						.getDropped_packets();
				wphy_dropped = ((WirelessPhy) SimulationController.sensors
						.get(0).sens.getComponent("wphy"))
						.getDropped_packets();

			} else {
				totalINpackets = -1;
			}

			// *****************************************************************
			// Determine collisions at Base station (MAC layer).
			// *****************************************************************
			if ((SimulationController.simChoice == 0)) {
				// get from MAC80211 Component
				BSmacCollisions = ((Mac_802_11) SimulationController.sink0.sink
						.getComponent("mac")).getCollision();
			}
			System.out.println("Avg. Overall  Latency: " + avgLatency);
			System.out.println("Base Station Received: " + totalINpackets);
			System.out
					.println("Collisions at Base Station: " + BSmacCollisions);
			System.out.println("Total packets dropped at Application layer: "
					+ app_dropped);
			System.out.println("Total packets dropped at physical layer: "
					+ wphy_dropped);

			if (SimulationController.simChoice == 0)
				totalDroppedPackets = wphy_dropped + app_dropped;

			System.out.println("Total Packets sent from all nodes: "
					+ totalPackets);

			System.out.println("Number of Dropped Packets: "
					+ totalDroppedPackets);

			if (SimulationController.simChoice != 5) {
				System.out
						.println("Success Rate (Reliability): "
								+ (((totalINpackets * 1.0) / (totalPackets * 1.0)) * 100));
			}

			// *****************************************************************
			// Stop the simulator
			// *****************************************************************
			sim.stop();
		}
	}

	public void start() {
		SimulationController.simChoice = 0;

	}

	public SinkNode getSink() {
		return SimulationController.sink0;
	}

	public void setSink(SinkNode sink) {
		SimulationController.sink0 = sink;
	}

	public Vector getSensors() {
		return SimulationController.sensors;
	}

	public Vector getTargets() {
		return SimulationController.targets;
	}

	public Vector getPlots() {
		return SimulationController.plots;
	}

	public void addSensor(SensorSimple sensor) {
		SimulationController.sensors.add(sensor);
	}

	public void addTarget(TargetNode target) {
		SimulationController.targets.add(target);
	}

	public void addPlot(Plotter plot) {
		SimulationController.plots.add(plot);
	}

	public double getSimulationTime() {
		return SimulationController.simulationTime;
	}

	public static void setSimulationTime(double time) {
		SimulationController.simulationTime = time;
	}
}
