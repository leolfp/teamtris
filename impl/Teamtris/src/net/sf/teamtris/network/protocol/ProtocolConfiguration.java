package net.sf.teamtris.network.protocol;

import java.nio.charset.Charset;

/**
 * Default protocol configuration constants.
 * @author Leonardo
 * @version 1.0
 * @created 01-jan-2008 11:19:41
 */
public class ProtocolConfiguration {
	
	/**
	 * The socket port number.
	 */
	public static final int PORT = 1234;
	
	/**
	 * The socket streams' charset.
	 */
	public static final Charset CHARSET = Charset.forName("UTF-8");
	
}