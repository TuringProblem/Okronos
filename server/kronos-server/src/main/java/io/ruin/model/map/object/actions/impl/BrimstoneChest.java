package io.ruin.model.map.object.actions.impl;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;


public class BrimstoneChest {

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


	public static final LootTable BRIMSTONE_TABLE = new LootTable().addTable(1,

		new LootItem(995, 50_000, 150_000, 1),
		new LootItem(1618, 20, 40, 1),
		new LootItem(1620, 20, 40, 1),
		new LootItem(454, 200, 350, 1),
		new LootItem(445, 50, 100, 1),
		new LootItem(11237, 70, 150, 1),
		new LootItem(441, 200, 350, 1),
		new LootItem(1164, 7, 12, 1),
		new LootItem(1128, 8, 12, 1),
		new LootItem(1080, 7, 12, 1),
		new LootItem(360, 150, 300, 1),
		new LootItem(378, 150, 300, 1),
		new LootItem(372, 150, 300, 1),
		new LootItem(384, 150, 300, 1),
		new LootItem(396, 150, 300, 1),
		new LootItem(390, 150, 300, 1),
		new LootItem(452, 10, 15, 1),
		new LootItem(2354, 150, 200, 1),
		new LootItem(1514, 100, 200, 1),
		new LootItem(11232, 40, 88, 1),
		new LootItem(5289, 10, 22, 1),
		new LootItem(5316, 1, 4, 1),
		new LootItem(22869, 10, 22, 1),
		new LootItem(22877, 10, 22, 1),
		new LootItem(22871, 10, 22, 1),
		new LootItem(5304, 10, 22, 1),
		new LootItem(5300, 8, 14, 1),
		new LootItem(5295, 10, 18, 1),
		new LootItem(7937, 3000, 6000, 1));

	public static final LootTable RARE = new LootTable().addTable(1,

		new LootItem(22963, 1, 1, 1),
		new LootItem(23047, 1, 1, 1),
		new LootItem(23050, 1, 1, 1),
		new LootItem(23053, 1, 1, 1),
		new LootItem(23056, 1, 1, 1),
		new LootItem(23059, 1, 1, 1)


	);


	public static void register() {
		ObjectAction.register(34660, "unlock", (player, obj) -> {
			Item brimstoneKey = player.getInventory().findItem(23083);
			if (brimstoneKey == null) {
				player.sendFilteredMessage("You need a brimstone key to open this chest.");
				return;
			}

			player.startEvent(event -> {
				player.lock();
				player.sendFilteredMessage("You unlock the Brimstone chest with your key.");
				if (Random.rollPercent(getDonatorSaveChance(player))) {
					player.sendMessage("<shad=3CB3DD>Your donator status allows you to save your key.");
				} else {
					player.getInventory().remove(23083, 1);
				}
				player.privateSound(51);
				player.animate(536);
				player.brimstoneChestsOpened++;
				World.startEvent(e -> {
					obj.setId(34661);
					e.delay(2);
					obj.setId(obj.originalId);
				});
				if (Random.get() <= 0.02) {
					/**
					 * Rare loot
					 */
					Item loot = RARE.rollItem();
					player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
					player.addToCollectionLog(loot);
					Broadcast.WORLD.sendNews(player.getName() + " just received " + loot.getDef().descriptiveName + " from the Brimstone chest on [Chest #" + player.brimstoneChestsOpened + "]!");
				} else {
					/**
					 * Regular loot
					 */
					Item loot = BRIMSTONE_TABLE.rollItem();
					player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
				}
				event.delay(1);
				player.unlock();
			});
		});
		ObjectAction.register(34660, "Check", (player, obj) -> {
			player.sendMessage(" You have opened " + player.brimstoneChestsOpened + " Brimstone chests.");
		});

	}
}
