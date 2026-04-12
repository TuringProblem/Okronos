package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.object.actions.impl.Ladder;

public class WizardTower {

	public static void register() {
		/**
		 * Ladders
		 */
		ObjectAction.register(2147, 3104, 3162, 0, "climb-down", (player, obj) -> Ladder.climb(player, 3104, 9576, 0, false, true, false));
		ObjectAction.register(2148, 3103, 9576, 0, "climb-up", (player, obj) -> Ladder.climb(player, 3105, 3162, 0, true, true, false));
	}
}
