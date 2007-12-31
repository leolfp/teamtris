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
	 * Indicates that the given player has lost the game.
	 * @param lostPlayer The player that lost the game.
	 * @param statuses All the statuses for every player.
	 */
	public void notifyLostPlayer(Player lostPlayer, Map<Player,Status> statuses);

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
	 * Indicates that the game has been started.
	 */
	public void notifyStartedGaming();

	/**
	 * Indicates that the given player has been removed from the arena.
	 * @param player The removed player.
	 */
	public void notifyUnregisteredPlayer(Player player);

}