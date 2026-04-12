package io.ruin.model.entity.doors;

import io.ruin.model.map.object.actions.ObjectAction;

public class Lighthouse {

	public static void register() {

		ObjectAction.register(4543, 2514, 4627, 0, "study", (player, obj) -> player.getMovement().teleport(2515, 4632, 0));
		ObjectAction.register(4544, 2515, 4627, 0, "study", (player, obj) -> player.getMovement().teleport(2515, 4632));

		ObjectAction.register(4546, 2513, 4627, 0, "open", (player, obj) -> player.getMovement().teleport(2515, 4632, 0));
		ObjectAction.register(4545, 2516, 4627, 0, "open", (player, obj) -> player.getMovement().teleport(2515, 4632));


		ObjectAction.register(4383, 2509, 3644, 0, "climb", (player, obj) -> player.getMovement().teleport(2520, 4618));
		ObjectAction.register(4412, 2519, 4618, 0, "climb", (player, obj) -> player.getMovement().teleport(2510, 3644));
		ObjectAction.register(4413, 2515, 4631, 0, "climb", (player, obj) -> player.getMovement().teleport(2515, 4625));

	}

}
