package io.ruin.model.map.object.actions.impl.fossilisland;

import io.ruin.model.map.object.actions.ObjectAction;

public class HouseOnTheHillObjects {

	public static void register() {
		ObjectAction.register(30682, 3754, 3868, 1, "descend", (player, obj) -> player.getMovement().teleport(3753, 3869));
		ObjectAction.register(30681, 3754, 3868, 0, "climb", (player, obj) -> player.getMovement().teleport(3757, 3869));

	}
}
