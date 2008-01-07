package thinlet;

import java.awt.*;

public class Separator extends Widget {
	
	private static final long serialVersionUID = 4052967915798568280L;

	public Separator() {
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(0, 2);
	}
	
	protected void paint(Graphics g) {
		g.setColor(Color.lightGray);
		g.drawLine(0, 0, getWidth() - 1, 0);
		g.setColor(Color.white);
		g.drawLine(0, 1, getWidth() - 1, 1);
	}
}
