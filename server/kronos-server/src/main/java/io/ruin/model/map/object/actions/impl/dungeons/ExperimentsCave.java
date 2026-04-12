package io.ruin.model.map.object.actions.impl.dungeons;


import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

import static io.ruin.data.impl.teleports.teleport;

public class ExperimentsCave {

	public static void register() {
		ObjectAction.register(5167, 3578, 3527, 0, "push", ((player, obj) -> teleport(player, new Position(3577, 9927, 0))));
	}
}
