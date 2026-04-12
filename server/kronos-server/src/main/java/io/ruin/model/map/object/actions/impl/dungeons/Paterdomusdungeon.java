package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.map.object.actions.ObjectAction;

public class Paterdomusdungeon {

	public static void register() {
		/**
		 * Open
		 */
		ObjectAction.register(3443, 3440, 9886, 0, "Pass-through", (player, obj) -> player.getMovement().teleport(3423, 3485, 0));

	}
}
