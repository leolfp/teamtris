package net.sf.teamtris.network.protocol;

import java.text.ParseException;
import java.util.Properties;

/**
 * The protocol message encapsulator.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 17:29:38
 */
public class Message {
	private MessageType type;
	private Properties parameters = new Properties();
	
	/**
	 * Default message constructor.
	 * @param type The message type.
	 */
	public Message(MessageType type){
		this.type = type;
	}
	
	/**
	 * Default message constructor for unparsed messages.
	 */
	public Message(){
		this.type = null;
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
			this.parameters.setProperty(parameter, value);
		}
		throw new IllegalArgumentException("Invalid parameter '" + parameter + "' for '" + type.name() + "' message.");
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
		return this.parameters.getProperty(parameter);
	}

	/**
	 * Obtains this message' type.
	 * @return This message' type.
	 */
	public MessageType getType(){
		if(this.type != null)
			return this.type;
		throw new IllegalStateException("Uninitialized message. Must parse first.");
	}

	/**
	 * Parses the given message string into this message.
	 * @param bareMessage The message string.
	 */
	public void parse(String bareMessage) throws ParseException {
		// TODO Implement this!
	}

	/**
	 * Returns the string representation of this message.
	 * @return This message' string representation.
	 */
	public String serialize(){
		// TODO Implement this!
		return "";
	}

}