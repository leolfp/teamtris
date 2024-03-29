package net.sf.teamtris.ui.start;


public interface StartGameUI {
	public void addMessage(String player, String messageStr);
	
	public void addError(String errorMessage);

	public void addPlayer(int id, String playerName, String origin);

	public void removePlayer(int id);
	
	public void sortPlayers(int[] ids);
}
