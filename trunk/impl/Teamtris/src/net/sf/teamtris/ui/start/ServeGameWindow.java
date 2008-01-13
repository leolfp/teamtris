package net.sf.teamtris.ui.start;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.teamtris.arena.ArenaGamingException;
import net.sf.teamtris.arena.ArenaManager;
import net.sf.teamtris.arena.Game;
import net.sf.teamtris.arena.GameOptions;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.ScoringOptions;
import net.sf.teamtris.arena.ServingArena;
import net.sf.teamtris.network.NetworkManager;
import net.sf.teamtris.ui.TeamtrisWindow;
import net.sf.teamtris.ui.play.GameArenaObserver;

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
public class ServeGameWindow  extends TeamtrisWindow implements StartGameUI {
	private static final Log log = LogFactory.getLog(ServeGameWindow.class);

	private static final long serialVersionUID = -5906313588026878527L;
	
	private boolean serving;
	private ServingArena servingArena;
	private final Player player = new Player(System.getProperty("user.name"));
	private Game game;
	private final Controller controller = new Controller();
	
	private Map<Integer, Row> players = new HashMap<Integer, Row>();
	
	public ServeGameWindow(){
		super("Serve Game", "net/sf/teamtris/ui/start/ServeGameWindow.xml", 318, 625);
		subscribeEvents();
	}
	
	protected void subscribeEvents(){
		((Button) internalPane.findByName("send")).addActionListener(new Listener(controller, "sendMessage"));
		((Button) internalPane.findByName("copy")).addActionListener(new Listener(controller, "copy"));
		((Button) internalPane.findByName("save")).addActionListener(new Listener(controller, "save"));
		((Button) internalPane.findByName("resort")).addActionListener(new Listener(controller, "resort"));
		((Button) internalPane.findByName("start")).addActionListener(new Listener(controller, "start"));
		((Button) internalPane.findByName("serveOrCancel")).addActionListener(new Listener(controller, "serveOrCancel"));
		((TextField) internalPane.findByName("inputMessage")).addActionListener(new Listener(controller, "sendMessage"));
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
	
	public void sortPlayers(int[] ids){
		
	}
	
	// Internal methods
	
	private void startServing() {
		serving = true;
		setTitle("Serving Game");
		servingArena = ArenaManager.createServingArena();
		configureArena();
		NetworkManager.serveArena(servingArena);
		servingArena.registerPlayer(player, new GameArenaObserver(this));
		game = servingArena.getGame(player);
		internalPane.findByName("name").setText(player.getName());
		internalPane.findByName("location").setText(player.getOrigin());
		internalPane.findByName("players").refresh();
	}
	
	private void configureArena() {
		if(servingArena != null){
			String pieces = internalPane.findByName("gamePieces").getText();
			boolean growing = !"".equals(internalPane.findByName("growDelay").getText());
			int grow = growing ? Integer.parseInt(internalPane.findByName("growDelay").getText()) : 0;
			int levelScore = Integer.parseInt(internalPane.findByName("levelScore").getText());
			int points1 = Integer.parseInt(internalPane.findByName("points1").getText());
			int points2 = Integer.parseInt(internalPane.findByName("points2").getText());
			int points3 = Integer.parseInt(internalPane.findByName("points3").getText());
			int points4 = Integer.parseInt(internalPane.findByName("points4").getText());
			long seed = Math.abs(new Random().nextLong());
			
			servingArena.configure(new GameOptions(pieces, seed, levelScore, grow, growing,
					new ScoringOptions(points1, points2, points3, points4)));
		}
	}
	
	private void stopServing() {
		serving = false;
		servingArena = null;
		game = null;
		setTitle("Serve Game");
		NetworkManager.stopServingArena();
	}

	private Row newPlayer(){
		Row row = new Row();
		try {
			new Parser(row, "net/sf/teamtris/ui/start/Player.xml");
		} catch (Exception e) {
			log.fatal("Fail reading window serialization.", e);
		}
		return row;
	}

	private Row newMessage(){
		Row row = new Row();
		try {
			new Parser(row, "net/sf/teamtris/ui/start/Message.xml");
		} catch (Exception e) {
			log.fatal("Fail reading window serialization.", e);
		}
		return row;
	}
	
	// The controller
	
	public class Controller {
		public void sendMessage(){
			TextField inputMessage = (TextField) internalPane.findByName("inputMessage");
			if(game != null){
				try {
					game.talk(inputMessage.getText());
					inputMessage.setText("");
				} catch (ArenaGamingException e) {
					addError(e.getMessage());
				}
			}
		}

		public void copy(){
			// TODO Implement copying profile
		}

		public void save(){
			// TODO Implement saving configuration
			configureArena();
		}

		public void resort(){
			servingArena.sortPlayers();
		}

		public void start(){
			servingArena.startGaming();
			try {
				game.starting();
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
