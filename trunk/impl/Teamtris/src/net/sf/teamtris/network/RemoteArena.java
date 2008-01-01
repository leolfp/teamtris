package net.sf.teamtris.network;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.teamtris.arena.Arena;
import net.sf.teamtris.arena.ArenaObserver;
import net.sf.teamtris.arena.Game;
import net.sf.teamtris.arena.GameOptions;
import net.sf.teamtris.arena.Player;
import net.sf.teamtris.arena.Status;
import net.sf.teamtris.piece.PieceStream;

/**
 * The proxied remote arena.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:01:15
 */
public class RemoteArena extends Thread implements Arena {
	
	private final Player player;
	private final ArenaObserver arenaObserver;
	private final List<Player> players = new ArrayList<Player>();
	
	RemoteArena(String server, Player player, ArenaObserver arenaObserver){
		this.player = player;
		this.arenaObserver = arenaObserver;
		// TODO Create socket
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Game getGame() {
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
		return Collections.unmodifiableList(players);
	}

	private class RemoteGame implements Game {

		@Override
		public Status builtLines(int howMany) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PieceStream getPieceStream() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Player getPlayer() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void lost() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void height(int lines) {
			// TODO Auto-generated method stub
			
		}

	}
	
}