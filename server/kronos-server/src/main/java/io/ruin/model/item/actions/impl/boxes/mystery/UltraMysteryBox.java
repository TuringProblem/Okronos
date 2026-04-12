package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.cache.Icon;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

import static io.ruin.cache.ItemID.COINS_995;

public class UltraMysteryBox extends ItemContainer {

	private static final int EASTER_EGG = 21227;

	private static final LootTable ECO_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(COINS_995, 25000000, 50000000, 20),

		new LootItem(12821, 1, 12).broadcast(Broadcast.WORLD),
		new LootItem(290, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(11847, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(30035, 85, 23).broadcast(Broadcast.WORLD),
		new LootItem(30185, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(21034, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(19532, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(19535, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(19541, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(19532, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(22983, 1, 20).broadcast(Broadcast.WORLD),
		new LootItem(1050, 1, 23).broadcast(Broadcast.WORLD),

		new LootItem(30185, 1, 15).broadcast(Broadcast.WORLD),

		new LootItem(4084, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(13344, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(30185, 3, 10).broadcast(Broadcast.WORLD),
		new LootItem(12357, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(22542, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(6831, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(21079, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(12825, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(13239, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(1037, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(13239, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(22978, 1, 20).broadcast(Broadcast.WORLD),

		new LootItem(30291, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(12817, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(13652, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(25941, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(25941, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(1053, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(22610, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(12399, 1, 1).broadcast(Broadcast.WORLD)

	);
}
