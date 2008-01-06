package net.sf.teamtris.piece;

/**
 * The piece stream that mimics the classic old style tetris pieces. 
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:41
 */
public class ClassicTetrisPieceStream implements PieceStream {
	private final int seed;
	
	public ClassicTetrisPieceStream(int seed) {
		this.seed = seed;
	}

	@Override
	public Piece getNextPiece() {
		// TODO Auto-generated method stub
		return null;
	}

}