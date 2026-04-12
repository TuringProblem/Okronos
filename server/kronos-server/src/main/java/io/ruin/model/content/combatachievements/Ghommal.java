package io.ruin.model.content.combatachievements;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;

public class Ghommal {
	private static void talk(Player player, NPC npc) {
		player.dialogue(
			new NPCDialogue(npc.getId(), "What do you want?"),
			new OptionsDialogue(new Option("I would like to claim the next combat achievement tier rewards.", () -> {
				player.dialogue(new PlayerDialogue("I'm here to claim combat achievement rewards.").action(() -> CombatAchievementSystem.claimNextReward(player)));
			}), new Option("I would like to open the combat achievements interface", () -> {
				player.dialogue(new PlayerDialogue("Open combat achievement interface.").action(() -> player.getCombatAchievementInterface().open(player)));
			}), new Option("What are combat achievements?", () -> {
				player.dialogue(new PlayerDialogue("What are these combat achievements?"));
				player.startEvent((e) -> {
					player.dialogue(new NPCDialogue(npc.getId(),
						"Combat Achievements are a set of tasks for all adventurers to try and accomplish." +
							" They range from simple missions to kill various creatures, to challenges that will push you to your limits."));
					e.waitForDialogue(player);
					player.dialogue(new NPCDialogue(npc.getId(),
						"The purpose of these tasks is for you to grow and improve, experience all that Reason has to offer, and become stronger in the process."));
					e.waitForDialogue(player);
					player.dialogue(new NPCDialogue(npc.getId(),
						"Combat Achievements are broken down into six tiers: Easy, Medium, Hard, Elite, Master and Grandmaster."));
					e.waitForDialogue(player);
					player.dialogue(new NPCDialogue(npc.getId(),
						"Each of these tiers is progressively harder than the last, and to earn any rewards from the latter tiers, the prior tiers must be completed."));
					e.waitForDialogue(player);
					player.dialogue(new NPCDialogue(npc.getId(),
						"To complete a tier you will need to conquer every task within it."));
				});
			}), new Option("Er... Nothing...", () -> player.dialogue(new PlayerDialogue("Er... Nothing..."))))
		);
	}

	public static void register() {
		NPCAction.register(13613, 1, Ghommal::talk);
	}
}
