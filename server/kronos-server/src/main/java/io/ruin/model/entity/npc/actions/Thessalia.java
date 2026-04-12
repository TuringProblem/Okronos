package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.shop.ShopManager;

public class Thessalia {
	public static void register() {
		NPCAction.register(534, "talk-to", (player, npc) -> player.dialogue(new NPCDialogue(npc, "Hello, " + player.getName() + ". How can I be of assistance?"),
			new OptionsDialogue(
				new Option("Open shop", () -> {
					ShopManager.openIfExists(player, "5464df07-5091-4ddc-8c5a-426db5ae3193");
				}),
				new Option("Nevermind"
				))));
		NPCAction.register(534, "trade", (player, npc) -> {
			ShopManager.openIfExists(player, "5464df07-5091-4ddc-8c5a-426db5ae3193");
		});
	}
}
