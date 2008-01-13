package net.sf.teamtris.ui.serve;

import java.util.List;
import java.util.Map;

import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.GameOptions;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.Status;

public class ServeGameArenaObserver implements ArenaObserver {
	
	private final ServeGameUI serveGameUI;
	private GameUI gameUI;
	
	public ServeGameArenaObserver(ServeGameUI serveGameUI){
		this.serveGameUI = serveGameUI;
	}
	
	public void setGameUI(GameUI gameUI){
		this.gameUI = gameUI;
	}

	@Override
	public void notifyChangedOptions(GameOptions options) {
		// Ignored for serving games
	}

	@Override
	public void notifyError(String errorMessage) {
		serveGameUI.addError(errorMessage);
	}

	@Override
	public void notifyGrow(int lines) {
		gameUI.grow(lines);		
	}

	@Override
	public void notifyHeight(Player player, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyPaused(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyRegisteredPlayer(Player player) {
		serveGameUI.addPlayer(player.getId(), player.getName(), player.getOrigin());		
	}

	@Override
	public void notifyResumed(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifySortedPlayers(List<Player> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStartGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStartedGaming(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStartedGaming() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStatus(Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyTalk(Player player, String message) {
		serveGameUI.addMessage(player.getName(), message);
	}

	@Override
	public void notifyUnregisteredPlayer(Player player) {
		serveGameUI.removePlayer(player.getId());
	}

	@Override
	public void notifyWinnerPlayer(Player winnerPlayer,
			Map<Player, Status> statuses) {
		// TODO Auto-generated method stub
		
	}
	
}
