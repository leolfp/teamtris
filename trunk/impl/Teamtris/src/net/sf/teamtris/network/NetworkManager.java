package net.sf.teamtris.network;
import net.sf.teamtris.arena.Arena;
import net.sf.teamtris.arena.ServingArena;

/**
 * The entry point for the network subsystem.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:08
 */
public class NetworkManager {

	/**
	 * Connects to a remote arena pointed by the server hostname.
	 * @param hostname The server hostname.
	 * @return A proxied arena in behalf of the remote connected arena.
	 */
	public static Arena connectToArena(String hostname){
		// TODO Implement this!
		return null;
	}

	/**
	 * Starts serving the given arena over network.
	 * @param arena The arena to serve.
	 */
	public static void serveArena(ServingArena arena){
		// TODO Implement this!
	}
}