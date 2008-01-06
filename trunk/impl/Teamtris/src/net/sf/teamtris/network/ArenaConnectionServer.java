package net.sf.teamtris.network;

import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.teamtris.arena.ArenaGamingException;
import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.Game;
import net.sf.teamtris.arena.GameOptions;
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

	private enum ConnectionState {
		LOGIN,
		NEGOTIATION,
		STARTING,
		GAMING,
		PAUSED,
		ENDING
	}
	
	private ConnectionState state;
	
	private Game game;
	
	private static final Map<ConnectionState, MessageType[]> acceptedMessages = new HashMap<ConnectionState, MessageType[]>();
	
	static {
		MessageType[] negotiation = new MessageType[] {MessageType.logout};
		MessageType[] starting = new MessageType[] {MessageType.starting};
		MessageType[] gaming = new MessageType[] {MessageType.height, MessageType.built, MessageType.pause, MessageType.lost};
		MessageType[] paused = new MessageType[] {MessageType.resume};
		MessageType[] ending = new MessageType[] {};
		
		Arrays.sort(negotiation);
		Arrays.sort(starting);
		Arrays.sort(gaming);
		Arrays.sort(paused);
		Arrays.sort(ending);
		
		acceptedMessages.put(ConnectionState.NEGOTIATION, negotiation);
		acceptedMessages.put(ConnectionState.STARTING, starting);
		acceptedMessages.put(ConnectionState.GAMING, gaming);
		acceptedMessages.put(ConnectionState.PAUSED, paused);
		acceptedMessages.put(ConnectionState.ENDING, ending);
	}
	
	/**
	 * The default constructor for an arena connection server.
	 * @param connection The game connection.
	 * @param servedArena The arena to be served.
	 * @throws IOException If there is a failure getting socket streams.
	 */
	ArenaConnectionServer(Socket connection, ServingArena servedArena, int connectionId) throws IOException{
		super("ArenaConnectionServer-" + connectionId);
		this.connectionId = "srvConn-" + connectionId;
		this.servedArena = servedArena;
		this.connection = new MessageConnection(connection, this.connectionId);
	}
	
	@Override
	public void run() {
		try {
			try {
				login();
				while(!isInterrupted()){
					if(!receiveMessage()){
						return;
					}
				}
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
			if(player != null){
				this.servedArena.unregisterPlayer(player);
			}
			connection.close();
		}
	}
	
	private void login() throws ProtocolException, ParseException, IOException {
		this.state = ConnectionState.LOGIN;
		// Welcome
		connection.write(ServerMessageFactory.welcome());
		// Wait for login
		Message login = connection.read(MessageType.login, new String[] {"name"});
		this.player = new Player(login.getString("name"), connection.getRemoteAddress());
		connection.write(ServerMessageFactory.logged(this.player.getId()));
		this.servedArena.registerPlayer(player, new ArenaObserverServer());
		this.game = this.servedArena.getGame(player);
		this.state = ConnectionState.NEGOTIATION;
	}

	private boolean receiveMessage() throws ProtocolException, ParseException, IOException {
		Message message = connection.read();
		MessageType[] expected = acceptedMessages.get(this.state);
		if(Arrays.binarySearch(expected, message.getType()) < 0){
			throw new ProtocolException("Wrong message type for state '" + this.state + "': was '" +
					message.getType().name() + "', expected in " + Arrays.toString(expected) + ".");
		}
			
		try {
			if(message.getType() == MessageType.starting){
				this.game.starting();
				return true;
			} else if(message.getType() == MessageType.height){
				this.game.height(message.getInt("lines"));
				return true;
			} else if(message.getType() == MessageType.built){
				this.game.builtLines(message.getInt("lines"));
				return true;
			} else if(message.getType() == MessageType.pause){
				this.game.pause();
				return true;
			} else if(message.getType() == MessageType.resume){
				this.game.resume();
				return true;
			} else if(message.getType() == MessageType.lost){
				this.game.lost();
				return true;
			} else if(message.getType() == MessageType.logout){
				connection.write(ServerMessageFactory.bye());
				return false;
			} else {
				throw new ProtocolException("Wrong message type: was '" + message.getType().name() +
				"', expected a valid client message.");
			}
		} catch (IllegalArgumentException e) {
			throw new ProtocolException("Missing parameter on '" + message.getType().name() + "' message.", e);
		} catch (ArenaGamingException e) {
			log.error("Arena failure.", e);
			return false;
		}
	}

	private class ArenaObserverServer implements ArenaObserver {

		@Override
		public void notifyWinnerPlayer(Player winnerPlayer, Map<Player, Status> statuses) {
			ArenaConnectionServer.this.state = ArenaConnectionServer.ConnectionState.ENDING;
			write(ServerMessageFactory.finish(winnerPlayer.getId()));
			
			for(Player player : statuses.keySet()){
				Status status = statuses.get(player);
				write(ServerMessageFactory.statusOnFinish(player.getId(), status.getLines(), status.getTotalBuiltLines(), status.getPoints()));
			}
			
			write(ServerMessageFactory.end());
			ArenaConnectionServer.this.state = ArenaConnectionServer.ConnectionState.NEGOTIATION;
		}

		@Override
		public void notifyRegisteredPlayer(Player player) {
			write(ServerMessageFactory.in(player.getId(), player.getName(), player.getOrigin()));
		}

		@Override
		public void notifySortedPlayers(List<Player> players) {
			int[] ids = new int[players.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = players.get(i).getId();
			}
			
			write(ServerMessageFactory.sorted(ids));
		}

		@Override
		public void notifyStartGame() {
			ArenaConnectionServer.this.state = ArenaConnectionServer.ConnectionState.STARTING;
			write(ServerMessageFactory.start());
		}

		@Override
		public void notifyStartedGaming(Player player){
			write(ServerMessageFactory.started(player.getId()));
		}

		@Override
		public void notifyStartedGaming(){
			ArenaConnectionServer.this.state = ArenaConnectionServer.ConnectionState.GAMING;
		}
		
		@Override
		public void notifyUnregisteredPlayer(Player player) {
			write(ServerMessageFactory.out(player.getId()));
		}

		@Override
		public void notifyGrow(int lines) {
			write(ServerMessageFactory.grow(lines));
		}

		@Override
		public void notifyHeight(Player player, int height) {
			write(ServerMessageFactory.status(player.getId(), height));
		}

		@Override
		public void notifyPaused(Player player) {
			ArenaConnectionServer.this.state = ArenaConnectionServer.ConnectionState.PAUSED;
			write(ServerMessageFactory.paused(player.getId()));
		}

		@Override
		public void notifyResumed(Player player) {
			ArenaConnectionServer.this.state = ArenaConnectionServer.ConnectionState.GAMING;
			write(ServerMessageFactory.resumed(player.getId()));
		}

		@Override
		public void notifyStatus(Status status) {
			write(ServerMessageFactory.statusOnBuilt(player.getId(), status.getTotalBuiltLines(), status.getPoints()));
		}

		@Override
		public void notifyChangedOptions(GameOptions options) {
			write(ServerMessageFactory.options(options.getPieceStreamType(), options.getPieceStreamSeed(),
					options.getLevelChangePoints(), options.getGrowDelay(), options.getScoringOptions().getPoints()));
			
		}

		private void write(Message message) {
			try {
				connection.write(message);
			} catch (IOException e) {
				log.fatal("IO error error serving game on " + connectionId + ".", e);
				connection.close();
				ArenaConnectionServer.this.interrupt();
			}
		}

	}

}