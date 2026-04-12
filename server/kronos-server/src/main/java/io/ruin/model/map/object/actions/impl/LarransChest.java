package io.ruin.model.map.object.actions.impl;

import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.model.activities.wilderness.LarranChestLoot;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;

public class LarransChest {

	private static final int CHEST = 34832;

	private static final int LARRAN_KEY_ID = 23490;

	public static final LootTable COMMON = new LootTable().addTable(1,
		new LootItem(995, 750000, 3000000, 100),
		new LootItem(1618, 20, 30, 100),
		new LootItem(1620, 30, 50, 100),
		new LootItem(454, 300, 500, 100),
		new LootItem(445, 50, 120, 70),
		new LootItem(11237, 75, 175, 70),
		new LootItem(441, 200, 420, 70),
		new LootItem(1164, 2, 4, 40),
		new LootItem(1128, 1, 3, 40),
		new LootItem(995, 3000000, 4500000, 35),
		new LootItem(1080, 2, 4, 50),
		new LootItem(360, 150, 250, 250),
		new LootItem(378, 150, 250, 250),
		new LootItem(372, 150, 250, 250),
		new LootItem(384, 150, 250, 250),
		new LootItem(396, 150, 250, 100),
		new LootItem(390, 150, 250, 100),
		new LootItem(452, 15, 40, 100),
		new LootItem(2354, 200, 400, 100),
		new LootItem(1514, 75, 175, 100),
		new LootItem(11232, 50, 110, 100),
		new LootItem(5289, 2, 2, 100),
		new LootItem(5316, 2, 2, 100),
		new LootItem(22869, 2, 2, 100),
		new LootItem(22877, 2, 2, 100),
		new LootItem(22871, 2, 2, 100),
		new LootItem(5304, 3, 3, 100),
		new LootItem(5300, 3, 3, 100),
		new LootItem(5295, 3, 3, 100),
		new LootItem(7937, 3000, 5000, 100),
		new LootItem(24288, 1, 1, 18).broadcast(Broadcast.GLOBAL), // dagon 'hai
		new LootItem(24291, 1, 1, 18).broadcast(Broadcast.GLOBAL), // dagon 'hai
		new LootItem(24294, 1, 1, 18).broadcast(Broadcast.GLOBAL) // dagon 'hai
	);

	private static int getDonatorSaveChance(Player player) {
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				return 2;
			}
			case SUPER_DONATOR -> {
				return 5;
			}
			case ELITE_DONATOR -> {
				return 8;
			}
			case NOBLE_DONATOR -> {
				return 10;
			}
			case GOLD_DONATOR -> {
				return 12;
			}
			case PLATINUM_DONATOR -> {
				return 15;
			}
			case LEGENDARY_DONATOR -> {
				return 18;
			}
			case SUPREME_DONATOR -> {
				return 20;
			}
		}
		return 0;
	}

	public static void register() {
		ObjectAction.register(CHEST, 1, ((player, obj) -> openChest(player)));
	}

	private static final int ANNOUNCE_INTERVAL = 25;

	private static void openChest(Player player) {
		Item larranKey = player.getInventory().findItem(LARRAN_KEY_ID);

		if (larranKey == null) {
			player.sendFilteredMessage("You need a key to open this chest");
			return;
		}

		player.startEvent(event -> {
			player.lock();
			player.sendFilteredMessage("You unlock the Larran's chest with your key");
			if (player.larranChestsOpened % ANNOUNCE_INTERVAL == 0) {
				Broadcast.WORLD.sendNews(Icon.WILDERNESS,
					"</col>[<shad=8A0011>Wilderness</shad>] Larran's Chest has just been opened by " + player.getName() + "!");
			}
			player.larranChestsOpened++;
			if (player.larranChestsOpened == Achievements.WHAT_DO_WE_HAVE_HERE_II.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
					+ Achievements.WHAT_DO_WE_HAVE_HERE_II.getAchievementName());

			if (Random.rollPercent(getDonatorSaveChance(player)))
				player.sendMessage("<shad=3CB3DD>Your donator status allows you to save your key!");
			else
				larranKey.remove(1);
			player.animate(536);
			Item loot = new LarranChestLoot().rollItem();
			player.getInventory().addOrDrop(loot);
			player.addToCollectionLog(loot);
			event.delay(1);
			player.unlock();
		});
	}
}
