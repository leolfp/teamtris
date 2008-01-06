package net.sf.teamtris.network.protocol;

import java.util.Arrays;

/**
 * The protocol message types enumeration.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 17:29:44
 */
public enum MessageType {
	// General purpose
	talk(new String[]{"message"}),
	talked(new String[]{"id", "message"}),

	// Login phase
	welcome(new String[]{"server", "version"}),
	login(new String[]{"name"}),
	logged(new String[]{"id"}),

	// Negotiation phase
	in(new String[]{"id", "name", "origin"}),
	out(new String[]{"id"}),
	sorted(new String[]{"ids"}),
	options(new String[]{"stream", "seed", "level", "delay", "grow", "single", "double", "triple", "quad"}),
	
	// Starting phase
	start(),
	starting(),
	started(new String[]{"id"}),
	
	// Gaming phase
	status(new String[]{"id", "height", "points", "lines"}),
	grow(new String[]{"lines"}),
	height(new String[]{"lines"}),
	built(new String[]{"lines"}),
	pause(),
	paused(new String[]{"id"}),
	resume(),
	resumed(new String[]{"id"}),
	lost(),
	
	// Ending phase
	finish(new String[]{"winner"}),
	end(),
	error(new String[]{"type"}),
	logout(),
	bye(),
	;
	
	private final String[] parameters;
	
	private MessageType(){
		this(new String[0]);
	}
	
	private MessageType(String[] parameters) {
		Arrays.sort(parameters);
		this.parameters = parameters;
	}
	
	/**
	 * Obtains all possible parameters for this message type.
	 * @return A set containing this message possible parameters.
	 */
	public String[] getParameters(){
		return parameters;
	}

	/**
	 * Checks if this message type accepts the given parameter.
	 * @param parameter The parameter name to be checked.
	 * @return True if accepted, false otherwise.
	 */
	public boolean acceptParameter(String parameter){
		return Arrays.binarySearch(parameters, parameter) >= 0;
	}
}