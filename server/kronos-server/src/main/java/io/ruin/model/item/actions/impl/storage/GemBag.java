package io.ruin.model.item.actions.impl.storage;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.actions.ItemObjectAction;

public class GemBag {

	private static final int[] GEMS = new int[]{1617, 1619, 1621, 1623, 1625, 1627, 1629, 1631, 6571, 19496};

	private static int amountStored(Player player, int gemID) {
		if (gemID == 1617) return player.gemBagDiamond;
		if (gemID == 1619) return player.gemBagRuby;
		if (gemID == 1621) return player.gemBagEmerald;
		if (gemID == 1623) return player.gemBagSapphire;
		if (gemID == 1631) return player.gemBagDragonstone;
		if (gemID == 1627) return player.gemBagOpal;
		if (gemID == 1625) return player.gemBagJade;
		if (gemID == 1629) return player.gemBagTopaz;
		if (gemID == 6571) return player.gemBagOnyx;
		if (gemID == 19496) return player.gemBagZenyte;
		return -1;
	}

	private static void incrementGemStored(Player player, int gemID, int amount) {
		if (gemID == 1617) player.gemBagDiamond += amount;
		if (gemID == 1619) player.gemBagRuby += amount;
		if (gemID == 1621) player.gemBagEmerald += amount;
		if (gemID == 1623) player.gemBagSapphire += amount;
		if (gemID == 1631) player.gemBagDragonstone += amount;
		if (gemID == 1627) player.gemBagOpal += amount;
		if (gemID == 1625) player.gemBagJade += amount;
		if (gemID == 1629) player.gemBagTopaz += amount;
		if (gemID == 6571) player.gemBagOnyx += amount;
		if (gemID == 19496) player.gemBagZenyte += amount;
	}

	private static void decrementGemStored(Player player, int gemID, int amount) {
		if (gemID == 1617) player.gemBagDiamond -= amount;
		if (gemID == 1619) player.gemBagRuby -= amount;
		if (gemID == 1621) player.gemBagEmerald -= amount;
		if (gemID == 1623) player.gemBagSapphire -= amount;
		if (gemID == 1631) player.gemBagDragonstone -= amount;
		if (gemID == 1627) player.gemBagOpal -= amount;
		if (gemID == 1625) player.gemBagJade -= amount;
		if (gemID == 1629) player.gemBagTopaz -= amount;
		if (gemID == 6571) player.gemBagOnyx -= amount;
		if (gemID == 19496) player.gemBagZenyte -= amount;
	}

	private static void clearBag(Player player) {
		player.gemBagDiamond = 0;
		player.gemBagDragonstone = 0;
		player.gemBagRuby = 0;
		player.gemBagSapphire = 0;
		player.gemBagEmerald = 0;
		player.gemBagOpal = 0;
		player.gemBagJade = 0;
		player.gemBagTopaz = 0;
		player.gemBagOnyx = 0;
		player.gemBagZenyte = 0;
	}

	private static void addToBag(Player player, int gemId, int amount) {
		int gemSize = amountStored(player, gemId);
		if (gemSize >= getMaxStorage(player)) {
			player.sendFilteredMessage("Your gam bag cannot carry anymore " + ObjType.get(gemId).name.toLowerCase() + "s.");
			return;
		}
		int minToAdd = Math.min(amount, getMaxStorage(player) - gemSize);
		int amountToAdd = Math.min(minToAdd, player.getInventory().count(gemId));
		if (amountToAdd == 0)
			return;
		player.getInventory().remove(gemId, amountToAdd);
		incrementGemStored(player, gemId, amountToAdd);
		player.sendFilteredMessage("You add " + amountToAdd + " gem" + (amount == 1 ? "" : "s") + " to the bag.");
	}

	private static int getMaxStorage(Player player) {
		switch (player.getSecondaryGroup()) {
			case DONATOR -> {
				return 75;
			}
			case SUPER_DONATOR -> {
				return 90;
			}
			case ELITE_DONATOR -> {
				return 120;
			}
			case NOBLE_DONATOR -> {
				return 150;
			}
			case GOLD_DONATOR -> {
				return 180;
			}
			case PLATINUM_DONATOR -> {
				return 210;
			}
			case LEGENDARY_DONATOR -> {
				return 240;
			}
			case SUPREME_DONATOR -> {
				return 300;
			}
			default -> {
				return 60;
			}
		}
	}

