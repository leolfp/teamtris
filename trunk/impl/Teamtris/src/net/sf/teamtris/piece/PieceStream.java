package net.sf.teamtris.piece;

/**
 * The piece stream is the font of random pieces for the tetris game.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:57
 */
public interface PieceStream {

	/**
	 * Obtains the next random piece.
	 * @return The piece.
	 */
	public Piece getNextPiece();

}