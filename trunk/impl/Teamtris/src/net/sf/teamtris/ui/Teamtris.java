package net.sf.teamtris.ui;

import javax.swing.WindowConstants;

import net.sf.teamtris.ui.main.MainWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The startup class for teamtris.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 15:31:49
 */
public class Teamtris {
	private static final Log log = LogFactory.getLog(Teamtris.class);
	
	public static void main(String[] args) {
		log.debug("Starting Teamtris...");
		MainWindow mainWindow = new MainWindow();
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
}