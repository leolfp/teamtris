package net.sf.teamtris.arena;

/**
 * A bean that collects the game options.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:38
 */
public class GameOptions {

	private final int pieceStreamSeed;
	private final String pieceStreamType;
	private final ScoringOptions scoringOptions;

	/**
	 * @param pieceStreamType
	 * @param pieceStreamSeed
	 * @param scoringOptions
	 */
	public GameOptions(String pieceStreamType, int pieceStreamSeed, ScoringOptions scoringOptions) {
		this.pieceStreamType = pieceStreamType;
		this.pieceStreamSeed = pieceStreamSeed;
		this.scoringOptions = scoringOptions;
	}

	/**
	 * Obtains the piece stream seed.
	 * @return The piece stream seed.
	 */
	public int getPieceStreamSeed() {
		return pieceStreamSeed;
	}
	
	/**
	 * Obtains the piece stream type.
	 * @return The piece stream type.
	 */
	public String getPieceStreamType() {
		return pieceStreamType;
	}
	
	/**
	 * Obtains the scoring options.
	 * @return The scoring options.
	 */
	public ScoringOptions getScoringOptions() {
		return scoringOptions;
	}

}