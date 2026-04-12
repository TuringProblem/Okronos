package io.ruin.model.item.actions.impl;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.actions.ItemAction;

public class PerkExpLamp {
	public static void register() {
		int perkExp = 100_000; //Change this value to whatever
		ItemAction.registerInventory(33020, "rub", (player, item) -> {
			player.dialogue(
				new YesNoDialogue(
					"Are you sure you want to rub this lamp?",
					"You will gain " + NumberUtils.formatNumber(perkExp) + " experience.",
					item,
					() -> {
						player.sendMessage("You rub the lamp and gain " + NumberUtils.formatNumber(perkExp) + " experience.");
						player.getPlayerPerkHandler().addPerkExperience(player, perkExp);
						item.remove();
					}
				)
			);
		});
	}
}
