package thinlet;

import java.awt.*;

public class Head extends Widget {
	
	private static final long serialVersionUID = -3088197239060985892L;

	public Head() {
	}
	
	public Head(String text) {
		this();
		add(new Text(text));
	}
	
	private int width = 80;
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	protected void paint(Graphics g) {
		g.setColor(new Color(0x91a0c0));
		g.drawLine(0, 0, getWidth() - 1, 0);
		((Graphics2D) g).setPaint(new GradientPaint(0, 1, new Color(0xa1b0cf),
			0, getHeight() - 1, new Color(0x7185ab)));
		g.fillRect(0, 1, getWidth(), getHeight() - 1);
		g.setColor(Color.white);
	}
}
