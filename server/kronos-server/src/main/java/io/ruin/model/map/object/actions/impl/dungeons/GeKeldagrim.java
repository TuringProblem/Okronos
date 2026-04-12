package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

import static io.ruin.data.impl.teleports.teleport;

public class GeKeldagrim {

	public static void register() {
		ObjectAction.register(16168, "travel", (player, obj) -> teleport(player, new Position(2909, 10174, 0)));
	}
}
