package player.mongo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.ruin.model.stat.Stat;
import io.ruin.test.ServerTest;

public class PlayerMongoTests {

	@BeforeAll
	static void startup() {
		ServerTest.start();
	}

	@Test
	void connects() throws Exception {
		Connection.get();
		var player = ServerTest.createPlayer();
		player.dummyStats = new Stat[1];
		player.dummyStats[0] = new Stat(1);
		player.dummyStats[0].setExperience(10);
		SaveScheduler.queue(player);
		while (SaveScheduler.pendingSaves() != 0) {
			Thread.sleep(1);
		}
		ServerTest.shutdown();

	}
}
