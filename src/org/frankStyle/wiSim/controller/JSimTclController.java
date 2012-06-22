package org.frankStyle.wiSim.controller;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ListIterator;
import java.util.StringTokenizer;

import org.frankStyle.wiSim.WSNeditor;
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
import drcl.inet.sensorsim.OneHop.OneHopApp;
import drcl.inet.sensorsim.OneHop.SinkAppOH;

/**
 * @author Arman
 * 
 */
// TODO: move related static final values here
/**
 * @author Arman
 * 
 */
public class JSimTclController {
	final static String newline = System.getProperty("line.separator");
	public final static double defaultEnvWidth = 700;
	public final static double defaultEnvHeight = 500;

	static boolean m_bJavaSimValid = false;

	static boolean isJavaSimValid() {
		if (drcl.comp.Component.Root.getAllComponents().length == 0)
			JSimTclController.m_bJavaSimValid = false;
		return JSimTclController.m_bJavaSimValid;
	}

	public static void setJavaSimValid(boolean bValid) {
		JSimTclController.m_bJavaSimValid = bValid;
	}

	public static drcl.DrclObj getNodeFromPathName(String strPathName) {
		strPathName = strPathName.substring(1);
		StringTokenizer st = new StringTokenizer(strPathName, "/");
		drcl.comp.Component compParent = drcl.comp.Component.Root;
		drcl.comp.Component compChild = null;
		while (st.hasMoreTokens()) {
			String strCompName = st.nextToken();
			compChild = compParent.getComponent(strCompName);
			if (compChild == null) {
				String strGroup = JSimTclController
						.getGroupfromName(strCompName);
				String strID = JSimTclController.getIDfromName(strCompName);
				drcl.comp.Port port = compParent.getPort(strGroup, strID);
				if (port == null)
					return null;
				return port;
			} else
				compParent = compChild;
		}
		return compChild;
	}

	public static final Point defaultNodeLocation = new Point(100, 100);





	public static String getTargetTcl(ComponentTargetNode tar) {

		String tcl = "set i " + tar.getId();
		tcl += "\n" + "puts \"creating target $i\"";
		tcl += "\n" + "set node$i [mkdir drcl.comp.Component n$i]";
		tcl += "\n" + "cd n$i";
		tcl += "\n" + "mkdir " + tar.getAgent() + " agent";
		tcl += "\n" + "! agent setBcastRate 1.0";
		tcl += "\n" + "! agent setSampleRate 1.0";
		tcl += "\n" + "mkdir " + tar.getPhy() + " phy";
		tcl += "\n" + "! phy setRxThresh 0.0";
		tcl += "\n" + "! phy setNid $i";
		tcl += "\n" + "! phy setRadius " + tar.getRadius();
		// TODO its not applicable here right? //esna ashari edition
		// tcl += "\n" + "! phy setTranmissionRange " + tar.getRadius();
		tcl += "\n" + "! phy setDebugEnabled 0";
		tcl += "\n" + "mkdir " + tar.getMobility() + " mobility";
		tcl += "\n" + "connect agent/down@ -to phy/up@";
		tcl += "\n" + "connect phy/down@ -to /$proj_name/chan/.node@";
		tcl += "\n"
				+ "connect phy/.propagation@ -and /$proj_name/seismic_Prop/.query@";
		tcl += "\n" + "! mobility setNid $i";
		// TODO: set
		tcl += "\n"
				+ "! mobility setTopologyParameters 30.0 100.0 0.0 30.0 100.0 0.0";

		// in the template tcl these two lines iterate after creating all
		// targets and sensors
		tcl += "\n\n#in the template tcl these two lines iterate after creating all targets and sensors";
		tcl += "\n"
				+ "connect mobility/.report_sensor@ -and /$proj_name/nodetracker/.node@";
		tcl += "\n" + "connect phy/.mobility@ -and mobility/.query@";

		tcl += "\n\n" + "! mobility setPosition 0.0 "
				+ tar.getLocation().getX() + " " + tar.getLocation().getY()
				+ " 0.0";

		tcl += "\n\n" + "cd .." + " \n####  End of target creation \n\n";

		return tcl;
	}

	public static String createSensorFeaturesArray(ComponentSensor sen) {
		// [ Name LocationX LocationY TransmissionRange SensorApp SensorPhy
		// SensorAgent CpuModel WirelessPhy MAC ARP LL SensorMobilityModel
		// WirelessPropagationMod WirelessAgent ]"
		String tclArray = " [ list ";
		tclArray += sen.getName() + " " + sen.getLocation().getX() + " "
				+ sen.getLocation().getY() + " " + sen.getRadius() + " "
				+ sen.getApp() + " " + sen.getPhy() + " " + sen.getAgent()
				+ " " + sen.getCpuModel() + " " + sen.getWiPhy() + " "
				+ sen.getMac() + " " + sen.getArp() + " " + sen.getLl() + " "
				+ sen.getSenMobilityModel() + " " + sen.getWiPropagationModel()
				+ " " + sen.getWiAgent() + " ] ";
		return tclArray;
	}

	public static String createTargetFeaturesArray(ComponentTargetNode tar) {
		// [ Name LocationX LocationY TransmissionRange SensorPhy
		// SensorMobilityModel TargetAgent ]"
		String tclArray = " [ list" + " " + tar.getName() + " "
				+ tar.getLocation().getX() + " " + tar.getLocation().getY()
				+ " " + tar.getRadius() + " " + tar.getPhy() + " "
				+ tar.getMobility() + " " + tar.getAgent() + " ] ";
		return tclArray;
	}

