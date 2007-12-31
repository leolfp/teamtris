package net.sf.teamtris.network;
import java.util.List;

import net.sf.teamtris.arena.Arena;
import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.Game;
import net.sf.teamtris.arena.GameOptions;
import net.sf.teamtris.arena.Player;

/**
 * The proxied remote arena.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:15
 */
public class RemoteArena implements Arena {

	@Override
	public Game getGame(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameOptions getGameOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerPlayer(Player player, ArenaObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterPlayer(Player player) {
		// TODO Auto-generated method stub
		
	}

}