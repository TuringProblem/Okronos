package io.ruin.model.activities.bosses.nightmare;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.*;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.object.actions.ObjectAction;

public class EnergyBarrier {
	public static void register() {
		ObjectAction.register(37730, "Pass-through", (player, obj) -> exit(player));
	}

	private static void exit(Player player) {
		player.dialogue(new MessageDialogue("Would you like to exit?"),
			new OptionsDialogue(
				new Option("Yes.", () -> {
					player.getMovement().teleport(3808, 9749, 1);
					player.currentDynamicMap = null;
					player.inDynamicMap = false;
				}),
				new Option("No.")));
		return;
	}

}
