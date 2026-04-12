package io.ruin.model.item.actions.impl.chargable;

import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;

import java.util.HashMap;
import java.util.Map;

public class CrystalRevert {

	// Defines the crystal item IDs
	public static final int CRYSTAL_HELM = 23971;
	public static final int CRYSTAL_BODY = 23975;
	private static final int CRYSTAL_LEGS = 23979;
	private static final int CRYSTAL_SHIELD = 23991;
	private static final int CRYSTAL_SHIELD2 = 24127;
	private static final int CRYSTAL_BOW = 23983;
	private static final int CRYSTAL_BOW2 = 24123;
	private static final int CRYSTAL_HALBERD = 23987;
	private static final int CRYSTAL_HALBERD2 = 24125;
	private static final int CRYSTAL_AXE = 23673;
	private static final int CRYSTAL_PICKAXE = 23680;
	private static final int CRYSTAL_HARPOON = 23762;
	private static final int CRYSTAL_ARMOUR_SEED = 23956;
	private static final int CRYSTAL_WEAPON_SEED = 4207;
	private static final int CRYSTAL_TOOL_SEED = 23953;

	private static final Map<Integer, RevertInfo> REVERT_MAP = new HashMap<>();

	public static void register() {
		// Initializes the revert map
		REVERT_MAP.put(CRYSTAL_HELM, new RevertInfo(CRYSTAL_ARMOUR_SEED, 1));
		REVERT_MAP.put(CRYSTAL_LEGS, new RevertInfo(CRYSTAL_ARMOUR_SEED, 2));
		REVERT_MAP.put(CRYSTAL_BODY, new RevertInfo(CRYSTAL_ARMOUR_SEED, 3));
		REVERT_MAP.put(CRYSTAL_SHIELD, new RevertInfo(CRYSTAL_WEAPON_SEED, 1));
		REVERT_MAP.put(CRYSTAL_SHIELD2, new RevertInfo(CRYSTAL_WEAPON_SEED, 1));
		REVERT_MAP.put(CRYSTAL_BOW, new RevertInfo(CRYSTAL_WEAPON_SEED, 1));
		REVERT_MAP.put(CRYSTAL_BOW2, new RevertInfo(CRYSTAL_WEAPON_SEED, 1));
		REVERT_MAP.put(CRYSTAL_HALBERD, new RevertInfo(CRYSTAL_WEAPON_SEED, 1));
		REVERT_MAP.put(CRYSTAL_HALBERD2, new RevertInfo(CRYSTAL_WEAPON_SEED, 1));
		REVERT_MAP.put(CRYSTAL_AXE, new RevertInfo(CRYSTAL_TOOL_SEED, 1));
		REVERT_MAP.put(CRYSTAL_PICKAXE, new RevertInfo(CRYSTAL_TOOL_SEED, 1));
		REVERT_MAP.put(CRYSTAL_HARPOON, new RevertInfo(CRYSTAL_TOOL_SEED, 1));

		// Registers "revert" action for each crystal item
		for (int itemId : REVERT_MAP.keySet()) {
			ItemAction.registerInventory(itemId, "revert", (player, item) -> revert(player, item));
		}
	}

	private static class RevertInfo {
		int seedId;
		int seedAmount;

		RevertInfo(int seedId, int seedAmount) {
			this.seedId = seedId;
			this.seedAmount = seedAmount;
		}
	}

	private static void revert(Player player, Item item) {
		RevertInfo revertInfo = REVERT_MAP.get(item.getId());
		if (revertInfo != null) {
			String itemName = item.getDef().name;
			String seedName = ObjType.get(revertInfo.seedId).name.toLowerCase(); // Assuming seed names are in lowercase

			player.dialogue(new YesNoDialogue("Are you sure you want to revert it?",
				"Your " + itemName + " will be exchanged for " + revertInfo.seedAmount + " " + seedName + "" + (revertInfo.seedAmount > 1 ? "s" : "") + ".",
				item, () -> {
				if (revertInfo.seedAmount > 0)
					player.getInventory().remove(item);
				player.getInventory().add(revertInfo.seedId, revertInfo.seedAmount);

			}));
		} else {
			// Handles any unknown crystal items
			player.sendMessage("Cannot revert this crystal item.");
		}
	}
}