	// TODO: mostly done
	public static String getSensorTcl(ComponentSensor sen) {
		int id = sen.getId();
		String tcl = "\n\nputs \"creating sensor " + id + "\"";
		tcl += "\nset i " + id;
		tcl += "\n" + "set node($i) [mkdir drcl.comp.Component n" + sen.getId()
				+ "]";
		tcl += "\n" + "cd n$i";
		tcl += "\n" + "mkdir " + sen.getApp() + " app";
		tcl += "\n" + "! app setNid $i";
		tcl += "\n" + "! app setSinkNid $sink_id";
		// threshold at which the sensor could no longer comprehend signals
		tcl += "\n" + "app setCoherentThreshold 1000.0";

		// TODO: what r these? should always be there?
		tcl += "\n" + "! app setNn_ [expr $node_num - 1]";
		tcl += "\n" + "! app setNum_clusters 3";
		tcl += "\n" + "! app setTotal_rounds 5";
		// end of todo

		tcl += "\n" + "mkdir " + sen.getAgent() + " agent";
		tcl += "\n" + "! agent setDebugEnabled 0";

		tcl += "\n" + "mkdir " + sen.getPhy() + " phy";

		tcl += "\n" + "! phy setRxThresh 0.0";
		tcl += "\n" + "! phy setDebugEnabled 0";
		tcl += "\n" + "mkdir " + sen.getSenMobilityModel() + " mobility";
		tcl += "\n" + "! phy setNid $i";
		tcl += "\n" + "! phy setRadius " + sen.getRadius();
		tcl += "\n" + "connect phy/.toAgent@ -to agent/.fromPhy@";
		tcl += "\n" + "connect agent/.toSensorApp@ -to app/.fromSensorAgent@";
		tcl += "\n"
				+ "! /$proj_name/chan attachPort $i [! phy getPort .channel]";
		tcl += "\n"
				+ "connect phy/.propagation@ -and /$proj_name/seismic_Prop/.query@";
		tcl += "\n" + "! mobility setNid $i";
		tcl += "\n" + "mkdir " + sen.getWiAgent() + " wireless_agent";
		tcl += "\n" + "connect app/down@ -to wireless_agent/up@	";
		tcl += "\n"
				+ "connect wireless_agent/.toSensorApp@ -to app/.fromWirelessAgent@";
		tcl += "\n" + "mkdir " + sen.getLl() + " ll";
		tcl += "\n" + "mkdir " + sen.getArp() + " arp";
		tcl += "\n" + "mkdir drcl.inet.core.queue.FIFO queue";
		tcl += "\n" + "mkdir " + sen.getMac() + " mac";
		tcl += "\n" + "mkdir " + sen.getWiPhy() + " wphy";

		// TODO: only in Leach
		tcl += "\n" + "! mac setLEACHmode 1";
		tcl += "\n" + "! wphy setLEACHmode 1";
		// when is it needed?
		tcl += "\n" + "! wphy setMIT_uAMPS 1"; // #turn on MH mode settings
		tcl += "\n" + "connect wphy/.channelCheck@ -and mac/.wphyRadioMode@";

		// esna ashari edition
		tcl += "\n" + "! wphy setTransmissionRange " + sen.getRadius();

		tcl += "\n" + "mkdir " + sen.getWiPropagationModel() + " propagation";
		tcl += "\n"
				+ "set PD [mkdir drcl.inet.core.PktDispatcher  pktdispatcher]";
		tcl += "\n" + "set RT [mkdir drcl.inet.core.RT  rt]";
		tcl += "\n" + "set ID [mkdir drcl.inet.core.Identity   id]";

		// TODO: when is it needed?
		tcl += "\n" + "! pktdispatcher setRouteBackEnabled 1";

		tcl += "\n" + "$PD bind $RT";
		tcl += "\n" + "$PD bind $ID";

		// TODO: what r these n are they always needed
		tcl += "\n"
				+ "set ifs [java::new drcl.data.BitSet [java::new {int[]} 1 {0}]]";
		tcl += "\n" + "set base_entry [java::new drcl.inet.data.RTEntry $ifs]";
		tcl += "\n" + "set key [java::new drcl.inet.data.RTKey $i 0 -1]";
		tcl += "\n" + "set entry_ [!!! [$base_entry clone]]";
		tcl += "\n" + "! rt add $key $entry_ ";

		tcl += "\n" + "connect app/.setRoute@ -to rt/.service_rt@";
		tcl += "\n" + "connect app/.energy@ -and wphy/.appEnergy@";
		tcl += "\n" + "mkdir " + sen.getCpuModel() + " cpu";
		tcl += "\n" + "connect app/.cpu@ -and cpu/.reportCPUMode@";
		tcl += "\n" + "connect cpu/.battery@ -and wphy/.cpuEnergyPort@";
		tcl += "\n" + "connect mac/.sensorApp@ -and app/.macSensor@";

		tcl += "\n" + "connect wphy/.mobility@    -and mobility/.query@";
		tcl += "\n" + "connect wphy/.propagation@ -and propagation/.query@";
		tcl += "\n" + "connect mac/down@ -and wphy/up@";
		tcl += "\n" + "connect mac/up@   -and queue/output@";
		tcl += "\n" + "connect ll/.mac@ -and mac/.linklayer@";
		tcl += "\n" + "connect ll/down@ -and queue/up@";
		tcl += "\n" + "connect ll/.arp@ -and arp/.arp@";
		tcl += "\n" + "connect -c pktdispatcher/0@down -and ll/up@";
		tcl += "\n" + "set nid $i";
		tcl += "\n" + "! arp setAddresses  $nid $nid";
		tcl += "\n" + "! ll  setAddresses  $nid $nid";
		tcl += "\n" + "! mac setNode_num_ $nid";
		tcl += "\n" + "! mac setMacAddress $nid";
		tcl += "\n" + "! wphy setNid        $nid";
		tcl += "\n" + "! id setDefaultID   $nid";
		tcl += "\n" + "! queue setMode      \"packet\"";
		tcl += "\n" + "! queue setCapacity  40";
		tcl += "\n" + "# disable ARP";
		tcl += "\n" + "! arp setBypassARP  [ expr 2>1]";
		// tclString += "\n" + "! mac setRTSThreshold 0";
		tcl += "\n"
				+ "connect mobility/.report@ -and /$proj_name/tracker/.node@";
		tcl += "\n" + "connect wphy/down@ -to /$proj_name/channel/.node@";
		tcl += "\n"
				+ "! /$proj_name/channel attachPort $i [! wphy getPort .channel]";
		// TODO: set these:
		tcl += "\n" + "#  maxX maxY maxZ minX minY minZ dX dY dZ";
		tcl += "\n"
				+ "! mobility setTopologyParameters 600.0 500.0 0.0 100.0 100.0 0.0 60.0 60.0 0.0";
		// tclString += "\n" + "! mac  disable_MAC_TRACE_ALL";
		tcl += "\n"
				+ "connect -c  wireless_agent/down@ -and pktdispatcher/1111@up";

		// in the template tcl these two lines iterate after creating all
		// targets and sensors
		tcl += "\n\n#in the template tcl these two lines iterate after creating all targets and sensors";
		tcl += "\n"
				+ "connect mobility/.report_sensor@ -and /$proj_name/nodetracker/.node@";
		tcl += "\n" + "connect phy/.mobility@ -and mobility/.query@";

		tcl += "\n" + "! mobility setPosition 0.0 " + sen.getLocation().getX()
				+ " " + sen.getLocation().getY() + " 0.0";

		tcl += "\n" + "cd ..";

		tcl += "\n\n" + "########### Sensor Node Created\n\n\n";

		//
		// // Energy
		// tclString += "\n" + "set cc(" + id +
		// ") [java::new drcl.inet.sensorsim.BatteryCoinCell 1.00]";
		// tclString += "\n" + "mkdir $cc(" + id + ") battery";
		// tclString += "\n" + "set cpu(" + id + ") [java::new " +
		// sen.getCpuModel() + "]";
		// tclString += "\n" + "mkdir $cpu(" + id + ") cpu";
		// tclString += "\n" + "set radio(" + id +
		// ") [java::new drcl.inet.sensorsim.RadioSimple]";
		// tclString += "\n" + "mkdir $radio(" + id + ") radio";
		// tclString += "\n" + "connect $cc(" + id + ")/batteryOut@ -to $cpu(" +
		// id + ")/batteryIn@";
		// tclString += "\n" + "connect $cc(" + id + ")/battery@ -and $cpu(" +
		// id + ")/battery@";
		// tclString += "\n" + "$cpu(" + id + ") setCPUMode 2";
		// tclString += "\n" + "connect $cc(" + id + ")/batteryOut@ -to $radio("
		// + id + ")/batteryIn@";
		// tclString += "\n" + "connect $cc(" + id + ")/battery@ -and $radio(" +
		// id + ")/battery@";
		// tclString += "\n" + "$radio(" + id + ") setRadioMode 4";
		// tclString += "\n" + "$cpu(" + id +
		// ") attachApp [! app getPort .cpu]";
		// tclString += "\n" + "$radio(" + id +
		// ") attachApp [! app getPort .radio]";
		// tclString += "\n" + "! wphy setRxThresh 0.0";
		// tclString += "\n" + "! wphy setCSThresh 0.0";
		// tclString += "\n" + "mkdir drcl.inet.protocol.aodv.AODV  aodv";
		// tclString += "\n" +
		// "connect -c aodv/down@ -and pktdispatcher/103@up";
		// tclString += "\n" + "connect aodv/.service_rt@ -and rt/.service_rt@";
		// tclString += "\n" + "connect aodv/.service_id@ -and id/.service_id@";
		// tclString += "\n" +
		// "connect aodv/.ucastquery@ -and pktdispatcher/.ucastquery@";
		// tclString += "\n" +
		// "connect mac/.linkbroken@ -and aodv/.linkbroken@";
		// // present if using 802.11 power-saving mode
		// tclString += "\n" + "connect mac/.energy@ -and wphy/.energy@";

		return tcl;
	}

