package net.sf.teamtris.ui.main;

import net.sf.teamtris.ui.TeamtrisWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import thinlet.Anchor;
import thinlet.Listener;

/**
 * The main window game.
 * @author Leonardo
 * @version 1.0
 * @created 06-jan-2008 15:31:49
 */
public class MainWindow extends TeamtrisWindow {
	private static final Log log = LogFactory.getLog(MainWindow.class);
	
	private static final long serialVersionUID = -1084862959456009478L;
	
	public MainWindow(){
		super(null, "net/sf/teamtris/ui/main/MainWindow.xml", 275, 600);
		((Anchor) internalPane.findByName("local")).addActionListener(new Listener(this, "localClicked"));
		((Anchor) internalPane.findByName("remote")).addActionListener(new Listener(this, "remoteClicked"));
		((Anchor) internalPane.findByName("serve")).addActionListener(new Listener(this, "serveClicked"));
		((Anchor) internalPane.findByName("options")).addActionListener(new Listener(this, "optionsClicked"));
		((Anchor) internalPane.findByName("about")).addActionListener(new Listener(this, "aboutClicked"));
		((Anchor) internalPane.findByName("exit")).addActionListener(new Listener(this, "exitClicked"));
	}
	
	public void localClicked(){
		log.debug("Local clicked.");
	}

	public void remoteClicked(){
		log.debug("Remote clicked.");
	}

	public void serveClicked(){
		log.debug("Serve clicked.");
	}

	public void optionsClicked(){
		log.debug("Options clicked.");
	}

	public void aboutClicked(){
		log.debug("About clicked.");
	}

	public void exitClicked(){
		log.debug("Exit clicked.");
		this.setVisible(false);
		System.exit(0);
	}

}
