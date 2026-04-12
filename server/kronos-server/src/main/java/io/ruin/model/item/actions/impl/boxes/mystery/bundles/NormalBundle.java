package io.ruin.model.item.actions.impl.boxes.mystery.bundles;

import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;

public class NormalBundle extends ItemContainer {

	public static void register() {
		int slotsRequired = 10;
		ItemAction.registerInventory(30433, "open", (player, item) -> {
			if (player.getInventory().getFreeSlots() < slotsRequired) {
				player.sendMessage("You need atleast 10 free inventory slots free to open the ultra budnle.");
				return;
			}
			player.lock();
			player.closeDialogue();
			item.remove();
			player.getInventory().add(10600, 50);
			player.getInventory().add(30185);
			player.getInventory().add(30448, 3);
			player.getInventory().add(290, 5);
			player.unlock();
		});
	}
}
