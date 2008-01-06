package net.sf.teamtris.piece;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The entry point for the piece subsystem.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:51
 */
public class PieceManager {
	private static final List<String> pieceTypes = Collections.unmodifiableList(Arrays.asList(new String[] {"Classic"}));

	/**
	 * Creates a new initialized piece stream with the given configuration.
	 * @param type The stream type.
	 * @param seed The stream seed for random pieces sequence.
	 */
	public static PieceStream createPieceStream(String type, long seed){
		// TODO Implement this!
		return new ConfigurableTetrisPieceStream(seed);
	}

	/**
	 * Obtains all available piece stream types.
	 * @return A list containing all available piece stream types.
	 */
	public static List<String> getPieceTypes(){
		// TODO Implement this!
		return pieceTypes;
	}
}