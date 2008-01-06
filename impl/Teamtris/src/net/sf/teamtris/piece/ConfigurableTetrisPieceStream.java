package net.sf.teamtris.piece;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The piece stream that mimics the classic old style tetris pieces.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:41
 * @TODO Make this really configurable (reading pieces configuration from XML)
 */
public class ConfigurableTetrisPieceStream implements PieceStream {
	private final Random random;
	
	private final List<Piece> pieces = Collections.unmodifiableList(Arrays.asList(new Piece[]{
		Piece.newInstance("##:##", "fff799"), // The O, yellow
		Piece.newInstance("#:#:##", "fdc689"), // The L, orange
		Piece.newInstance(" #: #:##", "6dcff6"), // The J, blue
		Piece.newInstance(" #:###", "a186be"), // The T, purple
		Piece.newInstance(" ##:##", "a2d39c"), // The S, green
		Piece.newInstance("##: ##", "f5989d"), // The Z, red
		Piece.newInstance("####", "6dcff6"), // The I, cyan
	}));
	
	public ConfigurableTetrisPieceStream(long seed) {
		this.random = new Random(seed);
	}

	@Override
	public Piece getNextPiece() {
		return pieces.get(random.nextInt(pieces.size()));
	}

}