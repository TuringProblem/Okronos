package io.ruin.model.map.object.actions.impl.edgeville;

import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;

import java.util.concurrent.atomic.AtomicInteger;

public class CrystalKeyChest {

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

	private static final Item[] COMMON_LOOT = {
		new Item(1632, 25), //Uncut dragon stones
		new Item(21905, 50), //Dragon bolts
		new Item(11230, 50), //Dragon dart
		new Item(23962, 10), //Crystal shard
		new Item(989, 2), //Crystal keys
		new Item(1128, 10), //Rune platebody
		new Item(1080, 10), //Rune platelegs
		new Item(5316, 2), //Magic seed
		new Item(537, 60), //Dragon bones
		new Item(3025, 25), //Super restores
		new Item(6686, 25), //Saradomin Brews
		new Item(1392, 150), //Battlestaff
		new Item(570, 150), //Fire orb
		new Item(572, 150), //Water orb
		new Item(392, 125), //Manta ray
		new Item(2362, 125), //Adamant bars
		new Item(2364, 50), //Runite bars
		new Item(1988, 500), //Grapes
		new Item(5300, 10), //Snapdragon seed
		new Item(5295, 15), //Ranarr seed
		new Item(220, 10), //Grimy Torstol
		new Item(20545, 1), //Medium clue reward casket
		new Item(560, 500), //Death runes
		new Item(565, 500), //Blood runes
	};
	private static final Item[][] RUNE_LOOTS = {
		new Item[]{
			new Item(560, 1000), //Death runes
			new Item(565, 1000), //Blood Runes
			new Item(566, 1000), //Soul Runes
			new Item(21880, 500) //Wrath runes
		}
	};
	private static final Item[] UNCOMMON_LOOT = {
		new Item(537, 150), //Dragon bones
		new Item(11840, 1), //Dragon boots
		new Item(8783, 250), //Mahogany planks
		new Item(11944, 50), //Lava dragon bones
		new Item(6739, 1), //Dragon axe
		new Item(12914, 10), //Anti venom+
		new Item(23962, 50), //Crystal shard
		new Item(4207, 3), //Dragon platelegs
		new Item(3205, 3), //Dragon Halberd
		new Item(9193, 500), //Dragonstone bolt tips
		new Item(21905, 250), //Dragon bolts
		new Item(11230, 250), //Dragon darts
		new Item(19484, 500), //Dragon javelins
		new Item(20544, 2), //Hard clue reward casket
		new Item(12829, 1), //Spirit shield
		new Item(2510, 150), //Black dragon leather
		new Item(21880, 1500), //Wrath runes
	};
	private static final Item[] RARE_LOOT = {
		new Item(6735, 1), //Warrior ring
		new Item(6737, 1), //Berserker ring
		new Item(6731, 1), //Seers ring
		new Item(6733, 1), //Archers ring
		new Item(23962, 100), //Crystal shard
		new Item(22125, 75), //Superior dragon bones
		new Item(11230, 500), //Dragon dart
		new Item(21905, 500) //Dragon bolts
	};

	private static final Item[] VERY_RARE_LOOT = {
		new Item(20543, 3), //Elite clue reward casket
		new Item(19836, 2), //Master clue reward casket
		new Item(24034, 1), //Dragonstone full helm
		new Item(24037, 1), //Dragonstone platebody
		new Item(24040, 1), //Dragonstone platelegs
		new Item(24043, 1), //Dragonstone boots
		new Item(24046, 1), //Dragonstone gauntlets
		new Item(12004, 1), //Kraken tentacle
		new Item(2577, 1), //Ranger boots
		new Item(12598, 1), //Holy sandals
		new Item(6571, 1), //Uncut onyx
	};


