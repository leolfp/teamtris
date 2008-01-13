package net.sf.teamtris.ui.serve;

import java.util.HashMap;
import java.util.Map;

import net.sf.teamtris.arena.ArenaGamingException;
import net.sf.teamtris.arena.ArenaManager;
import net.sf.teamtris.arena.Game;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.ServingArena;
import net.sf.teamtris.network.NetworkManager;
import net.sf.teamtris.ui.TeamtrisWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import thinlet.Button;
import thinlet.Listener;
import thinlet.Parser;
import thinlet.Row;
import thinlet.Table;
import thinlet.TextField;

/**
 * The main window game.
 * @author Leonardo
 * @version 1.0
 * @created 12-jan-2008 13:05:12
 */
public class ServeGameWindow  extends TeamtrisWindow implements ServeGameUI {
	private static final Log log = LogFactory.getLog(ServeGameWindow.class);

	private static final long serialVersionUID = -5906313588026878527L;
	
	private boolean serving;
	private ServingArena servingArena;
	private Player player;
	private Game game;
	private final Controller controller = new Controller();
	
	private Map<Integer, Row> players = new HashMap<Integer, Row>();
	
	public ServeGameWindow(){
		super("Serve Game", "net/sf/teamtris/ui/serve/ServeGameWindow.xml", 318, 565);
		((Button) internalPane.findByName("send")).addActionListener(new Listener(controller, "sendMessage"));
		((Button) internalPane.findByName("serveOrCancel")).addActionListener(new Listener(controller, "serveOrCancel"));
		
	}

	// Binding methods for observer
	
	public void addMessage(String player, String messageStr){
		Row message = newMessage();
		message.findByName("player").setText(player);
		message.findByName("message").setText(messageStr);
		Table table = (Table) internalPane.findByName("messages");
		table.preppendChild(message);
	}
	
	public void addError(String errorMessage){
		Row message = newMessage();
		message.findByName("player").setText("ERROR");
		message.findByName("message").setText(errorMessage);
		Table table = (Table) internalPane.findByName("messages");
		table.preppendChild(message);
	}
	
	public void addPlayer(int id, String playerName, String origin){
		if(id != player.getId()){
			Row player = newPlayer();
			player.findByName("name").setText(playerName);
			player.findByName("location").setText(origin);
			players.put(id, player);
			Table table = (Table) internalPane.findByName("players");
			table.appendChild(player);
		}
	}
	
	public void removePlayer(int id){
		Row player = players.remove(id);
		if(player != null){
			Table table = (Table) internalPane.findByName("players");
			table.removeChild(player);
		}
	}
	
	// Internal methods
	
	private void startServing() {
		serving = true;
		setTitle("Serving Game");
		servingArena = ArenaManager.getServingArena();
		NetworkManager.serveArena(servingArena);
		player = new Player(System.getProperty("user.name"));
		servingArena.registerPlayer(player, new ServeGameArenaObserver(this));
		game = servingArena.getGame(player);
		internalPane.findByName("name").setText(player.getName());
		internalPane.findByName("location").setText(player.getOrigin());
		internalPane.findByName("players").refresh();
	}
	
	private void stopServing() {
		// TODO Implement
		serving = false;
		setTitle("Serve Game");
	}

	private Row newPlayer(){
		Row row = new Row();
		try {
			new Parser(row, "net/sf/teamtris/ui/serve/Player.xml");
		} catch (Exception e) {
			log.fatal("Fail reading window serialization.", e);
		}
		return row;
	}

	private Row newMessage(){
		Row row = new Row();
		try {
			new Parser(row, "net/sf/teamtris/ui/serve/Message.xml");
		} catch (Exception e) {
			log.fatal("Fail reading window serialization.", e);
		}
		return row;
	}
	
	// The controller
	
	public class Controller {
		public void sendMessage(){
			TextField inputMessage = (TextField) internalPane.findByName("inputMessage");
			try {
				game.talk(inputMessage.getText());
			} catch (ArenaGamingException e) {
				addError(e.getMessage());
			}
		}

		public void serveOrCancel(){
			if(serving){
				stopServing();
				((Button) internalPane.findByName("serveOrCancel")).setText("Serve Game");
			} else {
				startServing();
				((Button) internalPane.findByName("serveOrCancel")).setText("Stop Server");
			}
		}
	}
	
}
