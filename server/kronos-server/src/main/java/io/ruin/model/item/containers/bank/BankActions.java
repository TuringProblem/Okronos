package io.ruin.model.item.containers.bank;

import io.ruin.cache.ObjType;
import io.ruin.cache.NPCType;
import io.ruin.cache.LocType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.SpawnListener;
import io.ruin.model.inter.dialogue.*;
import io.ruin.model.inter.handlers.CollectionBox;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.item.actions.impl.BloodyTokens;
import io.ruin.model.item.actions.impl.ItemSet;
import io.ruin.model.item.actions.impl.PlatinumToken;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.Utils;

import java.util.ArrayList;

import static io.ruin.cache.ItemID.*;
import static io.ruin.model.item.actions.impl.BloodyTokens.BLOODY_TOKENS;

public class BankActions {

	/**
	 * Npcs
	 */

	public static void register() {
		NPCType.forEach(it -> {
			if (!it.hasOption("bank")) {
				return;
			}
			ItemNPCAction.register(it.id, (p, item, obj) -> itemOnBank(p, item));
		});
		LocType.forEach(objDef -> {
			if (objDef.hasOption("bank") || objDef.name.toLowerCase().contains("bank")
					|| objDef.name.toLowerCase().contains("exchange")) {
				if (ObjectAction.register(objDef.id, "use", (p, obj) -> p.getBank().open())
						|| ObjectAction.register(objDef.id, "bank", (p, obj) -> p.getBank().open())) {
					objDef.bank = true;
					ObjectAction.register(objDef.id, "collect", (p, obj) -> CollectionBox.open(p));
					ItemObjectAction.register(objDef.id, (p, item, obj) -> itemOnBank(p, item));
				}
			}
			if (objDef.name.toLowerCase().contains("bank deposit box")
					|| objDef.id == 10661 // deposit chest in zul-andra
					|| objDef.id == 10530 // deposit chest in gem mine
			) {
				if (ObjectAction.register(objDef.id, "deposit", (p, obj) -> p.getBank().openDepositBox()))
					ItemObjectAction.register(objDef.id, (p, item, obj) -> itemOnDeposit(p, item));
			}
		});

		ArrayList<Integer> bankerIds = new ArrayList<>();
		NPCType.forEach(def -> {
			if (NPCAction.register(def.id, "bank", (p, n) -> p.getBank().open())) {
				NPCAction.register(def.id, "talk-to", BankActions::talk);
				NPCAction.register(def.id, "collect", (p, n) -> CollectionBox.open(p));
				bankerIds.add(def.id);
			}
		});

		SpawnListener.forEach(npc -> {
			if (bankerIds.contains(npc.getId())) {
				addWalkException(npc);
				markTiles(npc);
			}
		});
	}

	private static void talk(Player player, NPC npc) {
		player.dialogue(
				new NPCDialogue(npc, "Good day, how may I help you?"),
				new OptionsDialogue(
						new Option("I'd like to access my bank account, please.", () -> player.getBank().open()),
						new Option("I'd like to check my PIN settings.", () -> player.getBankPin().openSettings()),
						new Option("I'd like to collect items.", () -> CollectionBox.open(player)),
						new Option("Open Item Sets please.", () -> ItemSet.open(player)),
						new Option("Purchase extra space.", () -> promptExtraSpaceDialogue(player, npc))));
	}

