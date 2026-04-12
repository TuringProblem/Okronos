package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.shop.ShopManager;

public class SarahsFarmingShop {
	public static void register() {
		NPCAction.register(501, "talk-to", (player, npc) -> player.dialogue(new NPCDialogue(npc, "Hello, " + player.getName() + ". How can I be of assistance?"),
			new OptionsDialogue(
				new Option("Open shop", () -> {
					ShopManager.openIfExists(player, "25a27451-f6f4-4d6d-972c-de717e78dg97");

				}),
				new Option("Nevermind"
				))));
		NPCAction.register(501, "trade", (player, npc) -> {
			ShopManager.openIfExists(player, "25a27451-f6f4-4d6d-972c-de717e78dg97");

		});
	}
}
