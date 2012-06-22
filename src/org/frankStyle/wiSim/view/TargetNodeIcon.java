package org.frankStyle.wiSim.view;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

import org.frankStyle.wiSim.WSNeditor;
import org.frankStyle.wiSim.model.ComponentTargetNode;


/**
 * @author Arman
 * 
 */
public class TargetNodeIcon extends NodeIconAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4417615574594704267L;

	// Corresponding to the Model element
	ComponentTargetNode m_componentNode;

	boolean m_bDefault = false;
	static String strFileSeparator = System.getProperty("file.separator");

	private int type;
	private double radius;

	public TargetNodeIcon(ComponentTargetNode target) {
		super();
		m_componentNode = target;
		// TODO set wireless icon
		// setIcon(DrclConfiguration.getIcon(this, strClass, false));
		// ImageIcon icon = new ImageIcon(WSNeditor.ImageDirPrefix +
		// "target.png");
		ImageIcon icon = new ImageIcon(WSNeditor.class.getResource("/target.png"));
		setIcon(icon);
		radius = m_componentNode.getRadius();

		// setText(m_componentNode.getName());
		// setVerticalTextPosition(JLabel.TOP);
		// setHorizontalTextPosition(JLabel.CENTER);
		setToolTipText(m_componentNode.toLongString());
		setSize(getPreferredSize());
	}

	public ComponentTargetNode getComponentNode() {
		return m_componentNode;
	}

	@Override
	public String getEmbededClass() {
		return m_componentNode.getClassName();
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
		super.setLocation(nPosX - getPreferredSize().width / 2, nPosY
				- getPreferredSize().height / 2);
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
	public void paint(Graphics g_) {
		Point location_ = getLocation(); // new location
		location_.translate(getSize().width / 2 - getPreferredSize().width / 2,
				getSize().height / 2 - getPreferredSize().height / 2);
		setLocation(location_);
		setSize(getPreferredSize()); // update size
		super.paint(g_);
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
