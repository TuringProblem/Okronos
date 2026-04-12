package io.ruin.model.item.actions.impl.storage;

import io.ruin.cache.Color;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;

public class FamiliarStorage extends ItemContainer {

	public transient boolean deposit;

	{
		/**
		 * Fixes issue with bank not recognizing storage on login.
		 */
		sendAll = true;
	}

	public void openCheck() {
		if (player.wildernessLevel > 0) {
			player.sendMessage(Color.RED.wrap("You cannot use this feature in the wilderness."));
			return;
		}
		boolean left = false;
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			if (item != null) {
				int added = player.getInventory().add(item);
				if (added > 0) {
					if (added == item.getAmount()) {
						items[i] = null;
					} else {
						left = true;
						items[i].setAmount(items[i].getAmount() - added);
					}
				} else {
					left = true;
				}
			}
		}
		player.dialogue(new MessageDialogue(left ? "You withdraw as many items as you can, however there are still items remaining in your storage." : "You withdraw everything from your storage."));
	}

	public void openDeposit() {
		if (player.wildernessLevel > 0) {
			player.sendMessage(Color.RED.wrap("You cannot use this feature in the wilderness."));
			return;
		}
		deposit = true;
		player.openInterface(ToplevelComponent.INVENTORY_TAB_AREA, Interface.LOOTING_BAG);
		player.getPacketSender().sendIfEvents(Interface.LOOTING_BAG, 5, 0, 27, 542);
		player.getPacketSender().sendClientScript(495, "s1", "Familiar Storage", 1);
		player.getPacketSender().setHidden(Interface.LOOTING_BAG, 7, true);
	}

	public void deposit(Item item, int amount) {
		if (player.wildernessLevel > 0) {
			player.sendMessage(Color.RED.wrap("You cannot use this feature in the wilderness."));
			return;
		}
		if (!item.getDef().tradeable || item.getUniqueValue() != 0) {
			player.sendMessage("Only tradeable items can be put in the bag.");
			return;
		}
		if (item.move(item.getId(), amount, this) == 0)
			player.sendMessage("Not enough space in your storage.");
	}
}
