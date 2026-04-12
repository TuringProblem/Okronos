package io.ruin.model.activities.deadman;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.map.Tile;

public class DeadmanMechanics {

	public static int[] KEYS = {13302, 13303, 13304, 13305, 13306};

	public static void register() {

		for (int key = 0; key < KEYS.length; key++) {
			ItemAction.registerInventory(key, "destroy", DeadmanMechanics::destroyKey);
		}

	}

	public static void destroyKey(Player player, Item key) {

		player.dialogue(
			new YesNoDialogue("Are you sure you want to destroy this key?",
				"Your right to access the associated bank chest will be forfeit.", key, () -> {
				key.remove();
				player.sendMessage("Poof! It's gone!");
			}));
	}

	public static void waitForAction(Player player, String action, String title) {
		if (World.type.isDeadman()) {
			if (player.getCombat().isSkulled()) {
				// player.getPacketSender().sendInterface(550, 162, 228, 1);
				player.getPacketSender().sendClientScript(1149, "sisi", "before you can " + action + "...", 11, title, 1);

			}
		}

	}

	public static void triggerGuards(Player player) {
		if (!World.type.isDeadman()) {
			return;
		}

	}

	public static Tile findValidTile(Tile tile) {
		return tile;
	}

	public static int[] VARROCK = {3200, 3200, 3600, 3600};

	/*
	 * public static void typeForZone(Tile tile) {
	 * // 6698 ghost guard port phyas
	 * // 6699 desert guard the southern city sopham
	 * // 6700 rellekka guard shortsword
	 * // 6702 relleka longsword
	 * if (VARROCK.contains(tile)) {
	 * return 6701;
	 * }
	 * // return 6701;
	 * }
	 */

	public static void closeInterfaces(Player player) {
		player.closeInterface(ToplevelComponent.CHATBOX);
	}

}
