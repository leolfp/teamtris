package net.sf.teamtris.arena;

/**
 * An arena used for serving local games.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:26
 */
public interface ServingArena extends Arena {

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

}