	public static String getSinkTcl(ComponentSinkNode sink) {
		// sink id
		int i = sink.getId();

		String tcl = new String("\n\n## Creating Sink " + i);
		tcl += "\n" + "set i " + i;
		tcl += "\n" + "puts \"Creating sink $i\"";
		tcl += "\n" + "set node($i) [mkdir drcl.comp.Component n$i]";
		tcl += "\n" + "cd n$i";
		tcl += "\n" + "mkdir " + sink.getApp() + " app";
		tcl += "\n" + "! app setNid $i";
		tcl += "\n" + "! app setSinkNid $i";
		tcl += "\n" + "! app setCoherentThreshold 1000.0";
		tcl += "\n" + "mkdir " + sink.getWiAgent() + " wireless_agent";
		tcl += "\n" + "connect app/down@ -to wireless_agent/up@";
		tcl += "\n"
				+ "connect wireless_agent/.toSensorApp@ -to app/.fromWirelessAgent@";
		tcl += "\n" + "mkdir " + sink.getLl() + " ll";
		tcl += "\n" + "mkdir " + sink.getArp() + " arp";
		tcl += "\n" + "mkdir drcl.inet.core.queue.FIFO queue";
		tcl += "\n" + "mkdir " + sink.getMac() + " mac";
		tcl += "\n" + "mkdir " + sink.getWiPhy() + " wphy";
		// esna ashari edition
		tcl += "\n" + "! wphy setTransmissionRange " + sink.getRadius();
		// tcl += "\n" + "! wphy setRxThresh 0.0";
		// tcl += "\n" + "! wphy setCSThresh 0.0";

		// TODO: only if leach:
		tcl += "\n" + "! wphy setLEACHMode 1";
		// TODO: should it be called always?
		tcl += "\n" + "connect wphy/.channelCheck@ -and mac/.wphyRadioMode@";
		tcl += "\n" + "mkdir " + sink.getWiPropagationModel() + " propagation";
		tcl += "\n" + "mkdir " + sink.getSinkMobilityModel() + " mobility";
		tcl += "\n"
				+ "set PD [mkdir drcl.inet.core.PktDispatcher  pktdispatcher]";
		tcl += "\n" + "set RT [mkdir drcl.inet.core.RT  rt]";
		tcl += "\n" + "set ID [mkdir drcl.inet.core.Identity   id]";

		// TODO: should it be called always?
		tcl += "\n" + "! pktdispatcher setRouteBackEnabled 1";
		tcl += "\n" + "$PD bind $RT";
		tcl += "\n" + "$PD bind $ID";

		// TODO: should it be called always?
		tcl += "\n" + "connect app/.setRoute@ -to rt/.service_rt@";

		tcl += "\n" + "connect wphy/.mobility@    -and mobility/.query@";
		tcl += "\n" + "connect wphy/.propagation@ -and propagation/.query@";
		tcl += "\n" + "connect mac/down@ -and wphy/up@";
		tcl += "\n" + "connect mac/up@   -and queue/output@";
		tcl += "\n" + "connect ll/.mac@ -and mac/.linklayer@";
		tcl += "\n" + "connect ll/down@ -and queue/up@";
		tcl += "\n" + "connect ll/.arp@ -and arp/.arp@";
		tcl += "\n" + "connect -c pktdispatcher/0@down -and ll/up@";
		tcl += "\n" + "set nid $i";
		tcl += "\n" + "! arp setAddresses  $nid $nid";
		tcl += "\n" + "! ll  setAddresses  $nid $nid";
		tcl += "\n" + "! mac setNode_num_  $nid";

		// TODO:only in Leach mode:
		tcl += "\n" + "! mac setLEACHmode 1";

		tcl += "\n" + "! mac setMacAddress $nid";
		tcl += "\n" + "! wphy setNid        $nid";
		tcl += "\n" + "! mobility setNid   $nid";
		tcl += "\n" + "! id setDefaultID   $nid";
		tcl += "\n" + "! queue setMode      \"packet\"";
		tcl += "\n" + "! queue setCapacity  40";
		tcl += "\n" + "# disable ARP";
		tcl += "\n" + "! arp setBypassARP  [ expr 2>1]";
		tcl += "\n"
				+ "connect mobility/.report@ -and /$proj_name/tracker/.node@";
		tcl += "\n" + "connect wphy/down@ -to /$proj_name/channel/.node@";
		tcl += "\n"
				+ "! /$proj_name/channel attachPort $i [! wphy getPort .channel]";

		// tcl += "\n" + "mkdir drcl.inet.protocol.aodv.AODV  aodv";
		// tcl += "\n" + "connect -c aodv/down@ -and pktdispatcher/103@up";
		// tcl += "\n" + "connect aodv/.service_rt@ -and rt/.service_rt@";
		// tcl += "\n" + "connect aodv/.service_id@ -and id/.service_id@";
		// tcl += "\n" +
		// "connect aodv/.ucastquery@ -and pktdispatcher/.ucastquery@";
		// tcl += "\n" + "connect mac/.linkbroken@ -and aodv/.linkbroken@";
		// present if using 802.11 power-saving mode
		// tcl += "\n" + "connect mac/.energy@ -and wphy/.energy@";
		// tcl += "\n" + "! mac setRTSThreshold 0";

		// TODO: set these:
		// tcl += "\n" + "#  maxX maxY maxZ minX minY minZ dX dY dZ";
		tcl += "\n"
				+ "! mobility setTopologyParameters 600.0 500.0 0.0 100.0 100.0 0.0 60.0 60.0 0.0";
		// tcl += "\n" + "! mac  disable_MAC_TRACE_ALL";
		tcl += "\n"
				+ "connect -c  wireless_agent/down@ -and pktdispatcher/1111@up";

		// Positioning part
		tcl += "\n" + "! mobility setPosition 0.0 0.0 0.0 0.0";

		tcl += "\n" + "cd ..";

		tcl += "\n" + "puts \"Sink Created...\"\n\n\n";
		return tcl;
	}

