package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.Ladder;

public class MiningGuild {

	public static void register() {
		/**
		 * Ladders
		 */
		ObjectAction.register(11867, 3019, 3450, 0, "climb-down", (player, obj) -> Ladder.climb(player, 3020, 9850, 0, false, true, false));
		ObjectAction.register(17387, 3019, 9850, 0, "climb-up", (player, obj) -> Ladder.climb(player, 3018, 3450, 0, true, true, false));
	}
}
