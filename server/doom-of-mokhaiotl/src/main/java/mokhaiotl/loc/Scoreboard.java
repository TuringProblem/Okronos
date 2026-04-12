package mokhaiotl.loc;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
 */
public class Scoreboard {

	private final static int SCOREBOARD_OBJECT_ID = 57288;

	public static void registerScoreboardActions() {
		ObjectAction.register(SCOREBOARD_OBJECT_ID, "Read", Scoreboard::readScoreboard);
		ObjectAction.register(SCOREBOARD_OBJECT_ID, "General-stats", Scoreboard::readGlobalStats);
		ObjectAction.register(SCOREBOARD_OBJECT_ID, "Delve-stats", Scoreboard::readDelveStats);
	}

	private static void readScoreboard(Player player, GameObject board) {

	}

	private static void readGlobalStats(Player player, GameObject board) {

	}

	private static void readDelveStats(Player player, GameObject board) {

	}

}
