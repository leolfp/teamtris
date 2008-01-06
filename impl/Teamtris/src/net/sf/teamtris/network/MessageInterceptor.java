package net.sf.teamtris.network;

import net.sf.teamtris.network.protocol.Message;

/**
 * A message interceptor for all incoming messages from the connection.
 * @author Leonardo
 * @version 1.0
 * @created 05-jan-2008 15:55:17
 */
public interface MessageInterceptor {
	/**
	 * Notifies that the expected message has arrived.
	 * @param message The message.
	 * @return True if is desired to continue receiving this type of message, false otherwise.
	 */
	public boolean arrived(Message message);
}
