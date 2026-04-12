package io.ruin.model.item.actions.impl;

import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.skills.Tool;

public class SacredEel {
	private static final int SACRED_EEL = 13339;

	public static final LootTable chanceTable = new LootTable().addTable(1,
		new LootItem(12934, 3, 9, 16) // Zulrah scales

	);

	public static void register() {
		ItemItemAction.register(SACRED_EEL, Tool.KNIFE, (player, primary, secondary) -> {
			Item loot = chanceTable.rollItem();
			player.startEvent(event -> {
				player.lock();
				player.animate(7151);
				player.getInventory().remove(primary.getId(), 1);
				player.getInventory().add(loot);
				player.sendFilteredMessage("You dissect the eel and manage to gain " + loot.getDef().name + ".");
				player.unlock();
			});
		});
	}
}
