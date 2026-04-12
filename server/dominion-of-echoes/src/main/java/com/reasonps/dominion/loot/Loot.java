package com.reasonps.dominion.loot;

import io.ruin.cache.ItemID;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-04
 */
public class Loot {

	public static final LootTable PURPLE_UNIQUES = new LootTable()
		.addTable(1,
			new LootItem(ItemID.MORYTANIA_ECHO_ORB,	 		1,	 1).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.WILDERNESS_ECHO_ORB,	 	1,	 3).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.DESERT_ECHO_ORB,		 	1,	 3).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.FREMENNIK_ECHO_ORB,		 	1,	 4).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.KANDARIN_ECHO_ORB,	 		1,	 5).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.VARLAMORE_ECHO_ORB,	 		1,	 8).broadcast(Broadcast.GLOBAL),
			new LootItem(ItemID.ASGARNIA_ECHO_ORB,		 	1,	 10).broadcast(Broadcast.GLOBAL)
		);

	public static final LootTable LOOT_TABLE = new LootTable()
		.addTable("Ultra-Rares", 1,
			new LootItem(28973, 1, 3).broadcast(Broadcast.GLOBAL), // Varlamore Crest (Used to make the Sunlight Spear)
			new LootItem(ItemID.DRAGON_PICKAXE, 1, 1),
			new LootItem(ItemID.BIG_WING, 1, 1),
			new LootItem(30550, 1, 1), // Imbued bone (Cerberus)
			new LootItem(ItemID.PRIMORDIAL_CRYSTAL, 1, 1),
			new LootItem(ItemID.PEGASIAN_CRYSTAL, 1, 1),
			new LootItem(ItemID.ETERNAL_CRYSTAL, 1, 1),
			new LootItem(ItemID.CRYSTAL_ARMOUR_SEED, 1, 1, 1),
			new LootItem(25859, 1, 2, 1), // Enhanced Crystal Weapon Seed
			new LootItem(ItemID.ARCHERS_RING, 1, 1),
			new LootItem(ItemID.WARRIOR_RING, 1, 1),
			new LootItem(ItemID.BERSERKER_RING, 1, 1),
			new LootItem(ItemID.SEERS_RING, 1, 1),
			new LootItem(30549, 1, 1), // Dagannoth Tooth
			new LootItem(30586, 1, 1), // Bellator vestige
			new LootItem(30585, 1, 1), // Venator vestige
			new LootItem(ItemID.DIZANAS_QUIVER_UN, 1, 1, 3),
			new LootItem(ItemID.DONATOR_MYSTERY_BOX, 1, 1, 2),
			new LootItem(ItemID.SUNFIRE_FANATIC_HELM, 1, 1, 5),
			new LootItem(ItemID.SUNFIRE_FANATIC_CUIRASS, 1, 1, 5),
			new LootItem(ItemID.SUNFIRE_FANATIC_CHAUSSES, 1, 1, 5),
			new LootItem(ItemID.OSMUMTENS_SPIRIT, 1, 1, 1),
			new LootItem(ItemID.DRYGORE_DART, 10_000, 8)
		)
		.addTable("Rares", 40,
			new LootItem(ItemID.PERK_POINT_SCROLL, 2, 6, 1),
			new LootItem(ItemID.DOUBLE_DROP_SCROLL, 1,3, 1),
			new LootItem(ItemID.DOUBLE_EXP_SCROLL, 1,3, 15),
			new LootItem(ItemID.DAMAGE_BOOST_SCROLL, 1,2, 1),
			new LootItem(ItemID.DAMAGE_REDUCTION_SCROLL, 1,2, 1),
			new LootItem(ItemID.INSTANCE_TOKEN, 4, 8, 1),
			new LootItem(ItemID.DRAGON_DART, 85, 150, 1),
			new LootItem(ItemID.DRYGORE_DART, 5_000, 8),
			new LootItem(ItemID.MAHOGANY_PLANK + 1, 100, 150, 1),
			new LootItem(ItemID.MAGIC_LOGS + 1, 100, 150, 1),
			new LootItem(ItemID.YEW_LOGS + 1, 100, 150, 1),
			new LootItem(ItemID.REDWOOD_LOGS + 1, 100, 150, 1),
			new LootItem(ItemID.UNCUT_DIAMOND + 1, 100, 150, 1),
			new LootItem(ItemID.UNCUT_RUBY + 1, 100, 150, 1),
			new LootItem(ItemID.RAW_MANTA_RAY + 1, 100, 150, 1),
			new LootItem(ItemID.RAW_SHARK + 1, 100, 150, 1),
			new LootItem(ItemID.RAW_ANGLERFISH + 1, 100, 150, 1),
			new LootItem(ItemID.UNCUT_DRAGONSTONE + 1, 100, 150, 1),
			new LootItem(ItemID.GOLD_BAR + 1, 100, 150, 1),
			new LootItem(ItemID.TEAK_PLANK + 1, 100, 150, 1),
			new LootItem(ItemID.GOLD_ORE + 1, 100, 150, 1),
			new LootItem(ItemID.ADAMANTITE_BAR + 1, 100, 150, 1),
			new LootItem(ItemID.RUNITE_BAR + 1, 100, 150, 1)
		)
		.addTable("Uncommon", 50,
			new LootItem(ItemID.DRYGORE_DART, 2_500, 100)
		)
		.addTable("Common", 75,
			new LootItem(995, 750_000, 2_500_000, 10),
			new LootItem(995, 1500_000, 5_500_000, 4),
			new LootItem(ItemID.DRYGORE_DART, 500, 1000, 16),
			new LootItem(ItemID.CLUE_SCROLL_MASTER, 1, 1),
			new LootItem(ItemID.CLUE_SCROLL_ELITE, 1, 2)
		);
}
