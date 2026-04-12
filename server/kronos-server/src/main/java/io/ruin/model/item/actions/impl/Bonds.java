package io.ruin.model.item.actions.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

/*
 * @project Kronos
 * @author Patrity - https://github.com/Patrity
 * Created on - 7/20/2020
 */
public class Bonds {

	public static int[] IDS = new int[] {
			30276, 30279, 30282, 30285, 30288, 30291, 30294
	};

	public static int[] COUNTS = new int[] {
			5, 10, 25, 50, 100, 250, 500
	};

	private static void redeem(Player player, int amount, Item item) {
		player.dialogue(new ItemDialogue().one(item.getId(), "You are about to redeem this bond<br>" +
				"adding $" + amount + " to your amount donated.<br>" +
				"This will consume the bond forever."),
				new OptionsDialogue(
						new Option("Yes!", (player1 -> {
							player.getInventory().remove(item);
							player.getInventory().add(30035, amount);
							player.storeAmountSpent += amount;
							player.sendMessage(
									"You have redeemed a $" + amount + " Bond. Your new total is: $" + player.storeAmountSpent);
						})),
						new Option("I'll keep it for now.", player::closeDialogue)));
	}

	public static void register() {
		for (var i = 0; i < COUNTS.length; i++) {
			var id = IDS[i];
			var count = COUNTS[i];
			ItemAction.registerInventory(id, 1, ((player, item) -> redeem(player, count, item)));
		}
	}

	public static SecondaryGroup getGroup(Player player) {
		if (player.storeAmountSpent >= 5000)
			return SecondaryGroup.SUPREME_DONATOR;
		if (player.storeAmountSpent >= 2500)
			return SecondaryGroup.LEGENDARY_DONATOR;
		if (player.storeAmountSpent >= 1000)
			return SecondaryGroup.PLATINUM_DONATOR;
		if (player.storeAmountSpent >= 500)
			return SecondaryGroup.GOLD_DONATOR;
		if (player.storeAmountSpent >= 250)
			return SecondaryGroup.NOBLE_DONATOR;
		if (player.storeAmountSpent >= 100)
			return SecondaryGroup.ELITE_DONATOR;
		if (player.storeAmountSpent >= 50)
			return SecondaryGroup.SUPER_DONATOR;
		if (player.storeAmountSpent >= 1)
			return SecondaryGroup.DONATOR;
		return null;
	}
}
