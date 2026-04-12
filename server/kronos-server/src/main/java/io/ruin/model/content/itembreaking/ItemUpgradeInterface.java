package io.ruin.model.content.itembreaking;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.TabInventory;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;

import java.util.*;

import static io.ruin.model.content.itembreaking.ItemBreakingHandler.*;

public class ItemUpgradeInterface {
	Item selectedItem;
	ItemBreakPerks perk;

	public void open(Player player, boolean clear) {
		if (clear) {
			selectedItem = null;
		}

		perkAttachmentActive = false;
		if (perk == null)
			perk = ItemBreakPerks.VALUES[0];
		player.openInterface(ToplevelComponent.MAINMODAL, 1126);
		player.openInterface(ToplevelComponent.SIDEMODAL, 1128);
		player.getPacketSender().sendClientScript(149, "IviiiIsssss", 1128 << 16, 93, 4, 7, 0, -1,
				"Select", "", "", "", "");
		player.getPacketSender().sendIfEvents(1128, 0, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(1128, 0, 0, 27, 1086);
		update(player);
	}

	private void selectItem(Player player, Item item) {
		if (item.getDef().equipSlot == -1) {
			player.sendMessage("You cannot upgrade this item.");
			return;
		}
		if (perk == ItemBreakPerks.SOUL_REAVER && item.getDef().name.contains("Scythe")) {
			player.sendMessage("You cannot attach the Soul Reaver perk to this weapon.");
			return;
		}
		if (perk == ItemBreakPerks.SOUL_REAVER && item.getDef().name.contains("scythe")) {
			player.sendMessage("You cannot attach the Soul Reaver perk to this weapon.");
			return;
		}
		if (perk.specificSlot) {
			// System.out.println("equip slot " + item.getDef().equipSlot);
			boolean correctSlot = false;
			for (int slot : perk.slots) {
				// System.out.println("slot " + slot);
				if (item.getDef().equipSlot == slot) {
					correctSlot = true;
					break;
				}
			}
			if (!correctSlot) {
				player.sendMessage("You cannot attach this perk to this item slot.");
				return;
			}
		}
		selectedItem = item;
		update(player);
	}

	private void selectPerk(Player player, ItemBreakPerks perk) {
		this.perk = perk;
		selectedItem = null;
		update(player);
	}

	public static String getItemAttachments(Item item) {
		StringBuilder attachments = null;
		for (ItemBreakPerks perk : ItemBreakPerks.VALUES) {
			if (AttributeExtensions.hasAttribute(item, perk.perk)) {
				if (attachments == null)
					attachments = new StringBuilder();
				attachments.append(perk.name).append("(").append(AttributeExtensions.getCharges(perk.perk, item)).append(") ");
			}
		}
		if (attachments == null)
			return "None.";
		return attachments.toString();
	}

	public static String getItemAttachmentsComma(Item item) {
		StringBuilder attachments = new StringBuilder();
		boolean firstAttachment = true;

		for (ItemBreakPerks perk : ItemBreakPerks.VALUES) {
			if (AttributeExtensions.hasAttribute(item, perk.perk)) {
				if (!firstAttachment) {
					attachments.append(", ");
				}
				attachments.append(perk.name)
						.append("(")
						.append(AttributeExtensions.getCharges(perk.perk, item))
						.append(")");
				firstAttachment = false;
			}
		}
		return attachments.length() == 0 ? "None." : attachments.toString();
	}

	public static int getTotalAttachments(Item item) {
		int total = 0;
		for (ItemBreakPerks perk : ItemBreakPerks.VALUES) {
			if (AttributeExtensions.hasAttribute(item, perk.perk)) {
				total++;
			}
		}
		return total;
	}

	static String calculateBaseLevelChances(ItemBreakPerks perk) {
		int level5Range = perk.tier - 1 + 1;
		int level4Range = perk.level4Chance - (perk.tier + 1) + 1;
		int level3Range = perk.level3Chance - (perk.level4Chance + 1) + 1;
		int level2Range = perk.level2Chance - (perk.level3Chance + 1) + 1;
		int level1Range = 100 - perk.level2Chance;

		double total = 100.0;

		double level5Percent = (level5Range / total) * 100;
		double level4Percent = (level4Range / total) * 100;
		double level3Percent = (level3Range / total) * 100;
		double level2Percent = (level2Range / total) * 100;
		double level1Percent = (level1Range / total) * 100;
		return "<br><br>Level chances:<br><br>Level 1: " + level1Percent + "%<br>Level 2: " + level2Percent
				+ "%<br>Level 3: " + level3Percent + "%<br>Level 4: " + level4Percent + "%<br>Level 5: " + level5Percent + "%";

	}

	private void update(Player player) {
		// System.out.println("Updating interface");
		for (int i = 0; i < 3; i++) {
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1126 << 16 | 24 + i, 524 + i,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					24 + i,
					524 + i,
					new Item(-1));
		}
		if (perk == null) {
			player.getPacketSender().setHidden(1126, 72, true);
			player.getPacketSender().setHidden(1126, 24, true);
			player.getPacketSender().setHidden(1126, 19, true);
		} else {
			player.getPacketSender().setHidden(1126, 72, false);
			player.getPacketSender().setHidden(1126, 24, false);
			player.getPacketSender().setHidden(1126, 19, false);
			player.getPacketSender().sendString(1126, 119, perk.name);
			player.getPacketSender().sendString(1126, 118,
					ItemBreakPerks.getDescription(perk) + calculateBaseLevelChances(perk));
			StringBuilder cost = new StringBuilder();
			for (int i = 0; i < perk.cost.size(); i++) {
				player.getPacketSender().sendClientScript(
						149, "IviiiIsssss",
						1126 << 16 | 24 + i, 524 + i,
						4, 7, 1, -1, "", "", "", "", "");
				player.getPacketSender().sendItems(
						-1,
						24 + i,
						524 + i,
						perk.cost.get(i));
			}
		}
		if (selectedItem != null) {
			player.getPacketSender().setHidden(1126, 23, false);
			player.getPacketSender().sendString(1126, 23, getItemAttachments(selectedItem));
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1126 << 16 | 33, 343,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					33,
					343,
					selectedItem);
			player.getPacketSender().sendString(1126, 28,
					"Item Upgrades " + getTotalAttachments(selectedItem) + "/" + getMaxAttachments(selectedItem));
		} else {
			player.getPacketSender().setHidden(1126, 23, true);
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					1126 << 16 | 31, 343,
					4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
					-1,
					31,
					343,
					new Item(-1));
		}
	}

	List<Item> itemsInPerkAttachment = new ArrayList<>();
	boolean perkAttachmentActive = false;

	private void sendPerkAttachment(Player player) {
		if (selectedItem == null) {
			player.sendMessage("You must select an item to attach a perk to.");
			return;
		}
		if (getTotalAttachments(selectedItem) >= getMaxAttachments(selectedItem)
				&& !AttributeExtensions.hasAttribute(selectedItem, perk.perk)) {
			player.sendMessage("This item already has the maximum amount of attachments.");
			return;
		}
		perkAttachmentActive = true;
		minimumIngredientsAdded = false;
		itemsInPerkAttachment.clear();
		refreshPerkAttachment(player, true);

		player.getPacketSender().sendString(1126, 162, calculateBaseLevelChance(perk));
		player.getPacketSender().setHidden(1126, 122, false);
	}

	static String calculateBaseLevelChance(ItemBreakPerks perk) {
		int level5Range = perk.tier - 1 + 1;
		int level4Range = perk.level4Chance - (perk.tier + 1) + 1;
		int level3Range = perk.level3Chance - (perk.level4Chance + 1) + 1;
		int level2Range = perk.level2Chance - (perk.level3Chance + 1) + 1;
		int level1Range = 100 - perk.level2Chance;

		double total = 100.0;

		double level5Percent = (level5Range / total) * 100;
		double level4Percent = (level4Range / total) * 100;
		double level3Percent = (level3Range / total) * 100;
		double level2Percent = (level2Range / total) * 100;
		double level1Percent = (level1Range / total) * 100;
		return "Lv. 1: 100" + "%<br>Lv. 2: " + perk.level2Chance + "%<br>Lv. 3: " + perk.level3Chance + "%<br>Lv. 4: "
				+ perk.level4Chance + "%<br>Lv. 5: " + perk.level5Chance + "%";

	}

	private void refreshPerkAttachment(Player player, boolean clear) {
		if (clear) {
			player.getPacketSender().sendClientScript(149, "IviiiIsssss", 1126 << 16 | 148, 832, 4, 7, 0, -1,
					"", "", "", "", "");
			player.getPacketSender().sendItems(-1, 148, 832, new Item(-1));
			player.getPacketSender().sendClientScript(149, "IviiiIsssss", 1126 << 16 | 152, 833, 4, 7, 0, -1,
					"", "", "", "", "");
			player.getPacketSender().sendItems(-1, 152, 833, new Item(-1));
			player.getPacketSender().sendClientScript(149, "IviiiIsssss", 1126 << 16 | 156, 834, 4, 7, 0, -1,
					"", "", "", "", "");
			player.getPacketSender().sendItems(-1, 156, 834, new Item(-1));
			player.getPacketSender().sendClientScript(149, "IviiiIsssss", 1126 << 16 | 161, 835, 4, 7, 0, -1,
					"", "", "", "", "");
			player.getPacketSender().sendItems(-1, 161, 835, new Item(-1));
		} else {
			int startingComponent = 148;
			int startingContainer = 832;
			for (Item item : itemsInPerkAttachment) {
				if (item.getId() == OLD_ENHANCER || item.getId() == MODERN_ENHANCER || item.getId() == INNOVATIVE_ENHANCER) {
					player.getPacketSender().sendClientScript(149, "IviiiIsssss", 1126 << 16 | 161, 835, 4, 7, 0, -1,
							"", "", "", "", "");
					player.getPacketSender().sendItems(-1, 161, 835, item);
				} else {
					player.getPacketSender().sendClientScript(149, "IviiiIsssss", 1126 << 16 | startingComponent,
							startingContainer, 4, 7, 1, -1,
							"", "", "", "", "");
					player.getPacketSender().sendItems(-1, startingComponent, startingContainer, item);
					startingComponent += 4;
					startingContainer++;
				}
			}
		}
	}

	private void refreshLevelChances(Player player) {
		int levelTwoChance = ItemBreakPerks.getChance(player, perk.level2Chance, perk.cost, itemsInPerkAttachment);
		int levelThreeChance = ItemBreakPerks.getChance(player, perk.level3Chance, perk.cost, itemsInPerkAttachment);
		int levelFourChance = ItemBreakPerks.getChance(player, perk.level4Chance, perk.cost, itemsInPerkAttachment);
		int levelFiveChance = ItemBreakPerks.getChance(player, perk.level5Chance, perk.cost, itemsInPerkAttachment);
		StringBuilder chances = new StringBuilder();
		chances.append("Lv. 1: ").append("100").append("%<br>");
		chances.append("Lv. 2: ").append(levelTwoChance).append("%<br>");
		chances.append("Lv. 3: ").append(levelThreeChance).append("%<br>");
		chances.append("Lv. 4: ").append(levelFourChance).append("%<br>");
		chances.append("Lv. 5: ").append(levelFiveChance).append("%<br>");
		player.getPacketSender().sendString(1126, 162, chances.toString());
	}

	private int getEnhancersNeeded(ItemBreakPerks perk) {
		int totalMineralsNeeded = 0;
		int totalEnhancersNeeded = 0;

		for (Item item : perk.cost) {
			if (item.getDef().name.contains("mineral")) {
				totalMineralsNeeded += item.getAmount();
			} else if (item.getDef().name.contains("enhancer")) {
				totalEnhancersNeeded += item.getAmount();
			}
		}

		int totalMineralsProvided = 0;
		for (Item item : itemsInPerkAttachment) {
			if (item.getDef().name.contains("mineral")) {
				totalMineralsProvided += item.getAmount();
			}
		}

		if (totalMineralsProvided == 0) {
			return totalEnhancersNeeded;
		}

		double mineralRatio = (double) totalMineralsProvided / totalMineralsNeeded;

		int enhancersNeeded = (int) Math.ceil(totalEnhancersNeeded * mineralRatio);

		return enhancersNeeded;
	}

	boolean minimumIngredientsAdded = false;

	private void addMinimumIngredients(Player player) {
		if (minimumIngredientsAdded)
			return;
		if (!ItemBreakPerks.canUpgrade(player, perk)) {
			player.sendMessage("You do not have the required materials to upgrade this item.");
			StringBuilder cost = new StringBuilder();
			for (int i = 0; i < perk.cost.size(); i++) {
				cost.append(perk.cost.get(i).getAmount()).append(" ").append(perk.cost.get(i).getDef().getName()).append(", ");
			}

			player.sendMessage("You need: " + cost);
			return;
		}
		for (Item costItem : perk.cost) {
			itemsInPerkAttachment.add(new Item(costItem.getId(), costItem.getAmount()));
		}
		refreshPerkAttachment(player, false);
		minimumIngredientsAdded = true;

	}

	private void closePerkAttachment(Player player) {
		perkAttachmentActive = false;
		minimumIngredientsAdded = false;
		player.getPacketSender().setHidden(1126, 122, true);
	}

	private void offerIngredients(Player player, Item item) {
		if (!minimumIngredientsAdded) {
			player.sendMessage("You must add the minimum ingredients first.");
			return;
		}
		if (!item.getDef().name.contains("enhancer") && !item.getDef().name.contains("mineral")) {
			player.sendMessage("You cannot deposit this item.");
			return;
		}
		if (item.getDef().name.contains("minerals")) {
			int lowestTierMineralIdAllowed = getHighestMineralsAllowed(perk);
			if (item.getId() != lowestTierMineralIdAllowed) {
				player.sendMessage("You must deposit the mineral allowed for this perk.");
				return;
			}
		}
		if (item.getDef().name.contains("enhancer")) {
			int enhancerId = getEnhancerId(perk);
			if (item.getId() != enhancerId) {
				player.sendMessage("You must deposit the enhancer allowed for this perk.");
				return;
			}
		}
		player.integerInput("How many would you like to deposit?", amount -> {
			int mineralsAmountCurrentlyAdded = getMineralAmountInPerkAttachment(item);
			int enhancersAmountCurrentlyAdded = getEnhancersInPerkAttachment();
			if (amount < 1) {
				player.sendMessage("You must deposit at least 1 item.");
				return;
			}
			if (amount > player.getInventory().getAmount(item.getId())) {
				amount = player.getInventory().getAmount(item.getId());
			}
			if (mineralsAmountCurrentlyAdded + amount > player.getInventory().getAmount(item.getId())
					&& item.getDef().name.contains("minerals")) {
				amount = player.getInventory().getAmount(item.getId()) - mineralsAmountCurrentlyAdded;
			}
			if (enhancersAmountCurrentlyAdded + amount > player.getInventory().getAmount(item.getId())
					&& item.getDef().name.contains("enhancer")) {
				amount = player.getInventory().getAmount(item.getId()) - enhancersAmountCurrentlyAdded;
			}
			int maxMinerals = getBaseItemCost(item) * 3;
			if (maxMinerals > 0 && item.getDef().name.contains("minerals")) {
				if (getMineralAmountInPerkAttachment(item) + amount > maxMinerals) {
					// System.out.println("max minerals " + maxMinerals + " mineral amount in perk
					// attachment " + getMineralAmountInPerkAttachment(item));
					amount = maxMinerals - getMineralAmountInPerkAttachment(item);
				}
			}
			int maxEnhancers = getBaseItemCost(item) * 3;
			if (maxEnhancers > 0 && item.getDef().name.contains("enhancer")) {
				if (getEnhancersInPerkAttachment() + amount > maxEnhancers) {
					amount = maxEnhancers - getEnhancersInPerkAttachment();
				}
			}
			for (Item ing : itemsInPerkAttachment) {
				if (ing.getId() == item.getId()) {
					ing.setAmount(ing.getAmount() + amount);
				}
			}
			refreshPerkAttachment(player, false);
			refreshLevelChances(player);
		});
	}

	private int getBaseItemCost(Item item) {
		for (Item ing : perk.cost) {
			if (ing.getId() == item.getId()) {
				return ing.getAmount();
			}
		}
		return 0;
	}

	public int getHighestMineralsAllowed(ItemBreakPerks perk) {
		int highestId = 0;
		String highestType = "";

		for (Item item : perk.cost) {

			String itemName = item.getDef().getName().toLowerCase();

			if (!itemName.contains("minerals")) {
				continue;
			}
			// System.out.println("item name " + itemName);
			if (highestId == 0 || isHigherRank(itemName, highestType)) {
				System.out.println("highest type " + highestType + " item id " + item.getId());
				highestId = item.getId();
				highestType = itemName;
				// System.out.println("highest type " + highestType);
			}
		}
		return highestId;
	}

	private boolean isHigherRank(String newMineral, String currentHighest) {
		// Define hierarchy: the lower the number, the higher the priority
		int newRank = getMineralRank(newMineral);
		int currentRank = getMineralRank(currentHighest);

		return newRank < currentRank;
	}

	private int getMineralRank(String mineral) {
		if (mineral.contains("dull")) {
			return 3;
		} else if (mineral.contains("shiny")) {
			return 2;
		} else {
			return 1;
		}
	}

	private int getEnhancersInPerkAttachment() {
		int enhancersInPerkAttachment = 0;
		for (Item item : itemsInPerkAttachment) {
			if (item.getDef().name.contains("enhancer")) {
				enhancersInPerkAttachment += item.getAmount();
			}
		}
		return enhancersInPerkAttachment;
	}

	private int getMineralAmountInPerkAttachment(Item mineral) {
		int mineralsInPerkAttachment = 0;
		for (Item item : itemsInPerkAttachment) {
			if (item.getId() == mineral.getId()) {
				mineralsInPerkAttachment += item.getAmount();
				break;
			}
		}
		return mineralsInPerkAttachment;
	}

	private void confirmUpgrade(Player player) {
		if (this.selectedItem == null) {
			player.sendMessage("Please select the item first.");
			return;
		}

		if (!minimumIngredientsAdded) {
			player.sendMessage("You must add the minimum ingredients first.");
			return;
		}

		int enhancersNeeded = getEnhancersNeeded(perk);
		int enhancersInPerkAttachment = getEnhancersInPerkAttachment();
		if (enhancersInPerkAttachment < enhancersNeeded) {
			player.sendMessage("You do not have enough enhancers to upgrade this item.");
			return;
		}

		int currentLevel = AttributeExtensions.getCharges(perk.perk, selectedItem);
		if (currentLevel >= 5) {
			player.sendMessage("This item already has the maximum level for this perk.");
			return;
		}

		if (getTotalAttachments(selectedItem) >= getMaxAttachments(selectedItem)
				&& !AttributeExtensions.hasAttribute(selectedItem, perk.perk)) {
			player.sendMessage("This item already has the maximum amount of attachments.");
			return;
		}

		if (perk == ItemBreakPerks.SOUL_REAVER && selectedItem.getDef().name.contains("Scythe")) {
			player.sendMessage("You cannot attach the Soul Reaver perk to this weapon.");
			return;
		}
		
		if (perk.specificSlot) {
			boolean correctSlot = false;
			for (int slot : perk.slots) {
				// System.out.println("slot " + slot);
				if (selectedItem.getDef().equipSlot == slot) {
					correctSlot = true;
					break;
				}
			}
			if (!correctSlot) {
				player.sendMessage("You cannot attach this perk to this item slot.");
				return;
			}
		}
		player.dialogue(new OptionsDialogue("Are you sure you want to upgrade this item?",
				new Option("Yes, upgrade it.", () -> {
					// Delete the items
					for (Item item : itemsInPerkAttachment) {
						if (savedMaterial(player)) continue;
						int toRemove = item.getAmount();
						if(player.getInventory().contains(26986)) {
							if(player.mineralBagItems.get(item.getId()) != null) {
								int amount = player.mineralBagItems.get(item.getId());
								if(amount >= item.getAmount()) {
									player.mineralBagItems.put(item.getId(), amount - item.getAmount());
									continue;
								} else {
									player.mineralBagItems.remove(item.getId());
									toRemove -= amount;
								}
							}
						}
						player.getInventory().remove(item.getId(), toRemove);
					}
					// Upgrade the item
					ItemBreakPerks.upgradeItem(player, selectedItem, perk,
						ItemBreakPerks.getChance(player, perk.level5Chance, perk.cost, itemsInPerkAttachment),
						ItemBreakPerks.getChance(player, perk.level4Chance, perk.cost, itemsInPerkAttachment),
						ItemBreakPerks.getChance(player, perk.level3Chance, perk.cost, itemsInPerkAttachment),
						ItemBreakPerks.getChance(player, perk.level2Chance, perk.cost, itemsInPerkAttachment)
					);
					// Clear the item in the perk attachment
					itemsInPerkAttachment.clear();
					// Refresh and reopen the UI
					refreshPerkAttachment(player, true);
					open(player, false);
					sendPerkAttachment(player);
					addMinimumIngredients(player);
				}),
				new Option("Nevermind.")));

	}

	/**
	 * Determines whether a material is saved based on the player's donor group.
	 *
	 * @param player the player for whom the saved material logic is being checked
	 * @return true if the material is saved based on the player's secondary group and associated probability, false otherwise
	 */
	private boolean savedMaterial(Player player) {
		var rolled = new Random().nextDouble();
		return switch(player.getSecondaryGroup()) {
			case SUPREME_DONATOR, LEGENDARY_DONATOR -> rolled < 0.2;
			case PLATINUM_DONATOR, GOLD_DONATOR -> rolled < 0.15;
			case NOBLE_DONATOR, ELITE_DONATOR -> rolled < 0.1;
			case SUPER_DONATOR, DONATOR -> rolled < 0.05;
			case NONE -> false;
		};
	}

	private int getEnhancerId(ItemBreakPerks perk) {
		for (Item item : perk.cost) {
			if (item.getDef().name.contains("enhancer"))
				return item.getId();
		}
		return -1;
	}

	public static int getMaxAttachments(Item selectedItem) {
		BreakItems breakItems = BreakItems.forId(selectedItem.getId());
		if (breakItems == null)
			return 1;
		if (breakItems.tier > 7)
			return 3;
		else if (breakItems.tier > 3)
			return 2;
		return 1;
	}

	public static void register() {
		InterfaceHandler.register(1128, (interfaceHandler -> {
			interfaceHandler.actions[0] = new InterfaceAction() {

				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					if (player.getItemUpgradeInterface().perkAttachmentActive) {
						player.getItemUpgradeInterface().offerIngredients(player, player.getInventory().get(slot));
						return;
					} else {
						player.getItemUpgradeInterface().selectItem(player, player.getInventory().get(slot));
					}
				}

				@Override
				public void handleDrag(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId,
						int toSlot, int toItemId) {
					TabInventory.drag(player, fromSlot, toSlot);
				}
			};
		}));
		InterfaceHandler.register(1126, (interfaceHandler -> {
			interfaceHandler.actions[37] = (SimpleAction) player -> player.getItemUpgradeInterface()
					.sendPerkAttachment(player);
			interfaceHandler.actions[164] = (SimpleAction) player -> player.getItemUpgradeInterface()
					.closePerkAttachment(player);
			interfaceHandler.actions[141] = (SimpleAction) player -> player.getItemUpgradeInterface()
					.addMinimumIngredients(player);
			interfaceHandler.actions[143] = (SimpleAction) player -> player.getItemUpgradeInterface().confirmUpgrade(player);
			for (int i = 46; i <= 106; i += 3) {
				int index = (i - 44) / 3;
				interfaceHandler.actions[i] = (SimpleAction) player -> player.getItemUpgradeInterface().selectPerk(player,
						ItemBreakPerks.VALUES[index]);
			}
		}));
	}
}
