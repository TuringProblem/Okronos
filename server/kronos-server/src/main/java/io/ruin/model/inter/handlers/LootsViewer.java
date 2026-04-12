package io.ruin.model.inter.handlers;

import io.ruin.cache.Color;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class LootsViewer {
	public static final int WIDGET_ID = 855;

	public HashMap<String, LootsTables> tableSearch = new HashMap<>();
	public HashMap<Integer, LootsTables> tableComponents = new HashMap<>();

	public static LootTable loot = new LootTable();

	private String formatForSearch(String string) {
		return string.replace("'", "")
			.toLowerCase()
			.trim();
	}

	public void searchNpcs(Player player, String name) {
		for (int i = 22; i < 41; i++) {
			player.getPacketSender().setHidden(WIDGET_ID, i, true);
		}
		String search = formatForSearch(name);
		ArrayList<LootsTables> tables = new ArrayList<>();
		tableSearch.clear();
		tableComponents.clear();
		int componentId = 22;
		if (name.length() < 3) {
			player.sendMessage("Your search must be at least 3 characters long!");
			return;
		}
		for (LootsTables table : LootsTables.VALUES) {

			if (table.tableName.toLowerCase().contains(search)) {
				if (tableSearch.containsKey(table.tableName))
					continue;

				tableSearch.put(table.tableName, table);
			}
		}
		for (LootsTables defs : tableSearch.values()) {
			player.getPacketSender().setHidden(WIDGET_ID, componentId, false);
			player.getPacketSender().sendString(WIDGET_ID, componentId, "" + defs.tableName);
			tableComponents.put(componentId, defs);
			if (componentId == 40)
				break;
			componentId++;
		}
	}

	private static double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	public void updateInterface(Player player, LootsTables tables) {

		player.inMonsterViewer = false;
		player.openInterface(ToplevelComponent.MAINMODAL, WIDGET_ID);
		if (tables == null)
			return;
		if (tables.table.tables.size() < 1)
			return;
		player.getPacketSender().sendString(855, 2799, "" + tables.tableName);
		int totalTableWeights = 0;
		int itemContainer = 60;
		int itemContainerId = 1000;
		int nameStringComponent = 57;
		int quantityComponent = 58;
		int rarityComponent = 59;
		int parentComponentId = 51;
		player.getPacketSender().sendString(855, 12, "Loots Viewer");
		for (int i = 22; i < 41; i++)
			player.getPacketSender().setHidden(855, i, true);

		for (int i = 51; i <= 2781; i += 10) {
			player.getPacketSender().setHidden(855, i, true);
		}
		for (LootTable.ItemsTable table : tables.table.tables) {
			totalTableWeights += table.weight;
		}
		List<Item> items = new ArrayList<>();
		List<LootTable.DropItem> dropItems = new ArrayList<>();
		if (tables == LootsTables.ARAXXOR) {
			int base = 1000;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
				base *= c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				base *= 0.8F;
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				base *= 0.85F;

			base *= getPetDonatorBoost(player);
			LootTable.DropItem dropItem = new LootTable.DropItem(Pet.NID.itemId, base, 1, 1);
			dropItems.add(dropItem);
		}
		if (tables == LootsTables.SOL_HEREDIT) {
			int base = 950;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
				base *= c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				base *= 0.8F;
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				base *= 0.85F;

			base *= getPetDonatorBoost(player);
			LootTable.DropItem dropItem = new LootTable.DropItem(28960, base, 1, 1);
			dropItems.add(dropItem);
		}

		for (LootTable.ItemsTable table : tables.table.tables) {
			if (table.items.length == 0) {
			} else {
				for (LootItem item : table.items) {
					// player.getPacketSender().setHidden(855, parentComponentId, false);
					float tableProbability = (float) table.weight / totalTableWeights;
					double itemProbability = item.weight / table.totalWeight;
					float itemChance = (float) ((tableProbability * itemProbability));
					int dropRate = (int) Math.ceil(1.0 / itemChance);
					LootTable.DropItem dropItem = new LootTable.DropItem(item.id, dropRate, item.min, item.max);
					dropItems.add(dropItem);

				}
			}
		}

		Collections.sort(dropItems, new Comparator<LootTable.DropItem>() {
			@Override
			public int compare(LootTable.DropItem item1, LootTable.DropItem item2) {
				if (item1.chance == 1 && item2.chance == 1) {
					return 0;
				} else if (item1.chance == 1) {
					return -1;
				} else if (item2.chance == 1) {
					return 1;
				} else {
					return Integer.compare(item2.chance, item1.chance);
				}
			}
		});
		for (LootTable.DropItem dropItem : dropItems) {
			player.getPacketSender().setHidden(855, parentComponentId, false);
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				855 << 16 | itemContainer, itemContainerId,
				4, 7, 1, -1, "", "", "", "", "");
			player.getPacketSender().sendItems(
				-1,
				itemContainer,
				itemContainerId,
				new Item(dropItem.item, dropItem.maxAmount));
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 27, 1024);
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 27, 1086);
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 5, AccessMasks.ClickOp1);
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 27, 1086);
			player.getPacketSender().sendString(855, nameStringComponent, "" + new Item(dropItem.item).getDef().name);
			if (dropItem.minAmount == dropItem.maxAmount)
				player.getPacketSender().sendString(855, quantityComponent, "" + dropItem.minAmount);
			else
				player.getPacketSender().sendString(855, quantityComponent, "" + dropItem.minAmount + "-" + dropItem.maxAmount);

			if (dropItem.chance == 1)
				player.getPacketSender().sendString(855, rarityComponent, Color.PURPLE.wrap("Always"));
			else
				player.getPacketSender().sendString(855, rarityComponent, "1/" + dropItem.chance);

			itemContainer += 10;
			itemContainerId++;
			nameStringComponent += 10;
			quantityComponent += 10;
			rarityComponent += 10;
			parentComponentId += 10;
			if (parentComponentId > 2782)
				break;

		}
	}
}
