package net.sf.teamtris.arena;

/**
 * A bean that represents the scoring options for the game.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:59
 */
public class ScoringOptions {

	private final int singleLinePoints;
	private final int doubleLinePoints;
	private final int tripleLinePoints;
	private final int quadLinePoints;
	
	/**
	 * Default constructor for scoring options.
	 * @param singleLinePoints The points for single line.
	 * @param doubleLinePoints The points for double line.
	 * @param tripleLinePoints The points for triple line.
	 * @param quadLinePoints The points for quad line.
	 */
	public ScoringOptions(int singleLinePoints, int doubleLinePoints,
			int tripleLinePoints, int quadLinePoints) {
		this.singleLinePoints = singleLinePoints;
		this.doubleLinePoints = doubleLinePoints;
		this.tripleLinePoints = tripleLinePoints;
		this.quadLinePoints = quadLinePoints;
	}

	/**
	 * Obtains the points made when built a single line.
	 * @return The points for single line.
	 */
	public int getSingleLinePoints() {
		return singleLinePoints;
	}

	/**
	 * Obtains the points made when built a double line.
	 * @return The points for double line.
	 */
	public int getDoubleLinePoints() {
		return doubleLinePoints;
	}

	/**
	 * Obtains the points made when built a triple line.
	 * @return The points for triple line.
	 */
	public int getTripleLinePoints() {
		return tripleLinePoints;
	}

	/**
	 * Obtains the points made when built a quad line.
	 * @return The points for quad line.
	 */
	public int getQuadLinePoints() {
		return quadLinePoints;
	}

	/**
	 * Obtains the points score matrix.
	 * @return An array of points.
	 */
	public int[] getPoints(){
		return new int[] {singleLinePoints, doubleLinePoints, tripleLinePoints, quadLinePoints};
	}
}