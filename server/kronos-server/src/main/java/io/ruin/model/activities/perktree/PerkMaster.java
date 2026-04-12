package io.ruin.model.activities.perktree;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;

public class PerkMaster {

	public static void register() {
		NPCAction.register(7958, 3, (player, npc) -> {
			player.dialogue(new OptionsDialogue("What task type do you wish to complete?",
				new Option("Skilling", () -> {
					player.dialogue(new OptionsDialogue("What task difficulty would you like?",
						new Option("Easy", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.EASY, PerkTasks.TaskType.SKILLING)),
						new Option("Medium", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.MEDIUM, PerkTasks.TaskType.SKILLING)),
						new Option("Hard", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.HARD, PerkTasks.TaskType.SKILLING)),
						new Option("Elite", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.ELITE, PerkTasks.TaskType.SKILLING))));
				}),
				new Option("PVM", () -> {
					player.dialogue(new OptionsDialogue("What task difficulty would you like?",
						new Option("Easy", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.EASY, PerkTasks.TaskType.PVM)),
						new Option("Medium", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.MEDIUM, PerkTasks.TaskType.PVM)),
						new Option("Hard", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.HARD, PerkTasks.TaskType.PVM)),
						new Option("Elite", () -> player.getPlayerPerkHandler().assignTask(player, PerkTasks.TaskTiers.ELITE, PerkTasks.TaskType.PVM))));
				})));

		});
		ItemAction.registerInventory(30035, "claim", (player, item) -> player.getPerkInterface().openPlayerPerkHomeSection(player));
		NPCAction.register(7958, 2, (player, npc) -> player.getPerkInterface().openPlayerPerkHomeSection(player));
		NPCAction.register(7958, 1, (player, npc) -> {
			player.dialogue(new OptionsDialogue("What do you need?",
				new Option("Can I have a perk crystal?", () -> player.getInventory().addOrDrop(new Item(30496))),
				new Option("Can you skip my perk task?", () -> {
					player.dialogue(new NPCDialogue(npc, "I can skip your perk task. Are you sure?"),
						new OptionsDialogue("Do you wish to skip your task using one perk task skip scroll?",
							new Option("Sure, here you go.", () -> {
								if (player.getInventory().contains(7968)) {
									player.getInventory().remove(7968, 1);
									player.getPlayerPerkHandler().skipTask(player);
									player.dialogue(new NPCDialogue(npc, "Your task has been skipped!"));
								} else {
									player.dialogue(new NPCDialogue(npc, "You need at least one perk point task skip scroll to skip this task."));
								}
							}),
							new Option("Nevermind.", player::closeDialogue)
						)
					);
				}),
				new Option("What perk set do I have active?", () -> {
					String dialogue = player.activePerkSetsList.size() > 0
						? "You currently have the perk set '" + player.activePerkSetsList.get(0).perkSet.getPerkSetName() + "' active."
						: "You don't have a perk set active. Check out the perk combinations in the repository.";
					player.dialogue(new NPCDialogue(npc, dialogue));
				}),
				new Option("What is my current perk task?", () -> {
					String dialogue = player.currentPerkTask != null
						? "Your current perk task is to " + player.currentPerkTask.description.toLowerCase()
						+ " " + player.perkTaskCurrentAmount + player.currentPerkTask.description_2 + "."
						: "You don't have a perk task active. Talk to me to get a new one.";
					player.dialogue(new NPCDialogue(npc, dialogue));
				})
			));
		});
	}
}
