package net.sf.teamtris.arena;

/**
 * A bean that represents the status for a given game.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:05
 */
public class Status {

	private int lines = 0;
	private long points = 0;
	private int speed = 0;
	private int totalBuiltLines = 0;

	/**
	 * The default constructor for a game status.
	 */
	public Status(){
		// Nothing else to do.
	}

	/**
	 * Obtains the number of lines on the board.
	 * @return The number of lines on the board.
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * Sets the number of lines on the board.
	 * @param lines The number of lines on the board.
	 */
	public void setLines(int lines) {
		this.lines = lines;
	}

	/**
	 * Obtains the points made by the player.
	 * @return The points made by the player.
	 */
	public long getPoints() {
		return points;
	}

	/**
	 * Sets the points made by the player.
	 * @param points The points made by the player.
	 */
	public void setPoints(long points) {
		this.points = points;
	}

	/**
	 * Obtains the game speed.
	 * @return The game speed.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets the game speed.
	 * @param speed The game speed.
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Obtains the number of lines already built on this game.
	 * @return The number of built lines.
	 */
	public int getTotalBuiltLines() {
		return totalBuiltLines;
	}

	/**
	 * Sets the number of lines already built on this game.
	 * @param totalBuiltLines The number of built lines.
	 */
	public void setTotalBuiltLines(int totalBuiltLines) {
		this.totalBuiltLines = totalBuiltLines;
	}
}