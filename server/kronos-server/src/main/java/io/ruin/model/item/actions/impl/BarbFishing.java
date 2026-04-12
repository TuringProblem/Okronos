package io.ruin.model.item.actions.impl;

import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.skills.Tool;
import io.ruin.model.stat.StatType;

public class BarbFishing {
	private static final int LEAPING_TROUT = 11328;

	private static final int LEAPING_SALMON = 11330;

	private static final int LEAPING_STURGEON = 11332;

	public static final LootTable chanceTable = new LootTable().addTable(1,
		new LootItem(11324, 1, 1, 16), // Roe
		new LootItem(11334, 1, 1, 16) // Fish offcuts

	);

	public static final LootTable sturgeonTable = new LootTable().addTable(1,
		new LootItem(11326, 1, 1, 16), // Caviar
		new LootItem(11334, 1, 1, 16) // Fish offcuts

	);

	public static void register() {
		ItemItemAction.register(LEAPING_TROUT, Tool.KNIFE, (player, primary, secondary) -> {
			Item loot = chanceTable.rollItem();
			player.startEvent(event -> {
				player.lock();
				player.animate(7151);
				player.getInventory().remove(primary.getId(), 1);
				player.getInventory().add(loot);
				player.sendFilteredMessage("You dissect the trout and manage to gain " + loot.getDef().name + ".");
				player.getStats().addXp(StatType.Cooking, 10, true);
				event.delay(1);
				player.unlock();
			});
		});
		ItemItemAction.register(LEAPING_SALMON, Tool.KNIFE, (player, primary, secondary) -> {
			Item loot = chanceTable.rollItem();
			player.startEvent(event -> {
				player.lock();
				player.animate(7151);
				player.getInventory().remove(primary.getId(), 1);
				player.getInventory().add(loot);
				player.sendFilteredMessage("You dissect the salmon and manage to gain " + loot.getDef().name + ".");
				player.getStats().addXp(StatType.Cooking, 10, true);
				event.delay(1);
				player.unlock();
			});
		});
		ItemItemAction.register(LEAPING_STURGEON, Tool.KNIFE, (player, primary, secondary) -> {
			Item loot = sturgeonTable.rollItem();
			player.startEvent(event -> {
				player.lock();
				player.animate(7151);
				player.getInventory().remove(primary.getId(), 1);
				player.getInventory().add(loot);
				player.sendFilteredMessage("You dissect the sturgeon and manage to gain " + loot.getDef().name + ".");
				player.getStats().addXp(StatType.Cooking, 15, true);
				event.delay(1);
				player.unlock();
			});
		});
	}
}
