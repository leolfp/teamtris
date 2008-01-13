package net.sf.teamtris.ui.serve;


public interface ServeGameUI {
	public void addMessage(String player, String messageStr);
	
	public void addError(String errorMessage);

	public void addPlayer(int id, String playerName, String origin);

	public void removePlayer(int id);
}
