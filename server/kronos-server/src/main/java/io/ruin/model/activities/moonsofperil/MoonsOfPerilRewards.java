package io.ruin.model.activities.moonsofperil;

import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

public class MoonsOfPerilRewards {
	/*
	Placeholder
	 */
	public static LootTable table = new LootTable().addTable(30,
			new LootItem(995, 5000000, 10),
			new LootItem(995, 1000000, 20))
		.addTable(10,
			new LootItem(995, 10000000, 10),
			new LootItem(995, 5000000, 20))
		.addTable(5,
			new LootItem(995, 20000000, 10),
			new LootItem(995, 10000000, 20))
		.addTable(1,
			new LootItem(995, 50000000, 10),
			new LootItem(995, 20000000, 20));

}
