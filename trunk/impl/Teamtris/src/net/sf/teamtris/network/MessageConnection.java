package net.sf.teamtris.network;

import static net.sf.teamtris.network.protocol.ProtocolConfiguration.CHARSET;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.Socket;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.teamtris.network.protocol.Message;
import net.sf.teamtris.network.protocol.MessageType;
import net.sf.teamtris.network.protocol.ProtocolException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple socket wrapper for simplified message sending and receiving.
 * @author Leonardo
 * @version 1.0
 * @created 01-jan-2008 15:55:17
 */
public class MessageConnection {
	private static final Log log = LogFactory.getLog(MessageConnection.class);
	
	private final Socket connection;
	private final LineNumberReader reader;
	private final BufferedOutputStream writer;
	private final String connectionId;
	
	private final Map<MessageType, MessageInterceptor> interceptors = new HashMap<MessageType, MessageInterceptor>();

	/**
	 * The default constructor for a message socket wrapper.
	 * @param connection The underlying socket connection to be used.
	 * @param connectionId The connection Id.
	 * @throws IOException If there is a failure getting socket streams.
	 */
	public MessageConnection(Socket connection, String connectionId) throws IOException {
		this.connection = connection;
		this.connectionId = connectionId;
		reader = new LineNumberReader(new InputStreamReader(connection.getInputStream(), CHARSET));
		writer = new BufferedOutputStream(connection.getOutputStream());
	}
	
	/**
	 * Obtains the remote IP where this socket is connected to.
	 * @return The remote IP.
	 */
	public String getRemoteAddress(){
		return connection.getInetAddress().getHostAddress();
	}
	
	/**
	 * Closes the underlying socket.
	 * @throws IOException On error closing socket.
	 */
	public void close() {
		try {
			connection.close();
		} catch (IOException e) {
			log.fatal("IO error closing client socket on " + connectionId + ".", e);
		}
	}

	/**
	 * Writes the given message on the socket.
	 * @param message Message to be written.
	 * @throws IOException On underlying socket write failure.
	 */
	public void write(Message message) throws IOException {
		String serializedMessage = message.serialize();
		log.debug("Write on " + connectionId + ": " + serializedMessage);
		synchronized (writer) {
			writer.write((serializedMessage + "\r\n").getBytes(CHARSET));
		}
		writer.flush();
	}
	
	/**
	 * Reads the next message on the socket.
	 * @return The read message.
	 * @throws IOException On underlying socket read failure.
	 * @throws ParseException If there is an error parsing the received message.
	 */
	public Message read() throws ParseException, IOException {
		while(!Thread.currentThread().isInterrupted()){
			String bareMessage;
			synchronized (reader) {
				bareMessage = reader.readLine();
			}
			if(bareMessage != null){
				log.debug("Read on " + connectionId + ": " + bareMessage);
				Message message = Message.parse(bareMessage);
				synchronized (interceptors) {
					if(interceptors.containsKey(message.getType())){
						if(!interceptors.get(message.getType()).arrived(message)){
							interceptors.remove(message.getType());
						}
					} else {
						return message;
					}
				}
			} else {
				throw new IOException("End of input stream.");
			}
		}
		throw new IOException("Interrupted read.");
	}
	
	
	/**
	 * Reads the next message on the socket, only if it obeys the expected type.
	 * @param type The expected type.
	 * @param parameters The expected parameters.
	 * @return The read message.
	 * @throws IOException On underlying socket read failure.
	 * @throws ParseException If there is an error parsing the received message.
	 * @throws ProtocolException If the read message type differs from the type expected, or the expected
	 * 			parameters were not sent.
	 */
	public Message read(MessageType type, String[] parameters) throws ParseException, IOException, ProtocolException{
		Message message = read();
		if(message.getType() == type){
			if(parameters == null || message.containsAllParameters(parameters)){
				return message;
			} else {
				throw new ProtocolException("Missing required parameters on " + type.name() + " message: was " +
						Arrays.toString(message.getParameters()) + ", expected '"+ type.name() + "'.");
			}
		} else {
			throw new ProtocolException("Wrong message type: was '" + message.getType().name() + "', expected '"+ type.name() + "'.");
		}
	}
	
	/**
	 * Defines an interceptor for the given message type. The interceptor will be called whenever
	 * a message of that type is read on the connection (instead of the read methods).
	 * @param type The message type.
	 * @param interceptor The interceptor.
	 */
	public void setInterceptor(MessageType type, MessageInterceptor interceptor){
		synchronized (interceptors) {
			interceptors.put(type, interceptor);
		}
	}

	/**
	 * Removes the interceptor for the given type.
	 * @param type The type to be removed.
	 */
	public void removeInterceptor(MessageType type){
		synchronized (interceptors) {
			interceptors.remove(type);
		}
	}
}