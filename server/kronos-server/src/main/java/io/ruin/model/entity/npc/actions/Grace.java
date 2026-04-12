package io.ruin.model.entity.npc.actions;

import io.ruin.model.activities.miscpvm.slayer.Rockslug;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.shop.ShopManager;

import java.util.Arrays;

public class Grace {
	public static void register() {
		NPCAction.register(5919, "talk-to", (player, npc) -> player.dialogue(new NPCDialogue(npc, "Hello, " + player.getName() + ". Would you like to see the mark exchange, recolor your graceful outfit, or learn more about marks of grace?"),
			new OptionsDialogue(
				new Option("Open shop", () -> ShopManager.openIfExists(player, "130e716b-eb95-479b-ab1e-2a3dbdc52c6f")),
				new Option("Recolor graceful piece", () -> {
					player.dialogue(new NPCDialogue(npc, "Please use a graceful piece on me to recolor it."));

				}),
				new Option("Learn more", () -> {
					if (!player.insideWildernessAgilityCourse) {
						player.dialogue(new NPCDialogue(npc, "While practicing on rooftop agility courses, you will occasionally encounter marks of grace.<br>" +
							"Bring them to me and we can trade.<br>My outfit will allow you to run longer distances and recover more quickly."));
					} else {
						player.dialogue(new NPCDialogue(npc, "While completing this wilderness course, you'll be rewarded anywhere from 1 to 3 marks of grace along with a little blood money."),
							new NPCDialogue(npc, "You can spend those marks inside my shop for Graceful clothing. If you kill somebody in here, I'll reward you with 50k agility experience and"),
							new NPCDialogue(npc, "10 marks of grace. Be careful though, as this place gets quite dangerous!"),
							new PlayerDialogue("Thank you, Grace! I'll keep an eye out."));
					}
				}))

		));
		NPCAction.register(5919, 3, (player, npc) -> ShopManager.openIfExists(player, "130e716b-eb95-479b-ab1e-2a3dbdc52c6f"));
		NPCAction.register(5919, 4, (player, npc) ->
			player.dialogue(
				new NPCDialogue(npc,
					"Hello, " + player.getName() + ". Your currently have a total of " + player.totalLaps + " laps completed.")));
		NPCAction.register(5919, 5, (player, npc) ->
			player.dialogue(new NPCDialogue(npc, "Please use a graceful piece on me to recolor it.")));
		for (int i : Arrays.asList(
			11850, 11852, 11854, 11856, 11858, 11860, //Normal Graceful
			13579, 13581, 13583, 13585, 13587, 13589,//Purple Graceful
			13591, 13593, 13595, 13597, 13599, 13601, //Light blue Graceful
			13603, 13605, 13607, 13609, 13611, 13613, //Orange Graceful
			13615, 13617, 13619, 13621, 13623, 13625, //Red Graceful
			13627, 13629, 13631, 13633, 13635, 13637 //Green Graceful
		)) {
			Item gracefulItem = new Item(i);
			String itemName = gracefulItem.getDef().name;
			int itemId = 13579;
			if (itemName.endsWith("hood")) {
				itemId = 13579;
			} else if (itemName.endsWith("cape")) {
				itemId = 13581;
			} else if (itemName.endsWith("top")) {
				itemId = 13583;
			} else if (itemName.endsWith("legs")) {
				itemId = 13585;
			} else if (itemName.endsWith("gloves")) {
				itemId = 13587;
			} else if (itemName.endsWith("boots")) {
				itemId = 13589;
			}
			final int itemIdFinal = itemId;
			ItemNPCAction.register(i, 5919, (player, item, npc) -> {
				player.dialogue(new OptionsDialogue(
					new Option("Recolor " + itemName + " to Normal (Free)", () -> {
						player.getInventory().remove(i, 1);
						player.getInventory().add(itemIdFinal - 1729);
					}),
					new Option("Recolor " + itemName + " to Purple (15 Marks)", () -> {
						giveGracefulPiece(player, i, itemIdFinal);
					}),
					new Option("Recolor " + itemName + " to Blue (15 Marks)", () -> {
						giveGracefulPiece(player, i, itemIdFinal + 12);
					}),
					new Option("Recolor " + itemName + " to Orange (15 Marks)", () -> {
						giveGracefulPiece(player, i, itemIdFinal + 24);
					}),
					new Option("Next (2 more options)", () -> {
						player.dialogue(new OptionsDialogue(
							new Option("Recolor " + itemName + " to Red (15 Marks)", () -> {
								giveGracefulPiece(player, i, itemIdFinal + 36);
							}),
							new Option("Recolor " + itemName + " to Green (15 Marks)", () -> {
								giveGracefulPiece(player, i, itemIdFinal + 48);
							})
						));
					})
				));
			});
		}
	}

	private static void giveGracefulPiece(Player player, int currentGraceful, int newGraceful) {
		if (player.getInventory().hasItem(11849, 15)) {
			player.getInventory().remove(11849, 15);
			player.getInventory().remove(currentGraceful, 1);
			player.getInventory().add(newGraceful);
		} else {
			player.dialogue(new MessageDialogue("You need 15 marks of grace to recolor your piece."));
		}
	}

}
