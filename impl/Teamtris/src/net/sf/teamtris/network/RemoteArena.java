package net.sf.teamtris.network;

import static net.sf.teamtris.network.protocol.ProtocolConfiguration.SERVER_NAME;
import static net.sf.teamtris.network.protocol.ProtocolConfiguration.VERSION;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.teamtris.arena.Arena;
import net.sf.teamtris.arena.ArenaGamingException;
import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.Game;
import net.sf.teamtris.arena.GameOptions;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.ScoringOptions;
import net.sf.teamtris.arena.Status;
import net.sf.teamtris.network.protocol.ClientMessageFactory;
import net.sf.teamtris.network.protocol.Message;
import net.sf.teamtris.network.protocol.MessageType;
import net.sf.teamtris.network.protocol.ProtocolConfiguration;
import net.sf.teamtris.network.protocol.ProtocolException;
import net.sf.teamtris.piece.PieceManager;
import net.sf.teamtris.piece.PieceStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The proxied remote arena.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:15
 */
public class RemoteArena extends Thread implements Arena {
	private static final Log log = LogFactory.getLog(RemoteArena.class);
	
	private final Player player;
	private final ArenaObserver arenaObserver;
	private final List<Player> players = new ArrayList<Player>();
	private final Map<Integer, Player> playersById = new HashMap<Integer, Player>();

	private final MessageConnection connection;
	private final String connectionId;
	
	private int thisPlayerRemoteId;
	private int winnerPlayerId;
	private GameOptions gameOptions;
	private RemoteGame remoteGame;
	
	private final Object playersMonitor = new Object() {};
	
	/**
	 * The default constructor for a remote arena.
	 * @param server The server to connect to.
	 * @param player The player that will play game on this arena.
	 * @param arenaObserver The arena observer to be used.
	 * @param connectionId the connectionId;
	 * @throws UnknownHostException If the server host can't be contacted or resolved.
	 * @throws IOException If there was a problem connecting to the server host.
	 */
	RemoteArena(String server, Player player, ArenaObserver arenaObserver, String connectionId) throws UnknownHostException, IOException {
		this.player = player;
		this.arenaObserver = arenaObserver;
		this.connectionId = connectionId;
		Socket socket = new Socket(server, ProtocolConfiguration.PORT);
		this.connection = new MessageConnection(socket, this.connectionId);
	}

	@Override
	public void run() {
		try {
			connection.setInterceptor(MessageType.error, new MessageInterceptor(){
				@Override
				public boolean arrived(Message message) {
					arenaObserver.notifyError(message.getString("type"));
					Thread.currentThread().interrupt();
					return false;
				}
			});

			login();
			while(!isInterrupted()){
				if(!negotiation()){
					return;
				}
				starting();
				gaming();
				ending();
			}
		} catch (ProtocolException e) {
			log.fatal("Protocol error on client " + connectionId + ".", e);
		} catch (ParseException e) {
			log.fatal("Parse error on client " + connectionId + ".", e);
		} catch (IOException e) {
			log.fatal("IO error on client " + connectionId + ".", e);
		} finally {
			connection.close();
		}
	}

	private void login() throws ProtocolException, ParseException, IOException {
		// Wait for welcome
		Message welcome = connection.read(MessageType.welcome, new String[]{"server", "version"});
		if(welcome.getString("server").equals(SERVER_NAME) && welcome.getString("version").equals(VERSION)){
			// Send login
			connection.write(ClientMessageFactory.login(player.getName()));
			// Wait for logged
			Message logged = connection.read(MessageType.logged, new String[] {"id"});
			thisPlayerRemoteId = logged.getInt("id");

			connection.setInterceptor(MessageType.out, new MessageInterceptor(){
				@Override
				public boolean arrived(Message message) {
					int id = message.getInt("id");
					Player removed;
					synchronized (playersMonitor) {
						removed = playersById.remove(id);
						players.remove(removed);
					}
					arenaObserver.notifyUnregisteredPlayer(removed);
					return true;
				}
			});
			
			connection.setInterceptor(MessageType.talked, new MessageInterceptor(){
				@Override
				public boolean arrived(Message message) {
					arenaObserver.notifyTalk(playersById.get(message.getInt("id")), message.getString("message"));
					return true;
				}
			});
		} else {
			throw new ProtocolException("Invalid server/version on welcome message: was '" +
					welcome.getString("server") + "/" + welcome.getString("version") +
					"', expected '" + SERVER_NAME + "/" + VERSION + "'.");
		}
	}
	
