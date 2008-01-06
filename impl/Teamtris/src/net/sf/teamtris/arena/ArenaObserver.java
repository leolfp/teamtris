package net.sf.teamtris.arena;

import java.util.List;
import java.util.Map;

/**
 * The observer through which the arena will post events.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:16
 */
public interface ArenaObserver {

	/**
	 * Indicates that the given player has won the game.
	 * @param winnerPlayer The player that won the game.
	 * @param statuses All the statuses for every player.
	 */
	public void notifyWinnerPlayer(Player winnerPlayer, Map<Player,Status> statuses);

	/**
	 * Indicates that the given player has been added to the arena.
	 * @param player The added player.
	 */
	public void notifyRegisteredPlayer(Player player);

	/**
	 * Indicates that the players has been resorted.
	 * @param players The new sequence of sorted players.
	 */
	public void notifySortedPlayers(List<Player> players);

	/**
	 * Indicates that the game options have been modified.
	 * @param options The new game options.
	 */
	public void notifyChangedOptions(GameOptions options);
	
	/**
	 * Indicates that the game has been asked to start.
	 */
	public void notifyStartGame();

	/**
	 * Indicates that the game has already been started by the given player.
	 */
	public void notifyStartedGaming(Player player);

	/**
	 * Indicates that everybody has started gaming.
	 */
	public void notifyStartedGaming();
	
	/**
	 * Indicates that the given player has been removed from the arena.
	 * @param player The removed player.
	 */
	public void notifyUnregisteredPlayer(Player player);

	/**
	 * Indicates that your neighbour sent you a line growth.
	 * @param lines The number of lines to grow.
	 */
	public void notifyGrow(int lines);

	/**
	 * Indicates a change in height in a player game.
	 * @param player The player.
	 * @param height The new height.
	 */
	public void notifyHeight(Player player, int height);

	/**
	 * Indicates that the game has been paused by the given player.
	 */
	public void notifyPaused(Player player);
	
	/**
	 * Indicates that the game has been resumed by the given player.
	 */
	public void notifyResumed(Player player);

	/**
	 * Indicates that your current status on arena has changed.
	 */
	public void notifyStatus(Status status);

	/**
	 * Indicates that your received an error.
	 * @param errorMessage The error type.
	 */
	public void notifyError(String errorMessage);

	/**
	 * Indicates that your received an error.
	 * @param player The player who sents the message.
	 * @param message The message.
	 */
	public void notifyTalk(Player player, String message);

}