	private static void promptExtraSpaceDialogue(Player player, NPC npc) {
		var storageLevel = getStorageLevel(player.totalExtraBankStorage);
		if (storageLevel == 6) {
			player.sendMessage("You have the maximum bank space available.");
			return;
		}
		long upgradeCost = getLevelUpgradeCost(storageLevel);
		player.dialogue(
				new NPCDialogue(npc, "Would you like to expand your bank by 50 slots for a fee?"),
				new NPCDialogue(npc,
						"The fee will cost you %s for the upgrade.".formatted(Utils.formatMoneyString(upgradeCost))),
				new OptionsDialogue(
						new Option("Yes, please.", () -> {
							var cost = new Item(COINS_995, (int) upgradeCost);
							if (storageLevel > 3)
								cost = new Item(PLATINUM_TOKEN, (int) (upgradeCost / 1_000));
							// if the player can't pay one way or the other...
							if (!player.getInventory().contains(cost) && !player.getBank().contains(cost)) {
								player.sendMessage("You do not have enough to upgrade your bank.");
								return;
							}
							// first check their inventory
							if (player.getInventory().contains(cost)) {
								player.getInventory().remove(cost);
								player.getBank().extendCapacity(player, 50);
								player.sendMessage("Your bank has been expanded by 50 slots.");
								return;
							}
							// Inventory didn't have it, check their bank...
							if (player.getBank().contains(cost)) {
								player.getBank().remove(cost);
								player.getBank().extendCapacity(player, 50);
								player.sendMessage("Your bank has been expanded by 50 slots.");
							}
						}),
						new Option("Nevermind.", player::closeDialogue)));
	}

	private static int getSlotUpdateForLevel(int level) {
		if (level >= 6)
			return 0;
		return level * 50;
	}

	private static Long getLevelUpgradeCost(int level) {
		return switch (level) {
			case 0 -> 150_000_000L;
			case 1 -> 500_000_000L;
			case 2 -> 750_000_000L;
			case 3 -> 1_750_000_000L;
			case 4 -> 3_200_000_000L;
			case 5 -> 8_500_000_000L;
			default -> 0L;
		};
	}

	private static int getStorageLevel(int existingBoostedCapacity) {
		return switch (existingBoostedCapacity) {
			case 50 -> 1;
			case 100 -> 2;
			case 150 -> 3;
			case 200 -> 4;
			case 250 -> 5;
			case 300 -> 6;
			default -> 0;
		};
	}

	private static void addWalkException(NPC npc) {
		if (npc.walkBounds != null) {
			return;
		}
		int deltaX = npc.spawnDirection.deltaX;
		int deltaY = npc.spawnDirection.deltaY;
		int x = npc.getAbsX() + deltaX;
		int y = npc.getAbsY() + deltaY;
		int z = npc.getHeight();
		GameObject obj = Tile.getObject(-1, x, y, z, 10, -1);
		if (obj == null) {
			return;
		}
		LocType def = obj.getDef();
		if (def.xLength > 1 || def.yLength > 1) {
			return;
		}
		npc.walkTo = new Position(x + deltaX, y + deltaY, z);
	}

	private static void itemOnBank(Player player, Item item) {
		var itemId = item.getId();
		if (itemId == COINS_995 || itemId == PLATINUM_TOKEN) {
			exchangePlatinumToken(player, item);
		} else if (itemId == BLOODY_TOKENS || itemId == BLOOD_MONEY) {
			exchangeBloodyTokens(player, item);
		} else if (itemId == 24587) {
			exchangeRunePouchNote(player, item);
		} else {
			unnote(player, item);
		}
	}

	private static void itemOnDeposit(Player player, Item item) {
		int count = item.count();
		if (count == 1) {
			quickDeposit(player, item, 1);
		} else if (count == 2) {
			player.dialogue(new OptionsDialogue(
					"How many would you like to deposit?",
					new Option("One", () -> quickDeposit(player, item, 1)),
					new Option("Both", () -> quickDeposit(player, item, count))));
		} else if (count <= 5) {
			player.dialogue(new OptionsDialogue(
					"How many would you like to deposit?",
					new Option("One", () -> quickDeposit(player, item, 1)),
					new Option("X", () -> player.integerInput("Enter amount:", amt -> quickDeposit(player, item, amt))),
					new Option("All", () -> quickDeposit(player, item, count))));
		} else {
			player.dialogue(new OptionsDialogue(
					"How many would you like to deposit?",
					new Option("One", () -> quickDeposit(player, item, 1)),
					new Option("Five", () -> quickDeposit(player, item, 5)),
					new Option("X", () -> player.integerInput("Enter amount:", amt -> quickDeposit(player, item, amt))),
					new Option("All", () -> quickDeposit(player, item, count))));
		}
	}

	private static void quickDeposit(Player player, Item item, int amount) {
		if (player.getBank().deposit(item, amount, true) != 0)
			player.animate(834);
		player.closeDialogue();
	}

