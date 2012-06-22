package org.frankStyle.wiSim.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.frankStyle.wiSim.controller.ComponentController;


public class PreferencesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3876660272414714262L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;


	/**
	 * Create the dialog.
	 */
	public PreferencesDialog(JFrame owner, boolean isModal) {
		super(owner, isModal);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		setContentPane(contentPanel);
		setResizable(false);
		{
			JPanel panel = new JPanel();
			panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			panel.setBorder(new TitledBorder(new EtchedBorder(
					EtchedBorder.LOWERED, null, null), "Environment",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0,
							0, 0)));
			contentPanel.add(panel);
			{
				JLabel lblNewLabel = new JLabel("Width:");
				panel.add(lblNewLabel);
			}
			{
				textField = new JTextField();
				panel.add(textField);
				textField.setColumns(6);
			}
			{
				JLabel lblNewLabel_1 = new JLabel("Height");
				panel.add(lblNewLabel_1);
			}
			{
				textField_1 = new JTextField();
				panel.add(textField_1);
				textField_1.setColumns(6);
			}
		}
		{
			JPanel panel2 = new JPanel();
			panel2.setBorder(new TitledBorder(null, "Project Options",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel2);
			{
				JLabel lblProjecName = new JLabel("Projec Name:");
				panel2.add(lblProjecName);
			}
			{
				textField_2 = new JTextField();
				textField_2.setColumns(6);
				panel2.add(textField_2);
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
						try {
							setValues();
							EditorWindow.showWarning("Setting applied.");
						} catch (Exception e2) {
							EditorWindow
									.showWarning("Error in setting options");
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		loadValues();
	}

	private void loadValues() {
		String width = Integer.valueOf(
				ComponentController.getRoot().getEnvironmentDimension().width)
				.toString();
		textField.setText(width);
		textField_1.setText(Integer.valueOf(
				ComponentController.getRoot().getEnvironmentDimension().height)
				.toString());
	}

	private void setValues() throws Exception {
		int width = Integer.valueOf(textField.getText()).intValue();
		int height = Integer.valueOf(textField_1.getText()).intValue();
		ComponentController.setEnvironmentBound(width, height);
		ComponentController.getRoot().setName(textField_2.getText());
	}
}
