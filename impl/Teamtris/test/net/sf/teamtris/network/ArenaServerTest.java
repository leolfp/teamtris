package net.sf.teamtris.network;

import static org.junit.Assert.*;

import net.sf.teamtris.arena.ArenaManager;
import net.sf.teamtris.arena.GameOptions;
import net.sf.teamtris.arena.ScoringOptions;
import net.sf.teamtris.arena.ServingArena;

import org.junit.BeforeClass;
import org.junit.Test;

public class ArenaServerTest {
	
	private static ServingArena arena;

	@BeforeClass
	public static void globalSetUp(){
		arena = ArenaManager.getServingArena();
		assertNotNull("Arena null", arena);
	}
	
	@Test
	public void testRun() throws InterruptedException {
		ArenaServer server = new ArenaServer(arena);
		server.start();
		Thread.sleep(20 * 1000);
		arena.configure(new GameOptions("classic", 123, 5000, 3, true, new ScoringOptions(100, 300, 600, 1000)));
		Thread.sleep(10 * 1000);
		arena.sortPlayers();
		Thread.sleep(10 * 1000);
		arena.startGaming();
		server.join();
	}

}
