package net.sf.teamtris.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.teamtris.piece.PieceManager;
import net.sf.teamtris.piece.PieceStream;

/**
 * A default arena implementation for serving local games.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:49
 */
public class LocalServingArena implements ServingArena {
	
	private final Object playersMonitor = new Object() {};
	
	private final Map<Player, ArenaObserver> observers = new HashMap<Player, ArenaObserver>();
	private final Map<Player, LocalGame> games = new HashMap<Player, LocalGame>();
	private final List<Player> sortedPlayers = new ArrayList<Player>();
	private final List<Player> pendingPlayers = new ArrayList<Player>();
	
	private GameOptions gameOptions;
	private LocalGame defaultLocalGame;
	
	private final Object gameStateMonitor = new Object() {};
	private boolean paused;	
	
	@Override
	public void configure(GameOptions options) {
		this.gameOptions = options;
		synchronized (playersMonitor) {
			for(Player playerToNotify : sortedPlayers){
				observers.get(playerToNotify).notifyChangedOptions(options);
			}
		}
	}
	
	@Override
	public void sortPlayers() {
		synchronized (playersMonitor) {
			Collections.shuffle(sortedPlayers);
			// Just to get an inmutable copy of players
			List<Player> players = getPlayers();
			
			for(Player playerToNotify : sortedPlayers){
				observers.get(playerToNotify).notifySortedPlayers(players);
			}
		}
	}

	@Override
	public void startGaming() {
		synchronized (playersMonitor) {
			paused = false;
			pendingPlayers.addAll(sortedPlayers);
			for(Player playerToNotify : sortedPlayers){
				games.get(playerToNotify).setNewStatus(new Status(gameOptions));
				observers.get(playerToNotify).notifyStartGame();
			}
		}
	}

	@Override
	public Game getGame(Player player) {
		synchronized (games) {
			return games.get(player);
		}
	}

	@Override
	public Game getGame() {
		return defaultLocalGame;
	}

	@Override
	public GameOptions getGameOptions() {
		return this.gameOptions;
	}

	@Override
	public List<Player> getPlayers() {
		synchronized (playersMonitor) {
			return Collections.unmodifiableList(new ArrayList<Player>(sortedPlayers));
		}
	}

	@Override
	public void registerPlayer(Player player, ArenaObserver observer) {
		synchronized (playersMonitor) {
			// Notifies already registered players for the new one
			for(Player playerInGame : sortedPlayers){
				observer.notifyRegisteredPlayer(playerInGame);
			}
			
			sortedPlayers.add(player);
			observers.put(player, observer);
			LocalGame game = new LocalGame(player);
			synchronized (games) {
				games.put(player, game);
			}
			
			// The first local player is the default one
			if(player.getType().equals("local") && defaultLocalGame == null){
				defaultLocalGame = game;
			}
			
			for(Player playerToNotify : sortedPlayers){
				observers.get(playerToNotify).notifyRegisteredPlayer(player);
			}
			
			// Notify already defined configurations
			if(gameOptions != null){
				observer.notifyChangedOptions(gameOptions);
			}
			List<Player> players = getPlayers();
			for(Player playerToNotify : sortedPlayers){
				observers.get(playerToNotify).notifySortedPlayers(players);
			}
		}
	}

	@Override
	public void unregisterPlayer(Player player) {
		synchronized (playersMonitor) {
			sortedPlayers.remove(player);
			observers.remove(player);
			pendingPlayers.remove(player);
			synchronized (games) {
				games.remove(player);
			}

			for(Player playerToNotify : sortedPlayers){
				observers.get(playerToNotify).notifyUnregisteredPlayer(player);
			}
		}
	}
	
	private Player findWinner(){
		synchronized (games) {
			Set<LocalGame> sortedGames = new TreeSet<LocalGame>(games.values());
			return sortedGames.iterator().next().player;
		}
	}
	
	private Map<Player, Status> getStatuses(){
		Map<Player, Status> statuses = new HashMap<Player, Status>();
		synchronized (playersMonitor) {
			for(Player player : sortedPlayers){
				statuses.put(player, games.get(player).status);
			}
		}
		return statuses;
	}
	
	private Player getNext(Player player){
		synchronized (playersMonitor) {
			return sortedPlayers.get((sortedPlayers.indexOf(player) + 1) % sortedPlayers.size());
		}
	}
	
	private class LocalGame implements Game, Comparable<LocalGame> {
		
		private final Player player;
		private Status status;
		private boolean paused = false;
		
		private LocalGame(Player player){
			this.player = player;
		}
		
		private void setNewStatus(Status status){
			this.status = status;
		}

		@Override
		public Status builtLines(int howMany) {
			status.built(howMany);
			synchronized (playersMonitor) {
				observers.get(player).notifyStatus(status);
				if(howMany > 1 && gameOptions.isGrowingGame()){
					Player next = getNext(this.player);
					observers.get(next).notifyGrow(howMany - 1);
				}
			}
			return status;
		}

		@Override
		public PieceStream getPieceStream() {
			return PieceManager.createPieceStream(gameOptions.getPieceStreamType(), gameOptions.getPieceStreamSeed());
		}

		@Override
		public Player getPlayer() {
			return player;
		}

		@Override
		public void lost() {
			synchronized (playersMonitor) {
				Player winnerPlayer = findWinner();
				Map<Player, Status> statuses = getStatuses();
			
				for(Player playerToNotify : sortedPlayers){
					observers.get(playerToNotify).notifyWinnerPlayer(winnerPlayer, statuses);
				}
			}
		}

		@Override
		public void height(int lines) {
			if(lines != status.getLines()){
				status.setLines(lines);
				synchronized (playersMonitor) {
					for(Player playerToNotify : sortedPlayers){
						observers.get(playerToNotify).notifyHeight(this.player, lines);
					}
				}
			}			
		}

		@Override
		public void pause() throws ArenaGamingException {
			synchronized (gameStateMonitor) {
				if(!LocalServingArena.this.paused){
					LocalServingArena.this.paused = true;
					this.paused = true;
					synchronized (playersMonitor) {
						for(Player playerToNotify : sortedPlayers){
							observers.get(playerToNotify).notifyPaused(this.player);
						}
					}
				} else {
					throw new ArenaGamingException("Game already paused.");
				}
			}
		}

		@Override
		public void resume() throws ArenaGamingException {
			synchronized (gameStateMonitor) {
				if(LocalServingArena.this.paused){
					if(this.paused){
						LocalServingArena.this.paused = false;
						this.paused = false;
						synchronized (playersMonitor) {
							for(Player playerToNotify : sortedPlayers){
								observers.get(playerToNotify).notifyResumed(this.player);
							}
						}
					} else {
						throw new ArenaGamingException("Game paused by other player.");
					}
				} else {
					throw new ArenaGamingException("Game not paused.");
				}
			}
		}

		@Override
		public void starting() throws ArenaGamingException {
			synchronized (playersMonitor) {
				pendingPlayers.remove(this.player);
				for(Player playerToNotify : sortedPlayers){
					observers.get(playerToNotify).notifyStartedGaming(this.player);
				}
				if(pendingPlayers.size() == 0){
					for(Player playerToNotify : sortedPlayers){
						observers.get(playerToNotify).notifyStartedGaming();
					}
				}
			}
		}

		@Override
		public int compareTo(LocalGame other) {
			return this.status.compareTo(other.status);
		}

	}
	
}