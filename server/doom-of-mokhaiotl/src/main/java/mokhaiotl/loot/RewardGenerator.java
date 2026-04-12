package mokhaiotl.loot;

import core.api.Random;
import io.ruin.cache.ItemID;
import io.ruin.model.content.drop_rate.DropRateBonusManager;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-08-20
 */
public class RewardGenerator {

	public List<Item> generateLoot(Player player) {
		var delveLevel = player.get("MOKHAIOTL_DELVE_LEVEL", 1);
		// Load up the Unique table
		calculateUniqueTable(delveLevel);
		var rewards = new ArrayList<Item>();
		// Guaranteed loot
		fillGuaranteedLoot(delveLevel, rewards);
		// If the purple has something, roll for it
		if (delveLevel >= 2) {
			if (Loot.getTable(true).getLootItems() != null &&
				!Loot.getTable(true).getLootItems().isEmpty()
			) {
				var baseRate = 0.06;
				if (Random.get() < baseRate) rewards.add(Loot.getTable(true).rollItem());
			}
		}
		// Roll for normal loot
		var loot = new ArrayList<Item>();
		Loot.getTable(false)
			.rollItems(false, DropRateBonusManager.getInstance().getTotalBonusDropRate(player))
				.forEach(item -> {
					// Based on the delve level, adjust the amount of the item
					if (delveLevel == 1) loot.add(new Item(item.getId(), Math.max(1, (int) (item.getAmount() * 0.05))));
					else if (delveLevel == 2) loot.add(new Item(item.getId(), Math.max(1, (int) (item.getAmount() * 0.35))));
					else if (delveLevel == 3) loot.add(item);
					else if (delveLevel == 4) loot.add(new Item(item.getId(), (int) (item.getAmount() * 1.05)));
					else if (delveLevel == 5) loot.add(new Item(item.getId(), (int) (item.getAmount() * 1.1)));
					else if (delveLevel == 6) loot.add(new Item(item.getId(), (int) (item.getAmount() * 1.12)));
					else if (delveLevel == 7) loot.add(new Item(item.getId(), (int) (item.getAmount() * 1.14)));
					else if (delveLevel == 8) loot.add(new Item(item.getId(), (int) (item.getAmount() * 1.17)));
					else if (delveLevel >= 9) loot.add(new Item(item.getId(), (int) (item.getAmount() * 1.2)));
				});
		rewards.addAll(loot);
		return rewards;
	}

	private void fillGuaranteedLoot(int delveLevel, List<Item> rewards) {
		if (delveLevel == 3)
			rewards.add(new Item(ItemID.DEMON_TEAR, 50));

		else if (delveLevel == 4)
			rewards.add(new Item(ItemID.DEMON_TEAR, 60));

		else if (delveLevel == 5)
			rewards.add(new Item(ItemID.DEMON_TEAR, 70));

		else if (delveLevel == 6)
			rewards.add(new Item(ItemID.DEMON_TEAR, 80));

		else if (delveLevel == 7)
			rewards.add(new Item(ItemID.DEMON_TEAR, 90));

		else if (delveLevel >= 8)
			rewards.add(new Item(ItemID.DEMON_TEAR, 100));
	}

	private void calculateUniqueTable(int delveLevel) {
		if (delveLevel == 2) Loot.getTable(true)
			.addTable(1, new LootItem(ItemID.MOKHAIOTL_CLOTH, 1, 1).broadcast(Broadcast.GLOBAL));
		else if (delveLevel == 3) Loot.getTable(true)
			.addTable(1,
				new LootItem(ItemID.MOKHAIOTL_CLOTH, 1, 1).broadcast(Broadcast.GLOBAL),
				new LootItem(ItemID.EYE_OF_AYAK, 1, 1).broadcast(Broadcast.GLOBAL)
			);
		else if (delveLevel >= 4) Loot.getTable(true)
			.addTable(1,
				new LootItem(ItemID.MOKHAIOTL_CLOTH, 1, 1).broadcast(Broadcast.GLOBAL),
				new LootItem(ItemID.EYE_OF_AYAK, 1, 1).broadcast(Broadcast.GLOBAL),
				new LootItem(ItemID.AVERNIC_TREDS, 1, 1).broadcast(Broadcast.GLOBAL)
			);
	}
}
