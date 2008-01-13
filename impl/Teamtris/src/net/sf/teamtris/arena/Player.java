package net.sf.teamtris.arena;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The player bean.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:54
 */
public class Player {
	private static final String LOCAL_ORIGIN = "<local>";
	
	private static final AtomicInteger idGenerator = new AtomicInteger(0); 

	private final int id;
	private final String name;
	private final String origin;
	private final String type;

	/**
	 * The default player constructor, for remote served players.
	 * @param name Player's name.
	 * @param origin Player's origin (usually an IP).
	 */
	public Player(String name, String origin) {
		this.name = name;
		this.origin = origin;
		this.id = idGenerator.incrementAndGet();
		this.type = "remote";
	}

	/**
	 * The default player constructor, for local players.
	 * @param name Player's name.
	 */
	public Player(String name){
		this.name = name;
		this.origin = LOCAL_ORIGIN;
		this.id = idGenerator.incrementAndGet();
		this.type = "local";
	}

	/**
	 * The default player constructor, for proxied players.
	 * @param name Player's name.
	 * @param origin Player's origin (usually an IP).
	 * @param id Player's id.
	 */
	public Player(String name, String origin, int id){
		this.name = name;
		this.origin = origin;
		this.id = id;
		this.type = "proxied";
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
	
	/**
	 * Obtains this player's unique identifier.
	 * @return This player's id.
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Returns this player's type.
	 * @return Any of remote, local or proxied.
	 */
	public String getType(){
		return type;
	}

}