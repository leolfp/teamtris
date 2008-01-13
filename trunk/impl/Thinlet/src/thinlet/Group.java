package thinlet;

public class Group extends Widget {
	private static final long serialVersionUID = 657404637330226255L;

	private static final Layout layout = new TableLayout(4, 4, 0, 0, 0, 0);
	{ setLayout(layout); }

	public Group() {
	}

	protected boolean isFocusable() { return false; }
}
