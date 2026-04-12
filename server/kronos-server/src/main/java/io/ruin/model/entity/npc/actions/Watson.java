package io.ruin.model.entity.npc.actions;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;

public class Watson {
	public static void register() {
		NPCAction.register(7303, "talk-to", (player, npc) -> {
			player.dialogue(
				new NPCDialogue(
					npc,
					"'Ello chap, Watson here... Bring me clues and a master shall appear."),
				new OptionsDialogue(
					new Option(
						"Certainly, here you are!",
						() -> {
							player.dialogue(
								new PlayerDialogue("Certainly, here you are!"),
								new OptionsDialogue("Exchange one of each clue for a Master?",
									new Option("Exchange for multiple master clues",
										() -> {
											if (player.getInventory().getFreeSlots() >= 3) {
												boolean exchanged = false;
												int amount = player.getInventory().getAmount(ItemID.CLUE_SCROLL_EASY);
												for (int i = 0; i < amount; i++) {
													if (player.getInventory().contains(ItemID.CLUE_SCROLL_EASY)
														&& player.getInventory().contains(ItemID.CLUE_SCROLL_MEDIUM)
														&& player.getInventory().contains(ItemID.CLUE_SCROLL_HARD)
														&& player.getInventory().contains(ItemID.CLUE_SCROLL_ELITE)) {
														exchanged = true;

														player.getInventory().remove(ItemID.CLUE_SCROLL_EASY, 1);
														player.getInventory().remove(ItemID.CLUE_SCROLL_MEDIUM, 1);
														player.getInventory().remove(ItemID.CLUE_SCROLL_HARD, 1);
														player.getInventory().remove(ItemID.CLUE_SCROLL_ELITE, 1);
														player.getInventory().add(ItemID.CLUE_SCROLL_MASTER, 1);
														player.dialogue(
															new PlayerDialogue("Certainly, here you are!"),
															new NPCDialogue(npc, "There you go, enjoy your Master clue scroll!"));
													} else {
														if (exchanged) {
															player.dialogue(
																new NPCDialogue(
																	npc,
																	"There you go. Enjoy your Master clue scrolls!"));
														} else {
															player.dialogue(
																new NPCDialogue(
																	npc,
																	"You must bring me at least one Easy, Medium, Hard, and Elite clue " +
																		"in order to exchange for a Master."));
														}
														break;
													}
												}
											} else {
												player.dialogue(
													new NPCDialogue(npc, "You need at least 3 free inventory slots to perform this exchange."));
											}
										}),
									new Option("Exchange for multiple master caskets", () -> {
										if (player.getInventory().getFreeSlots() >= 3) {
											boolean exchanged = false;
											int amount = player.getInventory().getAmount(ItemID.REWARD_CASKET_EASY);
											for (int i = 0; i < amount; i++) {
												if (player.getInventory().contains(ItemID.REWARD_CASKET_EASY)
													&& player.getInventory().contains(ItemID.REWARD_CASKET_MEDIUM)
													&& player.getInventory().contains(ItemID.REWARD_CASKET_HARD)
													&& player.getInventory().contains(ItemID.REWARD_CASKET_ELITE)) {
													exchanged = true;

													player.getInventory().remove(ItemID.REWARD_CASKET_EASY, 1);
													player.getInventory().remove(ItemID.REWARD_CASKET_MEDIUM, 1);
													player.getInventory().remove(ItemID.REWARD_CASKET_HARD, 1);
													player.getInventory().remove(ItemID.REWARD_CASKET_ELITE, 1);
													player.getInventory().add(ItemID.REWARD_CASKET_MASTER, 1);
													player.dialogue(
														new PlayerDialogue("Certainly, here you are!"),
														new NPCDialogue(npc, "There you go, enjoy your Master casket!"));
												} else {
													if (exchanged) {
														player.dialogue(
															new NPCDialogue(npc, "There you go. Enjoy your Master caskets!"));
													} else {
														player.dialogue(
															new NPCDialogue(
																npc,
																"You must bring me at least one Easy, Medium, Hard, and Elite casket " +
																	"in order to exchange for a Master."));
													}
													break;
												}
											}
										} else {
											player.dialogue(
												new NPCDialogue(npc, "You need at least 3 free inventory slots to perform this exchange."));
										}
									}),
									new Option("No, thanks",
										player::closeDialogue))
							);
						}),
					new Option(
						"Actually, I don't have those clues on me...",
						player::closeDialogue)));
		});
	}
}
