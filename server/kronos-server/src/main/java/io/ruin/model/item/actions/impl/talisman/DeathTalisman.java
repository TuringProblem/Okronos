package io.ruin.model.item.actions.impl.talisman;

import io.ruin.model.item.actions.ItemAction;


public class DeathTalisman {


	public static void register() {
		ItemAction.registerInventory(1456, "Locate", (player, item) -> {
			player.getMovement().startTeleport(20, event -> {
				player.animate(714);
				player.graphics(111, 92, 0);
				player.publicSound(200);
				event.delay(3);
				player.getMovement().teleport(2207, 4832, 0);

			});
		});

	}
}
