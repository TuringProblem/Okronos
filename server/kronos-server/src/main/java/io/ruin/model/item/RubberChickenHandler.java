package io.ruin.model.item;

import io.ruin.model.item.actions.ItemAction;

public class RubberChickenHandler {
	final static int RubberChicken = 4566;

	public static void register() {
		ItemAction.registerEquipment(RubberChicken, "dance", (player, item) -> {
			player.startEvent(event -> {
				player.lock();
				player.animate(2068);
				event.delay(1);
				player.animate(2067);
				event.delay(1);
				player.unlock();
			});
		});
	}
}
