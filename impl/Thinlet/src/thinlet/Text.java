package thinlet;

import java.awt.*;

public class Text extends Widget {
	
	private static final long serialVersionUID = -5723064536667310477L;

	private String text;
	
	public Text(String text) {
		this.text = text;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public void setText(String text){
		this.text = text;
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		FontMetrics fm = RootPane.getFontMetrics();
		return new Metrics(fm.stringWidth(text),
			fm.getAscent() + fm.getDescent(), fm.getAscent());
	}
	
	protected Widget getWidgetAt(int mx, int my) { return null; }
	protected boolean isFocusable() { return false; }
	
	protected void paint(Graphics g) {
		g.drawString(text, 0, g.getFontMetrics().getAscent());
	}
	

		

		



		




}
