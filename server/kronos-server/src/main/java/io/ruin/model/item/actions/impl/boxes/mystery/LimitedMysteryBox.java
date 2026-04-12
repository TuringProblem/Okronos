package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.cache.Icon;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

import static io.ruin.cache.ItemID.COINS_995;

public class LimitedMysteryBox extends ItemContainer {

	private static final int LIMITED_MYSTERY_BOX = 30426;
	private static final int EASTER_EGG = 21227;

	private static final LootTable ECO_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(COINS_995, 50000000, 50000000, 20),

		new LootItem(30448, 1, 3, 23).broadcast(Broadcast.WORLD),
		new LootItem(290, 1, 5, 23).broadcast(Broadcast.WORLD),
		new LootItem(10600, 10, 75, 23).broadcast(Broadcast.WORLD),
		new LootItem(30288, 1, 23).broadcast(Broadcast.WORLD),
		new LootItem(30035, 85, 23).broadcast(Broadcast.WORLD),

		new LootItem(30185, 1, 2, 15).broadcast(Broadcast.WORLD),

		new LootItem(25859, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(11862, 1, 10).broadcast(Broadcast.WORLD),

		new LootItem(12357, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(30016, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(6831, 1, 10).broadcast(Broadcast.WORLD),
		new LootItem(21079, 1, 10).broadcast(Broadcast.WORLD),

		new LootItem(30291, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(12817, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(30294, 1, 1).broadcast(Broadcast.WORLD),
		new LootItem(20997, 1, 1).broadcast(Broadcast.WORLD)

	);

	public static void register() {
		ItemAction.registerInventory(LIMITED_MYSTERY_BOX, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
			if (player.firstMysteryBoxReward) {
				reward = ECO_MYSTERY_BOX_TABLE.rollItem();
				player.firstMysteryBoxReward = false;
			} else if (player.guaranteedMysteryBoxLoot >= 5) {
				reward = ECO_MYSTERY_BOX_TABLE.rollItem();
				player.guaranteedMysteryBoxLoot = 1;
			} else {
				reward = ECO_MYSTERY_BOX_TABLE.rollItem();
				player.guaranteedMysteryBoxLoot++;
			}
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.GLOBAL.sendNews(Icon.MYSTERY_BOX, "Limited Mystery Box",
					"" + player.getName() + " just received " + reward.getDef().descriptiveName + "!");
			player.unlock();
		});
	}
}
