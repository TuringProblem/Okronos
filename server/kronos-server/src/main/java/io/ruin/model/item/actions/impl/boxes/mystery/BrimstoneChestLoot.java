package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;

import static io.ruin.model.item.actions.impl.boxes.mystery.VoteMysteryBox.VIEW_REWARDS_WIDGET;
import static io.ruin.model.item.actions.impl.boxes.mystery.VoteMysteryBox.updateRewards;

public class BrimstoneChestLoot {

	public static final LootTable BRIMSTONE_TABLE = new LootTable().addTable(1,

		new LootItem(995, 50_000, 150_000, 1),
		new LootItem(1618, 20, 40, 1),
		new LootItem(1620, 20, 40, 1),
		new LootItem(454, 200, 350, 1),
		new LootItem(445, 50, 100, 1),
		new LootItem(11237, 70, 150, 1),
		new LootItem(441, 200, 350, 1),
		new LootItem(1164, 7, 12, 1),
		new LootItem(1128, 8, 12, 1),
		new LootItem(1080, 7, 12, 1),
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
		new LootItem(5289, 10, 22, 1),
		new LootItem(5316, 1, 4, 1),
		new LootItem(22869, 10, 22, 1),
		new LootItem(22877, 10, 22, 1),
		new LootItem(22871, 10, 22, 1),
		new LootItem(5304, 10, 22, 1),
		new LootItem(5300, 8, 14, 1),
		new LootItem(5295, 10, 18, 1),
		new LootItem(7937, 3000, 6000, 1),

		new LootItem(22731, 1, 1, 1),
		new LootItem(23047, 1, 1, 1),
		new LootItem(23050, 1, 1, 1),
		new LootItem(23053, 1, 1, 1),
		new LootItem(23056, 1, 1, 1),
		new LootItem(23059, 1, 1, 1)


	);

	private static final LootTable INCENTIVE_TABLE = new LootTable().addTable(1
//            new LootItem(6585, 1, 100), //Amulet of fury
//            new LootItem(11284, 1, 100), //Dragonfire shield
//            new LootItem(6737, 1, 50), //Berserker ring
//            new LootItem(6731, 1, 50), //Seers ring
//            new LootItem(2581, 1, 1), //Robin hood hat
//            new LootItem(12596, 1, 1), //Rangers' tunic
//            new LootItem(1038, 1, 1).broadcast(Broadcast.GLOBAL), //Red party hat
//            new LootItem(1040, 1, 1).broadcast(Broadcast.GLOBAL), //Yellow party hat
//            new LootItem(1042, 1, 1).broadcast(Broadcast.GLOBAL), //Blue party hat
//            new LootItem(1044, 1, 1).broadcast(Broadcast.GLOBAL), //Green party hat
//            new LootItem(1046, 1, 1).broadcast(Broadcast.GLOBAL), //Purple party hat
//            new LootItem(1048, 1, 1).broadcast(Broadcast.GLOBAL), //White  party hat
//            new LootItem(12437, 1, 1).broadcast(Broadcast.GLOBAL), // 3rd age cloak
//            new LootItem(1053, 1, 1).broadcast(Broadcast.GLOBAL), //Green halloween mask
//            new LootItem(1055, 1, 1).broadcast(Broadcast.GLOBAL), //Blue halloween mask
//            new LootItem(1057, 1, 1).broadcast(Broadcast.GLOBAL), //Red halloween mask
//            new LootItem(12696, 4000, 5000, 1), //5000 Super combat potions
//            new LootItem(13442, 4000, 5000, 1) //5000 Anglerfish
	);


	public static void openRewards(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, VIEW_REWARDS_WIDGET);
		player.closeInterface(ToplevelComponent.INVENTORY_TAB_AREA);
		updateRewards(player, BRIMSTONE_TABLE);
	}
}






