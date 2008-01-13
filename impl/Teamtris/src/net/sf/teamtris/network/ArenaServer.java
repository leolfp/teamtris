package net.sf.teamtris.network;

import static net.sf.teamtris.network.protocol.ProtocolConfiguration.PORT;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private ServerSocket server;
	private List<ArenaConnectionServer> arenas = new ArrayList<ArenaConnectionServer>();
	private volatile boolean closing;

	/**
	 * The default constructor for an arena server.
	 * @param servedArena The arena to be served on the network.
	 */
	public ArenaServer(ServingArena servedArena) {
		super("ArenaServer");
		this.servedArena = servedArena;
	}

	/**
	 * Stops the current serving thread and closes every open socket on this server.
	 */
	public void stopAndClose() {
		this.closing = true;
		this.interrupt();
		try {
			server.close();
			log.info("Stopped serving game.");
		} catch (IOException e) {
			log.error("Fail to close server socket.", e);
		}
		synchronized (arenas) {
			Iterator<ArenaConnectionServer> arenasIterator = arenas.iterator();
			while(arenasIterator.hasNext()){
				ArenaConnectionServer server = arenasIterator.next();
				server.close();
				arenasIterator.remove();
			}
		}
	}

	@Override
	public void run() {
		int id = 1;
		
		try {
			server = new ServerSocket(PORT);
			log.info("Started serving game.");
		} catch (IOException e) {
			log.fatal("Fail to initialize server socket.", e);
			return;
		}
		
		while(!interrupted()){
			try {
				Socket connection = server.accept();
				log.info("Accepted srvConn-" + id + " for '" + connection.getInetAddress().getHostAddress() + "'.");
				ArenaConnectionServer connServer = new ArenaConnectionServer(connection, servedArena, id++);
				synchronized (arenas) {
					arenas.add(connServer);
				}
				connServer.start();
			} catch (IOException e) {
				if(!closing){
					log.fatal("Fail to accept incomming connection.", e);
				}
				return;
			}
		}
	}
}