package net.sf.teamtris.arena;

/**
 * The entry point for arena subsystem.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:33
 */
public class ArenaManager {

	/**
	 * Obtains a local serving arena.
	 * @return The singleton instance of local serving arena.
	 */
	public static ServingArena getServingArena(){
		// TODO Implement this!
		return new LocalServingArena();
	}

}