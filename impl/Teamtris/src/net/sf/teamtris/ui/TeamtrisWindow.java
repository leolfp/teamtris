package net.sf.teamtris.ui;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import net.sf.teamtris.network.protocol.ProtocolConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import thinlet.Pane;
import thinlet.Parser;
import thinlet.TableLayout;

/**
 * The main window game.
 * @author Leonardo
 * @version 1.0
 * @created 06-jan-2008 23:46:10
 */
public abstract class TeamtrisWindow extends JFrame {
	private static final Log log = LogFactory.getLog(TeamtrisWindow.class);

	private static final long serialVersionUID = 9100400472845260180L;
	
	protected final Pane internalPane;
	
	public TeamtrisWindow(String subtitle, String resource, int width, int height){
		super("Teamtris v" + ProtocolConfiguration.VERSION + (subtitle != null ? " - " + subtitle : ""));
		internalPane = new Pane();
		// Remove the external borders
		internalPane.setLayout(new TableLayout(4,4,0,0,0,0));
		try {
			new Parser(internalPane, resource);
		} catch (Exception e) {
			log.fatal("Fail reading window serialization.", e);
		}

		setIconImage(new ImageIcon(TeamtrisWindow.class.getResource("Icon.png"), "Teamtris").getImage());

		JComponent component = (JComponent) internalPane.getComponent();
		component.setDoubleBuffered(true);
		this.setContentPane(component);
		this.setSize(width, height);
		this.setResizable(false);
	}
}
