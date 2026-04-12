package io.ruin.model.item.actions.impl;

import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.var.VarPlayerRepository;

public class SlayerTaskSkipScroll {
	private static boolean hasClaimedAnyDailyRewards(Player player) {
		boolean hasClaimed = false;
		for (int i = 0; i < 5; i++) {
			if (player.currentDailyRewardsClaimed[i]) {
				hasClaimed = true;
				break;
			}
			if (player.dailyTaskCompletionRewardClaimed)
				hasClaimed = true;
		}
		return hasClaimed;
	}

	public static void register() {
		ItemAction.registerInventory(30458, "read", (player, item) -> {
			if (player.getCombat().isDefending(17)) {
				player.sendMessage("You can't use this right now.");
				return;
			}
			int taskAmount = VarPlayerRepository.SLAYER_TASK_AMOUNT.get(player);

			if (taskAmount > 0 || player.bossSlayerName != null) {
				player.dialogue(new MessageDialogue("This will skip your current slayer task"));
				player.dialogue(new OptionsDialogue("Are you sure you want to skip your slayer task?",
					new Option("Yes, I want to skip my slayer task!", () -> {
						item.remove(1);
						VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, 0);
						VarPlayerRepository.SLAYER_TASK.set(player, 0);
						player.sendMessage("You have successfully skipped your slayer task.");
					}),
					new Option("Yes, I want to skip my slayer boss task!", () -> {
						item.remove(1);
						player.bossSlayerName = null;
						player.bossSlayerStartAmount = 0;
						player.currentBossSlayerAmount = 0;
						player.sendMessage("You have successfully skipped your slayer boss task.");
					}),
					new Option("No, I'm not ready yet!")));
			} else {
				player.sendMessage("You don't have a current slayer task to skip.");
			}
		});
		ItemAction.registerInventory(7968, "Activate", (player, item) -> {
			if (player.currentPerkTask != null) {
				player.dialogue(new MessageDialogue("This will skip your current perk task"));
				player.dialogue(new OptionsDialogue("Are you sure you want to skip your perk task?",
					new Option("Yes, I want to skip my perk task!", () -> {
						item.remove(1);
						player.currentPerkTask = null;
						player.perkTaskCurrentAmount = 0;
						player.sendMessage("You have successfully skipped your perk task.");
					}),
					new Option("No, I'm not ready yet!")));
			} else {
				player.sendMessage("You don't have a current perk task to skip.");
			}
		});
		ItemAction.registerInventory(607, "Activate", (player, item) -> {
			player.dialogue(new MessageDialogue("This will reroll all your daily tasks, are you sure?"));
			player.dialogue(new OptionsDialogue("Are you sure you want to reroll your daily tasks?",
				new Option("Yes, reroll them!", () -> {
					if (hasClaimedAnyDailyRewards(player)) {
						player.dialogue(new MessageDialogue("You have already started working on your daily tasks for today and can't reset them now."));
						return;
					}
					item.remove(1);
					DailyTasks.assignDailyTasks(player, true);
				}),
				new Option("No, I'm not ready yet!")));
		});
	}
}
