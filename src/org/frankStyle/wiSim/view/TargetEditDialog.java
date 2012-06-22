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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.frankStyle.wiSim.model.ComponentTargetNode;


/**
 * @author Arman
 * 
 */
public class TargetEditDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 987347632010825818L;
	private final JPanel contentPanel = new JPanel();
	private final ComponentTargetNode node;

	private JPanel pnlName;
	private JTextField txtNodeName;
	private JLabel lblName;

	private JPanel pnlRange;
	private JTextField txtWiRange;
	private JLabel lblWiRange;

	private JPanel pnlAgent;
	private JLabel lblTargetAgent;
	private JTextField txtTargetAgent;
	private JButton btnChangeAgent;

	private JPanel pnlPhy;
	private JLabel lblSensorPhy;
	private JTextField txtSensorPhy;
	private JButton btnChangePhy;

	private JPanel pnlMobilityModel;
	private JLabel lblMobilityModel;
	private JTextField txtMobilityModel;
	private JButton btnMobilityModel;

	Window thisWindow;

	/**
	 * Create the dialog.
	 */
	public TargetEditDialog(ComponentTargetNode node_, Window parent,
			String title) {
		super(parent, title, ModalityType.MODELESS);
		node = node_;
		thisWindow = this;
		setBounds(100, 100, 460, 452);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			pnlName = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnlName.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
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
			pnlAgent = new JPanel();
			FlowLayout fl_pnlAgent = (FlowLayout) pnlAgent.getLayout();
			fl_pnlAgent.setAlignment(FlowLayout.LEFT);
			pnlAgent.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			contentPanel.add(pnlAgent);
			{
				lblTargetAgent = new JLabel("Target Agent:");
				pnlAgent.add(lblTargetAgent);
			}
			{
				txtTargetAgent = new JTextField();
				txtTargetAgent.setEditable(false);
				txtTargetAgent
						.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
				txtTargetAgent.setColumns(10);
				pnlAgent.add(txtTargetAgent);
			}
			{
				btnChangeAgent = new JButton("...");
				btnChangeAgent.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						ClassChooserDialog dlgChooser = new ClassChooserDialog(
								"drcl.inet.sensorsim.TargetAgent",
								"Choose a Target Agent", thisWindow);
						dlgChooser.setVisible(true);
						dlgChooser.setEnabled(true);
						if (dlgChooser.choosedClass != null)
							txtTargetAgent.setText(dlgChooser.choosedClass);
					}
				});
				btnChangeAgent.setPreferredSize(new Dimension(40, 29));
				btnChangeAgent.setIconTextGap(0);
				pnlAgent.add(btnChangeAgent);
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
			pnlMobilityModel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnlMobilityModel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
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
		setTitle("Target " + node.getName() + " properties");
		txtNodeName.setText(node.getName());
		txtWiRange.setText(String.valueOf(node.getRadius()));
		txtTargetAgent.setText(node.getAgent());
		txtMobilityModel.setText(node.getMobility());
		txtSensorPhy.setText(node.getPhy());

	}

	public void updateSensor() {
		node.setName(txtNodeName.getText());
		node.setRadius(Double.valueOf(txtWiRange.getText()).doubleValue());
		node.setTargetAgent(txtTargetAgent.getText());
		node.setPhy(txtSensorPhy.getText());
		node.setMobility(txtMobilityModel.getText());
		//
		// System.out.println(JSimTclController.getSensorTcl(node));

	}

}
