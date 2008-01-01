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
	 * @return The game.
	 */
	public Game getGame();

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

}