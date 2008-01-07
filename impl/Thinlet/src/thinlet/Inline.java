package thinlet;

import java.awt.Color;
import java.awt.Graphics;

public class Inline extends Widget {

	private static final long serialVersionUID = 5333394558971970241L;

	private static final Layout layout = new InlineLayout(0, 15, 0, 0);
	{ setLayout(layout); }

	public Inline(){
	}

	protected void paint(Graphics g) {
		g.setColor(Color.gray);
	}

}
