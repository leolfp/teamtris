package net.sf.teamtris.network.protocol;

/**
 * A simple factory for common client messages.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 19:17:54
 */
public class ClientMessageFactory {

	public static Message login(String name){
		Message message = new Message(MessageType.login);
		message.setString("name", name);
		return message;
	}

	public static Message starting(){
		Message message = new Message(MessageType.starting);
		return message;
	}
	
	public static Message height(int lines){
		Message message = new Message(MessageType.height);
		message.setInt("lines", lines);
		return message;
	}

	public static Message built(int lines){
		Message message = new Message(MessageType.built);
		message.setInt("lines", lines);
		return message;
	}
	
	public static Message pause(){
		Message message = new Message(MessageType.pause);
		return message;
	}

	public static Message resume(){
		Message message = new Message(MessageType.resume);
		return message;
	}

	public static Message lost(){
		Message message = new Message(MessageType.lost);
		return message;
	}

	public static Message logout(){
		Message message = new Message(MessageType.logout);
		return message;
	}

	public static Message talk(String messageStr){
		Message message = new Message(MessageType.talk);
		message.setString("message", messageStr);
		return message;
	}
	
}