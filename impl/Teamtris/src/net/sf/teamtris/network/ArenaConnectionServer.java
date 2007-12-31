package net.sf.teamtris.network;

import java.net.Socket;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.ServingArena;
import net.sf.teamtris.arena.Status;
import net.sf.teamtris.network.protocol.Message;
import net.sf.teamtris.network.protocol.MessageType;
import net.sf.teamtris.network.protocol.ProtocolException;
import net.sf.teamtris.network.protocol.ServerMessageFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The class that remotely serves a specific game connection.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 16:06:41
 */
public class ArenaConnectionServer extends Thread {
	private static final Log log = LogFactory.getLog(ArenaConnectionServer.class);
	
	private final Socket connection;
	private final ServingArena servedArena;
	private final int connectionId;
		
	private Player player;
	
	/**
	 * The default constructor for an arena connection server.
	 * @param connection The game connection.
	 * @param servedArena The arena to be served.
	 */
	public ArenaConnectionServer(Socket connection, ServingArena servedArena, int connectionId){
		super("ArenaConnectionServer-" + connectionId);
		this.connection = connection;
		this.servedArena = servedArena;
		this.connectionId = connectionId;
	}
	
	@Override
	public void run() {
		try {
			login();
		} catch (ProtocolException e) {
			log.fatal("Protocol error serving game.", e);
		} catch (ParseException e) {
			log.fatal("Parse error serving game.", e);
		}
		// TODO Properly close the resources
	}
	
	private void login() throws ProtocolException, ParseException {
		// Welcome
		write(ServerMessageFactory.welcome());
		// Wait for login
		Message login = read();
		if(login.getType() == MessageType.login && login.containsParameter("name")){
			this.player = new Player(login.getString("name"), connection.getInetAddress().getHostAddress());
			write(ServerMessageFactory.logged(this.player.getId()));
			this.servedArena.registerPlayer(player, new ArenaObserverServer());
		} else {
			throw new ProtocolException("Expected login message.");
		}
	}
	
	/**
	 * Writes the given message on the socket.
	 * @param message Message to be written.
	 */
	private void write(Message message){
		// TODO Implement this!
		log.debug("Written on " + connectionId + ": " + message.serialize());
	}
	
	/**
	 * Reads the next message on the socket.
	 * @return The read message.
	 */
	private Message read() throws ParseException {
		// TODO Implement this!
		log.debug("Read on " + connectionId + ": ");
		return null;
	}
	
	private class ArenaObserverServer implements ArenaObserver {

		@Override
		public void notifyLostPlayer(Player lostPlayer, Map<Player, Status> statuses) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyRegisteredPlayer(Player player) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifySortedPlayers(List<Player> players) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyStartedGaming() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyUnregisteredPlayer(Player player) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyGrow(int lines) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyHeight(Player player, int height) {
			// TODO Auto-generated method stub
			
		}
		
	}
}