package io.ruin.model.item.loot;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.api.utils.WebTable;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.cache.NPCType;
import io.ruin.cache.ObjType;
import io.ruin.data.impl.npcs.npc_drops_new;
import io.ruin.model.activities.perktree.PerkSets;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.perktree.perksets.GoldDigger;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.item.Item;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Slf4j
public class LootTable {

	public static class DropItem {
		@Getter
		public int item;
		@Getter
		public int chance;
		@Getter
		public int minAmount;
		@Getter
		public int maxAmount;

		public DropItem(int item, int chance, int minAmount, int maxAmount) {
			this.item = item;
			this.chance = chance;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
		}
	}

	/**
	 * A table of items unique to this table type.
	 */

	public static final class ItemsTable {

		public final String name;
		public final int weight;
		public final LootItem[] items;
		public transient double totalWeight;
		public transient int rollChance;

		public ItemsTable(String name, int weight, LootItem[] items) {
			this(name, weight, items, 0);
		}

		public ItemsTable(String name, int weight, LootItem[] items, int rollChance) {
			this.name = name;
			this.weight = weight;
			this.items = items;
			for (LootItem item : items) {
				totalWeight += item.weight;
				if (ObjType.get(item.id) == null)
					System.err.println("Non-existing item in loot-table \"" + name + "\": " + item.id);
			}
			this.rollChance = rollChance;
		}


	}

	public static void simulateDrop(int npc, float bonusDropRate) {
		NPCType def = NPCType.get(npc);
		LootTable t = def.lootTable;
		List<Item> items = t.rollItems(true, bonusDropRate);
		log.info("Item Drops received: {}", items);
	}

