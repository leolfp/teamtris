package net.sf.teamtris.piece;

/**
 * The entry point for the piece subsystem.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:51
 */
public class PieceManager {

	/**
	 * Creates a new initialized piece stream with the given configuration.
	 * @param type The stream type.
	 * @param seed The stream seed for random pieces sequence.
	 */
	public static PieceStream createPieceStream(String type, int seed){
		// TODO Implement this!
		return new ClassicTetrisPieceStream(seed);
	}

}