	private boolean negotiation() throws ProtocolException, ParseException, IOException {
		for(;;){
			Message message = connection.read();
			try {
				if(message.getType() == MessageType.in){
					int id = message.getInt("id");
					Player player = new Player(message.getString("name"), message.getString("origin"), id);
					synchronized (playersMonitor) {
						playersById.put(id, player);
					}
					arenaObserver.notifyRegisteredPlayer(player);
				} else if(message.getType() == MessageType.sorted){
					int[] sortedIds = message.getIntArray("ids");
					synchronized (playersMonitor) {
						players.clear();
						for(int id : sortedIds){
							players.add(playersById.get(id));
						}
					}
					arenaObserver.notifySortedPlayers(players);
				} else if(message.getType() == MessageType.options){
					ScoringOptions scoring = new ScoringOptions(message.getInt("single"),
							message.getInt("double"), message.getInt("triple"), message.getInt("quad"));
					gameOptions = new GameOptions(message.getString("stream"), message.getLong("seed"),
							message.getInt("level"), message.getInt("delay"), message.getBoolean("grow"), scoring);
				} else if(message.getType() == MessageType.start){
					remoteGame = new RemoteGame();
					arenaObserver.notifyStartGame();
					return true;
				} else if(message.getType() == MessageType.bye){
					return false;
				} else {
					throw new ProtocolException("Wrong message type: was '" + message.getType().name() +
							"', expected in [in,out,sorted,options,start].");
				}
			} catch (IllegalArgumentException e) {
				throw new ProtocolException("Missing parameter on '" + message.getType().name() + "' message.", e);
			}
		}
	}

	private void starting() throws ProtocolException, ParseException, IOException {
		// TODO Put a timeout on starting, or a recovery strategy
		for(int i = 0; i < players.size(); ++i){
			Message started = connection.read(MessageType.started, new String[] {"id"});
			arenaObserver.notifyStartedGaming(playersById.get(started.getInt("id")));
		}
		arenaObserver.notifyStartedGaming();
	}

	private void gaming() throws ProtocolException, ParseException, IOException {
		for(;;){
			Message message = connection.read();
			try {
				if(message.getType() == MessageType.status){
					if(message.getInt("id") == thisPlayerRemoteId){
						arenaObserver.notifyStatus(new Status(-1, message.getLong("points"), message.getInt("lines")));
					} else {
						arenaObserver.notifyHeight(playersById.get(message.getInt("id")), message.getInt("height"));
					}
				} else if(message.getType() == MessageType.grow){
					arenaObserver.notifyGrow(message.getInt("lines"));
				} else if(message.getType() == MessageType.paused){
					arenaObserver.notifyPaused(playersById.get(message.getInt("id")));
				} else if(message.getType() == MessageType.resumed){
					arenaObserver.notifyResumed(playersById.get(message.getInt("id")));
				} else if(message.getType() == MessageType.finish){
					winnerPlayerId = message.getInt("winner");
					return;
				} else {
					throw new ProtocolException("Wrong message type: was '" + message.getType().name() +
							"', expected in [status,grow,paused,resumed,finish].");
				}
			} catch (IllegalArgumentException e) {
				throw new ProtocolException("Missing parameter on '" + message.getType().name() + "' message.", e);
			}
		}
	}

	private void ending() throws ProtocolException, ParseException, IOException {
		Map<Player, Status> statuses = new HashMap<Player, Status>(players.size());
		
		for(int i = 0; i < players.size(); ++i){
			Message status = connection.read(MessageType.status, new String[] {"id", "height", "points", "lines"});
			statuses.put(playersById.get(status.getInt("id")),
					new Status(status.getInt("height"), status.getLong("points"), status.getInt("lines")));
		}
		
		arenaObserver.notifyWinnerPlayer(playersById.get(winnerPlayerId), statuses);
		
		connection.read(MessageType.end, null);
		this.remoteGame = null;
	}
	
	@Override
	public Game getGame() {
		return remoteGame;
	}

	@Override
	public GameOptions getGameOptions() {
		return gameOptions;
	}

	@Override
	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	private class RemoteGame implements Game {
		private final PieceStream pieceStream = PieceManager.createPieceStream(gameOptions.getPieceStreamType(), gameOptions.getPieceStreamSeed());
		private final Status status = new Status(gameOptions); 

		@Override
		public Status builtLines(int howMany) throws ArenaGamingException {
			write(ClientMessageFactory.built(howMany));
			status.built(howMany);
			return status;
		}

		@Override
		public PieceStream getPieceStream() {
			return pieceStream;
		}

		@Override
		public Player getPlayer() {
			return playersById.get(thisPlayerRemoteId);
		}

		@Override
		public void lost() throws ArenaGamingException {
			write(ClientMessageFactory.lost());
		}

		@Override
		public void height(int lines) throws ArenaGamingException {
			write(ClientMessageFactory.height(lines));
			status.setLines(lines);
		}
		
		@Override
		public void pause() throws ArenaGamingException {
			write(ClientMessageFactory.pause());
		}

		@Override
		public void resume() throws ArenaGamingException {
			write(ClientMessageFactory.resume());
		}

		@Override
		public void starting() throws ArenaGamingException {
			write(ClientMessageFactory.starting());
		}

		@Override
		public void talk(String message) throws ArenaGamingException {
			write(ClientMessageFactory.talk(message));
		}

		private void write(Message message) throws ArenaGamingException {
			try {
				connection.write(message);
			} catch (IOException e) {
				log.fatal("IO error on client " + connectionId + ".", e);
				connection.close();
				RemoteArena.this.interrupt();
				throw new ArenaGamingException("Fail sending change to remote arena.", e);
			}
		}

	}
	
}