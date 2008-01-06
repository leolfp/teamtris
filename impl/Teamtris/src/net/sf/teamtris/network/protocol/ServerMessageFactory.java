package net.sf.teamtris.network.protocol;

import static net.sf.teamtris.network.protocol.ProtocolConfiguration.SERVER_NAME;
import static net.sf.teamtris.network.protocol.ProtocolConfiguration.VERSION;

/**
 * A simple factory for common server messages.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 19:13:06
 */
public class ServerMessageFactory {

	public static Message welcome(){
		Message message = new Message(MessageType.welcome);
		message.setString("server", SERVER_NAME);
		message.setString("version", VERSION);
		return message;
	}
	
	public static Message logged(int id){
		Message message = new Message(MessageType.logged);
		message.setInt("id", id);
		return message;
	}

	public static Message in(int id, String name, String origin){
		Message message = new Message(MessageType.in);
		message.setInt("id", id);
		message.setString("name", name);
		message.setString("origin", origin);
		return message;
	}

	public static Message out(int id){
		Message message = new Message(MessageType.out);
		message.setInt("id", id);
		return message;
	}

	public static Message sorted(int[] ids){
		Message message = new Message(MessageType.sorted);
		message.setIntArray("ids", ids);
		return message;
	}

	public static Message options(String stream, int seed, int level, int delay, int[] scoring){
		Message message = new Message(MessageType.options);
		message.setString("stream", stream);
		message.setInt("seed", seed);
		message.setInt("level", level);
		message.setInt("delay", delay);
		message.setInt("single", scoring[0]);
		message.setInt("double", scoring[1]);
		message.setInt("triple", scoring[2]);
		message.setInt("quad", scoring[3]);
		return message;
	}

	public static Message start(){
		Message message = new Message(MessageType.start);
		return message;
	}

	public static Message started(int id){
		Message message = new Message(MessageType.started);
		message.setInt("id", id);
		return message;
	}

	public static Message status(int id, int height){
		Message message = new Message(MessageType.status);
		message.setInt("id", id);
		message.setInt("height", height);
		return message;
	}
	
	public static Message statusOnBuilt(int id, int lines, long points){
		Message message = new Message(MessageType.status);
		message.setInt("id", id);
		message.setInt("lines", lines);
		message.setLong("points", points);
		return message;
	}

	public static Message statusOnFinish(int id, int height, int lines, long points){
		Message message = new Message(MessageType.status);
		message.setInt("id", id);
		message.setInt("height", height);
		message.setInt("lines", lines);
		message.setLong("points", points);
		return message;
	}

	public static Message grow(int lines){
		Message message = new Message(MessageType.grow);
		message.setInt("lines", lines);
		return message;
	}
	
	public static Message paused(int id){
		Message message = new Message(MessageType.paused);
		message.setInt("id", id);
		return message;
	}
	
	public static Message resumed(int id){
		Message message = new Message(MessageType.resumed);
		message.setInt("id", id);
		return message;
	}

	public static Message finish(int winner){
		Message message = new Message(MessageType.finish);
		message.setInt("winner", winner);
		return message;
	}

	public static Message end(){
		Message message = new Message(MessageType.end);
		return message;
	}

	public static Message bye(){
		Message message = new Message(MessageType.bye);
		return message;
	}

	public static Message error(String type){
		Message message = new Message(MessageType.error);
		message.setString("type", type);
		return message;
	}

}