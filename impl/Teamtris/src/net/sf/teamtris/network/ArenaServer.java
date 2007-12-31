package net.sf.teamtris.network;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.teamtris.arena.ServingArena;

/**
 * The class that remotely serves a serving arena for network games. 
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:01
 */
public class ArenaServer implements Runnable {
	private static final Log log = LogFactory.getLog(ArenaServer.class);
	
	private final ServingArena servedArena;	

	/**
	 * The default constructor for an arena server.
	 * @param servedArena The arena to be served on the network.
	 */
	public ArenaServer(ServingArena servedArena) {
		this.servedArena = servedArena;
	}

	@Override
	public void run() {
		ServerSocket server;
		try {
			server = new ServerSocket(RemoteArena.REMOTE_PORT);
		} catch (IOException e) {
			log.fatal("Fail to initialize server socket.", e);
			return;
		}
		
		
	}
}