	public static void calculateAndSave(int npcID) {
		List<Map<String, Object>> dropList = new ArrayList<>();

		// Retrieve loot tables for the NPC
		List<ItemsTable> tables = NPCType.get(npcID).lootTable.tables;
		if (tables == null || tables.isEmpty()) return;

		// Calculate total table weights
		int totalTableWeights = tables.stream().mapToInt(table -> table.weight).sum();

		// Include guaranteed drops if present
		if (NPCType.get(npcID).lootTable.guaranteed != null) {
			for (LootItem guaranteedDrop : NPCType.get(npcID).lootTable.guaranteed) {
				// Add guaranteed drop to drop list
				Map<String, Object> guaranteedDropMap = new HashMap<>();
				guaranteedDropMap.put("itemid", guaranteedDrop.id);
				guaranteedDropMap.put("minAmount", guaranteedDrop.min);
				guaranteedDropMap.put("maxAmount", guaranteedDrop.max);
				guaranteedDropMap.put("dropRate", 1); // Guaranteed drops have drop rate of 1
				guaranteedDropMap.put("broadcast", guaranteedDrop.broadcast); // Include broadcast mode
				dropList.add(guaranteedDropMap);
			}
		}

		// Iterate over each loot table
		for (ItemsTable table : tables) {
			float tableProbability = (float) table.weight / totalTableWeights;
			List<LootItem> items = Arrays.asList(table.items);
			if (!items.isEmpty()) {
				for (LootItem item : items) {
					double itemProbability = item.weight / table.totalWeight;
					float itemChance = (float) (tableProbability * itemProbability);
					int dropRate = (int) Math.ceil(1.0 / itemChance);

					// Add drop to drop list
					Map<String, Object> drop = new HashMap<>();
					drop.put("itemid", item.id);
					drop.put("minAmount", item.min);
					drop.put("maxAmount", item.max);
					drop.put("dropRate", dropRate);
					drop.put("broadcast", item.broadcast); // Include broadcast mode
					dropList.add(drop);
				}
			}
		}

		// Convert drop list to JSON
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(dropList);

		// Define file path and save JSON to file
		String filePath = "data/npcs/drops/newDrops/" + npcID + ".json";
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void calculateAndGuaranteedSave(int npcID) {
		List<Map<String, Object>> dropList = new ArrayList<>();

		// Include guaranteed drops if present
		if (NPCType.get(npcID).lootTable.guaranteed != null) {
			for (LootItem guaranteedDrop : NPCType.get(npcID).lootTable.guaranteed) {
				// Add guaranteed drop to drop list
				Map<String, Object> guaranteedDropMap = new HashMap<>();
				guaranteedDropMap.put("itemid", guaranteedDrop.id);
				guaranteedDropMap.put("minAmount", guaranteedDrop.min);
				guaranteedDropMap.put("maxAmount", guaranteedDrop.max);
				guaranteedDropMap.put("dropRate", 1); // Guaranteed drops have drop rate of 1
				guaranteedDropMap.put("broadcast", guaranteedDrop.broadcast); // Include broadcast mode
				dropList.add(guaranteedDropMap);
			}
		}

		// Convert drop list to JSON
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(dropList);

		// Define file path and check existence
		String filePath = "data/npcs/drops/newDrops/" + npcID + ".json";
		File file = new File(filePath);

		// Skip writing if the file already exists
		if (file.exists()) {
			return;
		}

		// Save JSON to file
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LootItem[] guaranteed;
	public List<ItemsTable> tables;
	public transient double totalWeight;

	/**
	 * Methods used for creating tables @ runtime.
	 */
	public LootTable guaranteedItems(LootItem... items) {
		guaranteed = items;
		return this;
	}

	public List<LootItem> getLootItems() {
		List<LootItem> items = Lists.newArrayList();
		for (ItemsTable table : tables) {
			items.addAll(Arrays.asList(table.items));
		}
		return items;
	}

	public LootTable addTable(int tableWeight, LootItem... tableItems) {
		return addTable(null, tableWeight, tableItems);
	}

	public LootTable addTable(String tableName, int tableWeight, LootItem... tableItems) {
		if (tables == null)
			tables = new ArrayList<>();
		tables.add(new ItemsTable(tableName, tableWeight, tableItems));
		totalWeight += tableWeight;
		return this;
	}

	/**
	 * Methods pretty much specifically for npc drop tables.
	 */

	public LootTable combine(LootTable table) {
		LootTable newTable = new LootTable();

		List<LootItem> newGuaranteed = new ArrayList<>();
		if (guaranteed != null)
			Collections.addAll(newGuaranteed, guaranteed);
		if (table.guaranteed != null)
			Collections.addAll(newGuaranteed, table.guaranteed);
		newTable.guaranteed = newGuaranteed.isEmpty() ? null : newGuaranteed.toArray(new LootItem[0]);

		List<ItemsTable> newTables = new ArrayList<>();
		if (tables != null)
			newTables.addAll(tables);
		if (table.tables != null)
			newTables.addAll(table.tables);
		newTable.tables = newTables.isEmpty() ? null : newTables;

		return newTable;
	}

	public void calculateWeight() {
		totalWeight = 0;
		if (tables != null) {
			for (ItemsTable table : tables) {
				totalWeight += table.weight;
				table.totalWeight = 0;
				if (table.items != null) {
					for (LootItem item : table.items)
						table.totalWeight += item.weight;
				}
			}
		}
	}

	/**
	 * Item selection
	 */

	public Item rollItem() {
		List<Item> items = rollItems(false, 0); //TODO
		return items == null ? null : items.get(0);
	}

	public List<Item> rollItems(boolean allowGuaranteed, float dropRateBonus) {

		List<Item> items = new ArrayList<>();

		if (allowGuaranteed && guaranteed != null) {
			for (LootItem item : guaranteed)
				items.add(item.toItem());
		}

		if (tables != null) {
			double tableRand = Random.get() * totalWeight/*(modifier + totalWeight)*/;
//            log.info("Random roll: {}", tableRand);
//            log.info("Bonus Drop rate in percentage: {}", dropRateBonus);
			for (ItemsTable table : tables) {
				if ((tableRand -= table.weight) <= 0) {
					if (table.items != null) {
						var itemsRand = Random.get() * ((table.totalWeight * dropRateBonus / 100) + table.totalWeight);
//                        log.info("Total table weight: {}", table.totalWeight);
//                        log.info("Random table roll: {}", itemsRand);
						for (LootItem item : table.items) {
							if (item.weight == 0) {
								/* weightless item landed, add it and continue loop */
								items.add(item.toItem());
								continue;
							}
//                            log.info("Normal item weight: {}", item.weight);
							float modifiedItemWeight = dropRateBonus == 0 ?
								item.weight :
								(item.weight * dropRateBonus / 100) + item.weight;
//                            log.info("Modified item weight: {}", modifiedItemWeight);

							if ((itemsRand -= modifiedItemWeight) <= 0) {
								/* weighted item landed, add it and break loop */
								items.add(item.toItem());
								break;
							}
						}
					}
					break;
				}
			}
		}
		return items.isEmpty() ? null : items;
	}

	public List<Item> allItems() {
		List<Item> items = new ArrayList<>();
		if (guaranteed != null) {
			for (LootItem item : guaranteed) {
				if (item != null)
					items.add(item.toItem());
			}
		}
		if (tables != null) {
			for (ItemsTable table : tables) {
				if (table.items != null) {
					for (LootItem item : table.items) {
						if (item != null)
							items.add(item.toItem());
					}
				}
			}
		}
		return items;
	}

	/*
	 * Returns the weight of an item on a loot table
	 */
	public int getWeight(Item item) {
		int weight = 100;
		List<LootItem> searchTable = getLootItems();
		for (LootItem itemSearch : searchTable) {
			if (itemSearch.id == item.getId()) {
				weight = itemSearch.weight;
			}
		}
		return weight;
	}


	public void calculate(String name) {
		DecimalFormat format = new DecimalFormat("0.###");
		WebTable tablesTable = new WebTable(name + " | Tables | Total weight = " + totalWeight);
		WebTable itemsTable = new WebTable(name + " | Items");
		for (ItemsTable table : tables) {
			double tableProbability = table.weight / totalWeight;
			tablesTable.newEntry().add("name", table.name)
				.add("weight", table.weight)
				.add("average (1 in X)", (int) (1 / (tableProbability)))
				.add("probability", format.format(tableProbability))
				.add("percentage", format.format(tableProbability * 100) + "%");
			if (table.items.length == 0) {
				int itemWeight = 1;
				double probabilityInTable = itemWeight / table.totalWeight;
				itemsTable.newEntry()
					.add("table", table.name)
					.add("id", -1)
					.add("name", "Nothing")
					.add("min amount", -1)
					.add("max amount", -1)

					.add("overall average (1 in X)", (int) (1 / (probabilityInTable * tableProbability)))
					.add("weight in table", itemWeight)
					.add("overall probability", format.format(probabilityInTable * tableProbability))
					.add("overall percentage", format.format(probabilityInTable * tableProbability * 100) + "%")

					.add("average in table (1 in X)", (int) (1 / (probabilityInTable)))
					.add("probability in table", format.format(probabilityInTable))
					.add("percentage in table", format.format(probabilityInTable * 100) + "%");
			} else {
				for (LootItem item : table.items) {
					double probabilityInTable = item.weight / table.totalWeight;
					itemsTable.newEntry()
						.add("table", table.name)
						.add("id", item.id)
						.add("name", item.getName())
						.add("min amount", item.min)
						.add("max amount", item.max)

						.add("overall average (1 in X)", (int) (1 / (probabilityInTable * tableProbability)))
						.add("weight in table", item.weight)
						.add("overall probability", format.format(probabilityInTable * tableProbability))
						.add("overall percentage", format.format(probabilityInTable * tableProbability * 100) + "%")

						.add("average in table (1 in X)", (int) (1 / (probabilityInTable)))
						.add("probability in table", format.format(probabilityInTable))
						.add("percentage in table", format.format(probabilityInTable * 100) + "%");

				}
			}
		}
	}

	public void calculate(int npcID, Player player) {
		List<npc_drops_new.DropTable> dropInformation = npc_drops_new.getDropInformationLootTable(npcID);

		if (dropInformation == null || dropInformation.isEmpty()) {
			player.closeInterfaces();
			return;
		}

		// Your existing code to setup UI components
		player.getPacketSender().sendString(855, 2799, "" + NPCType.get(npcID).name);
		int itemContainer = 60;
		int itemContainerId = 1000;
		int nameStringComponent = 57;
		int quantityComponent = 58;
		int rarityComponent = 59;
		int parentComponentId = 51;
		for (int i = 51; i < 2781; i += 10) {
			player.getPacketSender().setHidden(855, i, true);
		}

		List<DropItem> dropItems = new ArrayList<>();

		// Process each drop from the drop information
		for (npc_drops_new.DropTable drop : dropInformation) {
			int dropRate = drop.dropRate;
			float dropRateBonus = 1.0f - (player.calculateDropRate() / 100f);
			dropRate *= dropRateBonus;
			if (player.dropRateBoostTimer.remaining() > 0)
				dropRate *= 0.95;


			DropItem dropItem = new DropItem(drop.itemid, dropRate, drop.minAmount, drop.maxAmount);
			dropItems.add(dropItem);
		}
		NPC npc = new NPC(npcID);
		npc.setCombat();

		if (npc.getCombat() != null) {
			if (npc.getCombat().getInfo() != null) {
				if (npc.getCombat().getInfo().pet != null) {
					float petDropRate = npc.getCombat().getInfo().pet.dropAverage;
					float dropRateBonus = 1 - (player.calculateDropRate() / 100f);
					petDropRate *= dropRateBonus;
					if (player.dropRateBoostTimer.remaining() > 0)
						petDropRate *= 0.95f;
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
						ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
							getActivePerks(player).get(perkIndex).getPerk(player);
						assert c != null;
						petDropRate *= (float) c.getPetChanceBoost();
					}
					if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
						petDropRate *= 0.85F;
					if (player.petDropBonus.isDelayed())
						petDropRate *= 0.75f;
					petDropRate *= getPetDonatorBoost(player);
					dropItems.add(new DropItem(npc.getCombat().getInfo().pet.itemId, (int) petDropRate, 1, 1));

				}
			}
		}
		if(npc.getId() == 14176) {
			dropItems.add(new DropItem(33030, (int) 100000, 1, 1));
		}

		// Sort drop items by chance (drop rate)
		Collections.sort(dropItems, Comparator.comparingInt(DropItem::getChance).reversed());

		// Display drop items in UI
		for (DropItem dropItem : dropItems) {
			player.getPacketSender().setHidden(855, parentComponentId, false);
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				855 << 16 | itemContainer, itemContainerId,
				4, 7, 1, -1, "", "", "", "", ""
			);
			player.getPacketSender().sendItems(
				-1,
				itemContainer,
				itemContainerId,
				new Item(dropItem.item, dropItem.getMaxAmount())
			);
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 27, 1024);
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 27, 1086);
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 5, AccessMasks.ClickOp1);
			player.getPacketSender().sendIfEvents(855, itemContainer, 0, 27, 1086);

			player.getPacketSender().sendString(855, nameStringComponent, "" + new Item(dropItem.item).getDef().name);
			if (dropItem.getMinAmount() == dropItem.getMaxAmount())
				player.getPacketSender().sendString(855, quantityComponent, "" + dropItem.getMinAmount());
			else
				player.getPacketSender().sendString(855, quantityComponent, "" + dropItem.getMinAmount() + "-" + dropItem.getMaxAmount());

			if (dropItem.getChance() == 1)
				player.getPacketSender().sendString(855, rarityComponent, Color.PURPLE.wrap("Always"));
			else
				player.getPacketSender().sendString(855, rarityComponent, "1/" + NumberUtils.formatNumber(dropItem.getChance()));

			// Increment UI component indices
			itemContainer += 10;
			itemContainerId++;
			nameStringComponent += 10;
			quantityComponent += 10;
			rarityComponent += 10;
			parentComponentId += 10;
			if (parentComponentId > 779)
				break;
		}
	}


	public Item rollDrop(Player player) {
		List<Item> items = rollDrop(false, player);
		return items == null ? null : items.get(0);
	}

	public List<Item> rollDrop(boolean allowGuaranteed, Player player) {
		List<Item> items;
		List<Item> potentialItems = new ArrayList<>();
		if (allowGuaranteed && guaranteed != null) {
			items = new ArrayList<>(guaranteed.length + 1);
			for (LootItem item : guaranteed)
				items.add(item.toItem());
		} else {
			items = new ArrayList<>(1);
		}
		if (tables != null) {
			float dropRateBonus = 1.0f - (player.calculateDropRate() / 100f);
			double tableRand = Random.get() * totalWeight;
			int maxTableWeight = 0;
			int maxTableFinalWeight = 0;
			for (ItemsTable table : tables) {
				if (maxTableWeight < table.weight)
					maxTableWeight = table.weight;
				if (maxTableFinalWeight < table.weight)
					maxTableFinalWeight = table.weight;
			}
			List<ItemsTable> potentailTables = new ArrayList<>();

			for (ItemsTable table : tables) {
				if ((tableRand -= table.weight) <= 0) {
					if (table.items != null) {
						double itemsRand = Random.get() * (table.totalWeight * dropRateBonus);
						int maxWeight = 0;
						for (LootItem item : table.items) {
							if (maxWeight < item.weight)
								maxWeight = item.weight;
						}
						float multiplier = 1.0f;
						if (player.dropRateBoostTimer.remaining() > 0)
							multiplier *= 0.95f;
						if (player.getDifficulty() == Difficulty.EASY)
							multiplier *= 1.35F;
						else if (player.getDifficulty() == Difficulty.EXTREME)
							multiplier *= 0.85F;
						multiplier -= (player.calculateDropRate() / 100.0f);
						float chanceToRoll = (float) maxWeight * multiplier;
						int roll = Random.get((int) chanceToRoll);


						for (LootItem item : table.items) {
							if (item.weight == 0) {
								/* weightless item landed, add it and continue loop */
								items.add(item.toItem());
								continue;
							}


							if (roll <= item.weight) {
								/* item landed, add it to potentialItems */
								potentialItems.add(item.toItem());
							}
						}
						List<Item> genericTable = Arrays.asList(
							new Item(995, 3000),
							new Item(ItemID.LOOP_HALF_OF_KEY, 1),
							new Item(ItemID.TOOTH_HALF_OF_KEY, 1),
							new Item(ItemID.UNCUT_SAPPHIRE, 1),
							new Item(ItemID.UNCUT_EMERALD, 1),
							new Item(ItemID.UNCUT_RUBY, 1),
							new Item(ItemID.UNCUT_DIAMOND, 1),
							new Item(ItemID.NATURE_RUNE, 67),
							new Item(ItemID.ADAMANT_JAVELIN, 20),
							new Item(ItemID.DEATH_RUNE, 45),
							new Item(ItemID.LAW_RUNE, 45),
							new Item(ItemID.RUNE_ARROW, 42),
							new Item(ItemID.STEEL_ARROW, 150),
							new Item(ItemID.RUNITE_BAR, 1),
							new Item(ItemID.DRAGONSTONE, 1),
							new Item(ItemID.RUNE_2H_SWORD, 1),
							new Item(ItemID.RUNE_BATTLEAXE, 1),
							new Item(ItemID.RUNE_SQ_SHIELD, 1),
							new Item(ItemID.DRAGON_MED_HELM, 1),
							new Item(ItemID.RUNE_KITESHIELD, 1)

						);
						if (potentialItems.isEmpty())
							potentialItems.add(Random.get(genericTable));

						if (Random.get(14) == 0 || player.getDifficulty() == Difficulty.EASY)
							potentialItems.addAll(genericTable);

						/* Randomly select an item from potentialItems and add it to the final items list */
						items.add(Random.get(potentialItems));
					}
					break;
				}
			}
		}
		return items.isEmpty() ? null : items;
	}

	private double getPetDonatorBoost(Player player) {
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
}
