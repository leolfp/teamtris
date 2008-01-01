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
	private final int levelChangePoints;
	private final int growDelay;

	/**
	 * The default constructor for game options.
	 * @param pieceStreamType The type of piece stream.
	 * @param pieceStreamSeed The seed to be used on random piece stream.
	 * @param levelChangePoints The divisor of score at which the level changes.
	 * @param growDelay The delay, in seconds, to wait before growing.
	 * @param scoringOptions The detailed scoring options.
	 */
	public GameOptions(String pieceStreamType, int pieceStreamSeed, int levelChangePoints, int growDelay, ScoringOptions scoringOptions) {
		this.pieceStreamType = pieceStreamType;
		this.pieceStreamSeed = pieceStreamSeed;
		this.levelChangePoints = levelChangePoints;
		this.growDelay = growDelay;
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
	 * Obtains the level change score factor.
	 * @return The score divisor at which the level changes.
	 */
	public int getLevelChangePoints() {
		return levelChangePoints;
	}

	/**
	 * Obtains the time to wait, in seconds, before applying a grow.
	 * @return The grow delay time.
	 */
	public int getGrowDelay() {
		return growDelay;
	}

	/**
	 * Obtains the scoring options.
	 * @return The scoring options.
	 */
	public ScoringOptions getScoringOptions() {
		return scoringOptions;
	}
	
}