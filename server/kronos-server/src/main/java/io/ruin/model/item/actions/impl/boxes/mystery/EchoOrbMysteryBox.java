package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

public class EchoOrbMysteryBox {

	public static final LootTable summerMboxLoot = new LootTable().addTable(1,

		new LootItem(ItemID.ASGARNIA_ECHO_ORB, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DESERT_ECHO_ORB, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.FREMENNIK_ECHO_ORB, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.WILDERNESS_ECHO_ORB, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.KANDARIN_ECHO_ORB, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.MORYTANIA_ECHO_ORB, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VARLAMORE_ECHO_ORB, 1, 3).broadcast(Broadcast.GLOBAL)
	);

	public static void register() {
		ItemAction.registerInventory(33017, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
			if (player.firstMysteryBoxReward) {
				reward = summerMboxLoot.rollItem();
				player.firstMysteryBoxReward = false;
			} else if (player.guaranteedMysteryBoxLoot >= 5) {
				reward = summerMboxLoot.rollItem();
				player.guaranteedMysteryBoxLoot = 1;
			} else {
				reward = summerMboxLoot.rollItem();
				player.guaranteedMysteryBoxLoot++;
			}
			item.remove();
			player.getInventory().add(reward);
			player.unlock();
		});
	}
}
