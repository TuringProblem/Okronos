package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

import static io.ruin.data.impl.teleports.teleport;

public class KeldagrimDungeon {

	public static void register() {
		ObjectAction.register(5014, 2771, 10161, 0, "enter", (player, obj) -> teleport(player, new Position(2730, 3713, 0)));
		ObjectAction.register(5008, 2731, 3712, 0, "enter", (player, obj) -> teleport(player, new Position(2773, 10162, 0)));
		ObjectAction.register(5973, 2781, 10161, 0, "go-through", (player, obj) -> teleport(player, new Position(2838, 10124, 0)));
		ObjectAction.register(5998, 2838, 10123, 0, "go-through", (player, obj) -> teleport(player, new Position(2780, 10161, 0)));
	}
}
