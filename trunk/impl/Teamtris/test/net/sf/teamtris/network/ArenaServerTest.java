package net.sf.teamtris.network;

import static org.junit.Assert.*;

import net.sf.teamtris.arena.ArenaManager;
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
		server.join();
	}

}
