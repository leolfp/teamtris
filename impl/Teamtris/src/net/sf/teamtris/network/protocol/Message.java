package net.sf.teamtris.network.protocol;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents the protocol message.
 * The serialized message is encoded in UTF-8 and obeys to the following grammar
 * (the regular expressions are in java syntax):
 * <blockquote><code>
 * Message ::= Type | Type Spaces Parameters<br>
 * Type ::= <i>([a-z]++)</i><br>
 * Spaces ::= <i>' '</i> OptionalSpaces<br>
 * OptionalSpaces ::= <i>([ ]*)</i><br>
 * Parameters ::= Parameter | Parameter Separator Parameters<br>
 * Parameter ::= ParameterName Equal ParameterValue<br>
 * ParameterName ::= <i>([a-zA-Z0-9\._\:]++)</i><br>
 * Equal ::= <i>'='</i><br>
 * ParameterValue ::= <i>([^;]*)</i><br>
 * Separator ::= <i>';'</i> OptionalSpaces</code></blockquote>
 * <p>The <code>Type</code> was intended to match simple lower case ASCII words.</p>
 * <p>The <code>ParameterName</code> was intended to match any usual java property key (like
 * <code>net.sf.teamtris.A_PROPERTY</code>).</p>
 * <p>If <code>ParameterValue</code> requires a semi-colon (<code>';'</code>), this must be scaped
 * preppending a single backslash (<code>'\\'</code>) to it. And also if <code>ParameterValue</code> ends
 * with a backslash, it must be duplicated to avoid conflict with a scaped semi-colon.</p>
 * <p>All message parameters are validated on parse/set against message type to ensure correctness.
 * On failure, these operations throw <code>ParseException</code>. This way, a <code>Message</code> instance
 * is always in a valid state (a good design constraint).</p>
 * <p>Message type and parameters are all case-sensitive.</p>
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 17:29:38
 */
public class Message {
	private MessageType type;
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Default message constructor.
	 * @param type The message type.
	 */
	public Message(MessageType type){
		this.type = type;
	}
	
	/**
	 * A message constructor that does parsing. This is intended to be called by the <code>parse</code> method.
	 * @param bareMessage The message string.
	 * @throws ParseException If the message is somehow invalid.
	 */
	private Message(String bareMessage) throws ParseException {
		int firstSpace = bareMessage.indexOf(' ');
		try {
			if(firstSpace != -1){
				this.type = MessageType.valueOf(bareMessage.substring(0, firstSpace));
			} else {
				this.type = MessageType.valueOf(bareMessage);
			}
		} catch (IllegalArgumentException e) {
			throw new ParseException("No message type for '" + bareMessage + "'.", 0);			
		}
		if(firstSpace != -1){
			// It seems bareMessage has parameters
			while(firstSpace < bareMessage.length() && bareMessage.charAt(firstSpace) == ' '){
				firstSpace++;
			}
			// Now, firstSpace is the first non-space after ignored spaces
			if(firstSpace < bareMessage.length()){
				String parametersString = bareMessage.substring(firstSpace);
				// Now we seek for non-scaped separators (;, without preppending \)
				String[] parametersSplitted = parametersString.split("(?<=[^\\\\]|\\\\\\\\);[ ]*");
				for(String parameter : parametersSplitted){
					parseParameter(parameter);
				}
			}
		}
	}

	private void parseParameter(String parameter) throws ParseException {
		if(parameter.endsWith("\\\\")){
			parameter = parameter.substring(0, parameter.length() - 1);
		}
		parameter = parameter.replaceAll("\\\\;", ";");
		int equal = parameter.indexOf('=');
		if(equal != -1){
			try {
				setString(parameter.substring(0, equal), equal + 1 < parameter.length() ? parameter.substring(equal + 1) : "");
			} catch(IllegalArgumentException e) {
				throw new ParseException("Invalid message parameter '" + parameter + "'.", 0);
			}
		} else {
			throw new ParseException("Fail to parse message parameter '" + parameter + "'.", 0);			
		}
	}
	
