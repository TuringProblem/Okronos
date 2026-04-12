package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.model.map.object.actions.ObjectAction;

public class ObjectHook {

	public static void register() {
		ObjectAction.register(42819, "enter", (player, obj) -> {
			if (player.isGroupIronman() && player.getPosition().regionId() == 12342) {
				player.getMovement().teleport(3761, 3668, 0);
			} else {
				player.sendMessage("You must be a group ironman to enter this portal.");
			}
		});
		ObjectAction.register(42820, "enter", (player, obj) -> {
			if (player.isGroupIronman()) {
				if (player.getPosition().regionId() == 14905)
					player.getMovement().teleport(3093, 3479, 0);
				else
					player.getMovement().teleport(3761, 3668, 0);
			} else {
				player.sendMessage("You must be a group ironman to enter this portal.");
			}
		});
	}
}