	public static String getRootTcl(ComponentRoot root) {
		String tcl = "##-------------------- Initiating Network\n";

		tcl += "\n" + "source \"" + WSNeditor.projectPath
				+ "/script/test/include.tcl\"";
		tcl += "\n" + "set proj_name " + root.getName();
		tcl += "\n" + "cd [mkdir -q drcl.comp.Component /$proj_name]";
		tcl += "\n" + "set node_num " + root.getChildCount();
		tcl += "\n" + "set target_node_num " + root.targetCount();
		tcl += "\n" + "set sink_id " + root.getSinkNode().getId();
		tcl += "\n" + "mkdir drcl.inet.sensorsim.SensorChannel chan";
		tcl += "\n" + "! chan setCapacity $node_num";
		tcl += "\n" + "mkdir drcl.inet.sensorsim.SeismicProp seismic_Prop";
		tcl += "\n" + "! seismic_Prop setD0 0.2";
		tcl += "\n"
				+ "mkdir drcl.inet.sensorsim.SensorNodePositionTracker nodetracker";
		// TODO: ???
		tcl += "\n" + "! nodetracker setGrid " + root.getEnv().getWidth()
				+ " 0.0 " + root.getEnv().getHeight() + " 0.0";
		tcl += "\n" + "connect chan/.tracker@ -and nodetracker/.channel@";
		tcl += "\n" + "mkdir drcl.inet.mac.Channel channel";
		tcl += "\n"
				+ "! channel setCapacity [expr $node_num -$target_node_num]";
		tcl += "\n" + "mkdir drcl.inet.mac.NodePositionTracker tracker";
		// TODO: SET proper values
		tcl += "\n" + "! tracker setGrid 600.0 100.0 500.0 100.0 60.0 60.0";
		tcl += "\n" + "connect channel/.tracker@ -and tracker/.channel@";
		tcl += "\n\n";
		tcl += JSimTclController.getSinkTcl(root.getSinkNode());

		ComponentSensor sen;
		ListIterator<ComponentSensor> sensors = root.sensorsIterator();
		while (sensors.hasNext()) {
			sen = sensors.next();
			tcl += JSimTclController.getSensorTcl(sen);
		}
		ListIterator<ComponentTargetNode> targets = root.targetsIterator();
		while (targets.hasNext()) {
			tcl += JSimTclController.getTargetTcl(targets.next());
		}

		tcl += "\n\n\n"
				+ "#Output remaining energy levels of the sensors to a plotter";
		tcl += "\n" + "set plot_ [mkdir drcl.comp.tool.Plotter .plot]";
		tcl += "\n"
				+ "for {set i [expr $sink_id + 1]} {$i < [expr $node_num - $target_node_num]} {incr i} {";
		tcl += "\n" + "   connect -c n$i/app/.plotter@ -to $plot_/$i@0" + "\n}";

		return tcl;
	}

