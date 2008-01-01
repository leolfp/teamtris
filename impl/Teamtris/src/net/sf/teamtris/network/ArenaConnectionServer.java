package net.sf.teamtris.network;

import java.io.IOException;
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
	
	private final MessageConnection connection;
	private final ServingArena servedArena;
	private final String connectionId;
	
	private Player player;
	
	/**
	 * The default constructor for an arena connection server.
	 * @param connection The game connection.
	 * @param servedArena The arena to be served.
	 * @throws IOException If there is a failure getting socket streams.
	 */
	ArenaConnectionServer(Socket connection, ServingArena servedArena, int connectionId) throws IOException{
		super("ArenaConnectionServer-" + connectionId);
		this.connectionId = "sConn-" + connectionId;
		this.servedArena = servedArena;
		this.connection = new MessageConnection(connection, this.connectionId);
	}
	
	@Override
	public void run() {
		try {
			try {
				login();
			} catch (ProtocolException e) {
				connection.write(ServerMessageFactory.error("protocol"));
				log.fatal("Protocol error serving game on " + connectionId + ".", e);
			} catch (ParseException e) {
				connection.write(ServerMessageFactory.error("message"));
				log.fatal("Parse error serving game on " + connectionId + ".", e);
			}
		} catch (IOException e) {
			log.fatal("IO error serving game on " + connectionId + ".", e);
		} finally {
			connection.close();
		}
	}
	
	private void login() throws ProtocolException, ParseException, IOException {
		// Welcome
		connection.write(ServerMessageFactory.welcome());
		// Wait for login
		Message login = connection.read(MessageType.login, new String[] {"name"});
		this.player = new Player(login.getString("name"), connection.getRemoteAddress());
		connection.write(ServerMessageFactory.logged(this.player.getId()));
		this.servedArena.registerPlayer(player, new ArenaObserverServer());
	}
	
	private class ArenaObserverServer implements ArenaObserver {

		@Override
		public void notifyWinnerPlayer(Player lostPlayer, Map<Player, Status> statuses) {
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
		public void notifyStartGame() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyStartedGaming(Player player){
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyStartedGaming(){
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

		@Override
		public void notifyPaused(Player player) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyResumed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyStatus(Status status) {
			// TODO Auto-generated method stub
			
		}
		
	}

}