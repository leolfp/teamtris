package net.sf.teamtris.network;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.teamtris.arena.Arena;
import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.ServingArena;

/**
 * The entry point for the network subsystem.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:08
 */
public class NetworkManager {
	private static final Log log = LogFactory.getLog(NetworkManager.class);
	
	private static final AtomicInteger connId = new AtomicInteger(0);

	/**
	 * Connects to a remote arena pointed by the server hostname.
	 * @param hostname The server hostname.
	 * @return A proxied arena in behalf of the remote connected arena.
	 */
	public static Arena connectToArena(String hostname, Player player, ArenaObserver arenaObserver){
		try {
			return new RemoteArena(hostname, player, arenaObserver, "cliConn-" + connId.incrementAndGet());
		} catch (UnknownHostException e) {
			log.fatal("Fail to connect to remote arena on " + hostname + ".", e);
		} catch (IOException e) {
			log.fatal("Fail to connect to remote arena on " + hostname + ".", e);
		}
		return null;
	}

	/**
	 * Starts serving the given arena over network.
	 * @param arena The arena to serve.
	 */
	public static void serveArena(ServingArena arena){
		ArenaServer server = new ArenaServer(arena);
		server.start();
		// TODO How to stop serving the arena?
	}
}