	/**
	 * Indicates if this message contains the given parameter.
	 * @param parameter The parameter to be checked.
	 * @return True if contains, false otherwise.
	 */
	public boolean containsParameter(String parameter){
		return parameters.containsKey(parameter);
	}

	/**
	 * Indicates if this message contains all the given parameters.
	 * @param parameters The parameters to be checked.
	 * @return True if contains, false otherwise.
	 */
	public boolean containsAllParameters(String[] parameters){
		for(String parameter : parameters){
			if(!containsParameter(parameter)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Sets the message parameter as an int.
	 * @param parameter The parameter name.
	 * @param value The parameter value.
	 */
	public void setInt(String parameter, int value){
		setString(parameter, ((Integer) value).toString());
	}

	/**
	 * Sets the message parameter as an int.
	 * @param parameter The parameter name.
	 * @param values The parameter array of values.
	 */
	public void setIntArray(String parameter, int[] values){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < values.length; ++i){
			if(i != 0){
				sb.append(",");
			}
			sb.append(values[i]);
		}
		setString(parameter, sb.toString());
	}
	
	/**
	 * Sets the message parameter as String.
	 * @param parameter The parameter name.
	 * @param value The parameter value.
	 */
	public void setString(String parameter, String value){
		if(getType().acceptParameter(parameter)){
			this.parameters.put(parameter, value);
		} else {
			throw new IllegalArgumentException("Invalid parameter for '" + type.name() +
					"' message: was '" + parameter + "', expected in " + Arrays.toString(type.getParameters()) + ".");
		}
	}
	
	/**
	 * Obtains a message parameter as an int.
	 * @param parameter The parameter name.
	 * @return The parameter value as int.
	 */
	public int getInt(String parameter){
		return Integer.parseInt(getString(parameter));
	}

	/**
	 * Obtains a message parameter as an array of ints.
	 * @param parameter The parameter name.
	 * @return The parameter value as array of ints.
	 */
	public int[] getIntArray(String parameter){
		String value = getString(parameter);
		if(value != null){
			String[] values = value.split(",");
			int[] valuesInt = new int[values.length];
			for(int i = 0; i < values.length; ++i){
				valuesInt[i] = Integer.parseInt(values[i]);
			}
			return valuesInt;
		}
		return null;
	}
	
	/**
	 * Obtains a message parameter as String.
	 * @param parameter The parameter name.
	 * @return The parameter value as String.
	 */
	public String getString(String parameter){
		if(this.parameters.containsKey(parameter)){
			return this.parameters.get(parameter);
		} else {
			throw new IllegalArgumentException("Missing parameter for '" + type.name() + "' message: was '" +
					parameter + "', expected in " + Arrays.toString(getParameters()) + ".");
		}
	}
	
	/**
	 * Obtains this message's parameter names.
	 * @return An array of parameter names.
	 */
	public String[] getParameters(){
		return this.parameters.keySet().toArray(new String[this.parameters.size()]);
	}

	/**
	 * Obtains this message' type.
	 * @return This message' type.
	 */
	public MessageType getType(){
		if(this.type != null){
			return this.type;
		} else {
			throw new IllegalStateException("Uninitialized message. Must parse first.");
		}
	}

	/**
	 * Parses the given message string into a new message. So, this method is a parser and also a factory.
	 * @param bareMessage The message string.
	 * @throws ParseException If the message is somehow invalid.
	 * @return A new parsed message.
	 */
	public static Message parse(String bareMessage) throws ParseException {
		return new Message(bareMessage);
	}
	
	/**
	 * Returns the string representation of this message.
	 * @return This message' string representation.
	 */
	public String serialize(){
		StringBuilder sb = new StringBuilder(getType().name());
		boolean first = true;
		
		for(Entry<String,String> entry : parameters.entrySet()){
			if(!first){
				sb.append(";");
			} else {
				sb.append(" ");
			}
			String value = entry.getValue().replaceAll(";", "\\;");
			if(value.endsWith("\\")){
				value += "\\";
			}
			sb.append(entry.getKey()).append("=").append(value);
			first = false;
		}
		
		return sb.toString();
	}

}