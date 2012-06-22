package org.frankStyle.wiSim.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.frankStyle.wiSim.WSNeditor;
import org.frankStyle.wiSim.controller.ComponentController;
import org.frankStyle.wiSim.controller.JSimTclController;
import org.w3c.dom.Document;


public class EditorWindow extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4603596390580970107L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenu mnNewMenu_1;
	private JMenu mnNewMenu_2;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JToolBar toolBar;
	private JButton toolSelectBtn;
	private JLabel lblStatus;
	private JButton toolAddSensorBtn;
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;
	private JMenuItem mntmNewMenuItem_6;
	private JMenuItem mntmNewMenuItem_7;
	private JMenuItem mntmNewMenuItem_8;
	private JMenuItem mntmNewMenuItem_9;
	private JMenuItem mntmNewMenuItem_10;
	private JMenu mnNewMenu_3;
	private JMenuItem mntmNewMenuItem_15;
	private JMenuItem mntmNewMenuItem_16;
	private JMenuItem mntmNewMenuItem_17;
	private JMenuItem mntmNewMenuItem_18;
	private JMenu mnNewMenu_4;
	private JMenuItem menuItem;
	private JMenuItem menuItem_1;
	private JMenuItem menuItem_2;
	private JMenuItem mntmNewMenuItem_12;
	private JMenuItem menuItem_3;
	private JMenuItem mntmNewMenuItem_11;
	private JButton toolAddTargetBtn;
	private JButton toolDeleteBtn;
	private JButton toolGridBtn;
	private JButton toolArrangeBtn;
	private JButton toolRandomBtn;
	private JPanel panel;
	private JButton toolZoomInBtn;
	private JMenuItem mntmNewMenuItem_13;
	private JSeparator separator;
	private JButton toolZoomOutBtn;
	private JButton toolSaveBtn;
	private JButton toolOpenBtn;
	private JButton toolTclBtn;
	private JButton toolPrefBtn;
	private JButton toolNewBtn;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JSeparator separator_3;

	/**
	 * My Fields
	 */
	private JScrollPane scenScroll;
	static final int STOP = 0;
	static final int STARTdic = 1;
	static final int PAUSE = 2;
	static double zoomFactor = 1.0;
	private static boolean gridMode = false;
	private static double gridSpace = 50;
	private static double gridSpaceFactor = 1;
	private ComponentController cmpController;
	private ScenarioPanel scenarioPane; // $hide$
	private drcl.sim.process.SMMTSimulator simulator;
	private final drcl.comp.Component simComponent = null;
	private int simStatus = EditorWindow.STOP;
	// private final drcl.ruv.System m_console = null;
	private File fileXML = null;
	private String strFileName = new String();
	private final Object lock = new Object();
	private JFileChooser fc = null;
	// private SimulationController simManager;
	private Component horizontalGlue;
	private JMenuItem mntmSaveAsMenuItem_14;

	/**
	 * @wbp.nonvisual location=42,-19
	 */

	/**
	 * Create the frame.
	 */
	public EditorWindow(String[] args) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				EditorWindow.class.getResource("/frame_images/wireless.png"))); //$NON-NLS-1$
		setTitle("WSNeditor"); //$NON-NLS-1$
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cmdHandlerExit();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cmpController = new ComponentController();

		setBounds(100, 100, 800, 400);
		setMinimumSize(new Dimension(700, 400));
		{
			menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			{
				mnNewMenu = new JMenu("File"); //$NON-NLS-1$
				menuBar.add(mnNewMenu);
				{
					mntmNewMenuItem = new JMenuItem("About"); //$NON-NLS-1$
					mntmNewMenuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerAbout();
						}
					});
					mnNewMenu.add(mntmNewMenuItem);
				}
				{
					mntmNewMenuItem_13 = new JMenuItem("New"); //$NON-NLS-1$
					mntmNewMenuItem_13.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerNew();
						}
					});
					mntmNewMenuItem_13.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_N, InputEvent.META_MASK));
					mnNewMenu.add(mntmNewMenuItem_13);
				}
				{
					mntmNewMenuItem_2 = new JMenuItem("Open"); //$NON-NLS-1$
					mntmNewMenuItem_2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerOpen();
						}
					});
					mntmNewMenuItem_2.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_O, InputEvent.META_MASK));
					mnNewMenu.add(mntmNewMenuItem_2);
				}
				{
					mntmNewMenuItem_3 = new JMenuItem("Save"); //$NON-NLS-1$
					mntmNewMenuItem_3.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerSave();
						}
					});
					mntmNewMenuItem_3.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_S, InputEvent.META_MASK));
					mnNewMenu.add(mntmNewMenuItem_3);
				}
				{
					mntmNewMenuItem_1 = new JMenuItem("Quit"); //$NON-NLS-1$
					mntmNewMenuItem_1.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerExit();
						}
					});
					{
						mntmSaveAsMenuItem_14 = new JMenuItem("Save As..."); //$NON-NLS-1$
						mntmSaveAsMenuItem_14
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										cmdHandlerSaveAs();
									}
								});
						mntmSaveAsMenuItem_14.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_S,
										InputEvent.SHIFT_MASK
												| InputEvent.META_MASK));
						mnNewMenu.add(mntmSaveAsMenuItem_14);
					}
					mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_Q, InputEvent.META_MASK));
					mnNewMenu.add(mntmNewMenuItem_1);
				}
			}
			{
				mnNewMenu_1 = new JMenu("Edit"); //$NON-NLS-1$
				mnNewMenu_1.setActionCommand("Edit"); //$NON-NLS-1$
				menuBar.add(mnNewMenu_1);
				{
					mntmNewMenuItem_4 = new JMenuItem("Select All"); //$NON-NLS-1$
					mntmNewMenuItem_4.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO: Arman:
						}
					});
					mntmNewMenuItem_4.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_ALL_CANDIDATES, 0));
					mnNewMenu_1.add(mntmNewMenuItem_4);
				}
				{
					mntmNewMenuItem_5 = new JMenuItem("Delete"); //$NON-NLS-1$
					mntmNewMenuItem_5.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerDeleteNode();
						}
					});
					mntmNewMenuItem_5.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_DELETE, 0));
					mnNewMenu_1.add(mntmNewMenuItem_5);
				}
				{
					mntmNewMenuItem_6 = new JMenuItem("Edit Node"); //$NON-NLS-1$
					mntmNewMenuItem_6.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerEditNode();
						}
					});
					mnNewMenu_1.add(mntmNewMenuItem_6);
				}
				{
					mntmNewMenuItem_7 = new JMenuItem("Copy"); //$NON-NLS-1$
					mntmNewMenuItem_7.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerCopy();
						}
					});
					mntmNewMenuItem_7.setAccelerator(KeyStroke.getKeyStroke(
							KeyEvent.VK_COPY, 0));
					mntmNewMenuItem_7.setHideActionText(true);
					mnNewMenu_1.add(mntmNewMenuItem_7);
				}
				{
					{
						mntmNewMenuItem_10 = new JMenuItem("Cut"); //$NON-NLS-1$
						mntmNewMenuItem_10
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										cmdHandlerCut();
									}
								});
						mntmNewMenuItem_10.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_CUT, 0));
						mnNewMenu_1.add(mntmNewMenuItem_10);
					}
					{
						mntmNewMenuItem_9 = new JMenuItem("Paste"); //$NON-NLS-1$
						mntmNewMenuItem_9
								.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										cmdHandlerPaste();
									}
								});
						mntmNewMenuItem_9.setAccelerator(KeyStroke
								.getKeyStroke(KeyEvent.VK_PASTE, 0));
						mnNewMenu_1.add(mntmNewMenuItem_9);
					}
				}
			}
			{
				mnNewMenu_3 = new JMenu("View"); //$NON-NLS-1$
				menuBar.add(mnNewMenu_3);
				{
					mntmNewMenuItem_17 = new JMenuItem("Node Details"); //$NON-NLS-1$
					mntmNewMenuItem_17.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerViewNodeDetails();
						}
					});
					mnNewMenu_3.add(mntmNewMenuItem_17);
				}
				{
					mntmNewMenuItem_15 = new JMenuItem("Show Grid"); //$NON-NLS-1$
					mntmNewMenuItem_15.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerToggleGrid();
						}
					});
					mnNewMenu_3.add(mntmNewMenuItem_15);
				}
				{
					mntmNewMenuItem_16 = new JMenuItem("Hide Log Panel"); //$NON-NLS-1$
					mntmNewMenuItem_16.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
						}
					});
					mnNewMenu_3.add(mntmNewMenuItem_16);
				}
			}
			{
				mnNewMenu_2 = new JMenu("Tools"); //$NON-NLS-1$
				menuBar.add(mnNewMenu_2);
				{
					mntmNewMenuItem_11 = new JMenuItem("Arrange Nodes"); //$NON-NLS-1$
					mntmNewMenuItem_11.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerFixOnGrid();
						}
					});
					mnNewMenu_2.add(mntmNewMenuItem_11);
				}
				{
					mntmNewMenuItem_18 = new JMenuItem("Insert Random"); //$NON-NLS-1$
					mntmNewMenuItem_18.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerInsertRandom();
						}
					});
					mnNewMenu_2.add(mntmNewMenuItem_18);
				}
				mntmNewMenuItem_8 = new JMenuItem("Scenario Preferences"); //$NON-NLS-1$
				mnNewMenu_2.add(mntmNewMenuItem_8);
				mntmNewMenuItem_8.setMnemonic(KeyEvent.VK_CONTROL);
				mntmNewMenuItem_8.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						cmdHandlerPreferences();
					}
				});
				mntmNewMenuItem_8.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_COMMA, InputEvent.META_MASK));
			}
			{
				mnNewMenu_4 = new JMenu("Simulation"); //$NON-NLS-1$
				menuBar.add(mnNewMenu_4);
				{
					menuItem_3 = new JMenuItem("CreateTCL"); //$NON-NLS-1$
					menuItem_3.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerSaveTCL();
						}
					});
					mnNewMenu_4.add(menuItem_3);
				}
				{
					menuItem = new JMenuItem("Start Simulation"); //$NON-NLS-1$
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerStart();
						}
					});
					mnNewMenu_4.add(menuItem);
				}
				{
					menuItem_1 = new JMenuItem("Pause Simulation"); //$NON-NLS-1$
					menuItem_1.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerPause();
						}
					});
					mnNewMenu_4.add(menuItem_1);
				}
				{
					menuItem_2 = new JMenuItem("Stop Simulation"); //$NON-NLS-1$
					menuItem_2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerStop();
						}
					});
					mnNewMenu_4.add(menuItem_2);
				}
				{
					mntmNewMenuItem_12 = new JMenuItem("New Plot"); //$NON-NLS-1$
					mntmNewMenuItem_12.setEnabled(false);
					mnNewMenu_4.add(mntmNewMenuItem_12);
				}
			}
		}
		contentPane = new JPanel();
		contentPane.setOpaque(true);
		contentPane.setBackground(UIManager.getColor("Panel.background")); //$NON-NLS-1$
		contentPane.setLayout(new BorderLayout(4, 1));
		setContentPane(contentPane);
		{
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.setRollover(true);
			toolBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			toolBar.setMargin(new Insets(5, 0, 3, 0));
			toolBar.setForeground(UIManager
					.getColor("ToolBar.floatingForeground")); //$NON-NLS-1$
			toolBar.setAlignmentY(Component.CENTER_ALIGNMENT);
			toolBar.setName("Toolbar"); //$NON-NLS-1$
			toolBar.setBackground(Color.LIGHT_GRAY);
			contentPane.add(toolBar, BorderLayout.NORTH);
			{
				toolSelectBtn = new JButton(""); //$NON-NLS-1$
				toolSelectBtn.setIconTextGap(0);
				toolSelectBtn.setActionCommand("Select"); //$NON-NLS-1$
				toolSelectBtn.setToolTipText("Select"); //$NON-NLS-1$
				toolSelectBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/mouse.png"))); //$NON-NLS-1$
				// toolSelectBtn.setIcon(new
				// ImageIcon(EditorWindow.class.getResource("/com/sun/java/swing/plaf/gtk/resources/gtk-no-4.png")));
				toolSelectBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						scenarioPane.setMode(ScenarioPanel.ARROW);
						scenarioPane.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				});
				{
					toolNewBtn = new JButton(""); //$NON-NLS-1$
					toolNewBtn.setToolTipText("New Scenario File"); //$NON-NLS-1$
					toolNewBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					toolNewBtn.setActionCommand("New"); //$NON-NLS-1$
					toolNewBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerNew();
						}
					});
					toolNewBtn.setIcon(new ImageIcon(EditorWindow.class
							.getResource("/frame_images/new_page.png"))); //$NON-NLS-1$
					toolBar.add(toolNewBtn);
				}
				{
					toolOpenBtn = new JButton(""); //$NON-NLS-1$
					toolOpenBtn.setActionCommand("Open"); //$NON-NLS-1$
					toolOpenBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					toolOpenBtn.setToolTipText("Open Scenario"); //$NON-NLS-1$
					toolOpenBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerOpen();
						}
					});
					toolOpenBtn.setIcon(new ImageIcon(EditorWindow.class
							.getResource("/frame_images/folder.png"))); //$NON-NLS-1$
					toolBar.add(toolOpenBtn);
				}
				{
					toolSaveBtn = new JButton(""); //$NON-NLS-1$
					toolSaveBtn.setToolTipText("Save"); //$NON-NLS-1$
					toolSaveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					toolSaveBtn.setActionCommand("Save"); //$NON-NLS-1$
					toolSaveBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerSave();
						}
					});
					toolSaveBtn.setIcon(new ImageIcon(EditorWindow.class
							.getResource("/frame_images/save.png"))); //$NON-NLS-1$
					toolBar.add(toolSaveBtn);
				}
				{
					separator_1 = new JSeparator();
					separator_1.setAlignmentX(Component.LEFT_ALIGNMENT);
					separator_1.setOpaque(true);
					separator_1.setEnabled(false);
					separator_1.setRequestFocusEnabled(false);
					separator_1.setOrientation(SwingConstants.VERTICAL);
					toolBar.add(separator_1);
				}
				toolSelectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolBar.add(toolSelectBtn);
			}
			{
			}
			{
				toolAddSensorBtn = new JButton("Sensor"); //$NON-NLS-1$
				toolAddSensorBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
				toolAddSensorBtn.setToolTipText("Add Sensor"); //$NON-NLS-1$
				toolAddSensorBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolAddSensorBtn
						.setHorizontalTextPosition(SwingConstants.CENTER);
				toolAddSensorBtn.setIconTextGap(-15);
				toolAddSensorBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/sensorbut.png"))); //$NON-NLS-1$
				toolAddSensorBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						scenarioPane.setCursor(Cursor
								.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
						scenarioPane.setMode(ScenarioPanel.ADD_SENSOR);

					}
				});
				toolBar.add(toolAddSensorBtn);
			}
			{
				toolAddTargetBtn = new JButton("Target"); //$NON-NLS-1$
				toolAddTargetBtn.setToolTipText("Add Target"); //$NON-NLS-1$
				toolAddTargetBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolAddTargetBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
				toolAddTargetBtn
						.setHorizontalTextPosition(SwingConstants.CENTER);
				toolAddTargetBtn.setIconTextGap(-15);
				toolAddTargetBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/flag.png"))); //$NON-NLS-1$
				toolAddTargetBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						scenarioPane.setCursor(Cursor
								.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
						scenarioPane.setMode(ScenarioPanel.ADD_TARGET);
					}
				});
				toolBar.add(toolAddTargetBtn);
			}
			{
				toolRandomBtn = new JButton(""); //$NON-NLS-1$
				toolRandomBtn.setIconTextGap(0);
				toolRandomBtn.setHorizontalTextPosition(SwingConstants.CENTER);
				toolRandomBtn.setActionCommand("Random"); //$NON-NLS-1$
				toolRandomBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolRandomBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
				toolRandomBtn.setToolTipText("Insert Random"); //$NON-NLS-1$
				toolRandomBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/games.png"))); //$NON-NLS-1$
				toolRandomBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cmdHandlerInsertRandom();
					}
				});
				toolDeleteBtn = new JButton(""); //$NON-NLS-1$
				toolDeleteBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolDeleteBtn.setActionCommand("Delete"); //$NON-NLS-1$
				toolDeleteBtn.setToolTipText("Delete"); //$NON-NLS-1$
				toolDeleteBtn.setHorizontalAlignment(SwingConstants.RIGHT);
				toolDeleteBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
				toolDeleteBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/trash.png"))); //$NON-NLS-1$
				toolDeleteBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						scenarioPane.removeSelectedNode();
					}
				});
				toolBar.add(toolDeleteBtn);
				{
					separator = new JSeparator();
					separator.setAlignmentX(Component.LEFT_ALIGNMENT);
					separator.setOpaque(true);
					separator.setEnabled(false);
					separator.setRequestFocusEnabled(false);
					separator.setOrientation(SwingConstants.VERTICAL);
					toolBar.add(separator);
				}
				{
					toolZoomInBtn = new JButton(""); //$NON-NLS-1$
					toolZoomInBtn.setActionCommand("ZoomIn"); //$NON-NLS-1$
					toolZoomInBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					toolZoomInBtn.setIcon(new ImageIcon(EditorWindow.class
							.getResource("/frame_images/zoom_in.png"))); //$NON-NLS-1$
					toolZoomInBtn.setToolTipText("Zoom In"); //$NON-NLS-1$
					toolZoomInBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerZoomIn();
						}
					});
					toolBar.add(toolZoomInBtn);
				}
				{
					toolZoomOutBtn = new JButton(""); //$NON-NLS-1$
					toolZoomOutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					toolZoomOutBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerZoomOut();
						}

					});
					toolZoomOutBtn.setActionCommand("ZoomOut"); //$NON-NLS-1$
					toolZoomOutBtn.setIcon(new ImageIcon(EditorWindow.class
							.getResource("/frame_images/zoom_out.png"))); //$NON-NLS-1$
					toolZoomOutBtn.setToolTipText("Zoom Out"); //$NON-NLS-1$
					toolBar.add(toolZoomOutBtn);
				}
				{
					toolGridBtn = new JButton(""); //$NON-NLS-1$
					toolGridBtn.setActionCommand("Grid"); //$NON-NLS-1$
					toolGridBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
					toolGridBtn.setToolTipText("Toggle Grid"); //$NON-NLS-1$
					toolGridBtn.setIcon(new ImageIcon(EditorWindow.class
							.getResource("/frame_images/grid.png"))); //$NON-NLS-1$
					toolGridBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cmdHandlerToggleGrid();
						}
					});
					toolBar.add(toolGridBtn);
				}
				{
					separator_2 = new JSeparator();
					separator_2.setAlignmentX(Component.LEFT_ALIGNMENT);
					separator_2.setOpaque(true);
					separator_2.setRequestFocusEnabled(false);
					separator_2.setOrientation(SwingConstants.VERTICAL);
					separator_2.setEnabled(false);
					toolBar.add(separator_2);
				}
				toolBar.add(toolRandomBtn);
			}
			{
				toolArrangeBtn = new JButton(""); //$NON-NLS-1$
				toolArrangeBtn.setActionCommand("Fix"); //$NON-NLS-1$
				toolArrangeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolArrangeBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/lock.png"))); //$NON-NLS-1$
				toolArrangeBtn.setToolTipText("Fix On Grid"); //$NON-NLS-1$
				toolArrangeBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cmdHandlerFixOnGrid();
					}
				});
				toolBar.add(toolArrangeBtn);
			}
			{
				toolTclBtn = new JButton(""); //$NON-NLS-1$
				toolTclBtn.setActionCommand("TCL"); //$NON-NLS-1$
				toolTclBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolTclBtn.setToolTipText("TCL Output"); //$NON-NLS-1$
				toolTclBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cmdHandlerSaveTCL();
					}
				});
				toolTclBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/computer.png"))); //$NON-NLS-1$
				toolBar.add(toolTclBtn);
			}
			{
				toolPrefBtn = new JButton(""); //$NON-NLS-1$
				toolPrefBtn.setActionCommand("Preferencess"); //$NON-NLS-1$
				toolPrefBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
				toolPrefBtn.setToolTipText("Preferences"); //$NON-NLS-1$
				toolPrefBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cmdHandlerPreferences();
					}
				});
				toolPrefBtn.setIcon(new ImageIcon(EditorWindow.class
						.getResource("/frame_images/tools.png"))); //$NON-NLS-1$
				toolBar.add(toolPrefBtn);
			}
			{
				horizontalGlue = Box.createHorizontalGlue();
				toolBar.add(horizontalGlue);
			}
			{
				separator_3 = new JSeparator();
				separator_3.setOpaque(true);
				separator_3.setOrientation(SwingConstants.VERTICAL);
				toolBar.add(separator_3);
			}
		}
		{
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			scenarioPane = new ScenarioPanel(this);
			scenarioPane.updatePaneSize();
			scenScroll = new JScrollPane(scenarioPane);
			scenScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
			panel.add(scenScroll);
			panel.revalidate();
			contentPane.add(panel, BorderLayout.CENTER);
		}
		{
			lblStatus = new JLabel("no Info"); //$NON-NLS-1$
			lblStatus.setIconTextGap(5);
			lblStatus
					.setIcon(new ImageIcon(
							EditorWindow.class
									.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif"))); //$NON-NLS-1$
			lblStatus.setFont(new Font("Lucida Grande", Font.PLAIN, 12)); //$NON-NLS-1$
			lblStatus.setVerifyInputWhenFocusTarget(false);
			lblStatus.setSize(new Dimension(0, 16));
			lblStatus.setRequestFocusEnabled(false);
			lblStatus.setAutoscrolls(true);
			lblStatus.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(
					47, 79, 79), null, null, null));
			lblStatus.setLabelFor(contentPane);
			contentPane.add(lblStatus, BorderLayout.SOUTH);
		}

		if (args.length == 1) {
			fileXML = new File(args[0]);
			strFileName = new String(args[0]);
			cmpController.inputXML(fileXML);
			scenarioPane.updateGraphicComponents();
			cmpController.setDirty(false);
			setTitle(getTitle() + " -- " + fileXML); //$NON-NLS-1$
		}
		try {
			simulator = new drcl.sim.process.SMMTSimulator("GUI"); //$NON-NLS-1$
		} catch (Exception e) {
			System.err.println("ShellTcl initialization error!"); //$NON-NLS-1$
			e.printStackTrace();
		}

		// Preload useful dialogs in a low-priority background thread
		Thread backgroundTask_ = new Thread() {
			@Override
			public void run() {
				synchronized (lock) {
					fc = new JFileChooser();
					File fCurrentDir = new File(System.getProperty("user.dir")); //$NON-NLS-1$
					fc.setCurrentDirectory(fCurrentDir);
					fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
						@Override
						public boolean accept(File f) {
							return (f.getName().toLowerCase().endsWith(".xml") || f //$NON-NLS-1$
									.isDirectory());
						}

						@Override
						public String getDescription() {
							return "XML File"; //$NON-NLS-1$
						}
					});
					lock.notifyAll();
				}
			}
		};
		backgroundTask_.setPriority(Thread.MIN_PRIORITY);
		backgroundTask_.start();
		WSNeditor.handleLog("EditorWindow constructor success");

	}

	protected void cmdHandlerToggleGrid() {
		if (EditorWindow.gridMode) {
			EditorWindow.gridMode = false;
			toolGridBtn.setBackground(Color.DARK_GRAY);
			toolGridBtn.setBorderPainted(true);
			mntmNewMenuItem_15.setText("Show Grid"); //$NON-NLS-1$
			repaint();
		} else {
			EditorWindow.gridMode = true;
			toolGridBtn.setBackground(Color.GRAY);
			toolGridBtn.setBorderPainted(false);
			mntmNewMenuItem_15.setText("Hide Grid"); //$NON-NLS-1$
			repaint();
		}
	}

	protected void cmdHandlerInsertRandom() {
		// TODO Auto-generated method stub

	}

	protected void cmdHandlerViewNodeDetails() {
		// TODO Auto-generated method stub

	}

	protected void cmdHandlerPaste() {
		// TODO Auto-generated method stub

	}

	protected void cmdHandlerCut() {
		// TODO Auto-generated method stub

	}

	protected void cmdHandlerCopy() {
		// TODO Auto-generated method stub

	}

	protected void cmdHandlerEditNode() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	protected void cmdHandlerDeleteNode() {
		scenarioPane.removeSelectedNode();

	}

	public JPanel getPanel() {
		return panel;
	}

	public Document getXMLDocument() {
		return cmpController.getXMLDocument();
	}

	public ComponentController getComponentController() {
		return cmpController;
	}

	void cmdHandlerExit() {
		if (cmpController.isDirty()) {
			int nRes = JOptionPane.showConfirmDialog(this,
					"Document modified. Quit without save?", "Warning", //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (nRes == JOptionPane.NO_OPTION) {
				cmdHandlerSave();
				System.exit(0);
			}
		}
		System.exit(0);
	}

	protected void cmdHandlerAbout() {
		JOptionPane
				.showMessageDialog(
						this,
						"WSNEditor v0.2Beta: WSN GUI for J-Sim.\nWritten by: Arman Radmanesh.\nAmirkabir University of Tehran.\n2010", //$NON-NLS-1$
				"About", JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
	}

	public void cmdHandlerNew() {
		fileXML = null;
		cmpController.clearDocument();
		scenarioPane.updateGraphicComponents();
		cmpController.setDirty(false);
		JSimTclController.setJavaSimValid(false);
		// undoManager.init();
	}

	public void cmdHandlerOpen() {
		synchronized (lock) {
			try {
				if (fc == null)
					lock.wait();
			} catch (Exception e_) {
				e_.printStackTrace();
			}
		}

		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			fileXML = fc.getSelectedFile();
			strFileName = new String(fc.getName(fileXML));
			cmpController.inputXML(fileXML);
			addStatusMsg(String.valueOf((cmpController.getRootComponent())
					.getChildCount()));
			System.out
					.println(String.valueOf((cmpController.getRootComponent())
							.getChildCount()));
			setTitle(getTitle() + " -- " + strFileName); //$NON-NLS-1$
			scenarioPane.updateGraphicComponents();
			cmpController.setDirty(false);

			// undoManager.init();
		}
	}

	public void cmdHandlerSave() {
		if (fileXML != null) {
			cmpController.outputXML(fileXML);
			return;
		} else
			cmdHandlerSaveAs();
	}

	public void cmdHandlerSaveAs() {
		synchronized (lock) {
			try {
				if (fc == null)
					lock.wait();
			} catch (Exception e_) {
				e_.printStackTrace();
			}
		}

		int res = fc.showSaveDialog(this);
		if (res == JFileChooser.APPROVE_OPTION) {
			fileXML = fc.getSelectedFile();
			strFileName = new String(fc.getName(fileXML));
			cmpController.outputXML(fileXML);
			setTitle(getTitle() + " -- " + strFileName); //$NON-NLS-1$
		}
	}

	public void cmdHandlerPreferences() {
		if ((ComponentController.getRoot()).getChildCount() == 0) {
			JOptionPane.showMessageDialog(this, "Please add a node first", //$NON-NLS-1$
					"Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			return;
		}
		PreferencesDialog dialog = new PreferencesDialog(this, true);
		dialog.setVisible(true);
		scenarioPane.updateGraphicComponents();
	}

	// TODO: check
	public void cmdHandlerBuildJSimNodes() {
		// drcl.ruv.System.resetSystem();
		// drcl.ruv.System.cleanUpSystem();
		// drcl.comp.Component root = JSimTclController
		// .constructRoot(ComponentController.getRoot());
		// JSimTclController.bind_ID_RT(root);
		// JSimTclController.setJavaSimValid(true);
	}

	private void addStatusMsg(String msg) {
		lblStatus.setText(lblStatus.getText() + "  " + msg);
	}

	public int getSimStatus() {
		return simStatus;
	}

	public void setSimStatus(int status) {
		simStatus = status;
	}

	// TODO: important to be changed
	public void cmdHandlerStart() {
		// if Root is the only node, no simulation, return;
		if ((cmpController.getRootComponent()).sensorCount() == 0) {
			JOptionPane.showMessageDialog(this, "Please add a node first", //$NON-NLS-1$
					"Error", JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
			return;
		}
		// simManager = new
		// SimulationController(cmpController.getRootComponent());

	}

	public void cmdHandlerPause() {
		simulator.stop();
		// setSimStatus(PAUSE);
	}

	public void cmdHandlerStop() {
		simComponent.reboot();
		simulator.stop();
		simulator.reset();
		// setSimStatus(STOP);
	}

	private void cmdHandlerFixOnGrid() {
		if (ComponentController.getRoot() == null) {
			System.err.println("root null"); //$NON-NLS-1$
			return;
		}
		cmpController.arrangeOnGrid(EditorWindow.gridSpace);
		scenarioPane.updateGraphicComponents();
		scenarioPane.repaint();
	}

	public void cmdHandlerZoomOut() {
		if (scenarioPane.getWidth() < (panel.getWidth() * 3 / 2)
				|| scenarioPane.getHeight() < (panel.getHeight() * 3 / 2))
			return;
		EditorWindow.zoomFactor /= 2;
		scenarioPane.updateGraphicComponents();
	}

	public void cmdHandlerZoomIn() {
		EditorWindow.zoomFactor *= 2;
		scenarioPane.updateGraphicComponents();
	}

	public void cmdHandlerSaveTCL() {
		if (ComponentController.getRoot() == null) {
			System.err.println("root null"); //$NON-NLS-1$
			return;
		}
		// String tcl=JSimTclController.getRootTcl(cmpController.getRoot());
		String tcl = JSimTclController.getRootIterativeTcl(ComponentController
				.getRoot());
		try {
			// logTextArea.append(tcl);
			cmpController.saveTCL(tcl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public void setupTiming(String str) {
	// if (str == null)
	// return;
	// Vector<?>[] list = MyTableModel1.stringToVectorArray(str);
	//
	// class Timing extends drcl.DrclObj implements Runnable {
	// /**
	// *
	// */
	// private static final long serialVersionUID = -5848385713606770369L;
	// String name;
	// String action;
	//
	// public Timing(String name_, String action_) {
	// name = name_;
	// action = action_;
	// }
	//
	// @Override
	// public void run() {
	// drcl.comp.Component comp = (drcl.comp.Component) JSimTclController
	// .getNodeFromPathName(name);
	// if (comp instanceof drcl.comp.ActiveComponent)
	//					if (action.equals("Start")) //$NON-NLS-1$
	// ((drcl.comp.Component) JSimTclController
	// .getNodeFromPathName(name)).run();
	//					else if (action.equals("Stop")) //$NON-NLS-1$
	// ((drcl.comp.Component) JSimTclController
	// .getNodeFromPathName(name)).stop();
	// }
	// }
	//
	// // Set up timing in J-Sim
	// for (int i = 0; i < list[0].size(); i++) {
	// double d = ((Double) list[2].get(i)).doubleValue();
	// String name = (String) list[0].get(i), action = (String) list[1]
	// .get(i);
	//			if ((name.equals("/") || name.toLowerCase().equals("simulation")) //$NON-NLS-1$ //$NON-NLS-2$
	//					&& action.equals("Stop")) //$NON-NLS-1$
	// simulator.stopAt(d);
	// else {
	// Timing t = new Timing(name, action);
	// simulator.addRunnableAt(d, t);
	// }
	// }
	// }

	public static double getGridSpace() {
		return EditorWindow.gridSpace;
	}

	public static double getGridSpaceFactor() {
		return EditorWindow.gridSpaceFactor;
	}

	public static boolean isGridEnabled() {
		return EditorWindow.gridMode;
	}

	public double getZoomFactor() {
		return EditorWindow.zoomFactor;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	// TODO: Arman: overall process doesnt work
	public void setScenarioPaneSize(Dimension size) {
		if (scenScroll == null)
			return;

		scenScroll
				.setMaximumSize(new Dimension(size.width
						+ scenScroll.getHorizontalScrollBar()
								.getVisibleAmount(), size.height
						+ scenScroll.getVerticalScrollBar().getVisibleAmount()));
		scenScroll.revalidate();
		// scenScroll.setMinimumSize(size);
		// scenScroll.setSize(size);
	}

	public static void showWarning(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE);
	}

	public static void showWarning(Component parent, String msg) {
		JOptionPane.showMessageDialog(parent, msg, "Error", //$NON-NLS-1$
				JOptionPane.ERROR_MESSAGE);
	}
}
