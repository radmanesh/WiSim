package org.frankStyle.wiSim.view;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DebugGraphics;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.frankStyle.wiSim.model.ComponentSensor;


/**
 * @author Arman
 * 
 */
public class SensorEditDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4970449351101715128L;
	private final JPanel contentPanel = new JPanel();
	private final ComponentSensor node;

	private JPanel pnlName;
	private JLabel lblName;
	private JTextField txtNodeName;

	private JPanel pnlRange;
	private JTextField txtWiRange;
	private JLabel lblWiRange;

	private JPanel pnlApp;
	private JTextField txtSenApp;
	private JLabel lblSenApp;

	private JPanel pnlArp;
	private JLabel lblArp;
	private JTextField txtARP;
	private JButton btnChangeARP;

	private JPanel pnlLL;
	private JLabel lblLL;
	private JTextField txtLL;
	private JButton button_1;

	private JPanel pnlWiAgent;
	private JLabel lblWiAgent;
	private JTextField txtWiAgent;
	private JButton btnWiAgent;

	private JPanel pnlMobilityModel;
	private JLabel lblMobilityModel;
	private JTextField txtMobilityModel;
	private JButton btnMobilityModel;

	private JPanel pnlPhy;
	private JLabel lblSensorPhy;
	private JTextField txtSensorPhy;
	private JButton btnChangePhy;

	private JPanel pnlAgent;
	private JLabel lblSensorAgent;
	private JTextField txtSensorAgent;
	private JButton btnChangeAgent;

	private JPanel pnlCpu;
	private JLabel lblCpuModel;
	private JTextField txtCpuModel;
	private JButton button_3;

	private JPanel pnlWiPhy;
	private JLabel lblWiPhy;
	private JTextField txtWiPhy;
	private JButton button_4;

	private JPanel pnlMac;
	private JLabel lblMac;
	private JTextField txtMac;
	private JButton button_5;

	private JPanel pnlWiPropagModel;
	private JLabel lblWiPropagModel;
	private JTextField txtWiPropagModel;
	private JButton button_6;

	Window thisWindow;

	/**
	 * Create the dialog.
	 */
	public SensorEditDialog(ComponentSensor node_, JFrame parent, String title) {
		super(parent, title, ModalityType.MODELESS);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		node = node_;
		thisWindow = this;
		setBounds(100, 100, 460, 493);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			pnlName = new JPanel();
			FlowLayout fl_pnlName = (FlowLayout) pnlName.getLayout();
			fl_pnlName.setAlignment(FlowLayout.LEFT);
			pnlName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlName);
			{
				lblName = new JLabel("Name:");
				pnlName.add(lblName);
			}
			lblName.setLabelFor(txtNodeName);
			{
				txtNodeName = new JTextField();
				pnlName.add(txtNodeName);
				txtNodeName.setColumns(10);
			}
		}
		{
			pnlRange = new JPanel();
			FlowLayout fl_pnlRange = (FlowLayout) pnlRange.getLayout();
			fl_pnlRange.setAlignment(FlowLayout.LEFT);
			pnlRange.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlRange);
			{
				lblWiRange = new JLabel("Wireless Range:");
				pnlRange.add(lblWiRange);
			}
			lblWiRange.setLabelFor(txtWiRange);
			{
				txtWiRange = new JTextField();
				pnlRange.add(txtWiRange);
				txtWiRange.setColumns(10);
			}
		}
		{
			pnlApp = new JPanel();
			FlowLayout fl_pnlApp = (FlowLayout) pnlApp.getLayout();
			fl_pnlApp.setAlignment(FlowLayout.LEFT);
			pnlApp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlApp);
			{
				lblSenApp = new JLabel("Sensor App:");
				pnlApp.add(lblSenApp);
			}
			lblSenApp.setLabelFor(txtSenApp);
			{
				txtSenApp = new JTextField();
				pnlApp.add(txtSenApp);
				txtSenApp
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtSenApp.setEditable(false);
				txtSenApp.setColumns(10);
			}
			{
				JButton btnChangeApp = new JButton("...");
				btnChangeApp.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.sensorsim.SensorApp",
								"Choose a Sensor App", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtSenApp.setText(dlgChooser.choosedClass);
					}
				});
				pnlApp.add(btnChangeApp);
				btnChangeApp.setIconTextGap(0);
				btnChangeApp.setPreferredSize(new Dimension(40, 29));
			}
		}
		{
			pnlPhy = new JPanel();
			FlowLayout fl_pnlPhy = (FlowLayout) pnlPhy.getLayout();
			fl_pnlPhy.setAlignment(FlowLayout.LEFT);
			pnlPhy.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlPhy);
			{
				lblSensorPhy = new JLabel("Sensor Phy:");
				pnlPhy.add(lblSensorPhy);
			}
			{
				txtSensorPhy = new JTextField();
				txtSensorPhy.setEditable(false);
				txtSensorPhy
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtSensorPhy.setColumns(10);
				pnlPhy.add(txtSensorPhy);
			}
			{
				btnChangePhy = new JButton("...");
				btnChangePhy.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.sensorsim.SensorPhy",
								"Choose a Sensor Phy", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtSensorPhy.setText(dlgChooser.choosedClass);
					}
				});
				btnChangePhy.setPreferredSize(new Dimension(40, 29));
				btnChangePhy.setIconTextGap(0);
				pnlPhy.add(btnChangePhy);
			}
		}
		{
			pnlAgent = new JPanel();
			FlowLayout fl_pnlAgent = (FlowLayout) pnlAgent.getLayout();
			fl_pnlAgent.setAlignment(FlowLayout.LEFT);
			pnlAgent.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlAgent);
			{
				lblSensorAgent = new JLabel("Sensor Agent:");
				pnlAgent.add(lblSensorAgent);
			}
			{
				txtSensorAgent = new JTextField();
				txtSensorAgent.setEditable(false);
				txtSensorAgent
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtSensorAgent.setColumns(10);
				pnlAgent.add(txtSensorAgent);
			}
			{
				btnChangeAgent = new JButton("...");
				btnChangeAgent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.sensorsim.SensorAgent",
								"Choose a Sensor Agent", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtSensorAgent.setText(dlgChooser.choosedClass);
					}
				});
				btnChangeAgent.setPreferredSize(new Dimension(40, 29));
				btnChangeAgent.setIconTextGap(0);
				pnlAgent.add(btnChangeAgent);
			}
		}
		{
			pnlCpu = new JPanel();
			FlowLayout fl_pnlCpu = (FlowLayout) pnlCpu.getLayout();
			fl_pnlCpu.setAlignment(FlowLayout.LEFT);
			pnlCpu.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlCpu);
			{
				lblCpuModel = new JLabel("CPU Model:");
				pnlCpu.add(lblCpuModel);
			}
			{
				txtCpuModel = new JTextField();
				txtCpuModel.setEditable(false);
				txtCpuModel
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtCpuModel.setColumns(10);
				pnlCpu.add(txtCpuModel);
			}
			{
				button_3 = new JButton("...");
				button_3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.sensorsim.CPUBase",
								"Choose a Sensor CPU Model", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtCpuModel.setText(dlgChooser.choosedClass);
					}
				});
				button_3.setPreferredSize(new Dimension(40, 29));
				button_3.setIconTextGap(0);
				pnlCpu.add(button_3);
			}
		}

		{
			pnlArp = new JPanel();
			FlowLayout fl_pnlArp = (FlowLayout) pnlArp.getLayout();
			fl_pnlArp.setAlignment(FlowLayout.LEFT);
			pnlArp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlArp);
			{
				lblArp = new JLabel("ARP:");
				pnlArp.add(lblArp);
			}
			{
				txtARP = new JTextField();
				pnlArp.add(txtARP);
				txtARP.setEditable(false);
				txtARP.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtARP.setColumns(10);
			}
			{
				btnChangeARP = new JButton("...");
				btnChangeARP.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.mac.ARP", "Choose an ARP",
								thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtARP.setText(dlgChooser.choosedClass);

					}
				});
				pnlArp.add(btnChangeARP);
				btnChangeARP.setPreferredSize(new Dimension(40, 29));
				btnChangeARP.setIconTextGap(0);
			}
		}
		{
			pnlLL = new JPanel();
			FlowLayout fl_pnlLL = (FlowLayout) pnlLL.getLayout();
			fl_pnlLL.setAlignment(FlowLayout.LEFT);
			pnlLL.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlLL);
			{
				lblLL = new JLabel("LL:");
				pnlLL.add(lblLL);
			}
			{
				txtLL = new JTextField();
				pnlLL.add(txtLL);
				txtLL.setEditable(false);
				txtLL.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtLL.setColumns(10);
			}
			{
				button_1 = new JButton("...");
				button_1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.mac.LL", "Choose LL", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtLL.setText(dlgChooser.choosedClass);
					}
				});
				pnlLL.add(button_1);
				button_1.setPreferredSize(new Dimension(40, 29));
				button_1.setIconTextGap(0);
			}
		}
		{
			pnlMac = new JPanel();
			FlowLayout fl_pnlMac = (FlowLayout) pnlMac.getLayout();
			fl_pnlMac.setAlignment(FlowLayout.LEFT);
			pnlMac.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlMac);
			{
				lblMac = new JLabel("MAC:");
				pnlMac.add(lblMac);
			}
			{
				txtMac = new JTextField();
				txtMac.setEditable(false);
				txtMac.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtMac.setColumns(10);
				pnlMac.add(txtMac);
			}
			{
				button_5 = new JButton("...");
				button_5.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.mac.Mac_802_11", "Choose a MAC",
								thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtMac.setText(dlgChooser.choosedClass);
					}
				});
				button_5.setPreferredSize(new Dimension(40, 29));
				button_5.setIconTextGap(0);
				pnlMac.add(button_5);
			}
		}
		{
			pnlWiAgent = new JPanel();
			FlowLayout fl_pnlWiAgent = (FlowLayout) pnlWiAgent.getLayout();
			fl_pnlWiAgent.setAlignment(FlowLayout.LEFT);
			pnlWiAgent
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlWiAgent);
			{
				lblWiAgent = new JLabel("Wireless Agent:");
				pnlWiAgent.add(lblWiAgent);
			}
			{
				txtWiAgent = new JTextField();
				pnlWiAgent.add(txtWiAgent);
				txtWiAgent.setEditable(false);
				txtWiAgent
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtWiAgent.setColumns(10);
			}
			{
				btnWiAgent = new JButton("...");
				btnWiAgent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.sensorsim.WirelessAgent",
								"Choose a Wireless Agent", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtWiAgent.setText(dlgChooser.choosedClass);
					}
				});
				pnlWiAgent.add(btnWiAgent);
				btnWiAgent.setPreferredSize(new Dimension(40, 29));
				btnWiAgent.setIconTextGap(0);
			}
		}
		{
			pnlMobilityModel = new JPanel();
			FlowLayout fl_pnlWiModel = (FlowLayout) pnlMobilityModel
					.getLayout();
			fl_pnlWiModel.setAlignment(FlowLayout.LEFT);
			pnlMobilityModel
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlMobilityModel);
			{
				lblMobilityModel = new JLabel("Mobility Model:");
				pnlMobilityModel.add(lblMobilityModel);
			}
			{
				txtMobilityModel = new JTextField();
				pnlMobilityModel.add(txtMobilityModel);
				txtMobilityModel.setEditable(false);
				txtMobilityModel
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtMobilityModel.setColumns(10);
			}
			{
				btnMobilityModel = new JButton("...");
				btnMobilityModel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.sensorsim.SensorMobilityModel",
								"Choose a Mobility Model", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtMobilityModel.setText(dlgChooser.choosedClass);
					}
				});
				pnlMobilityModel.add(btnMobilityModel);
				btnMobilityModel.setPreferredSize(new Dimension(40, 29));
				btnMobilityModel.setIconTextGap(0);
			}
		}
		{
			pnlWiPhy = new JPanel();
			FlowLayout fl_pnlWiPhy = (FlowLayout) pnlWiPhy.getLayout();
			fl_pnlWiPhy.setAlignment(FlowLayout.LEFT);
			pnlWiPhy.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlWiPhy);
			{
				lblWiPhy = new JLabel("Wireless Phy:");
				pnlWiPhy.add(lblWiPhy);
			}
			{
				txtWiPhy = new JTextField();
				txtWiPhy.setEditable(false);
				txtWiPhy.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtWiPhy.setColumns(10);
				pnlWiPhy.add(txtWiPhy);
			}
			{
				button_4 = new JButton("...");
				button_4.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.mac.WirelessPhy",
								"Choose Wireless Phy", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtWiPhy.setText(dlgChooser.choosedClass);
					}
				});
				button_4.setPreferredSize(new Dimension(40, 29));
				button_4.setIconTextGap(0);
				pnlWiPhy.add(button_4);
			}
		}

		{
			pnlWiPropagModel = new JPanel();
			FlowLayout fl_pnlWiPropagModel = (FlowLayout) pnlWiPropagModel
					.getLayout();
			fl_pnlWiPropagModel.setAlignment(FlowLayout.LEFT);
			pnlWiPropagModel
					.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlWiPropagModel);
			{
				lblWiPropagModel = new JLabel("Wireless Propagation Model");
				pnlWiPropagModel.add(lblWiPropagModel);
			}
			{
				txtWiPropagModel = new JTextField();
				txtWiPropagModel.setEditable(false);
				txtWiPropagModel
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtWiPropagModel.setColumns(10);
				pnlWiPropagModel.add(txtWiPropagModel);
			}
			{
				button_6 = new JButton("...");
				button_6.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.mac.RadioPropagationModel",
								"Choose Wireless Propagation Model", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtWiPropagModel.setText(dlgChooser.choosedClass);
					}
				});
				button_6.setPreferredSize(new Dimension(40, 29));
				button_6.setIconTextGap(0);
				pnlWiPropagModel.add(button_6);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						updateSensor();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		// End of autogen code
		init();

	}

	// TODO: ??
	void init() {

		setTitle("Sensor " + node.getName() + " properties");
		txtNodeName.setText(node.getName());
		txtWiRange.setText(String.valueOf(node.getRadius()));
		txtSenApp.setText(node.getApp());
		txtSensorAgent.setText(node.getAgent());
		txtARP.setText(node.getArp());
		txtCpuModel.setText(node.getCpuModel());
		txtLL.setText(node.getLl());
		txtMac.setText(node.getMac());
		txtSensorPhy.setText(node.getPhy());
		txtMobilityModel.setText(node.getSenMobilityModel());
		txtWiAgent.setText(node.getWiAgent());
		txtWiPhy.setText(node.getWiPhy());
		txtWiPropagModel.setText(node.getWiPropagationModel());
	}

	public void updateSensor() {
		node.setName(txtNodeName.getText());
		node.setRadius(Double.valueOf(txtWiRange.getText()).doubleValue());
		node.setApp(txtSenApp.getText());
		node.setAgent(txtSensorAgent.getText());
		node.setArp(txtARP.getText());
		node.setCpuModel(txtCpuModel.getText());
		node.setLl(txtLL.getText());
		node.setMac(txtMac.getText());
		node.setPhy(txtSensorPhy.getText());
		node.setSenMobilityModel(txtMobilityModel.getText());
		node.setWiAgent(txtWiAgent.getText());
		node.setWiPhy(txtWiPhy.getText());
		node.setWiPropagationModel(txtWiPropagModel.getText());
		//
		// System.out.println(JSimTclController.getSensorTcl(node));

	}

}
