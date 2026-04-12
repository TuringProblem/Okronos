package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.shop.ShopManager;

public class Runa {
	private static final int RUNA = 1078;

	public static void register() {
		NPCAction.register(RUNA, "talk-to", (player, npc) -> player.dialogue(
			new NPCDialogue(npc, "Hello there, " + player.getName() + ". Would you like to see the Tournament Shop?"),
			new OptionsDialogue(
				new Option("Yes Please!", () -> {
					ShopManager.openIfExists(player, "347e6721-8007-444b-8249-736036d84ab4");
				}),
				new Option("Hard pass", () -> {
					player.dialogue(new PlayerDialogue("Hard pass"),
						new NPCDialogue(npc, "Okay, if you change your mind you know where to find me."));
				})
			)));
	}
}
