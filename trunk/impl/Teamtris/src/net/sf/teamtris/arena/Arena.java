package net.sf.teamtris.arena;

import java.util.List;

/**
 * An arena is the coordinator for a multiplayer game.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:10
 */
public interface Arena {

	/**
	 * Gets a given player game interaction on the arena.
	 * @param player The player.
	 * @return The game.
	 */
	public Game getGame(Player player);

	/**
	 * Gets the game options bean.
	 * @return The game options.
	 */
	public GameOptions getGameOptions();

	/**
	 * Gets all players registered on this arena.
	 * @return This arena's players.
	 */
	public List<Player> getPlayers();

	/**
	 * Registers a given player on the arena.
	 * @param player The player (can be remote).
	 * @param observer The arena observer.
	 */
	public void registerPlayer(Player player, ArenaObserver observer);

	/**
	 * Removes the player from the arena.
	 * @param player The player to be removed.
	 */
	public void unregisterPlayer(Player player);

}