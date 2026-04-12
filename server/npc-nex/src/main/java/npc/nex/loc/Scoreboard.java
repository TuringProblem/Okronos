package npc.nex.loc;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class Scoreboard {

	private static final int ANCIENT_SCOREBOARD_ID = 42936;

	public static void register() {
		ObjectAction.register(ANCIENT_SCOREBOARD_ID, "Read", Scoreboard::readAction);
	}

	public static void readAction(Player player, GameObject barrier) {

	}
}
