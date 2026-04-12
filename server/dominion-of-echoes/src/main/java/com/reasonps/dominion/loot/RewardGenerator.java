package com.reasonps.dominion.loot;

import io.ruin.cache.ItemID;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.RaidingRestorations;
import io.ruin.model.content.drop_rate.DropRateBonusManager;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.Random;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static core.api.ReasonUtils.pos;
import static io.ruin.cache.ItemID.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-06-05
 */
@RequiredArgsConstructor
public class RewardGenerator {

	private final Player player;

	public List<Item> generateLoot(int rolls, DynamicMap map) {
		var baseRate = 0.06;

		if (player.getEquipment().get(Equipment.SLOT_RING) != null && AttributeExtensions
			.hasAttribute(player.getEquipment().get(Equipment.SLOT_RING), AttributeTypes.RAID_UNIQUE_CHARM)) {
			int level = AttributeExtensions.getCharges(AttributeTypes.RAID_UNIQUE_CHARM,
				player.getEquipment().get(Equipment.SLOT_RING));
			var multiplier = 1.0 + (level * 0.05);
			baseRate *= multiplier;
		}
		var perks = player.getPlayerPerkHandler().getActivePerks(player);
		if (perks.contains(Perks.RAIDING_RESTORATIONS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.RAIDING_RESTORATIONS);
			var perk = (RaidingRestorations) perks.get(perkIndex).getPerk(player);
			if (perk != null) {
				var multiplier = 1.0 + perk.getLootChance();
				baseRate *= multiplier;
			}
		}

		var rewards = new ArrayList<Item>();

		rewards.add(new Item(ItemID.DRYGORE_DART, Random.get(200, 250)));

		for (int i = 0; i < rolls; i++)
			rewards.add(getRewardItem(rewards));
		if (map != null) {
			new GameObject(51342, pos(map, 8, 37), 10, 0).spawn();
			new GameObject(51342, pos(map, 12, 37), 10, 0).spawn();
		}
		// the purple is roll outside the loop, so it's only rolled once.
		if (Random.get() < baseRate) { // 6% chance to roll a purple unique
			var purpleUnique = rollForPurpleUnique().getFirst();
			rewards.add(purpleUnique);
			if (map != null) {
				new GameObject(51343, pos(map, 8, 37), 10, 0).spawn();
				new GameObject(51343, pos(map, 12, 37), 10, 0).spawn();
			}
		}
		return rewards;
	}

	/**
	 * Recursively retrieves a reward item that is not already present in the given list of rewards.
	 *
	 * @param rewards a list of {@code Item} objects representing the existing rewards
	 * @return a new {@code Item} object that is not already in the provided list of rewards
	 */
	private Item getRewardItem(List<Item> rewards) {
		var first = rollForLoot().getFirst();
		for (Item reward : rewards) {
			if (reward.getId() == first.getId())
				if (reward.getId() == ItemID.DRYGORE_DART && reward.getAmount() > 300)
					return getRewardItem(rewards);
				else
					return getRewardItem(rewards);
		}
		return first;
	}

	private List<Item> rollForLoot() {
		return Loot.LOOT_TABLE.rollItems(false, DropRateBonusManager.getInstance().getTotalBonusDropRate(player));
	}

	private List<Item> rollForPurpleUnique() {
		return Loot.PURPLE_UNIQUES.rollItems(false, DropRateBonusManager.getInstance().getTotalBonusDropRate(player));
	}
}
