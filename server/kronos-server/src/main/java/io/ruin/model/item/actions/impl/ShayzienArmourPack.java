package io.ruin.model.item.actions.impl;

import io.ruin.model.item.actions.ItemAction;

public class ShayzienArmourPack {

	public static void register() {
		ItemAction.registerInventory(13569, "open", (player, item) -> {
			if (player.getInventory().getFreeSlots() < 5) {
				player.sendMessage("You have no space in your inventory");
				return;
			}
			player.getInventory().remove(item.getId(), 1);
			player.getInventory().add(13377, 1);
			player.getInventory().add(13378, 1);
			player.getInventory().add(13379, 1);
			player.getInventory().add(13380, 1);
			player.getInventory().add(13381, 1);
		});
	}

}
