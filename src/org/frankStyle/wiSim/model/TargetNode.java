package org.frankStyle.wiSim.model;

import drcl.comp.Component;
import drcl.inet.sensorsim.SensorChannel;
import drcl.inet.sensorsim.SensorMobilityModel;
import drcl.inet.sensorsim.SensorNodePositionTracker;
import drcl.inet.sensorsim.SensorPhy;
import drcl.inet.sensorsim.TargetAgent;

/**
 * @author Arman
 * 
 */
public class TargetNode {

	public Component targ;

	double xmin = 100.0;
	double xmax = 600.0;
	double ymin = 100.0;
	double ymax = 500.0;
	double dx = 60.0;
	double dy = 60.0;

	protected SensorMobilityModel mobility;

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

	public TargetNode(int nid, Environment env) {

		System.out.println("Creation TargetNode" + nid);

		targ = new Component("targ" + nid);
		TargetAgent agent = new TargetAgent();
		agent.setID("agent");
		targ.addComponent(agent);
		agent.setBcastRate(20.0);
		agent.setSampleRate(1.0);

		SensorPhy phy = new SensorPhy();
		phy.setID("phy");
		targ.addComponent(phy);
		phy.setNid(nid);
		phy.setRadius(250.0);
		phy.setRxThresh(0.0);

		mobility = new SensorMobilityModel();
		mobility.setID("mobility");
		targ.addComponent(mobility);

		// connection agent -> sensorphy
		agent.downPort.connectTo(phy.upPort);
		// connection external : sensorphy -> sensorchannel
		phy.downPort.connectTo(env.chan.getPort(SensorChannel.NODE_PORT_ID));
		phy.getPort(SensorPhy.PROPAGATION_PORT_ID).connect(
				env.seismic_prop.getPort(".query"));

		mobility.setNid(nid);

		env.root.addComponent(targ);
		setTopo(env);

		mobility.getPort(SensorMobilityModel.REPORT_SENSOR_PORT_ID)
				.connect(
						env.nodetracker
								.getPort(SensorNodePositionTracker.NODE_PORT_ID));
		phy.getPort(SensorPhy.MOBILITY_PORT_ID).connect(
				mobility.getPort(".query"));
	}
}
