package io.ruin.model.map.object.actions.impl.dungeons;


import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

import static io.ruin.data.impl.teleports.teleport;

public class LizardShamans {
	public static void register() {
		ObjectAction.register(30380, "enter", (player, obj) -> teleport(player, new Position(1305, 9973, 0)));
		ObjectAction.register(30381, "squeeze-through", (player, obj) -> teleport(player, new Position(1309, 3574, 0)));
		ObjectAction.register(34405, "enter", (player, obj) -> teleport(player, new Position(1312, 10086, 0)));
		ObjectAction.register(34404, "enter", (player, obj) -> teleport(player, new Position(1330, 10070, 0)));
		ObjectAction.register(34403, "enter", (player, obj) -> teleport(player, new Position(1314, 10064, 0)));
		ObjectAction.register(34402, "enter", (player, obj) -> teleport(player, new Position(1292, 10058, 0)));
		ObjectAction.register(34422, "jump-in", (player, obj) -> teleport(player, new Position(1284, 3678, 0)));
	}
}
