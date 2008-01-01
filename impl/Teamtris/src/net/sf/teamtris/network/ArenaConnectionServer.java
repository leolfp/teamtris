package net.sf.teamtris.network;

import static net.sf.teamtris.network.protocol.ProtocolConfiguration.CHARSET;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
	private final String connectionId;
	
	private final LineNumberReader reader;
	private final BufferedOutputStream writer;
		
	private Player player;
	
	/**
	 * The default constructor for an arena connection server.
	 * @param connection The game connection.
	 * @param servedArena The arena to be served.
	 * @throws IOException If there is a failure getting socket streams.
	 */
	public ArenaConnectionServer(Socket connection, ServingArena servedArena, int connectionId) throws IOException{
		super("ArenaConnectionServer-" + connectionId);
		this.connection = connection;
		this.servedArena = servedArena;
		this.connectionId = "conn-" + connectionId;
		
		reader = new LineNumberReader(new InputStreamReader(connection.getInputStream(), CHARSET));
		writer = new BufferedOutputStream(connection.getOutputStream());
	}
	
	@Override
	public void run() {
		try {
			try {
				login();
			} catch (ProtocolException e) {
				write(ServerMessageFactory.error("protocol"));
				log.fatal("Protocol error serving game on " + connectionId + ".", e);
			} catch (ParseException e) {
				write(ServerMessageFactory.error("message"));
				log.fatal("Parse error serving game on " + connectionId + ".", e);
			}
		} catch (IOException e) {
			log.fatal("IO error serving game on " + connectionId + ".", e);
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				log.fatal("IO error closing server socket on " + connectionId + ".", e);
			}
		}
	}
	
	private void login() throws ProtocolException, ParseException, IOException {
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
	 * @throws IOException On underlying socket write failure.
	 */
	private void write(Message message) throws IOException {
		String serializedMessage = message.serialize();
		log.debug("Write on " + connectionId + ": " + serializedMessage);
		writer.write((serializedMessage + "\r\n").getBytes(CHARSET));
		writer.flush();
	}
	
	/**
	 * Reads the next message on the socket.
	 * @return The read message.
	 * @throws IOException On underlying socket read failure.
	 */
	private Message read() throws ParseException, IOException {
		String bareMessage = reader.readLine();
		if(bareMessage != null){
			log.debug("Read on " + connectionId + ": " + bareMessage);
			Message message = Message.parse(bareMessage);
			return message;
		} else {
			throw new IOException("End of input stream.");
		}
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