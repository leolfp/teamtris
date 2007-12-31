package net.sf.teamtris.arena;

/**
 * The player bean.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:54
 */
public class Player {
	private static final String LOCAL_ORIGIN = "local";
	
	private final String name;
	private final String origin;

	/**
	 * The default player constructor, for remote players.
	 * @param name Player's name.
	 * @param origin Player's origin (usually a hostname).
	 */
	public Player(String name, String origin) {
		this.name = name;
		this.origin = origin;
	}

	/**
	 * The default player constructor, for local players.
	 * @param name Player's name.
	 */
	public Player(String name){
		this(name, LOCAL_ORIGIN);
	}

	/**
	 * Obtains the player's name.
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtains the player's origin.
	 * @return The player's origin.
	 */
	public String getOrigin() {
		return origin;
	}

}