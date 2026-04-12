package io.ruin.model.item.actions.impl;

import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.var.VarPlayerRepository;

public class DonatorClaimScroll {
	public static void register() {
		ItemAction.registerInventory(30576, "read", (player, item) -> {
			player.dialogue(new MessageDialogue("This will redeem $25 donated on your account"));
			player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
				new Option("Yes, I want claim it!", () -> {
					item.remove();
					player.updateTotalDonated(25);
					DonatorBond.checkDonatorStatus(player);
					player.sendMessage("You now have a total of " + player.getTotalDonated() + "$ donated!");
				}),
				new Option("Nevermind.")));
		});
		ItemAction.registerInventory(32001, "claim", (player, item) -> {
			if (player.hasFreePerkUnlock) {
				player.sendMessage("You are unable to claim another max perk unlock until you redeem your current unlock.");
				return;
			}
			player.startEvent(e -> {
				player.dialogue(new MessageDialogue("This will allow you to purchase a free level 5 perk from the perk master."));
				e.waitForDialogue(player);
				player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
					new Option("Yes, I want claim it!", () -> {
						item.remove(1);
						player.hasFreePerkUnlock = true;
						player.sendMessage("You now have a free max perk unlock available at the perk master!");
					}),
					new Option("Nevermind.")));
			});
		});
		ItemAction.registerInventory(30575, "read", (player, item) -> {
			player.dialogue(new MessageDialogue("This will redeem $10 donated on your account"));
			player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
				new Option("Yes, I want claim it!", () -> {
					item.remove();
					player.updateTotalDonated(10);
					DonatorBond.checkDonatorStatus(player);
					player.sendMessage("You now have a total of " + player.getTotalDonated() + "$ donated!");
				}),
				new Option("Nevermind.")));
		});
		ItemAction.registerInventory(30573, "read", (player, item) -> {
			player.dialogue(new MessageDialogue("This will redeem $50 donated on your account"));
			player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
				new Option("Yes, I want claim it!", () -> {
					item.remove();
					player.updateTotalDonated(50);
					DonatorBond.checkDonatorStatus(player);
					player.sendMessage("You now have a total of " + player.getTotalDonated() + "$ donated!");
				}),
				new Option("Nevermind.")));
		});
		ItemAction.registerInventory(30574, "read", (player, item) -> {
			player.dialogue(new MessageDialogue("This will redeem $100 donated on your account"));
			player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
				new Option("Yes, I want claim it!", () -> {
					item.remove();
					player.updateTotalDonated(100);
					DonatorBond.checkDonatorStatus(player);
					player.sendMessage("You now have a total of " + player.getTotalDonated() + "$ donated!");
				}),
				new Option("Nevermind.")));
		});

	}
}
