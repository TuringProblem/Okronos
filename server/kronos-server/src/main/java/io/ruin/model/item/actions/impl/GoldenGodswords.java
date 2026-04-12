package io.ruin.model.item.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

/**
 * @author Shinatobe
 */

public class GoldenGodswords {

	private static final int goldenArmadylGodsword = 24868;
	private static final int goldenBandosGodsword = 24869;
	private static final int goldenSaradominGodsword = 24870;
	private static final int goldenZamorakGodsword = 24871;

	private static void redeemArmadyl(Player player) {
		if (player.goldenArmadylGodsword == true) {
			player.sendMessage("You have already claimed this.");
		} else {
			player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Claim the Golden armadyl special attack?", new Item(goldenArmadylGodsword), () -> {
				player.getInventory().remove(goldenArmadylGodsword, 1);
				player.goldenArmadylGodsword = true;
				player.sendMessage("You have claimed the Golden Armadyl godsword special attack override!");
			}));
		}
	}

	private static void redeemBandos(Player player) {
		if (player.goldenBandosGodsword == true) {
			player.sendMessage("You have already claimed this.");
		} else {
			player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Claim the Golden bandos special attack?", new Item(goldenBandosGodsword), () -> {
				player.getInventory().remove(goldenBandosGodsword, 1);
				player.goldenBandosGodsword = true;
				player.sendMessage("You have claimed the Golden Bandos godsword special attack override!");
			}));
		}
	}

	private static void redeemSaradomin(Player player) {
		if (player.goldenSaradominGodsword == true) {
			player.sendMessage("You have already claimed this.");
		} else {
			player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Claim the Golden saradomin special attack?", new Item(goldenSaradominGodsword), () -> {
				player.getInventory().remove(goldenSaradominGodsword, 1);
				player.goldenSaradominGodsword = true;
				player.sendMessage("You have claimed the Golden Saradomin godsword special attack override!");
			}));
		}
	}

	private static void redeemZamorak(Player player) {
		if (player.goldenZamorakGodsword == true) {
			player.sendMessage("You have already claimed this.");
		} else {
			player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Claim the Golden saradomin special attack?", new Item(goldenZamorakGodsword), () -> {
				player.getInventory().remove(goldenZamorakGodsword, 1);
				player.goldenZamorakGodsword = true;
				player.sendMessage("You have claimed the Golden Zamorak godsword special attack override!");
			}));
		}
	}

	public static void register() {
		ItemAction.registerInventory(goldenArmadylGodsword, "Claim", (player, item) -> {
			redeemArmadyl(player);
		});
		ItemAction.registerInventory(goldenBandosGodsword, "Claim", (player, item) -> {
			redeemBandos(player);
		});
		ItemAction.registerInventory(goldenSaradominGodsword, "Claim", (player, item) -> {
			redeemSaradomin(player);
		});
		ItemAction.registerInventory(goldenZamorakGodsword, "Claim", (player, item) -> {
			redeemZamorak(player);
		});
	}

}