	public static String getRootIterativeTcl(ComponentRoot root) {

		// *********************************************
		// Constructing root component and Initializing environment variables
		// *********************************************
		String tcl = "##-------------------- Initiating Network\n";

		tcl += "\n" + "source \"" + WSNeditor.projectPath
				+ "/script/test/include.tcl\"";
		tcl += "\n" + "set proj_name " + root.getName();
		tcl += "\n" + "cd [mkdir -q drcl.comp.Component /$proj_name]";
		tcl += "\n" + "set node_num " + root.getChildCount();
		tcl += "\n" + "set target_node_num " + root.targetCount();
		tcl += "\n" + "set sink_id 0";
		tcl += "\n" + "mkdir drcl.inet.sensorsim.SensorChannel chan";
		tcl += "\n" + "! chan setCapacity $node_num";
		tcl += "\n" + "mkdir drcl.inet.sensorsim.SeismicProp seismic_Prop";
		tcl += "\n" + "! seismic_Prop setD0 0.2";
		tcl += "\n"
				+ "mkdir drcl.inet.sensorsim.SensorNodePositionTracker nodetracker";
		// TODO: ???
		tcl += "\n" + "! nodetracker setGrid " + root.getEnv().getWidth()
				+ " 0.0 " + root.getEnv().getHeight() + " 0.0";
		tcl += "\n" + "connect chan/.tracker@ -and nodetracker/.channel@";
		tcl += "\n" + "mkdir drcl.inet.mac.Channel channel";
		tcl += "\n"
				+ "! channel setCapacity [expr $node_num - $target_node_num]";
		tcl += "\n" + "mkdir drcl.inet.mac.NodePositionTracker tracker";
		// TODO: Arman SET proper values
		tcl += "\n" + "! tracker setGrid " + root.getEnv().getWidth() + " 0.0 "
				+ root.getEnv().getHeight() + " 0.0 "+ComponentController.defaultSensorRadius+" "+ComponentController.defaultSensorRadius;
		tcl += "\n" + "connect channel/.tracker@ -and tracker/.channel@";
		tcl += "\n\n############################"
				+ "\n## this is just for testing purpose by a general plotting";
		tcl += "\n"
				+ "mkdir drcl.inet.sensorsim.AliveSensors	liveSensors \n"
				+ "set numNodesPlot_ [mkdir drcl.comp.tool.Plotter .numNodesPlot] \n"
				+ "connect -c liveSensors/.plotter@ -to $numNodesPlot_/0@0";
		tcl += "\n" + "################";

		// *********************************************
		// Construting Sink node
		// *********************************************
		tcl += JSimTclController.getSinkTcl(root.getSinkNode());
		tcl += "\n\n\n";

		// *********************************************
		// Constructing sensors feature array
		// *********************************************
		tcl += "\n ## this array contains each sensor node's construction";
		tcl += "\n ## [ 0-Name 1-Position.X 2-Position.Y 3-TransmissionRange 4-SensorApp 5-SensorPhy 6-SensorAgent 7-CpuModel 8-WirelessPhy 9-MAC 10-ARP 11-LL 12-SensorMobilityModel 13-WirelessPropagationModel 14-WirelessAgent ]";
		// 0)Name 1)Loc.X 2)Loc.Y 3)TransmissionRange 4)SensorApp 5)SensorPhy
		// 6)SensorAgent 7)CpuModel 8)WirelessPhy 9)MAC 10)ARP 11)LL
		// 12)SensorMobilityModel 13)WirelessPropagationMod 14)WirelessAgent ]
		tcl += "\n" + "set sensorsProperties [list ";

		ComponentSensor sen;
		ListIterator<ComponentSensor> sensors = root.sensorsIterator();
		while (sensors.hasNext()) {
			sen = sensors.next();
			tcl += JSimTclController.createSensorFeaturesArray(sen);
		}
		tcl += " ]";
		tcl += "\n\n";
		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		// end of sensors feature array construction
		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		// *********************************************
		// Constructing Sensor nodes using a loop
		// *********************************************
		tcl += "\n" + "set senIndex 0";
		tcl += "\n"
				+ "for {set i [expr $sink_id + 1]} {$i < [expr $node_num - $target_node_num]} {incr i} {";

		tcl += "\n\t" + "puts \"creating sensor $i\"";
		tcl += "\n\t" + "set node($i) [mkdir drcl.comp.Component n$i]";
		tcl += "\n\t" + "cd n$i";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 4] app";
		tcl += "\n\t" + "! app setNid $i";
		tcl += "\n\t" + "! app setSinkNid $sink_id";
		// TODO: Arman: is it the opposite of transmission range? threshold at
		// which the sensor could no longer comprehend signals
		tcl += "\n\t" + "! app setCoherentThreshold 1000.0";

