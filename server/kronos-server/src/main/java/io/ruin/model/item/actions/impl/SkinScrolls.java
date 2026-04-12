package io.ruin.model.item.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

/**
 * @author Shinatobe
 */

public class SkinScrolls {

	private static final int GREEN_SKIN_SCROLL = 6806;
	private static final int BLUE_SKIN_SCROLL = 6807;
	private static final int PURPLE_SKIN_SCROLL = 6808;

	private static void redeemGreen(Player player) {
		if (player.unlockedGreenSkin) {
			player.sendMessage("You have already claimed this.");
		} else {
			player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Claim the Green skin scroll override?", new Item(GREEN_SKIN_SCROLL), () -> {
				player.getInventory().remove(GREEN_SKIN_SCROLL, 1);
				player.unlockedGreenSkin = true;
				player.sendMessage("You have claimed the Green skin override!");
			}));
		}
	}

	private static void redeemBlue(Player player) {
		if (player.unlockedBlueSkin) {
			player.sendMessage("You have already claimed this.");
		} else {
			player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Claim the Blue skin scroll override?", new Item(BLUE_SKIN_SCROLL), () -> {
				player.getInventory().remove(BLUE_SKIN_SCROLL, 1);
				player.unlockedBlueSkin = true;
				player.sendMessage("You have claimed the Blue skin override!");
			}));
		}
	}

	private static void redeemPurple(Player player) {
		if (player.unlockedPurpleSkin) {
			player.sendMessage("You have already claimed this.");
		} else {
			player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Claim the Purple skin scroll override?", new Item(PURPLE_SKIN_SCROLL), () -> {
				player.getInventory().remove(PURPLE_SKIN_SCROLL, 1);
				player.unlockedPurpleSkin = true;
				player.sendMessage("You have claimed the Purple skin override!");
			}));
		}
	}

	public static void register() {
		ItemAction.registerInventory(GREEN_SKIN_SCROLL, "Redeem", (player, item) -> {
			redeemGreen(player);
		});
		ItemAction.registerInventory(BLUE_SKIN_SCROLL, "Redeem", (player, item) -> {
			redeemBlue(player);
		});
		ItemAction.registerInventory(PURPLE_SKIN_SCROLL, "Redeem", (player, item) -> {
			redeemPurple(player);
		});
	}

}
