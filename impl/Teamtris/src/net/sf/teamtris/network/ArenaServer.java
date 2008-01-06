package net.sf.teamtris.network;

import static net.sf.teamtris.network.protocol.ProtocolConfiguration.PORT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.sf.teamtris.arena.ServingArena;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The class that remotely serves a serving arena for network games. It is responsible for accepting
 * the game connections. 
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
		int id = 1;
		
		ServerSocket server;
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			log.fatal("Fail to initialize server socket.", e);
			return;
		}
		
		while(!Thread.interrupted()){
			try {
				Socket connection = server.accept();
				log.info("Accepted srvConn-" + id + " for '" + connection.getInetAddress().getHostAddress() + "'.");
				ArenaConnectionServer connServer = new ArenaConnectionServer(connection, servedArena, id++);
				connServer.start();
			} catch (IOException e) {
				log.fatal("Fail to accept incomming connection.", e);
				return;
			}
		}
	}
}