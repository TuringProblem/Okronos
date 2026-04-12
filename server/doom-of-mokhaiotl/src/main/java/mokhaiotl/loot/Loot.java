package mokhaiotl.loot;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-20
 */
public interface Loot {

	static List<Item> getSortedItems(Player player) {
		var rewards = new ArrayList<Item>();
		for (var item : player.mokhaiotlRewardItems) {
			Item existing = null;
			for (var r : rewards) {
				if (r.getId() == item.getId()) {
					existing = r;
					break;
				}
			}
			if (existing != null)
				existing.setAmount(existing.getAmount() + item.getAmount());
			else
				rewards.add(new Item(item.getId(), item.getAmount()));
		}
		return rewards;
	}


	static LootTable getTable(boolean unique) {
		return unique ? UNIQUE_TABLE : LOOT_TABLE;
	}

	// This is left Empty, as it get populated based on the Delve Level
	LootTable UNIQUE_TABLE = new LootTable();

	LootTable LOOT_TABLE = new LootTable()
		.addTable("Weapons and Armour", 1,
			new LootItem(ItemID.DRAGON_MED_HELM + 1, 1, 1),
			new LootItem(ItemID.MYSTIC_EARTH_STAFF + 1, 1, 1),
			new LootItem(ItemID.RUNE_PICKAXE + 1, 1, 3, 1),
			new LootItem(ItemID.DRAGON_PLATELEGS + 1, 2, 4, 1)
		)
		.addTable("Runes and ammunition", 1,
			new LootItem(ItemID.DEATH_RUNE, 50, 70, 1),
			new LootItem(ItemID.CHAOS_RUNE, 50, 70, 1),
			new LootItem(ItemID.EARTH_RUNE, 50, 70, 1),
			new LootItem(ItemID.FIRE_RUNE, 500, 1_000, 1),
			new LootItem(ItemID.CANNONBALL, 200, 600, 1),
			new LootItem(ItemID.ONYX_BOLTS, 5, 15, 1)
		)
		.addTable("Ores", 1,
			new LootItem(ItemID.COAL + 1, 15, 50, 1),
			new LootItem(ItemID.GOLD_ORE + 1, 20, 60, 1),
			new LootItem(ItemID.RUNITE_ORE + 1, 3, 6, 1)
		)
		.addTable("Seeds", 1,
			new LootItem(ItemID.CELASTRUS_SEED, 1, 1),
			new LootItem(ItemID.SPIRIT_SEED, 1, 1),
			new LootItem(ItemID.RANARR_SEED, 1, 3, 1)
		)
		.addTable("Resources", 1,
			new LootItem(ItemID.AETHER_CATALYST, 150, 400, 1),
			new LootItem(ItemID.DRAGON_DART_TIP, 30, 90, 1),
			new LootItem(ItemID.SUN_KISSED_BONES, 25, 75, 1),
			new LootItem(ItemID.RAW_SHARK + 1, 20, 35, 1),
			new LootItem(ItemID.SHARK_LURE, 40, 70, 1),
			new LootItem(ItemID.SUNFIRE_SPLINTERS, 500, 1_500, 1)
		)
		.addTable("Other", 1,
			new LootItem(ItemID.DEMON_TEAR, 1, 1),
			new LootItem(ItemID.MOKHAIOTL_WAYSTONE, 1, 1),
			new LootItem(ItemID.CLUE_SCROLL_ELITE, 1, 1),
			new LootItem(ItemID.TOOTH_HALF_OF_MOON_KEY, 1, 1)
		);
}
