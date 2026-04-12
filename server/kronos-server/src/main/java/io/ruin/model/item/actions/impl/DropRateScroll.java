package io.ruin.model.item.actions.impl;

import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.var.VarPlayerRepository;

public class DropRateScroll {

	public static void register() {
		ItemAction.registerInventory(30572, "read", (player, item) -> {
			player.dialogue(new MessageDialogue("This will give you an additional 5% drop rate permanently."));
			player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
				new Option("Yes, I want claim it!", () -> {
					item.remove();
					player.dropRate += 5;
					player.sendMessage("You have successfully claimed the 5% drop rate scroll!");
					player.sendMessage("Your drop rate is now " + player.calculateDropRate() + "%.");
				}),
				new Option("Nevermind.")));
		});
		ItemAction.registerInventory(30571, "read", (player, item) -> {
			player.dialogue(new MessageDialogue("This will give you an additional 15% drop rate permanently."));
			player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
				new Option("Yes, I want claim it!", () -> {
					item.remove();
					player.dropRate += 15;
					player.sendMessage("You have successfully claimed the 15% drop rate scroll!");
					player.sendMessage("Your drop rate is now " + player.calculateDropRate() + "%.");
				}),
				new Option("Nevermind.")));
		});


	}
}
