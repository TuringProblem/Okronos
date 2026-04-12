package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.shop.ShopManager;

public class CandleShop {
	public static void register() {
		NPCAction.register(3199, "talk-to", (player, npc) -> player.dialogue(new NPCDialogue(npc, "Hello, " + player.getName() + ". How can I be of assistance?"),
			new OptionsDialogue(
				new Option("Open shop", () -> {
					ShopManager.openIfExists(player, "a1e55885-3b24-4d63-8a5e-5808c01f54e0");
				}),
				new Option("Nevermind"
				))));
		NPCAction.register(3199, "trade", (player, npc) -> {
			ShopManager.openIfExists(player, "a1e55885-3b24-4d63-8a5e-5808c01f54e0");
		});
	}
}
