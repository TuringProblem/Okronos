package io.ruin.model.item.actions.impl;

import com.google.gson.annotations.Expose;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.skills.prayer.Bone;
import io.ruin.model.stat.StatType;


public class BoneCrusher {

	private transient static final int FULL_CHARGE = Integer.MAX_VALUE;

	private static final int tokenMultiplier = 25;

	public static boolean degrade(Player player, Bone bone) {
		player.getStats().addXp(StatType.Prayer, bone.exp, true);
		return true;
	}

	public static boolean has(Player player) {
		return player.getInventory().hasId(ItemID.BONECRUSHER) || player.getInventory().hasId(ItemID.BONECRUSHER_NECKLACE) || player.getEquipment().hasId(ItemID.BONECRUSHER_NECKLACE);
	}

	public static void register() {
		ItemItemAction.register(ItemID.BONECRUSHER, ItemID.ECTOTOKEN, BoneCrusher::charge);
		ItemAction.registerInventory(ItemID.BONECRUSHER, "check", BoneCrusher::check);
		ItemAction.registerInventory(ItemID.BONECRUSHER, "activity", BoneCrusher::activity);
		ItemAction.registerInventory(ItemID.BONECRUSHER, "uncharge", BoneCrusher::uncharge);
	}

	private static void charge(Player player, Item primary, Item secondary) {
		Item token = primary.getId() == ItemID.ECTOTOKEN ? primary : secondary;

		if (player.bonecrusher.charges == FULL_CHARGE) {
			player.sendMessage("Your Bonecrusher is full.");
			return;
		}

		int added;

		long chargesAvailable = (long) token.getAmount() * (long) tokenMultiplier;

		if ((long) (player.bonecrusher.charges + (chargesAvailable)) > FULL_CHARGE) {
			added = FULL_CHARGE - player.bonecrusher.charges; //0
		} else {
			added = token.getAmount() * tokenMultiplier;
		}

		player.bonecrusher.charges += added;
		player.getInventory().remove(token.getId(), added / tokenMultiplier);
		check(player, null);
	}

	private static void check(Player player, Item item) {
		int charges = player.bonecrusher.charges;

		player.sendMessage("Your Bonecrusher has " + (charges > 0 ? charges : "no") + " charges.");
	}

	private static void activity(Player player, Item item) {
		player.bonecrusher.active = !player.bonecrusher.active;
		player.sendMessage("The Bonecrusher effect is now " + (player.bonecrusher.active ? "active" : "inactive") + ".");
	}

	private static void uncharge(Player player, Item item) {
		if (player.bonecrusher.charges < 25) {
			player.sendMessage("You need at least 25 charges to uncharge Bonecrusher.");
			return;
		}

		int amount = player.bonecrusher.charges / tokenMultiplier;
		player.getInventory().addOrDrop(4278, amount);
		player.bonecrusher.charges -= amount * tokenMultiplier;
	}

	private boolean active;
	private int charges;
}
