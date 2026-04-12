package io.ruin.model.map.object.actions.impl.edgeville;

import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Broadcast;

import java.util.concurrent.atomic.AtomicInteger;

import static io.ruin.model.map.object.actions.impl.edgeville.CrystalKeyChest.getTotalKeysPlayerCanOpen;

public class EnhancedCrystalKeyChest {
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
		new Item(1632, 50), //Uncut dragon stones
		new Item(21905, 100), //Dragon bolts
		new Item(11230, 125), //Dragon dart
		new Item(23962, 30), //Crystal shards
		new Item(23951, 2), //Enhanced crystal keys
		new Item(5315, 10), //Yew seeds
		new Item(5316, 5), //Magic seeds
		new Item(537, 100), //Dragon bones
		new Item(2364, 75), //Runite bars
		new Item(5300, 20), //Snapdragon seeds
		new Item(5295, 20), //Ranarr seeds
		new Item(220, 30), //Grimy Torstols
		new Item(8783, 200), //Mahogany planks
		new Item(1514, 250), //Magic logs
		new Item(384, 300), //Raw shark
		new Item(13442, 40), //Anglerfish
		new Item(12914, 5), //Anti venom+
		new Item(30596, 2), //Vote Buff Streak Box
		new Item(20545, 3), //Medium clue reward casket
	};
	private static final Item[][] RUNE_LOOTS = {
		new Item[]{
			new Item(560, 1500), //Death runes
			new Item(565, 1500), //Blood Runes
			new Item(566, 1500), //Soul Runes
			new Item(21880, 1000) //Wrath runes
		}
	};
	private static final Item[][] POTION_LOOTS = {
		new Item[]{
			new Item(6686, 30), //Saradomin Brew
			new Item(3025, 30), //Super restore
		}
	};
	private static final Item[][] GEM_LOOTS = {
		new Item[]{
			new Item(1622, 150), //Uncut Emerald
			new Item(1620, 150), //Uncut Ruby
			new Item(1618, 75), //Uncut Diamond
		}
	};
	private static final Item[] UNCOMMON_LOOT = {
		new Item(13440, 250), //Raw anglerfish
		new Item(11840, 1), //Dragon boots
		new Item(11944, 75), //Lava dragon bones
		new Item(6739, 1), //Dragon axe
		new Item(12914, 20), //Anti venom+
		new Item(23962, 69), //Crystal shard
		new Item(ItemID.DRAGON_PLATELEGS + 1, 6), //Dragon platelegs
		new Item(3205, 5), //Dragon Halberd
		new Item(9193, 750), //Dragonstone bolt tips
		new Item(19484, 1150), //Dragon javelins
		new Item(20544, 3), //Hard clue reward casket
		new Item(12831, 1), //Blessed spirit shield
		new Item(2510, 250), //Black dragon leather
		new Item(6571, 1), //Uncut onyx
		new Item(2577, 1), //Ranger boots
		new Item(12598, 1), //Holy sandals
		new Item(6735, 1), //Warrior ring
		new Item(6737, 1), //Berserker ring
		new Item(6731, 1), //Seers ring
		new Item(6733, 1), //Archers ring
		new Item(22125, 75), //Superior dragon bones
		new Item(11230, 500), //Dragon dart
		new Item(21905, 375) //Dragon bolts
	};
	private static final Item[] RARE_LOOT = {
		new Item(19836, 4), //Master clue reward casket
		new Item(11335, 1), //Dragon full helm
		new Item(11286, 1), //Draconic visage
		new Item(12004, 1), //Kraken tentacle
		new Item(20543, 4), //Elite clue reward casket
		new Item(30582, 1), //GWD Mystery Box
	};

	private static final Item[] VERY_RARE_LOOT = {
		new Item(24034, 1), //Dragonstone full helm
		new Item(24037, 1), //Dragonstone platebody
		new Item(24040, 1), //Dragonstone platelegs
		new Item(24043, 1), //Dragonstone boots
		new Item(24046, 1), //Dragonstone gauntlets
		new Item(23911, 1), //Crystal crown
		new Item(23962, 200), //Crystal shard
	};


	public static void register() {
		ObjectAction.register(36582, "open", (player, obj) -> {
			Item enhancedCrystalKey = player.getInventory().findItem(23951);
			if (enhancedCrystalKey == null) {
				player.sendFilteredMessage("You need an Enhanced crystal key to open this chest.");
				return;
			}
			boolean hasDragonstoneArmorSet = player.getEquipment().contains(24034) && player.getEquipment().contains(24037) && player.getEquipment().contains(24040) && player.getEquipment().contains(24043) && player.getEquipment().contains(24046);

			AtomicInteger openedKeys = new AtomicInteger(0);
			var loops = getTotalKeysPlayerCanOpen(player);
			var keysToOpen = Math.min(enhancedCrystalKey.getAmount(), loops);
			if (keysToOpen == 1) {
				player.startEvent(event -> {
					player.lock();
					if (hasDragonstoneArmorSet && Random.rollPercent(15)) {
						player.sendFilteredMessage("<shad=3CB3DD>Your dragonstone armor resonates with the chest, saving your key from being consumed.");
					} else {
						player.sendFilteredMessage("You unlock the chest with your Enhanced crystal key.");
						if (Random.rollPercent(getDonatorSaveChance(player))) {
							player.sendMessage("<shad=3CB3DD>Your donator status allows you to save your key.");
						} else {
							enhancedCrystalKey.remove(1);
						}
					}
					player.privateSound(51);
					event.delay(2);
					openChest(player);
					event.delay(1);
					player.unlock();
					player.enhancedCrystalChestsOpened++;
				});
			}
			else {
				player.sendFilteredMessage("You unlock the chest with your Enhanced crystal key.");
				for (int loop = 0; loop < keysToOpen; loop++) {
					if (hasDragonstoneArmorSet && Random.rollPercent(15)) {
						player.sendFilteredMessage("<shad=3CB3DD>Your dragonstone armor resonates with the chest, saving your key from being consumed.");
					}
					else {
						player.sendFilteredMessage("You unlock the chest with your Enhanced crystal key.");
						if (Random.rollPercent(getDonatorSaveChance(player))) {
							player.sendMessage("<shad=3CB3DD>Your donator status allows you to save your key.");
						} else {
							enhancedCrystalKey.remove(1);
							openedKeys.getAndIncrement();
							openChest(player);
							player.enhancedCrystalChestsOpened++;
						}
					}
				}
			}
		});
		ObjectAction.register(36582, "Check", (player, obj) -> {
			player.sendMessage(" You have opened " + player.enhancedCrystalChestsOpened + " Enhanced crystal chests.");
		});
	}

	private static void openChest(Player player) {
		int amount = Random.get(300000, 500000);
		player.getInventory().addOrDrop(995, amount);
		if (Random.rollDie(225, 1)) {
			/**
			 * Very rare loot
			 */
			Item loot = VERY_RARE_LOOT[Random.get(VERY_RARE_LOOT.length - 1)];
			if (player.getTotalDonated() >= 500)
				player.getBank().deposit(loot.getId(), loot.getAmount());
			else
				player.getInventory().addOrDrop(loot.getId(), loot.getAmount());
			player.addToCollectionLog(loot);
			Broadcast.GLOBAL.sendNewsDropMessage(player, Icon.ADMINISTRATOR, player.getName(), " just received " + loot.getDef().descriptiveName + " from the Enhanced crystal chest on [Chest #" + player.enhancedCrystalChestsOpened + "]!");

		} else if (Random.rollDie(100, 1)) {
			/**
			 * Rare loot
			 */
			Item loot = RARE_LOOT[Random.get(RARE_LOOT.length - 1)];
			if (player.getTotalDonated() >= 500)
				player.getBank().deposit(loot.getId(), loot.getAmount());
			else
				player.getInventory().addOrDrop(loot.getId(), loot.getAmount());

		} else if (Random.rollDie(25, 1)) {
			/**
			 * Uncommon loot
			 */
			Item loot = UNCOMMON_LOOT[Random.get(UNCOMMON_LOOT.length - 1)];
			if (player.getTotalDonated() >= 500)
				player.getBank().deposit(loot.getId(), loot.getAmount());
			else
			player.getInventory().addOrDrop(loot.getId(), loot.getAmount());

		} else if (Random.rollDie(10, 1)) {
			/**
			 * Rune loot
			 */
			Item[] loot = RUNE_LOOTS[0];
			for (Item item : loot)
				if (player.getTotalDonated() >= 500)
					player.getBank().deposit(item.getId(), item.getAmount());
				else
					player.getInventory().addOrDrop(item.getId(), item.getAmount());

		} else if (Random.rollDie(10, 1)) {
			/**
			 * Potion loot
			 */
			Item[] loot = POTION_LOOTS[0];
			for (Item item : loot)
				if (player.getTotalDonated() >= 500)
					player.getBank().deposit(item.getId(), item.getAmount());
				else
					player.getInventory().addOrDrop(item.getId(), item.getAmount());

		} else if (Random.rollDie(10, 1)) {
			/**
			 * Gem loots
			 */
			Item[] loot = GEM_LOOTS[0];
			for (Item item : loot)
				if (player.getTotalDonated() >= 500)
					player.getBank().deposit(item.getId(), item.getAmount());
				else
					player.getInventory().addOrDrop(item.getId(), item.getAmount());

		} else {
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
}