	public static void register() {
		/**
		 * Fill
		 */
		ItemAction.registerInventory(24481, "fill", (player, item) -> {
			int added = 0;
			for (int gemId : GEMS) {
				Item gem = player.getInventory().findItem(gemId);
				if (gem == null)
					continue;
				int gemSize = amountStored(player, gemId);
				if (gemSize >= getMaxStorage(player)) {
					player.sendFilteredMessage("Your gam bag cannot carry anymore " + gem.getDef().name.toLowerCase() + "s.");
					continue;
				}
				int amountToAdd = Math.min(gem.count(), getMaxStorage(player) - gemSize);
				player.getInventory().remove(gem.getId(), amountToAdd);
				incrementGemStored(player, gemId, amountToAdd);
				added += amountToAdd;
			}
			if (added > 0)
				player.sendFilteredMessage("You add " + added + " gem" + (added == 1 ? "" : "s") + " to the bag.");
			else
				player.sendFilteredMessage("You have no gems to add to the bag.");
		});
		ItemAction.registerInventory(12020, "fill", (player, item) -> {
			player.sendMessage("You need to open the bag before filling.");
		});
		/**
		 * Check
		 */
		ItemAction.registerInventory(12020, "check", (player, item) -> {
			StringBuilder sb = new StringBuilder();
			sb.append("The gem bag contains: ");
			int amount = 0;
			for (int gemId : GEMS) {
				int gemSize = amountStored(player, gemId);
				sb.append(gemSize).append(" ").append(ObjType.get(gemId).name.toLowerCase()).append("s").append(gemId == 1631 ? "." : ", ");
				if (gemId > 0)
					amount++;
			}
			if (amount > 0)
				player.sendFilteredMessage(sb.toString());
			else
				player.sendFilteredMessage("The bag is currently empty.");
		});

		ItemAction.registerInventory(24481, "check", (player, item) -> {
			StringBuilder sb = new StringBuilder();
			sb.append("The gem bag contains: ");
			int amount = 0;
			for (int gemId : GEMS) {
				int gemSize = amountStored(player, gemId);
				sb.append(gemSize).append(" ").append(ObjType.get(gemId).name.toLowerCase()).append("s").append(gemId == 1631 ? "." : ", ");
				if (gemId > 0)
					amount++;
			}
			if (amount > 0)
				player.sendFilteredMessage(sb.toString());
			else
				player.sendFilteredMessage("The bag is currently empty.");
		});
		/*
		 * Open Bag
		 */
		ItemAction.registerInventory(12020, "open", (player, item) -> {
			item.setId(24481);
			player.sendFilteredMessage("You open your gem bag, ready to fill it.");
		});
		ItemAction.registerInventory(24481, "close", (player, item) -> {
			item.setId(12020);
			player.sendFilteredMessage("You close your gem bag, you can no longer store gems inside of it!");
		});
		/**
		 * Empty
		 */
		ItemObjectAction.register(24481, "Bank Booth", (player, item, obj) -> {
			for (int gemId : GEMS) {
				int freeSlots = player.getBank().getFreeSlots();
				if (freeSlots == 0)
					return;
				int gemSize = amountStored(player, gemId);
				int amountToRemove = Math.min(gemSize, freeSlots);
				if (amountToRemove == 0)
					continue;
				decrementGemStored(player, gemId, amountToRemove);
				player.getBank().add(gemId, amountToRemove);
				player.sendFilteredMessage("You empty your sack into the bank!");
			}

		});
		ItemAction.registerInventory(24481, "empty", (player, item) -> {
			for (int gemId : GEMS) {
				int freeSlots = player.getInventory().getFreeSlots();
				if (freeSlots == 0)
					return;
				int gemSize = amountStored(player, gemId);
				int amountToRemove = Math.min(gemSize, freeSlots);
				if (amountToRemove == 0)
					continue;
				decrementGemStored(player, gemId, amountToRemove);
				player.getInventory().add(gemId, amountToRemove);
			}
		});
		/**
		 * Destroy
		 */
		ItemAction.registerInventory(12020, "destroy", (player, item) ->
			player.dialogue(new YesNoDialogue("Are you sure you want to destroy the item?",
					"The contents of the bag will be destroyed with it.", item, () -> {
					item.remove(1);
					clearBag(player);
				})
			)
		);
		ItemAction.registerInventory(24481, "destroy", (player, item) ->
			player.dialogue(new YesNoDialogue("Are you sure you want to destroy the item?",
					"The contents of the bag will be destroyed with it.", item, () -> {
					item.remove(1);
					clearBag(player);
				})
			)
		);
		/**
		 * Item on bag
		 */
		for (int gemId : GEMS) {
			ItemItemAction.register(gemId, 24481, (player, primary, secondary) -> player.dialogue(new OptionsDialogue(
				"How many would you like to deposit?",
				new Option("One", () -> addToBag(player, gemId, 1)),
				new Option("Five", () -> addToBag(player, gemId, 5)),
				new Option("X", () -> player.integerInput("How many would you like to deposit?", amt -> addToBag(player, gemId, amt))),
				new Option("All", () -> addToBag(player, gemId, Integer.MAX_VALUE))
			)));
		}
	}
}
