package net.sf.teamtris.arena;

/**
 * An arena used for serving local games.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:26
 */
public interface ServingArena extends Arena {

	/**
	 * Gets a given player game interaction on the arena.
	 * @param player The player.
	 * @return The game.
	 */
	public Game getGame(Player player);

	/**
	 * Initializes this arena to serve games with the given options.
	 * @param options The game options.
	 */
	public void init(GameOptions options);

	/**
	 * Resorts the players on the arena.
	 */
	public void sortPlayers();

	/**
	 * Starts the games on the arena.
	 */
	public void startGaming();

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