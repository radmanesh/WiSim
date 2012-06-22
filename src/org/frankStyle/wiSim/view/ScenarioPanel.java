// $codepro.audit.disable equalityTestWithBooleanLiteral
package org.frankStyle.wiSim.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;

import org.frankStyle.wiSim.controller.ComponentController;
import org.frankStyle.wiSim.controller.JSimTclController;
import org.frankStyle.wiSim.model.ComponentSensor;
import org.frankStyle.wiSim.model.ComponentSinkNode;
import org.frankStyle.wiSim.model.ComponentTargetNode;
import org.w3c.dom.DOMException;


/**
 * @author Arman
 * 
 */
// TODO:
// 1: enable deleting a node
// 2: limit zooming or enable dyanamic grid space
// 3: radius doesn't change after zoom
//
// when using setPreferedSized for scrolling it won't disable pixels out of env
// range
// setTransmissionRange for nodes >> done for sensor and sink in wphy

public class ScenarioPanel extends JPanel implements MouseInputListener,
		ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7023010180726442387L;
	// Frame
	private final EditorWindow m_parent;
	// ComponentController contains all information on xml doc
	private final ComponentController compController;

	private final static float dash1[] = { 10.0f };
	private final static BasicStroke dashed = new BasicStroke(1.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
			ScenarioPanel.dash1, 0.0f);
	private final static int minGridSpacingPxl = 15;
	private final static int fixingRadiusPxl = 5;

	private NodeIconAbstract selectedComp = null;

	private Dimension scenarioSize;

	// Mouse pointer mode
	private int m_mode;
	static final int ARROW = 0;
	static final int ADD = 1;
	static final int ADD_SENSOR = 7;
	static final int ADD_TARGET = 8;
	static final int ADD_PLOT = 9;
	static final int DELETE = 2;

	private NodeIconAbstract m_compMoving;
	private boolean compFixed = false;
	private Point compMovingRealPos;
	private Point pointCompOff;

	private int lastID = 0;

	// Whether we have a moving node
	private boolean m_bCompMoving = false;


	// Grid Settings
	private final Color gridColor = Color.BLUE;

	// private Object lock = new Object();

	public ScenarioPanel(EditorWindow editor) {
		setBackground(UIManager.getColor("EditorPane.background"));
		// setBackground(Color.MAGENTA);
		setOpaque(true);
		m_parent = editor;
		m_mode = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
		compController = m_parent.getComponentController();
		compController.setDirty(false);

		setLayout(null);

		updateGraphicComponents();

		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}


	public void setMode(int mode) {
		m_mode = mode;
	}

	void updateNodeLocations() {
		Component[] comps = getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (NodeIconAbstract.class.isInstance(comps[i])) {
				((NodeIconAbstract) comps[i]).updateLocationFromComp();
			}

		}
		updatePaneSize();
		invalidate();
		repaint();
	}

	// TODO: Arman: GlyphNodes should handle location updates not like this!
	void updateGraphicComponents() {
		updatePaneSize();
		removeAll();

		// Handling Sensors
		ListIterator<?> sensorList = ComponentController.getRoot()
				.sensorsIterator();
		while (sensorList.hasNext()) {
			ComponentSensor sensor = (ComponentSensor) sensorList.next();
			// rootComponent.addSensorNode(sensor);
			SensorNodeIcon gNode = new SensorNodeIcon(sensor);
			gNode.addMouseListener(this);
			gNode.addMouseMotionListener(this);
			add(gNode);
			try {
				int nPosX = Double.valueOf(
						sensor.getLocation().getX() * EditorWindow.zoomFactor)
						.intValue();
				int nPosY = Double.valueOf(
						sensor.getLocation().getY() * EditorWindow.zoomFactor)
						.intValue();
				gNode.setLocation(nPosX, nPosY);
			} catch (DOMException e) {
				java.lang.System.out
						.println("DOMException at ScenarioPanel:updateGraphicComponents for sensor icon position with id="
								+ sensor.getId());
			}
		}

		// Handle Targets
		ListIterator<ComponentTargetNode> targetList = ComponentController
				.getRoot().targetsIterator();
		while (targetList.hasNext()) {
			ComponentTargetNode target = targetList.next();
			// rootComponent.addSensorNode(sensor);
			TargetNodeIcon gNode = new TargetNodeIcon(target);
			gNode.addMouseListener(this);
			gNode.addMouseMotionListener(this);
			add(gNode);
			try {
				int nPosX = Double.valueOf(
						target.getLocation().getX() * EditorWindow.zoomFactor)
						.intValue();
				int nPosY = Double.valueOf(
						target.getLocation().getY() * EditorWindow.zoomFactor)
						.intValue();
				gNode.setLocation(nPosX, nPosY);
			} catch (DOMException e) {
				java.lang.System.out
						.println("DOMException at ScenarioPanel:updateGraphicComponents for target icon position named: "
								+ target.getName());
			}
		}

		ComponentSinkNode sink = ComponentController.getRoot().getSinkNode();
		SinkNodeIcon gNode = new SinkNodeIcon(sink);
		gNode.addMouseListener(this);
		gNode.addMouseMotionListener(this);
		add(gNode);
		try {
			int nPosX = Double.valueOf(
					sink.getLocation().getX() * EditorWindow.zoomFactor)
					.intValue();
			int nPosY = Double.valueOf(
					sink.getLocation().getY() * EditorWindow.zoomFactor)
					.intValue();
			gNode.setLocation(nPosX, nPosY);
		} catch (DOMException e) {
			java.lang.System.out
					.println("DOMException at ScenarioPanel:updateGraphicComponents for sink icon position");
		}
		m_parent.setScenarioPaneSize(scenarioSize);
		repaint();
	}

	public void updatePaneSize() {
		scenarioSize = new Dimension(
				(int) (ComponentController.getRoot().getEnvironmentDimension().width * m_parent
						.getZoomFactor()),
				(int) (ComponentController.getRoot().getEnvironmentDimension().height * m_parent
						.getZoomFactor()));
		setSize(scenarioSize);
		setMinimumSize(scenarioSize);
		setPreferredSize(scenarioSize);
		setMaximumSize(scenarioSize);
		invalidate();
	}

	// only used when node components location should change
	public void updateComponentNodeLocation(java.awt.Component comp) {
		// System.out.println("update");

		if (SensorNodeIcon.class.isInstance(comp)) {
			SensorNodeIcon gnode = (SensorNodeIcon) comp;

			Point loc = new Point();
			loc.setLocation(gnode.getLocation().x / EditorWindow.zoomFactor,
					gnode.getLocation().y / EditorWindow.zoomFactor);
			gnode.getComponentNode().setLocation(loc);
		} else if (TargetNodeIcon.class.isInstance(comp)) {
			TargetNodeIcon gnode = (TargetNodeIcon) comp;
			Point loc = new Point();
			loc.setLocation(gnode.getLocation().x / EditorWindow.zoomFactor,
					gnode.getLocation().y / EditorWindow.zoomFactor);
			gnode.getComponentNode().setLocation(loc);
		} else if (SinkNodeIcon.class.isInstance(comp)) {
			System.err.print("only 0,0 is supported for Sink in J-Sim\n");
		} else {
			System.out.println("ComponentPane::updateNodePosition()"
					+ " Unknown input comp" + comp);
		}
		// updateGraphicComponents();
	}

	private int clickCount = 0;
	private long lastTimeClicked = 0;

	// MouseInputListener functions
	@Override
	// TODO: DONT build model object!!!!!!!!!!!!!!!!!
	public synchronized void mouseClicked(MouseEvent e) {
		if (selectedComp != null) {
			selectedComp = null;
		}
		if (isArrow()) {
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
				if (e.getSource() instanceof NodeIconAbstract) {
					selectedComp = ((NodeIconAbstract) e.getSource());
					if (e.getClickCount() == 1) {
						long now_ = System.currentTimeMillis();
						if (now_ - lastTimeClicked > 255) {
							clickCount = 1;
							lastTimeClicked = now_;
							repaint();
						} else
							clickCount++;
					}
					if (e.getClickCount() == 2 || clickCount >= 2) {
						clickCount = 0;
						if (e.getSource() instanceof SensorNodeIcon) {
							SensorNodeIcon clickedNode = (SensorNodeIcon) e
									.getSource();
							ComponentSensor sNode = clickedNode
									.getComponentNode();
							SensorEditDialog edit = new SensorEditDialog(sNode,
									m_parent, "");
							edit.setVisible(true);
							edit.setEnabled(true);
						} else if (e.getSource() instanceof TargetNodeIcon) {
							TargetNodeIcon clickedNode = (TargetNodeIcon) e
									.getSource();
							ComponentTargetNode targetNode = clickedNode
									.getComponentNode();
							TargetEditDialog edit = new TargetEditDialog(
									targetNode, m_parent, "");
							edit.setVisible(true);
							edit.setEnabled(true);
						} else if (e.getSource() instanceof SinkNodeIcon) {
							SinkNodeIcon clickedNode = (SinkNodeIcon) e.getSource();
							ComponentSinkNode sinkNode = clickedNode
									.getComponentNode();
							SinkEditDialog edit = new SinkEditDialog(sinkNode,
									m_parent, "");
							edit.setVisible(true);
							edit.setEnabled(true);
						}else return;
						
						updateGraphicComponents();
					}
				}
		} else if (isAddSensor()) {
			if ((e.getModifiers() == InputEvent.BUTTON1_MASK)
					&& e.getSource() == this) {
				addSensorNode(e.getPoint());
			}
		} else if (getMode() == ScenarioPanel.ADD_TARGET) {
			if ((e.getModifiers() == InputEvent.BUTTON1_MASK)
					&& e.getSource() == this) {
				addTargetNode(e.getPoint());
			}
		}

	}

	public boolean isAddSensor() {
		return (getMode() == ScenarioPanel.ADD_SENSOR);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (isArrow()) {
			// Now we are in arrow state
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
				// Now left button is clicked.
				if (e.getSource() == this) {
					// Mouse click does not hit a component.
					m_bCompMoving = false;
					selectedComp = null;
					return;
				}

				if (e.getSource() instanceof NodeIconAbstract) {
					// Hit a node or a port.
					NodeIconAbstract gcomp = (NodeIconAbstract) e.getSource();
					if (gcomp == null || SinkNodeIcon.class.isInstance(gcomp))
						return;
					remove(gcomp);
					add(gcomp, 0);
					m_compMoving = gcomp;
					compMovingRealPos = new Point(m_compMoving.getLocation());

					m_bCompMoving = true;
					pointCompOff = new Point(compMovingRealPos.x
							- e.getLocationOnScreen().x, compMovingRealPos.y
							- e.getLocationOnScreen().y);
					// selectedComp = gcomp;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isArrow()) {
			if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
				if (m_bCompMoving == true) { // $codepro.audit.disable
												// equalityTestWithBooleanLiteral
					updateComponentNodeLocation(m_compMoving);
					selectedComp = m_compMoving;

					compController.setDirty(true);
				}

			} else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
				// mouse right clicked
				if (e.getSource() != this) {
					// click happened on a component
					// JPopupMenu menuPop = createPopMenu(e.getSource());
					// m_componentPop = (gComponent) e.getSource();
					// menuPop.show(m_componentPop, e.getX(), e.getY());
				}
			}
		}
		m_bCompMoving = false;
		compFixed = false;
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		if (isArrow()) {
			Point pMouse = e.getLocationOnScreen();

			if (m_bCompMoving) {
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

				Point newLoc = new Point(pMouse.x + pointCompOff.x, pMouse.y
						+ pointCompOff.y);

				if (newLoc.x <= 0 || newLoc.y <= 0)
					return;

				int fixingSpacePxl = Double.valueOf(
						EditorWindow.getGridSpace()
								* EditorWindow.getGridSpaceFactor()
								* EditorWindow.zoomFactor).intValue();
				int fixOffsetX = newLoc.x
						- ((((newLoc.x + ScenarioPanel.fixingRadiusPxl) / fixingSpacePxl)) * fixingSpacePxl);
				int fixOffsetY = newLoc.y
						- ((((newLoc.y + ScenarioPanel.fixingRadiusPxl) / fixingSpacePxl)) * fixingSpacePxl);

				if (fixOffsetX < ScenarioPanel.fixingRadiusPxl
						&& fixOffsetY < ScenarioPanel.fixingRadiusPxl) {
					if (!compFixed) {
						newLoc.setLocation(new Point(newLoc.x - fixOffsetX,
								newLoc.y - fixOffsetY));
						compFixed = true;
						m_compMoving.setLocation(newLoc);
					}
				} else {
					compFixed = false;
					m_compMoving.setLocation(newLoc);
				}

				compMovingRealPos.setLocation(new Point(pMouse.x
						+ pointCompOff.x, pMouse.y + pointCompOff.y));

				m_compMoving.repaint();
				invalidate();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getSource() instanceof SensorNodeIcon) {

		}
	}

	public int getMode() {
		return (m_mode);
	}

	public boolean isArrow() {
		return (m_mode == ScenarioPanel.ARROW);
	}

	public boolean isAdd() {
		return (m_mode == ScenarioPanel.ADD);
	}

	public boolean isDelete() {
		return (m_mode == ScenarioPanel.DELETE);
	}

	public void addSensorNode(Point point) {

		// TODO: what the fuck are therese? workFlow shouldnt make dcrlComponent
		// instances
		if (point.x >= scenarioSize.width || point.y >= scenarioSize.height)
			return;
		Point modelLoc = new Point((int) (point.x / EditorWindow.zoomFactor),
				(int) (point.y / EditorWindow.zoomFactor));
		String sensorName = "sensor"
				+ ComponentController.getRoot().sensorCount();
		ComponentSensor sen = new ComponentSensor(sensorName,
				"wsnGUI.v2.SensorNode", modelLoc);
		sen.setId(++lastID);

		compController.addSensor(sen);
		SensorNodeIcon gSen = new SensorNodeIcon(sen);
		gSen.addMouseListener(this);
		gSen.addMouseMotionListener(this);
		add(gSen, 0);
		gSen.setLocation(point.x, point.y);
		// selectedComp=gSen;

		compController.setDirty(true);
		JSimTclController.setJavaSimValid(false);
	}

	public void addTargetNode(Point point) {

		if (point.x >= scenarioSize.width || point.y >= scenarioSize.height)
			return;
		Point modelLoc = new Point((int) (point.x / EditorWindow.zoomFactor),
				(int) (point.y / EditorWindow.zoomFactor));
		String targetName = "target"
				+ ComponentController.getRoot().targetCount();
		ComponentTargetNode tar = new ComponentTargetNode(targetName,
				"wsnGUI.v2.TargetNode", modelLoc);
		tar.setId(++lastID);
		compController.addTarget(tar);
		TargetNodeIcon gTarget = new TargetNodeIcon(tar);
		gTarget.addMouseListener(this);
		gTarget.addMouseMotionListener(this);
		add(gTarget, 0);
		gTarget.setLocation(point.x, point.y);
		// selectedComp=gTarget;

		compController.setDirty(true);
		JSimTclController.setJavaSimValid(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// drawing radius
		if (selectedComp != null) {
			selectedComp.updateLocationFromComp();
			g2.setColor(Color.PINK);
			int rad = 0;
			Point po = selectedComp.getLocation();
			Point p = new Point(((int) (po.x * m_parent.getZoomFactor())),
					(int) (po.y * m_parent.getZoomFactor()));
			rad = (int) (selectedComp.getRadius() * m_parent.getZoomFactor());
			g2.drawOval(p.x - rad, p.y - rad, rad * 2, rad * 2);
		}

		Stroke defaultStroke = g2.getStroke();
		// // drawing grid
		if (EditorWindow.isGridEnabled()) {
			int spacingPixel = Double.valueOf(
					EditorWindow.getGridSpace()
							* EditorWindow.getGridSpaceFactor()
							* EditorWindow.zoomFactor).intValue();

			if (spacingPixel < ScenarioPanel.minGridSpacingPxl)
				spacingPixel = ScenarioPanel.minGridSpacingPxl;
			g2.setColor(gridColor);
			g2.setStroke(ScenarioPanel.dashed);
			// Horizontal
			for (int i = spacingPixel; i < this.getWidth(); i += spacingPixel) {
				g2.draw(new Line2D.Double(i, 0, i, this.getHeight()));
			}
			// Vertical
			for (int i = spacingPixel; i < this.getHeight(); i += spacingPixel) {
				g2.draw(new Line2D.Double(0, i, this.getWidth(), i));
			}
		}
		g2.setStroke(defaultStroke);
		invalidate();
	}

	public void removeSelectedNode() {
		if (selectedComp == null)
			return;

		if (selectedComp instanceof SensorNodeIcon) {
			compController.removeSensor(((SensorNodeIcon) selectedComp)
					.getComponentNode());
			remove(selectedComp);
			selectedComp = null;
		} else if (selectedComp instanceof TargetNodeIcon) {
			compController.removeTarget(((TargetNodeIcon) selectedComp)
					.getComponentNode());
			remove(selectedComp);
			selectedComp = null;
		}
		repaint();
		validate();
	}

	public Dimension getScenarioSize() {
		return scenarioSize;
	}

}
