package io.ruin.model.item.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

import java.util.Arrays;
import java.util.List;

public class EliteVoidKnightSet {
	private static void open(Player player) {
		List<Item> items = Arrays.asList(
			new Item(13072, 1),
			new Item(13073, 1),
			new Item(8842, 1),
			new Item(11664, 1),
			new Item(11663, 1),
			new Item(11665, 1)
		);
		if (player.getInventory().getFreeSlots() < items.size()) {
			player.sendMessage("You need at least " + items.size() + " free inventory slots to do this.");
			return;
		}
		player.getInventory().remove(11670, 1);
		items.forEach(player.getInventory()::addOrDrop);
	}

	public static void register() {
		ItemAction.registerInventory(11670, "open", (player, item) -> open(player));
	}
}
