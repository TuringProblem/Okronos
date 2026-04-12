
package io.ruin.model.activities.brimstonechest;

import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

public class BrimstoneLoot extends LootTable {

	public BrimstoneLoot() {
		addTable(
			29,
			new LootItem(995, 50000, 150000, 1),
			new LootItem(1618, 20, 40, 1),
			new LootItem(1620, 20, 40, 1),
			new LootItem(454, 200, 350, 1),
			new LootItem(445, 50, 100, 1),
			new LootItem(11237, 70, 150, 1),
			new LootItem(441, 200, 350, 1),
			new LootItem(1164, 2, 4, 1),
			new LootItem(1128, 1, 2, 1),
			new LootItem(1080, 1, 2, 1),
			new LootItem(360, 150, 300, 1),
			new LootItem(378, 150, 300, 1),
			new LootItem(372, 150, 300, 1),
			new LootItem(384, 150, 300, 1),
			new LootItem(396, 150, 300, 1),
			new LootItem(390, 150, 300, 1),
			new LootItem(452, 10, 15, 1),
			new LootItem(2354, 150, 200, 1),
			new LootItem(1514, 100, 200, 1),
			new LootItem(11232, 40, 88, 1),
			new LootItem(5289, 1, 4, 1),
			new LootItem(5316, 1, 4, 1),
			new LootItem(22869, 1, 4, 1),
			new LootItem(22877, 1, 4, 1),
			new LootItem(22871, 1, 4, 1),
			new LootItem(5304, 1, 4, 1),
			new LootItem(5300, 1, 4, 1),
			new LootItem(5295, 1, 4, 1),
			new LootItem(7937, 3000, 6000, 1)
		);

		addTable(
			1,
			new LootItem(22731, 1, 1, 1),
			new LootItem(23047, 1, 1, 1),
			new LootItem(23050, 1, 1, 1),
			new LootItem(23053, 1, 1, 1),
			new LootItem(23056, 1, 1, 1),
			new LootItem(23059, 1, 1, 1)
		);
	}
}

