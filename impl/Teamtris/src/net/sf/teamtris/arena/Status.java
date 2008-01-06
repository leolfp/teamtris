package net.sf.teamtris.arena;

/**
 * A bean that represents the status for a given game.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:05
 */
public class Status implements Comparable<Status> {

	private int lines = 0;
	private long points = 0;
	private int speed = 0;
	private int totalBuiltLines = 0;
	
	private final GameOptions gameOptions;

	/**
	 * The default constructor for a game status, to be used by game logic control.
	 */
	public Status(GameOptions gameOptions){
		this.gameOptions = gameOptions;
	}

	/**
	 * The default constructor for a game status, to be used on status propagation.
	 */
	public Status(int lines, long points, int totalBuiltLines){
		this.lines = lines;
		this.points = points;
		this.totalBuiltLines = totalBuiltLines;
		this.gameOptions = null;
	}
	
	/**
	 * Obtains the number of lines on the board.
	 * @return The number of lines on the board.
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * Obtains the points made by the player.
	 * @return The points made by the player.
	 */
	public long getPoints() {
		return points;
	}

	/**
	 * Obtains the game speed.
	 * @return The game speed.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Obtains the number of lines already built on this game.
	 * @return The number of built lines.
	 */
	public int getTotalBuiltLines() {
		return totalBuiltLines;
	}

	/**
	 * Alters the status given the user has built lines.
	 * @param lines The number of lines built by the user.
	 */
	public void built(int lines){
		if(gameOptions != null){
			this.totalBuiltLines += lines;
			long oldPoints = this.points;
			this.points += gameOptions.getScoringOptions().getPoints()[lines - 1];
			if((oldPoints + 1) / gameOptions.getLevelChangePoints() < (this.points + 1) / gameOptions.getLevelChangePoints()){
				// We changed the level
				this.speed++;
			}
		} else throw new UnsupportedOperationException("This status is not meant to be changed.");
	}

	/**
	 * Defines the number of lines on the board.
	 * @param lines The new number of lines on the board.
	 */
	public void setLines(int lines) {
		if(gameOptions != null){
			this.lines = lines;
		} else throw new UnsupportedOperationException("This status is not meant to be changed.");
	}

	@Override
	public int compareTo(Status other) {
		// Reverse order: the first, the winner
		// -1 if this < other (this has more points than other)
		// 0 if this == other
		// 1 if this > other (this has less points than other)
		
		// Who made more points with less lines wins
		if(((Long) this.points).compareTo(other.points) != 0){
			return -((Long) this.points).compareTo(other.points);
		} else {
			return ((Integer) this.totalBuiltLines).compareTo(other.totalBuiltLines);
		}
	}
	
}