	public static void register() {
		ObjectAction.register(172, "open", (player, obj) -> {
			Item crystalKey = player.getInventory().findItem(989);
			if (crystalKey == null) {
				player.sendFilteredMessage("You need a crystal key to open this chest.");
				return;
			}

			boolean hasDragonstoneArmorSet = player.getEquipment().contains(24034) &&
				player.getEquipment().contains(24037) &&
				player.getEquipment().contains(24040) &&
				player.getEquipment().contains(24043) &&
				player.getEquipment().contains(24046);

			AtomicInteger openedKeys = new AtomicInteger(0);
			var loops = getTotalKeysPlayerCanOpen(player);
			var keysToOpen = Math.min(crystalKey.getAmount(), loops);
			if (keysToOpen == 1) {
				player.startEvent(event -> {
					player.lock();
					if (hasDragonstoneArmorSet && Random.rollPercent(15)) {
						player.sendFilteredMessage("<shad=3CB3DD>Your dragonstone armor resonates with the chest, saving your key from being consumed.");
					}
					else {
						player.sendFilteredMessage("You unlock the Crystal chest with your key.");
						if (Random.rollPercent(getDonatorSaveChance(player))) {
							player.sendMessage("<shad=3CB3DD>Your donator status allows you to save your key.");
						}
						else {
							crystalKey.remove(1);
							openedKeys.getAndIncrement();
						}
					}
					player.privateSound(51);
					event.delay(2);
					openChest(player);
					event.delay(1);
					player.unlock();
					player.crystalChestsOpened++;
				});
			}
			else {
				player.sendFilteredMessage("You unlock the Crystal chest with your keys.");
				for (int loop = 0; loop < keysToOpen; loop++) {
					if (hasDragonstoneArmorSet && Random.rollPercent(15)) {
						player.sendFilteredMessage("<shad=3CB3DD>Your dragonstone armor resonates with the chest, saving your key from being consumed.");
					}
					else {
						if (Random.rollPercent(getDonatorSaveChance(player))) {
							player.sendMessage("<shad=3CB3DD>Your donator status allows you to save your key.");
						}
						else {
							openedKeys.getAndIncrement();
							crystalKey.remove(1);
							openChest(player);
							player.crystalChestsOpened++;
						}
					}
				}
				player.sendMessage("You opened " + openedKeys.get() + " Crystal chests.");
			}
		});

		ObjectAction.register(172, "Check", (player, obj) -> {
			player.sendMessage(" You have opened " + player.crystalChestsOpened + " Crystal chests.");
		});

	}

	private static void openChest(Player player) {
		int amount = Random.get(50000, 100000);
		if (player.getTotalDonated() >= 500)
			player.getBank().deposit(995, amount);
		else
			player.getInventory().addOrDrop(995, amount);

		if (Random.rollDie(350, 1)) {
			/**
			 * Very rare loot
			 */
			Item loot = VERY_RARE_LOOT[Random.get(VERY_RARE_LOOT.length - 1)];
			if (player.getTotalDonated() >= 500)
				player.getBank().deposit(loot.getId(), loot.getAmount());
			else
				player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
			Broadcast.GLOBAL.sendNewsDropMessage(player, Icon.ADMINISTRATOR, player.getName(), " just received " + loot.getDef().descriptiveName + " from the Crystal chest on [Chest #" + player.crystalChestsOpened + "]!");

		}
		else if (Random.rollDie(150, 1)) {
			/**
			 * Rare loot
			 */
			Item loot = RARE_LOOT[Random.get(RARE_LOOT.length - 1)];
			if (player.getTotalDonated() >= 500)
				player.getBank().deposit(loot.getId(), loot.getAmount());
			else
				player.getInventory().addOrDrop(loot.getId(), loot.getAmount());

		}
		else if (Random.rollDie(25, 1)) {
			/**
			 * Uncommon loot
			 */
			Item loot = UNCOMMON_LOOT[Random.get(UNCOMMON_LOOT.length - 1)];
			if (player.getTotalDonated() >= 500)
				player.getBank().deposit(loot.getId(), loot.getAmount());
			else
				player.getInventory().addOrDrop(loot.getId(), loot.getAmount());

		}
		else if (Random.rollDie(10, 1)) {
			/**
			 * Rune loot
			 */
			Item[] loot = RUNE_LOOTS[0];
			for (Item item : loot)
				if (player.getTotalDonated() >= 500)
					player.getBank().deposit(item.getId(), item.getAmount());
				else
					player.getInventory().addOrDrop(item.getId(), item.getAmount());

		}
		else {
			/**
			 * Common loot
			 */
			Item loot = COMMON_LOOT[Random.get(COMMON_LOOT.length - 1)];
			if (player.getTotalDonated() >= 500)
				player.getBank().deposit(loot.getId(), loot.getAmount());
			else
				player.getInventory().addOrDrop(loot.getId(), loot.getAmount());

		}
	}

	protected static int getTotalKeysPlayerCanOpen(Player player) {
		if (player.getTotalDonated() >= 5_000)
			return 40;
		else if (player.getTotalDonated() >= 2_500)
			return 35;
		else if (player.getTotalDonated() >= 1_000)
			return 30;
		else if (player.getTotalDonated() >= 500)
			return 25;
		else if (player.getTotalDonated() >= 250)
			return 20;
		else if (player.getTotalDonated() >= 100)
			return 15;
		else if (player.getTotalDonated() >= 50)
			return 10;
		else if (player.getTotalDonated() >= 10)
			return 5;
		else
			return 1;
	}
}
