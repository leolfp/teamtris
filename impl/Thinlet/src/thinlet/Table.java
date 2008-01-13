package thinlet;

public class Table extends ScrollPanel {
	
	private static final long serialVersionUID = 8128528475958311641L;

	public Table() {
	}
	
	@Override
	public Widget add(Widget widget) {
		if(widget instanceof Head){
			return getHeader().add(widget);
		} else {
			return getContent().add(widget);
		}
	}
	
	@Override
	public void append(Widget widget) {
		if(widget instanceof Head){
			getHeader().append(widget);
		} else {
			getContent().append(widget);
		}
	}

	public void preppendChild(Row row) {
		getContent().preppendChild(row);
		refresh();
	}

	public void appendChild(Row row) {
		add(row);
		refresh();
	}

	public void removeChild(Row row) {
		row.remove();
		refresh();
	}
	
	private int width;
	private int height;
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public Metrics getPreferredSize(int preferredWidth) {
		return new Metrics(width, height);
	}
	
	protected Metrics layoutContent(int preferredWidth) {
		int ncol = 0, nrow = 0;
		
		for (Widget widget = getContent().getChild(); widget != null; widget = widget.getNext()) {
			if (widget instanceof Row) {
				int cols = 0;
				for (Widget item = widget.getChild(); item != null; item = item.getNext()) {
					cols++;
				}
				ncol = Math.max(ncol, cols);
			}
			nrow++;
		}
		for (Widget widget = getHeader().getChild(); widget != null; widget = widget.getNext()) {
			if (widget instanceof Head) {
				int cols = 0;
				for (Widget item = widget.getChild(); item != null; item = item.getNext()) {
					cols++;
				}
				ncol = Math.max(ncol, cols);
			}
		}
		
		int[] widths = new int[ncol];
		int i = 0;
		for (Widget widget = getHeader().getChild(); widget != null; widget = widget.getNext()) {
			if (widget instanceof Head) {
				widths[i] = widget.getWidth();
				++i;
			}
		}
		for(;i < widths.length; ++i){
			widths[i] = 80;
		}
		
		int x = 0, y = 0;
		for (Widget widget = getContent().getChild(); widget != null; widget = widget.getNext()) {
			widget.setBounds(0, 18 * y, getWidth(), 18); y++;
			if (widget instanceof Row) {
				x = 0;
				int scale = 0;
				for (Widget item = widget.getChild(); item != null; item = item.getNext()) {
					item.setBounds(scale, 0, widths[x], 18);
					scale += widths[x];
					x++;
				}
			}
		}
		return new Metrics(getWidth(), nrow * 18);
	}

}
