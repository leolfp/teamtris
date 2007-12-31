package net.sf.teamtris.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.teamtris.arena.ServingArena;

/**
 * The class that remotely serves a serving arena for network games. 
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:01
 */
public class ArenaServer extends Thread {
	private static final Log log = LogFactory.getLog(ArenaServer.class);
	
	private final ServingArena servedArena;	

	/**
	 * The default constructor for an arena server.
	 * @param servedArena The arena to be served on the network.
	 */
	public ArenaServer(ServingArena servedArena) {
		super("ArenaServer");
		this.servedArena = servedArena;
	}

	@Override
	public void run() {
		int id = 0;
		
		ServerSocket server;
		try {
			server = new ServerSocket(RemoteArena.REMOTE_PORT);
		} catch (IOException e) {
			log.fatal("Fail to initialize server socket.", e);
			return;
		}
		
		while(!Thread.interrupted()){
			try {
				Socket connection = server.accept();
				log.info("Accepted connection for '" + connection.getInetAddress() + "'.");
				ArenaConnectionServer connServer = new ArenaConnectionServer(connection, servedArena, id++);
				connServer.start();
			} catch (IOException e) {
				log.fatal("Fail to accept incomming connection.", e);
				return;
			}
		}
	}
}