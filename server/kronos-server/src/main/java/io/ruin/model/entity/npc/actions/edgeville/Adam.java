package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.var.VarPlayerRepository;

public class Adam {

	private final static int ADAM = 311;

	public static void register() {
//        NPCAction.register(ADAM, "Talk-to", (player, npc) -> {
//            if (player.getGameMode().isIronMan()) {
//                player.dialogue(new NPCDialogue(npc, "Hello " + player.getName() + ", how can I help you today?"),
//                        new OptionsDialogue(
//                                new Option("Open Shop", () -> CustomShop2.openIronmanShop(player)), //TODO fill this out
//                                new Option("Remove Ironman Rank", () -> player.dialogue(
//                                        new OptionsDialogue("Are you sure you want to remove your ironman status?",
//                                                new Option("Yes, I don't want to be an ironman anymore!", Adam::removeIronmanMode),
//                                                new Option("No, I want to keep my ironman status!", player::closeDialogue)))
//                                )
//                        ));
//            } else {
//                player.dialogue(new NPCDialogue(npc, "Hi " + player.getName() + ", if you'd like to play as an ironman, simply create a new account and select it during the tutorial."),
//                        new OptionsDialogue("Would you like to register a new account?",
//                                new Option("Yes", () -> player.openUrl(World.type.getWorldName() + " Account Registration", "https://prifddinas.io/forums/")),
//                                new Option("No", player::closeDialogue)
//                        ));
//            }
//        });
		NPCAction.register(ADAM, "Armour", ((player, npc) -> player.dialogue(new OptionsDialogue("Collect a set of the Ironman Armour?",
			new Option("Yes, collect the armour.", () -> {
				if (player.getGameMode().isIronMan()) {
					giveArmour(player);
				} else {
					player.dialogue(new NPCDialogue(npc, "You're not an ironman!"));
				}
			}),
			new Option("No, I don't want the ironman armour.", player::closeDialogue)))));
		NPCAction.register(ADAM, 1, (player, npc) -> {
			if (!player.getGameMode().isIronMan() && !player.isAdmin()) {
				player.sendMessage("Only ironmen can trade this shop!");
				return;
			}

			NewShopHandler.openShop(player, NewShopHandler.ironManStore);

		});
	}

	private static void removeIronmanMode(Player player) {
		if (player.getBankPin().requiresVerification(Adam::removeIronmanMode))
			return;
		GameMode.changeForumsGroup(player, GameMode.STANDARD.groupId);
		VarPlayerRepository.IRONMAN_MODE.set(player, 0);
		player.dialogue(new MessageDialogue("You have successfully removed your ironman status."));
	}

	private static void giveArmour(Player player) {
		if (player.getInventory().getFreeSlots() < 3) {
			player.sendMessage("You need at least 3 inventory slots to collect your ironman armour.");
			return;
		}
		switch (player.getGameMode()) {
			case IRONMAN:
				player.getInventory().add(12810, 1);
				player.getInventory().add(12811, 1);
				player.getInventory().add(12812, 1);
				break;
			case ULTIMATE_IRONMAN:
				player.getInventory().add(12813, 1);
				player.getInventory().add(12814, 1);
				player.getInventory().add(12815, 1);
				break;
			case HARDCORE_IRONMAN:
				player.getInventory().add(20792, 1);
				player.getInventory().add(20794, 1);
				player.getInventory().add(20796, 1);
				break;
		}
	}

}
