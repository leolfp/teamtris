package net.sf.teamtris.arena;
import net.sf.teamtris.piece.PieceStream;

/**
 * A game represents a player based point of interaction to the arena.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:21
 */
public interface Game {

	/**
	 * Event post when the player has built lines.
	 * @param howMany The number of lines built.
	 */
	public Status builtLines(int howMany);

	/**
	 * Obtains the initialized piece stream.
	 * @return The piece stream.
	 */
	public PieceStream getPieceStream();

	/**
	 * Obtains the player bean associated with this game.
	 * @return The player.
	 */
	public Player getPlayer();

	/**
	 * Event post when the player has lost the game.
	 */
	public void lost();

	/**
	 * Event post when the number of lines changes.
	 * @param lines The number of lines height.
	 */
	public void height(int lines);

}