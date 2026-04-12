package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ActionDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;

public class Gertrude {

	public static void register() {
		NPCAction.register(7284, "talk-to", (player, npc) -> talkTo(player));
	}

	public static void talkTo(Player player) {
		player.dialogue(
			new NPCDialogue(7284, "Hello. What can I do for you?"),
			new OptionsDialogue("Select an option",
				new Option("I'd like to purchase a cat.", () -> purchaseCat(player)),
				new Option("Nevermind")
			)
		);
	}

	public static void purchaseCat(Player player) {
		player.dialogue(
			new PlayerDialogue("I'd like to purchase a cat."),
			new NPCDialogue(7284, "Certainly, that will cost 200,000 gp"),
			new ActionDialogue(() -> {
				if (player.getInventory().contains(995, 200000)) {
					player.getInventory().remove(995, 200000);
					player.getInventory().add(1555, 1);
					player.sendMessage("You purchase a kitten for 200,000 gp.");
				} else {
					player.sendMessage("You do not have enough gp to purchase a kitten.");
				}
			})
		);
	}

}
