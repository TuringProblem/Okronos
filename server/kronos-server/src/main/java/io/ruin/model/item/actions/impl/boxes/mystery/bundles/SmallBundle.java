package io.ruin.model.item.actions.impl.boxes.mystery.bundles;

import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;

public class SmallBundle extends ItemContainer {

	public static void register() {
		int slotsRequired = 6;
		ItemAction.registerInventory(30434, "search", (player, item) -> {
			if (player.getInventory().getFreeSlots() < slotsRequired) {
				player.sendMessage("You need atleast 6 free inventory slots free to open the ultra budnle.");
				return;
			}
			player.lock();
			player.closeDialogue();
			item.remove();
			player.getInventory().add(10600, 25);
			player.getInventory().add(30448, 2);
			player.getInventory().add(290, 3);
			player.unlock();
		});
	}
}
