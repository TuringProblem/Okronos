package io.ruin.model.activities.brimstonechest;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.actions.ObjectAction;

public class BrimstoneChest {

	private static final int CHEST = 34662;

	private static final int LARRAN_KEY_ID = 23083;

	public static void register() {
		ObjectAction.register(CHEST, 1, ((player, obj) -> openChest(player))); //TODO: Add options to object def
	}

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

	private static void openChest(Player player) {
		Item larrenKey = player.getInventory().findItem(LARRAN_KEY_ID);

		if (larrenKey == null) {
			player.sendFilteredMessage("You need a key to open this chest");
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.sendFilteredMessage("You unlock the Brimstone chest with your key");
			if (Random.rollPercent(getDonatorSaveChance(player)))
				player.sendMessage("<shad=3CB3DD>Your donator status allows you to save your key!");
			else
				larrenKey.remove(1);
			player.animate(536);
			Item loot = new BrimstoneLoot().rollItem();
			player.getInventory().addOrDrop(loot);
			event.delay(1);
			player.unlock();
		});
	}
}
