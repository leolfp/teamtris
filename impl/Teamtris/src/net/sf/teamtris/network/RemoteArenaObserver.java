package net.sf.teamtris.network;
import java.util.List;
import java.util.Map;

import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.Status;

/**
 * The proxied remote arena observer.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:20
 */
public class RemoteArenaObserver implements ArenaObserver {

	@Override
	public void notifyLostPlayer(Player lostPlayer, Map<Player, Status> statuses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyRegisteredPlayer(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifySortedPlayers(List<Player> players) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyStartedGaming() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyUnregisteredPlayer(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyGrow(int lines) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyHeight(Player player, int height) {
		// TODO Auto-generated method stub
		
	}

}