		// TODO: Arman: what r these? should always be there?
		tcl += "\n\t" + "! app setNn_ [expr $node_num - 1]";
		tcl += "\n\t" + "! app setNum_clusters 3";
		tcl += "\n\t" + "! app setTotal_rounds 5";
		// end of todo

		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 6] agent";
		tcl += "\n\t" + "! agent setDebugEnabled 0";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 5] phy";
		tcl += "\n\t" + "! phy setRxThresh 0.0";
		tcl += "\n\t" + "! phy setDebugEnabled 0";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 12] mobility";
		tcl += "\n\t" + "! phy setNid $i";
		tcl += "\n\t"
				+ "! phy setRadius [lindex [lindex $sensorsProperties $senIndex] 3]";
		tcl += "\n\t" + "connect phy/.toAgent@ -to agent/.fromPhy@";
		tcl += "\n\t" + "connect agent/.toSensorApp@ -to app/.fromSensorAgent@";
		tcl += "\n\t"
				+ "! /$proj_name/chan attachPort $i [! phy getPort .channel]";
		tcl += "\n\t"
				+ "connect phy/.propagation@ -and /$proj_name/seismic_Prop/.query@";
		tcl += "\n\t" + "! mobility setNid $i";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 14] wireless_agent";
		tcl += "\n\t" + "connect app/down@ -to wireless_agent/up@	";
		tcl += "\n\t"
				+ "connect wireless_agent/.toSensorApp@ -to app/.fromWirelessAgent@";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 11] ll";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 10] arp";
		tcl += "\n\t" + "mkdir drcl.inet.core.queue.FIFO queue";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 9] mac";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 8] wphy";

		// TODO: only in Leach
		tcl += "\n\t" + "! mac setLEACHmode 1";
		tcl += "\n\t" + "! wphy setLEACHMode 1";
		// TODO: Arman: when is it needed?
		tcl += "\n\t" + "! wphy setMIT_uAMPS 1"; // #turn on MH mode settings
		tcl += "\n\t" + "connect wphy/.channelCheck@ -and mac/.wphyRadioMode@";

		// esna ashari edition
		tcl += "\n\t"
				+ "! wphy setTransmissionRange [lindex [lindex $sensorsProperties $senIndex] 3]";

		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 13] propagation";
		tcl += "\n\t"
				+ "set PD [mkdir drcl.inet.core.PktDispatcher  pktdispatcher]";
		tcl += "\n\t" + "set RT [mkdir drcl.inet.core.RT  rt]";
		tcl += "\n\t" + "set ID [mkdir drcl.inet.core.Identity   id]";

		// TODO: Arman: when is it needed?
		tcl += "\n\t" + "! pktdispatcher setRouteBackEnabled 1";

		tcl += "\n\t" + "$PD bind $RT";
		tcl += "\n\t" + "$PD bind $ID";

		// TODO: Arman: what r these n are they always needed ;; for plotting i
		// guess
		tcl += "\n\n######################### these r used for ploting i guess!!!!";
		tcl += "\n\t"
				+ "set ifs [java::new drcl.data.BitSet [java::new {int[]} 1 {0}]]";
		tcl += "\n\t"
				+ "set base_entry [java::new drcl.inet.data.RTEntry $ifs]";
		tcl += "\n\t" + "set key [java::new drcl.inet.data.RTKey $i 0 -1]";
		tcl += "\n\t" + "set entry_ [!!! [$base_entry clone]]";
		tcl += "\n\t" + "! rt add $key $entry_ ";
		tcl += "\n#############";

		tcl += "\n\t" + "connect app/.setRoute@ -to rt/.service_rt@";
		tcl += "\n\t" + "connect app/.energy@ -and wphy/.appEnergy@";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $sensorsProperties $senIndex] 7] cpu";
		tcl += "\n\t" + "connect app/.cpu@ -and cpu/.reportCPUMode@";
		tcl += "\n\t" + "connect cpu/.battery@ -and wphy/.cpuEnergyPort@";
		tcl += "\n\t" + "connect mac/.sensorApp@ -and app/.macSensor@";

		tcl += "\n\t" + "connect wphy/.mobility@    -and mobility/.query@";
		tcl += "\n\t" + "connect wphy/.propagation@ -and propagation/.query@";
		tcl += "\n\t" + "connect mac/down@ -and wphy/up@";
		tcl += "\n\t" + "connect mac/up@   -and queue/output@";
		tcl += "\n\t" + "connect ll/.mac@ -and mac/.linklayer@";
		tcl += "\n\t" + "connect ll/down@ -and queue/up@";
		tcl += "\n\t" + "connect ll/.arp@ -and arp/.arp@";
		tcl += "\n\t" + "connect -c pktdispatcher/0@down -and ll/up@";
		tcl += "\n\t" + "set nid $i";
		tcl += "\n\t" + "! arp setAddresses  $nid $nid";
		tcl += "\n\t" + "! ll  setAddresses  $nid $nid";
		tcl += "\n\t" + "! mac setNode_num_ $nid";
		tcl += "\n\t" + "! mac setMacAddress $nid";
		tcl += "\n\t" + "! wphy setNid        $nid";
		tcl += "\n\t" + "! id setDefaultID   $nid";
		tcl += "\n\t" + "! queue setMode      \"packet\"";
		tcl += "\n\t" + "! queue setCapacity  40";
		tcl += "\n\t" + "# disable ARP";
		tcl += "\n\t" + "! arp setBypassARP  [ expr 2>1]";
		// tclString += "\n" + "! mac setRTSThreshold 0";
		tcl += "\n\t"
				+ "connect mobility/.report@ -and /$proj_name/tracker/.node@";
		tcl += "\n\t" + "connect wphy/down@ -to /$proj_name/channel/.node@";
		tcl += "\n\t"
				+ "! /$proj_name/channel attachPort $i [! wphy getPort .channel]";
		// TODO: set these:
		tcl += "\n\t" + "#  maxX maxY maxZ minX minY minZ dX dY dZ";
		tcl += "\n\t"
 + "! mobility setTopologyParameters "
				+ "100.0 100.0 0.0 100.0 100.0 0.0 60.0 60.0 0.0";
		// tclString += "\n" + "! mac  disable_MAC_TRACE_ALL";
		tcl += "\n\t"
				+ "connect -c  wireless_agent/down@ -and pktdispatcher/1111@up";

		// in the template tcl these two lines iterate after creating all
		// targets and sensors
		// tcl +=
		// "\n\n#in the template tcl these two lines iterate after creating all targets and sensors";
		tcl += "\n\t"
				+ "connect mobility/.report_sensor@ -and /$proj_name/nodetracker/.node@";
		tcl += "\n\t" + "connect phy/.mobility@ -and mobility/.query@";

		tcl += "\n\t"
				+ "! mobility setPosition 0.0 [lindex [lindex $sensorsProperties $senIndex] 1] [lindex [lindex $sensorsProperties $senIndex] 2] 0.0";

		tcl += "\n\t" + "cd ..";
		tcl += "\n\t" + "incr senIndex";
		tcl += "\n}";
		// ----------------
		// end of Sensors construction loop
		// ----------------

		// *********************************************
		// Constructing targets feature array
		// *********************************************
		tcl += "\n ## this array contains each target node's construction";
		tcl += "\n ## [ 0-Name 1-LocationX 2-LocationY 3-TransmissionRange 4-SensorPhy 5-SensorMobilityModel 6-TargetAgent ] \n";
		tcl += "set targetsProperties [list ";

		ComponentTargetNode tar;
		ListIterator<ComponentTargetNode> targets = root.targetsIterator();
		while (targets.hasNext()) {
			tar = targets.next();
			tcl += JSimTclController.createTargetFeaturesArray(tar);
		}
		tcl += " ]";
		tcl += "\n\n";
		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		// end of targets feature array construction
		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		// *********************************************
		// Constructing target nodes within a loop
		// *********************************************
		// [ 0)Name 1)LocationX 2)LocationY 3)TransmissionRange 4)SensorPhy
		// 5)SensorMobilityModel 6)TargetAgent ]

		tcl += "\n" + "set targetIndex 0";
		tcl += "\n"
				+ "for {set i [expr $node_num - $target_node_num]} {$i < $node_num} {incr i} {";
		tcl += "\n\t" + "puts \"creating target $i\"";
		tcl += "\n\t" + "set node$i [mkdir drcl.comp.Component n$i]";
		tcl += "\n\t" + "cd n$i";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $targetsProperties $targetIndex] 6] agent";
		tcl += "\n\t" + "! agent setBcastRate 1.0";
		tcl += "\n\t" + "! agent setSampleRate 1.0";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $targetsProperties $targetIndex] 4] phy";
		tcl += "\n\t" + "! phy setRxThresh 0.0";
		tcl += "\n\t" + "! phy setNid $i";
		tcl += "\n\t"
				+ "! phy setRadius [lindex [lindex $targetsProperties $targetIndex] 3]";
		// TODO its not applicable here right? //esna ashari edition
		// tcl += "\n" + "! phy setTranmissionRange " + tar.getRadius();
		tcl += "\n\t" + "! phy setDebugEnabled 0";
		tcl += "\n\t"
				+ "mkdir [lindex [lindex $targetsProperties $targetIndex] 5] mobility";
		tcl += "\n\t" + "connect agent/down@ -to phy/up@";
		tcl += "\n\t" + "connect phy/down@ -to /$proj_name/chan/.node@";
		tcl += "\n\t"
				+ "connect phy/.propagation@ -and /$proj_name/seismic_Prop/.query@";
		tcl += "\n\t" + "! mobility setNid $i";
		// TODO: set
		tcl += "\n\t"
				+ "! mobility setTopologyParameters 30.0 100.0 0.0 30.0 100.0 0.0";

		// in the template tcl these two lines iterate after creating all
		// targets and sensors
		tcl += "\n\t"
				+ "connect mobility/.report_sensor@ -and /$proj_name/nodetracker/.node@";
		tcl += "\n\t" + "connect phy/.mobility@ -and mobility/.query@";

		tcl += "\n\t"
				+ "! mobility setPosition 0.0 [lindex [lindex $targetsProperties $targetIndex] 1] [lindex [lindex $targetsProperties $targetIndex] 2] 0.0";
		tcl += "\n\t" + "incr targetIndex";

		tcl += "\n\n\t" + "cd ..";
		tcl += "\n} \n\n\n";

		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		// end of Target nodes construction loop
		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		// *********************************************
		// Starting Simulation with Simple Plots
		// *********************************************
		// required functions
		tcl += "\n\n"
				+ "####### wsnLoop()"
				+ "\n"
				+ "#This method is called periodically to check if the simulation should continue or not. If"
				+ "\n"
				+ "# all nodes are dead then stop the simulator and	display the cummulative statistics... o.w keep running";
		tcl += "\n##########\n";
		tcl += "proc wsnLoop { } {\n global sim node_num node sink_id target_node_num"
				+ "\n set live_sensors 0 \n set dead_sensors 0 \n	set total_packets 0 \n set dropped_packets 0";
		tcl += "\n for {set i [expr $sink_id + 1]} {$i < [expr $node_num - $target_node_num]} {incr i} {"
				+ "\n\t if {[! $node($i)/app isSensorDead] == 0} { \n\t\t incr live_sensors \n\t } else { \n\t\t incr dead_sensors \n\t} \n}";
		tcl += "\n script [! liveSensors setLiveNodes $live_sensors] \n script [! liveSensors updateGraph]";
		tcl += "\n if { $dead_sensors == [expr $node_num - $target_node_num - 1] } { \n\t puts \"All nodes dead at [! $sim getTime]\" \n $sim stop";
		tcl += "\n puts \"Simulation results:\n Base Station Received [! n0/app getTotalINPackets] \n Collisions at Base Station: [! n0/mac getCollision] \" ";
		tcl += "\n for {set i [expr $sink_id + 1]} {$i < [expr $node_num - $target_node_num]} {incr i} { \n\t set curr_packets [! n$i/app geteID] \n\t puts \"Sensor$i Sent $curr_packets Packets to BS\" "
				+ "\n\t set total_packets [expr $total_packets + $curr_packets] \n }";
		tcl += "\n set app_dropped [! n1/app getDropped_packets]\n"
				+ "set wphy_dropped [! n1/wphy getDropped_packets]"
				+ "\n set mac_dropped [! n1/mac getDropped_packets]"
				+ "\n puts \"Total packets dropped at Application layer: $app_dropped\""
				+ "\n puts \"Total packets dropped at physical layer: $wphy_dropped\"\n"
				+ "puts \"Drops due to collisions (discovered at MAC layer: $mac_dropped\""
				+ "\n set dropped_packets [expr $app_dropped + $wphy_dropped + $mac_dropped]\n"
				+ "puts \"Total Packets sent from all nodes: $total_packets\""
				+ "\n puts \"Number of Dropped Packets: $dropped_packets\"\n"
				+ "puts \"Success Rate: [expr ([! n0/app getTotalINPackets].0 / $total_packets.0) * 100]\" \n}\n}";
		// sensorLocPrintOut
		tcl += "\n\n"
				+ "proc sensorLocPrintOut { } {\n\t"
				+ "global sink_id node_num	target_node_num\n\t"
				+ "for {set i [expr $sink_id + 1]} {$i < [expr $node_num - $target_node_num]} {incr i} {\n\t\t"
				+ "script [! n$i/app printNodeLoc]\n\t}\n}";

		tcl += "\n\n#########################\n"
				+ "#Output remaining energy levels of the sensors to a plotter";
		tcl += "\n" + "set plot_ [mkdir drcl.comp.tool.Plotter .plot]";
		tcl += "\n"
				+ "for {set i [expr $sink_id + 1]} {$i < [expr $node_num - $target_node_num]} {incr i} {";
		tcl += "\n\t" + "connect -c n$i/app/.plotter@ -to $plot_/$i@0" + "\n}";

		tcl += "\n\n########### simulating with a few simple plots";
		tcl += "\n" + "set sim [attach_simulator .]" + " \n$sim stop ";
		tcl += "\n" + "############ starting sink";
		tcl += "\n" + "script {run n0} -at 0.001 -on $sim";
		tcl += "\n" + "############ starting sensors";
		tcl += "\n"
				+ "for {set i [expr $sink_id + 1]} {$i < $node_num} {incr i} { \n\t script puts \"run n$i\" -at 0.1 -on $sim"
				+ "\n}";
		tcl += "\n" + "script \"sensorLocPrintOut\" -at 0.002 -on $sim";
		tcl += "\n" + "script \"wsnLoop\" -at 1.0 -period 2.0 -on $sim";
		tcl += "\n" + "$sim resumeTo 1000.0";

		return tcl;
	}

	public static void removeAllComponents() {
		drcl.comp.Component comp = drcl.comp.Component.Root;
		comp.removeAll();
		comp.reset();
	}

	// TODO implementation needed // Its a dummy now
	// -> recieves a Simulation Manager and sends "Environment Data"
	public static drcl.comp.Component constructRoot(ComponentRoot nodeRoot) {
		drcl.comp.Component comp = new Component(nodeRoot.getName());

		// Constructing Enviorment // TODO dummy
		Environment env = new Environment(comp, nodeRoot.getChildCount(),
				nodeRoot.targetCount());
		System.out.println("Building Env: total child:"
				+ nodeRoot.getChildCount() + " targets: "
				+ nodeRoot.targetCount());
		env.setTopo(0.0, 30.0, 0.0, 100.0, 100.0, 100.0);

		// Constructing Sink Nodes
		ComponentSinkNode compSink = new ComponentSinkNode("0",
				"wsnGUI.v2.TargetNode", new Point(0, 0));
		nodeRoot.setSinkNode(compSink);
		int sink_id = Integer.valueOf(compSink.getName()).intValue();
		System.out.println("Creating sink:  nid:" + sink_id);
		SinkNode sink0 = new SinkNode(sink_id, env, 0);
		env.setSink(sink0);

		// Construct Sensors
		// Dummy implementation
		ListIterator<?> iter = nodeRoot.sensorsIterator();
		int nid = 1;
		while (iter.hasNext()) {
			ComponentSensor compSensor = (ComponentSensor) iter.next();

			// SensorSimple sens= new
			// SensorSimple(Integer.valueOf(compSensor.getName()).intValue() ,
			// sink_id, env, 0 ,
			// compSensor.getLocation().getX(),compSensor.getLocation().getY(),0.0);
			System.out.println("Creating SensorSimple:   " + "id:" + nid
					+ "  loc:" + compSensor.getLocation().toString());
			SensorSimple sens = new SensorSimple(nid, sink_id, env, 0,
					compSensor.getLocation().getX(), compSensor.getLocation()
							.getY(), 0.0);
			nid++;
			// drcl.comp.Component compNew = constructSensor(compSensor, comp);
			env.addSensor(sens);
		}

		// Construct Targets
		// Dummy implementation
		iter = nodeRoot.targetsIterator();
		while (iter.hasNext()) {
			// ComponentTargetNode compTarget = (ComponentTargetNode)
			// iter.next();
			iter.next();
			// TargetNode targ= new
			// TargetNode(Integer.parseInt(compTarget.getName()), env);
			System.out.println("Creating Target:    " + " id:" + nid);
			TargetNode targ = new TargetNode(nid, env);
			nid++;
			env.addTarget(targ);
		}
		// Construct Plots
		// Dummy Implementation
		Plotter sinkPlot1_ = new Plotter("sinkPlot1_");
		Plotter sinkPlot2_ = new Plotter("sinkPlot2_");
		Plotter plot3 = new Plotter("plot3");
		Plotter plot4 = new Plotter("plot4");
		env.root.addComponent(sinkPlot1_);
		env.root.addComponent(sinkPlot2_);
		env.root.addComponent(plot3);
		env.root.addComponent(plot4);
		sink0.app.createSnrPorts(nodeRoot.getChildCount(),
				nodeRoot.targetCount());
		Plotter plot = new Plotter("plot");
		env.root.addComponent(plot);

		env.addPlot(sinkPlot2_);
		env.addPlot(sinkPlot1_);
		env.addPlot(plot3);
		env.addPlot(plot4);
		env.addPlot(plot);

		((SinkAppOH) sink0.sink.getComponent("SinkAppOH")).getPort(
				".PacketsReceivedPlot").connectTo(sinkPlot1_.addPort("0", "0"));
		((SinkAppOH) sink0.sink.getComponent("SinkAppOH")).getPort(
				".latencyPlot").connectTo(sinkPlot2_.addPort("0", "0"));
		iter = nodeRoot.sensorsIterator();

		for (int i = 0; i < nodeRoot.sensorCount(); i++) {
			Integer temp = new Integer(i);
			((OneHopApp) ((SensorSimple) env.getSensors().get(i)).sens
					.getComponent("OneHopApp")).getPort(".plotter").connectTo(
					plot3.addPort("0", temp.toString()));
		}
		int i = 0;
		iter = nodeRoot.targetsIterator();
		while (iter.hasNext()) {
			((SinkAppOH) sink0.sink.getComponent("SinkAppOH")).getPort(
					".snr" + i).connectTo(plot.addPort(i + "", i + ""));
			iter.next();
			i++;
		}

		return comp;
	}

	// TODO implementation needed
	public static drcl.comp.Component constructSensor(ComponentSensor sensor,
			drcl.comp.Component comParent) {

		drcl.comp.Component comp;

		try {
			String strNodeName = sensor.getName();
			System.out.print("Creating sensor: " + strNodeName);
			String strClassName = sensor.getClassName();
			comp = (drcl.comp.Component) Class.forName(strClassName)
					.newInstance();
			comp.setName(strNodeName);
			comp.setID();

		} catch (Exception e) {
			System.out.println("Could not create " + sensor.getName());
			e.printStackTrace();
			return null;
		}

		return comp;
	}

	// TODO implementation needed
	public static drcl.comp.Component constrcutEnviorment() {

		return null;
	}

	// TODO implementation needed
	public static drcl.comp.Component constrcutSink() {

		return null;
	}

	// TODO implementation needed
	public static drcl.comp.Component constrcutTarget() {

		return null;
	}

	/*
	 * drcl.inet.core.PktDispatcher needs to bind drcl.inet.core.Identity and
	 * drcl.inet.core.RT. *
	 */
	public static void bind_ID_RT(drcl.comp.Component root) {
		Method md1 = null, md2 = null;
		try {
			md1 = Class.forName("drcl.inet.core.PktDispatcher").getMethod(
					"bind",
					new Class[] { Class.forName("drcl.inet.core.Identity") });
			md2 = Class.forName("drcl.inet.core.PktDispatcher").getMethod(
					"bind", new Class[] { Class.forName("drcl.inet.core.RT") });
		} catch (NoSuchMethodException e) {
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (root != null) {
			drcl.comp.Component[] cp = root.getAllComponents();
			drcl.inet.core.PktDispatcher pd = null;
			drcl.inet.core.Identity id = null;
			drcl.inet.core.RT rt = null;
			for (int i = 0; i < cp.length; i++) {
				if (cp[i] instanceof drcl.inet.core.PktDispatcher) {
					pd = (drcl.inet.core.PktDispatcher) cp[i];
				} else if (cp[i] instanceof drcl.inet.core.Identity) {
					id = (drcl.inet.core.Identity) cp[i];
				} else if (cp[i] instanceof drcl.inet.core.RT) {
					rt = (drcl.inet.core.RT) cp[i];
				} else
					JSimTclController.bind_ID_RT(cp[i]);
			}
			if (pd != null) {
				try {
					if (id != null && md1 != null)
						md1.invoke(pd, new Object[] { id });
					if (rt != null && md2 != null)
						md2.invoke(pd, new Object[] { rt });
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					System.exit(-1);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	public static String getIDfromName(String strName) {
		int nPos = strName.indexOf('@');
		if (nPos == -1)
			return strName;
		else
			return strName.substring(0, nPos);
	}

	public static String getGroupfromName(String strName) {
		int nPos = strName.indexOf('@');
		if (nPos == -1)
			return "";
		else
			return strName.substring(nPos + 1);
	}

	public static boolean needBinding() {
		try {
			Class.forName("drcl.inet.core.PktDispatcher").getMethod("bind",
					new Class[] { Class.forName("drcl.inet.core.Identity") });
		} catch (NoSuchMethodException e) {
			return (false);
		} catch (SecurityException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return (true);
	}
}
