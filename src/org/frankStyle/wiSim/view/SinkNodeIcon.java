package org.frankStyle.wiSim.view;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

import org.frankStyle.wiSim.WSNeditor;
import org.frankStyle.wiSim.model.ComponentSinkNode;


/**
 * @author Arman
 * 
 */
public class SinkNodeIcon extends NodeIconAbstract {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8824693808324164925L;

	// Corresponding to the Model element
	ComponentSinkNode m_componentNode;

	boolean m_bDefault = false;
	static String strFileSeparator = System.getProperty("file.separator");

	private int type;
	private  double radius;

	public SinkNodeIcon(ComponentSinkNode sink) {
		super();
		try {
		m_componentNode = sink;

		// setIcon(DrclConfiguration.getIcon(this, strClass, false));
		// ImageIcon icon = new ImageIcon(WSNeditor.ImageDirPrefix +
		// "sink.png");
			ImageIcon icon = new ImageIcon(WSNeditor.class.getResource("/sink.png"));

		setIcon(icon);
		radius = m_componentNode.getRadius();

		setToolTipText(m_componentNode.toLongString());
		// updateLocationFromComp();
		setToolTipText(m_componentNode.toLongString());
		setSize(getPreferredSize());
		} catch (Exception ex) {
			WSNeditor.handleLog(ex.getMessage());
		}

	}


	public ComponentSinkNode getComponentNode() {
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

//		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(
//				"sink update location should not be used");
		int nPosX = Double.valueOf(
				m_componentNode.getLocation().getX() * EditorWindow.zoomFactor)
				.intValue();
		int nPosY = Double.valueOf(
				m_componentNode.getLocation().getY() * EditorWindow.zoomFactor)
				.intValue();
		setLocation(nPosX, nPosY);
	}

}
