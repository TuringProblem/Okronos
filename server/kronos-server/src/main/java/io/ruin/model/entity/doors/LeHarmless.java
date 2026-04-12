package io.ruin.model.entity.doors;

import io.ruin.model.map.object.actions.ObjectAction;

public class LeHarmless {

	public static void register() {

		ObjectAction.register(5553, 3749, 9373, 0, "exit", (player, obj) -> player.getMovement().teleport(3749, 2973, 0));


		ObjectAction.register(6702, 3749, 9374, 0, "exit", (player, obj) -> player.getMovement().teleport(3749, 2973, 0));
		ObjectAction.register(3650, 3748, 2973, 0, "enter", (player, obj) -> player.getMovement().teleport(3748, 9374));

	}

}
