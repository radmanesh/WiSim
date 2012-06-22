/**
 * @author Arman
 * 
 */
package org.frankStyle.wiSim.view;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

import org.frankStyle.wiSim.WSNeditor;
import org.frankStyle.wiSim.model.ComponentSensor;


/**
 * @author Arman
 * 
 */
public class SensorNodeIcon extends NodeIconAbstract {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6416775601065622760L;

	// Corresponding to the Model element
	ComponentSensor m_componentNode;

	boolean m_bDefault = false;
	static String strFileSeparator = System.getProperty("file.separator");

	final static int SENSOR = 1;
	private int type;
	private double radius;

	public SensorNodeIcon(ComponentSensor sensor) {
		super();
		m_componentNode = sensor;

		// TODO set wireless icon
		// setIcon(DrclConfiguration.getIcon(this, strClass, false));
		ImageIcon icon = new ImageIcon(WSNeditor.class.getResource("/sensor.png"));
		setIcon(icon);
		radius = m_componentNode.getRadius();
		setToolTipText(m_componentNode.toLongString());
		setSize(getPreferredSize());

		// setText(m_componentNode.getName());
		// setVerticalTextPosition(JLabel.CENTER);
		// setHorizontalTextPosition(JLabel.CENTER);
	}

	public ComponentSensor getComponentNode() {
		return m_componentNode;
	}

	@Override
	public String getEmbededClass() {
		return m_componentNode.getClassName();
	}

	@Override
	public void paint(Graphics g_) {
		// Tyan: relocate to previous location in case icon image is changed
		Point location_ = getLocation(); // new location
		// infer the previous location from size
		// note: dont do divide by 2 after substraction, the slight difference
		// may cause gNode and gLine disconnected as it relies on gNode location
		location_.translate(getSize().width / 2 - getPreferredSize().width / 2,
				getSize().height / 2 - getPreferredSize().height / 2);
		setLocation(location_);
		setSize(getPreferredSize()); // update size
		super.paint(g_);
	}

	@Override
	public void setLocation(Point p) {
		super.setLocation(p.x - getPreferredSize().width / 2, p.y
				- getPreferredSize().height / 2);
	}

	@Override
	public void setLocation(int nPosX, int nPosY) {
		// super.setLocation(nPosX-preferredWidth/2,
		// nPosY-preferredHeight/2);
		super.setLocation(nPosX - (getPreferredSize().width) / 2, nPosY
				- (getPreferredSize().height) / 2);
		// orig: super.setLocation(nPosX - getPreferredSize().width / 2, nPosY -
		// getPreferredSize().height / 2);
	}

	@Override
	public Point getLocation() {
		Point p = super.getLocation();
		// p.translate(preferredWidth/2, preferredHeight/2);
		p.translate(getPreferredSize().width / 2, getPreferredSize().height / 2);
		return p;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

	@Override
	public void debug() {
		String s = new String();
		s += "\ngNode\n" + m_componentNode.toLongString();
		System.out.println(s);
	}

	@Override
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public double getRadius() {
		return radius;
	}

	@Override
	public void updateLocationFromComp() {
		radius = m_componentNode.getRadius();
		setToolTipText(m_componentNode.toLongString());
		setSize(getPreferredSize());

		int nPosX = Double.valueOf(
				m_componentNode.getLocation().getX() * EditorWindow.zoomFactor)
				.intValue();
		int nPosY = Double.valueOf(
				m_componentNode.getLocation().getY() * EditorWindow.zoomFactor)
				.intValue();
		setLocation(nPosX, nPosY);

	}

}
