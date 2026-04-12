
package io.ruin.model.activities.wilderness;

import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

public class LarranChestLoot extends LootTable {
	public LarranChestLoot() {
		addTable(1,
			new LootItem(995, 750000, 3000000, 100),
			new LootItem(1618, 20, 30, 100),
			new LootItem(1620, 30, 50, 100),
			new LootItem(454, 300, 500, 100),
			new LootItem(445, 50, 120, 70),
			new LootItem(11237, 75, 175, 70),
			new LootItem(441, 200, 420, 70),
			new LootItem(1164, 2, 4, 40),
			new LootItem(1128, 1, 3, 40),
			new LootItem(995, 3000000, 4500000, 35),
			new LootItem(1080, 2, 4, 50),
			new LootItem(360, 150, 250, 250),
			new LootItem(378, 150, 250, 250),
			new LootItem(372, 150, 250, 250),
			new LootItem(384, 150, 250, 250),
			new LootItem(396, 150, 250, 100),
			new LootItem(390, 150, 250, 100),
			new LootItem(452, 15, 40, 100),
			new LootItem(2354, 200, 400, 100),
			new LootItem(1514, 75, 175, 100),
			new LootItem(11232, 50, 110, 100),
			new LootItem(5289, 2, 2, 100),
			new LootItem(5316, 2, 2, 100),
			new LootItem(22869, 2, 2, 100),
			new LootItem(22877, 2, 2, 100),
			new LootItem(22871, 2, 2, 100),
			new LootItem(5304, 3, 3, 100),
			new LootItem(5300, 3, 3, 100),
			new LootItem(5295, 3, 3, 100),
			new LootItem(7937, 3000, 5000, 100),
			new LootItem(24288, 1, 1, 18).broadcast(Broadcast.GLOBAL), // dagon 'hai
			new LootItem(24291, 1, 1, 18).broadcast(Broadcast.GLOBAL), // dagon 'hai
			new LootItem(24294, 1, 1, 18).broadcast(Broadcast.GLOBAL) // dagon 'hai
		);
	}
}
