package io.ruin.model.item.actions.impl.boxes.mystery.bundles;

import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;

public class UltraBundle extends ItemContainer {

	public static void register() {
		int slotsRequired = 16;
		ItemAction.registerInventory(30435, "open", (player, item) -> {
			if (player.getInventory().getFreeSlots() < slotsRequired) {
				player.sendMessage("You need atleast 16 free inventory slots free to open the ultra budnle.");
				return;
			}
			player.lock();
			player.closeDialogue();
			item.remove();
			player.getInventory().add(10600, 100);
			player.getInventory().add(30185);
			player.getInventory().add(30448, 5);
			player.getInventory().add(290, 8);
			player.getInventory().add(30426, 2);
			player.unlock();
		});
	}
}
