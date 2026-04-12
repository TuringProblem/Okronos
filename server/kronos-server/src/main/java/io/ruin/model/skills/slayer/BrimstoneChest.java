package io.ruin.model.skills.slayer;

import io.ruin.api.utils.JsonUtils;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.player.DonatorBonus;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.object.actions.ObjectAction;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
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

	private static final int BRIMSTONE_KEY = 23083;
	private static LootTable potentialRewards;

	public static void openChest(Player player) {
		int keyCount = player.getInventory().getAmount(BRIMSTONE_KEY);
		if (keyCount > 0) {
			if (player.getInventory().getFreeSlots() == 0 && keyCount > 1) {
				player.sendMessage("You don't have enough free slots to do that!");
				return;
			}
			player.privateSound(51);
			player.animate(536);
			if (Random.rollPercent(getDonatorSaveChance(player))) {
				player.sendFilteredMessage("<col=804080>Your key has been saved due to your donator rank.");
			} else {
				player.getInventory().remove(BRIMSTONE_KEY, 1);
			}

			Item item = potentialRewards.rollItem();
			if (item == null) {
				player.sendFilteredMessage("<col=804080>Your key has given you nothing.");
				return;
			}

			player.getInventory().add(item);
			World.getPlayerStream()
					.filter(plr -> plr != player)
					.filter(plr -> plr.getPosition().distance(player.getPosition()) < 15)
					.forEach(plr -> {
						plr.sendMessage(
								"<col=33cc33>" + player.getName() + " found " + item.getAmount() + " x " + item.getDef().name);
					});
			player.brimstoneChestsOpened++;
		}
	}

	public static void register() {
		try {
			potentialRewards =
					JsonUtils.fromJson(JsonUtils.fromFile(new File("./data/brimstone_chest.json")), LootTable.class);
		} catch (Exception ex) {
			log.error("Failed to load brimstone chest", ex);
		}

		ObjectAction.register(34662, 1, (player, obj) -> player.startEvent(mainEvent -> {
			Item brimstoneKey = player.getInventory().findItem(BRIMSTONE_KEY);
			if (brimstoneKey == null) {
				player.sendFilteredMessage("You need a special key that'll fit that keyhole to unlock the chest.");
				return;
			}

			player.startEvent(event -> {
				player.lock();
				openChest(player);
				event.delay(2);
				player.unlock();
			});
		}));
		ObjectAction.register(34662, 2,
				(player, obj) -> player.startEvent(mainEvent -> player.sendMessage("You have unlocked the brimstone chest "
						+ player.brimstoneChestsOpened + " times.")));
	}

}
