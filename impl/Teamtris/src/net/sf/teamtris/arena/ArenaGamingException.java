package net.sf.teamtris.arena;

/**
 * A generic exception threw by the arena when there is an error or problem.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:05
 */
public class ArenaGamingException extends Exception {

	private static final long serialVersionUID = -4628001411030615044L;

	/**
	 * Default arena exception constructor.
	 * @param message The exception message.
	 * @param cause The exception cause.
	 */
	public ArenaGamingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default arena exception constructor.
	 * @param message The exception message.
	 */
	public ArenaGamingException(String message) {
		super(message);
	}

}