	/**
	 * Marking
	 */
	private static void markTiles(NPC npc) {
		markTiles(npc.getAbsX(), npc.getAbsY(), npc.getHeight());
	}

	public static void markTiles(GameObject obj) {
		// This will only mark tiles for objects in cache.
		// Custom objects will require custom exceptions!
		if (obj.getDef() != null && obj.getDef().bank)
			markTiles(obj.x, obj.y, obj.z);
	}

	public static void markTiles(int srcX, int srcY, int srcZ) {
		Tile srcTile = Tile.get(srcX, srcY, srcZ, true);
		int westMostX = srcX, eastMostX = srcX;
		int southMostY = srcY, northMostY = srcY;
		int distance, maxDistance = srcTile.roofExists ? 30 : 8;
		/*
		 * West
		 */
		distance = maxDistance;
		while (distance-- > 0) {
			Tile tile = Tile.get(westMostX - 1, srcY, srcZ, true);
			if (tile.roofExists != srcTile.roofExists)
				break;
			westMostX--;
		}
		/*
		 * East
		 */
		distance = maxDistance;
		while (distance-- > 0) {
			Tile tile = Tile.get(eastMostX + 1, srcY, srcZ, true);
			if (tile.roofExists != srcTile.roofExists)
				break;
			eastMostX++;
		}
		/*
		 * South
		 */
		distance = maxDistance;
		while (distance-- > 0) {
			Tile tile = Tile.get(srcX, southMostY - 1, srcZ, true);
			if (tile.roofExists != srcTile.roofExists)
				break;
			southMostY--;
		}
		/*
		 * North
		 */
		distance = maxDistance;
		while (distance-- > 0) {
			Tile tile = Tile.get(srcX, northMostY + 1, srcZ, true);
			if (tile.roofExists != srcTile.roofExists)
				break;
			northMostY++;
		}
		/*
		 * Mark
		 */
		for (int x = westMostX; x <= eastMostX; x++) {
			for (int y = southMostY; y <= northMostY; y++)
				Tile.get(x, y, srcZ, true).nearBank = true;
		}
	}

	private static void exchangeRunePouchNote(Player player, Item item) {
		var inv = player.getInventory();
		if (inv.remove(item.getId(), 1) == 0) {
			return;
		}
		inv.add(RUNE_POUCH);
	}

	private static void exchangeBloodyTokens(Player player, Item item) {
		BloodyTokens.exchange(player, item);
	}

	private static void exchangePlatinumToken(Player player, Item item) {
		PlatinumToken.exchange(player, item);
	}

	private static void unnote(Player player, Item item) {
		ObjType def = item.getDef();
		if (def.isNote()) {
			player.integerInput("How many would you like to un-note?", amt -> {
				if (amt < 1) {
					player.sendMessage("You must enter a number greater than 0.");
					return;
				}
				if (amt > player.getInventory().getAmount(item.getId())) {
					amt = player.getInventory().getAmount(item.getId());
				}
				int freeSlots = player.getInventory().getFreeSlots();
				if (item.getAmount() == 1)
					freeSlots++;
				if (amt > freeSlots)
					amt = freeSlots;
				int exchanged;
				player.getInventory().remove(item.getId(), amt);
				player.getInventory().add(def.notedId, amt);
				player.dialogue(
						new MessageDialogue("The bank exchanges your banknote for an item" + (amt == 1 ? "" : "s") + "."));
			});
		} else {
			if (def.notedId != -1) {
				player.dialogue(new OptionsDialogue(
						"Note the " + (item.getAmount() == 1 ? "item" : "items") + "?",
						new Option("Yes", () -> {
							int removed = item.remove(player.getInventory().getAmount(item.getId()));
							player.getInventory().add(def.notedId, removed);
							player.dialogue(
									new MessageDialogue("The bank exchanges your items for banknotes" + (removed == 1 ? "" : "s") + "."));
						}),
						new Option("No", player::closeDialogue)));
			} else {
				player.sendMessage("This item cannot be noted.");
			}
		}

	}
}
