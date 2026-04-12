package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.ActionDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.shop.ShopManager;

public class Otto {
	public static void register() {
		NPCAction.register(2914, "talk-to", (player, npc) -> {
			player.dialogue(
				new NPCDialogue(
					npc,
					"Would you like me to make a Zamorakian hasta from your spear?"),
				new OptionsDialogue(
					new Option(
						"Yes, please!",
						() -> {
							player.dialogue(
								new PlayerDialogue("Yes, please!"),
								new OptionsDialogue("This will cost 5M coins, are you sure?",
									new Option("Yes, please!", () -> {
										if (player.getInventory().contains(ItemID.ZAMORAKIAN_SPEAR) && player.getInventory().contains(995, 5000000)) {
											player.getInventory().remove(ItemID.ZAMORAKIAN_SPEAR, 1);
											player.getInventory().add(ItemID.ZAMORAKIAN_HASTA, 1);
											player.getInventory().remove(995, 5000000);
											player.dialogue(
												new PlayerDialogue("Yes, please!"),
												new NPCDialogue(npc, "Here you go, enjoy!"));
										} else {
											player.dialogue(
												new NPCDialogue(
													npc,
													"Come back when you have everything you need to make a hasta."));
										}
									}),
									new Option(
										"No, thanks.",
										player::closeDialogue))
							);
						}),
					new Option(
						"Can you convert my hasta back into a spear?",
						() -> {
							if (player.getInventory().contains(ItemID.ZAMORAKIAN_HASTA)) {
								player.getInventory().remove(ItemID.ZAMORAKIAN_HASTA, 1);
								player.getInventory().add(ItemID.ZAMORAKIAN_SPEAR, 1);
								player.dialogue(
									new PlayerDialogue("Can you convert my hasta back into a spear?"),
									new NPCDialogue(npc, "There you go. Free of charge."));
							} else {
								player.dialogue(
									new NPCDialogue(
										npc,
										"It looks like you don't have a hasta in your inventory to convert."));
							}
						}),
					new Option(
						"Can you fix my dragon hasta?",
						() -> {
							if (player.getInventory().contains(ItemID.BROKEN_DRAGON_HASTA)) {
								// Repair any broken dragon hastas
								int brokenHastas = player.getInventory().getAmount(ItemID.BROKEN_DRAGON_HASTA);
								player.getInventory().remove(ItemID.BROKEN_DRAGON_HASTA, brokenHastas);
								player.getInventory().add(ItemID.DRAGON_HASTA, brokenHastas);
								player.dialogue(
									new NPCDialogue(npc, "I haven't seen one of these in a while, consider it free of charge."));
							} else {
								player.dialogue(
									new NPCDialogue(
										npc,
										"Come back when you have a hasta that needs repaired."));
							}
						})
				));
		});
	}
}
