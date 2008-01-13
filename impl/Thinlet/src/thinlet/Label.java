package thinlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class Label extends Widget {
	
	private static final long serialVersionUID = -5154256380396512223L;

	private static final Layout layout = new InlineLayout(2, 0, 0, 0);
	{ setLayout(layout); }
	
	private boolean bold;
	
	public void setBold(boolean bold){
		this.bold = bold;
	}
	
	public Label() {
	}
	
	protected void paint(Graphics g) {
		if(bold){
			g.setFont(g.getFont().deriveFont(Font.BOLD));
		}
		g.setColor(Color.darkGray);
	}
	
	protected boolean isFocusable() { return false; }
}