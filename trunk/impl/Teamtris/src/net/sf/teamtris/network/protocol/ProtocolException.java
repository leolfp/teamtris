package net.sf.teamtris.network.protocol;

/**
 * Exception threw when there is a protocol message exchange error.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 20:08:48
 */
public class ProtocolException extends Exception {

	private static final long serialVersionUID = -4931808231473126038L;

	/**
	 * Default protocol exception constructor.
	 * @param message The exception message.
	 * @param cause The exception cause.
	 */
	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default protocol exception constructor.
	 * @param message The exception message.
	 */
	public ProtocolException(String message) {
		super(message);
	}

}