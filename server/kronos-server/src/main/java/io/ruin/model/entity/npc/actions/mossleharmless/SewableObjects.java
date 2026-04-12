package io.ruin.model.entity.npc.actions.mossleharmless;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.Dialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;

public enum SewableObjects {
	BANDANA_EYEPATCH_BLUE(8926, "Blue Bandana Eyepatch", 1025, "right eye patch", 7130, "blue pirate bandana"),
	BANDANA_EYEPATCH_WHITE(8924, "White Bandana Eyepatch", 1025, "right eye patch", 7112, "white pirate bandana"),
	BANDANA_EYEPATCH_BROWN(8927, "Brown Bandana Eyepatch", 1025, "right eye patch", 7136, "brown pirate bandana"),
	BANDANA_EYEPATCH_RED(8925, "Red Bandana Eyepatch", 1025, "right eye patch", 7124, "red pirate bandana"),
	HAT_EYEPATCH(8928, "Hat with an Eyepatch", 1025, "right eye patch", 2651, "pirate hat"),
	PIRATEHAT_EYEPATCH(12412, "Big Pirate Hat with an Eyepatch", 1025, "right eye patch", 12355, "big pirate hat"),
	DOUBLE_EYEPATCH(19727, "Double Eye Patch", 1025, "right eye patch", 19724, "left eye patch"),
	BERET_MASK(11278, "Beret Mask", 2635, "black beret", 3057, "mime mask"),
	CAVALIER_MASK(11280, "Cavalier Mask", 2643, "black cavalier", 2631, "highwayman mask"),
	CRABCLAW_HOOK(8929, "Crabclaw Hook", 7537, "crab claw", 2997, "pirate hook"),
	TOPHAT_MONOCLE(12434, "Top Hat & Monocle", 12432, "top hat", 12353, "monocle"),
	PARTYHAT_SPECS(12399, "Partyhat & Specs", 1042, "blue partyhat", 12337, "sagacious spectacles");

	public final int sewedItemID;
	public final String sewedItemName;
	public final int partAItemID;
	public final String partAItemName;
	public final int partBItemID;
	public final String partBItemName;

	SewableObjects(int sewedItemID, String sewedItemName, int partAItemID, String partAItemName, int partBItemID, String partBItemName) {
		this.sewedItemID = sewedItemID;
		this.sewedItemName = sewedItemName;
		this.partAItemID = partAItemID;
		this.partAItemName = partAItemName;
		this.partBItemID = partBItemID;
		this.partBItemName = partBItemName;
	}


	public static Dialogue[] getDialogue(Player player, NPC npc) {
		return new Dialogue[]{
			new OptionsDialogue(
				new Option(BANDANA_EYEPATCH_BLUE.sewedItemName, () -> {
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_BLUE.partAItemID, 1)) {
						player.sendMessage("Oh looks like you don't have a " + BANDANA_EYEPATCH_BLUE.partAItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_BLUE.partBItemID, 1)) {
						player.sendMessage("Oh look like you don't have a " + BANDANA_EYEPATCH_BLUE.partBItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(995, 100000)) {
						player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
						return;
					}
					player.getInventory().remove(995, 100000);
					player.getInventory().remove(BANDANA_EYEPATCH_BLUE.partAItemID, 1);
					player.getInventory().remove(BANDANA_EYEPATCH_BLUE.partBItemID, 1);
					player.getInventory().add(BANDANA_EYEPATCH_BLUE.sewedItemID, 1);
				}),
				new Option(BANDANA_EYEPATCH_WHITE.sewedItemName, () -> {
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_WHITE.partAItemID, 1)) {
						player.sendMessage("Oh looks like you don't have a " + BANDANA_EYEPATCH_WHITE.partAItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_WHITE.partBItemID, 1)) {
						player.sendMessage("Oh look like you don't have a " + BANDANA_EYEPATCH_WHITE.partBItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(995, 100000)) {
						player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
						return;
					}
					player.getInventory().remove(995, 100000);
					player.getInventory().remove(BANDANA_EYEPATCH_WHITE.partAItemID, 1);
					player.getInventory().remove(BANDANA_EYEPATCH_WHITE.partBItemID, 1);
					player.getInventory().add(BANDANA_EYEPATCH_WHITE.sewedItemID, 1);
				}),
				new Option(BANDANA_EYEPATCH_BROWN.sewedItemName, () -> {
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_BROWN.partAItemID, 1)) {
						player.sendMessage("Oh looks like you don't have a " + BANDANA_EYEPATCH_BROWN.partAItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_BROWN.partBItemID, 1)) {
						player.sendMessage("Oh look like you don't have a " + BANDANA_EYEPATCH_BROWN.partBItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(995, 100000)) {
						player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
						return;
					}
					player.getInventory().remove(995, 100000);
					player.getInventory().remove(BANDANA_EYEPATCH_BROWN.partAItemID, 1);
					player.getInventory().remove(BANDANA_EYEPATCH_BROWN.partBItemID, 1);
					player.getInventory().add(BANDANA_EYEPATCH_BROWN.sewedItemID, 1);
				}),
				new Option(BANDANA_EYEPATCH_RED.sewedItemName, () -> {
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_RED.partAItemID, 1)) {
						player.sendMessage("Oh looks like you don't have a " + BANDANA_EYEPATCH_RED.partAItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(BANDANA_EYEPATCH_RED.partBItemID, 1)) {
						player.sendMessage("Oh look like you don't have a " + BANDANA_EYEPATCH_RED.partBItemName + ".");
						return;
					}
					if (!player.getInventory().hasItem(995, 100000)) {
						player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
						return;
					}
					player.getInventory().remove(995, 100000);
					player.getInventory().remove(BANDANA_EYEPATCH_RED.partAItemID, 1);
					player.getInventory().remove(BANDANA_EYEPATCH_RED.partBItemID, 1);
					player.getInventory().add(BANDANA_EYEPATCH_RED.sewedItemID, 1);
				}),
				new Option("Next page", () -> {
					player.dialogue(new OptionsDialogue(
						new Option(HAT_EYEPATCH.sewedItemName, () -> {
							if (!player.getInventory().hasItem(HAT_EYEPATCH.partAItemID, 1)) {
								player.sendMessage("Oh looks like you don't have a " + HAT_EYEPATCH.partAItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(HAT_EYEPATCH.partBItemID, 1)) {
								player.sendMessage("Oh look like you don't have a " + HAT_EYEPATCH.partBItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(995, 100000)) {
								player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
								return;
							}
							player.getInventory().remove(995, 100000);
							player.getInventory().remove(HAT_EYEPATCH.partAItemID, 1);
							player.getInventory().remove(HAT_EYEPATCH.partBItemID, 1);
							player.getInventory().add(HAT_EYEPATCH.sewedItemID, 1);
						}),
						new Option(PIRATEHAT_EYEPATCH.sewedItemName, () -> {
							if (!player.getInventory().hasItem(PIRATEHAT_EYEPATCH.partAItemID, 1)) {
								player.sendMessage("Oh looks like you don't have a " + PIRATEHAT_EYEPATCH.partAItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(PIRATEHAT_EYEPATCH.partBItemID, 1)) {
								player.sendMessage("Oh look like you don't have a " + PIRATEHAT_EYEPATCH.partBItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(995, 100000)) {
								player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
								return;
							}
							player.getInventory().remove(995, 100000);
							player.getInventory().remove(PIRATEHAT_EYEPATCH.partAItemID, 1);
							player.getInventory().remove(PIRATEHAT_EYEPATCH.partBItemID, 1);
							player.getInventory().add(PIRATEHAT_EYEPATCH.sewedItemID, 1);
						}),
						new Option(DOUBLE_EYEPATCH.sewedItemName, () -> {
							if (!player.getInventory().hasItem(DOUBLE_EYEPATCH.partAItemID, 1)) {
								player.sendMessage("Oh looks like you don't have a " + DOUBLE_EYEPATCH.partAItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(DOUBLE_EYEPATCH.partBItemID, 1)) {
								player.sendMessage("Oh look like you don't have a " + DOUBLE_EYEPATCH.partBItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(995, 100000)) {
								player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
								return;
							}
							player.getInventory().remove(995, 100000);
							player.getInventory().remove(DOUBLE_EYEPATCH.partAItemID, 1);
							player.getInventory().remove(DOUBLE_EYEPATCH.partBItemID, 1);
							player.getInventory().add(DOUBLE_EYEPATCH.sewedItemID, 1);
						}),
						new Option(BERET_MASK.sewedItemName, () -> {
							if (!player.getInventory().hasItem(BERET_MASK.partAItemID, 1)) {
								player.sendMessage("Oh looks like you don't have a " + BERET_MASK.partAItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(BERET_MASK.partBItemID, 1)) {
								player.sendMessage("Oh look like you don't have a " + BERET_MASK.partBItemName + ".");
								return;
							}
							if (!player.getInventory().hasItem(995, 100000)) {
								player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
								return;
							}
							player.getInventory().remove(995, 100000);
							player.getInventory().remove(BERET_MASK.partAItemID, 1);
							player.getInventory().remove(BERET_MASK.partBItemID, 1);
							player.getInventory().add(BERET_MASK.sewedItemID, 1);
						}),
						new Option("Next page", () -> {
							player.dialogue(new OptionsDialogue(
								new Option(CAVALIER_MASK.sewedItemName, () -> {
									if (!player.getInventory().hasItem(CAVALIER_MASK.partAItemID, 1)) {
										player.sendMessage("Oh looks like you don't have a " + CAVALIER_MASK.partAItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(CAVALIER_MASK.partBItemID, 1)) {
										player.sendMessage("Oh look like you don't have a " + CAVALIER_MASK.partBItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(995, 100000)) {
										player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
										return;
									}
									player.getInventory().remove(995, 100000);
									player.getInventory().remove(CAVALIER_MASK.partAItemID, 1);
									player.getInventory().remove(CAVALIER_MASK.partBItemID, 1);
									player.getInventory().add(CAVALIER_MASK.sewedItemID, 1);
								}),
								new Option(CRABCLAW_HOOK.sewedItemName, () -> {
									if (!player.getInventory().hasItem(CRABCLAW_HOOK.partAItemID, 1)) {
										player.sendMessage("Oh looks like you don't have a " + CRABCLAW_HOOK.partAItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(CRABCLAW_HOOK.partBItemID, 1)) {
										player.sendMessage("Oh look like you don't have a " + CRABCLAW_HOOK.partBItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(995, 100000)) {
										player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
										return;
									}
									player.getInventory().remove(995, 100000);
									player.getInventory().remove(CRABCLAW_HOOK.partAItemID, 1);
									player.getInventory().remove(CRABCLAW_HOOK.partBItemID, 1);
									player.getInventory().add(CRABCLAW_HOOK.sewedItemID, 1);
								}),
								new Option(TOPHAT_MONOCLE.sewedItemName, () -> {
									if (!player.getInventory().hasItem(TOPHAT_MONOCLE.partAItemID, 1)) {
										player.sendMessage("Oh looks like you don't have a " + TOPHAT_MONOCLE.partAItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(TOPHAT_MONOCLE.partBItemID, 1)) {
										player.sendMessage("Oh look like you don't have a " + TOPHAT_MONOCLE.partBItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(995, 100000)) {
										player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
										return;
									}
									player.getInventory().remove(995, 100000);
									player.getInventory().remove(TOPHAT_MONOCLE.partAItemID, 1);
									player.getInventory().remove(TOPHAT_MONOCLE.partBItemID, 1);
									player.getInventory().add(TOPHAT_MONOCLE.sewedItemID, 1);
								}),
								new Option(PARTYHAT_SPECS.sewedItemName, () -> {
									if (!player.getInventory().hasItem(PARTYHAT_SPECS.partAItemID, 1)) {
										player.sendMessage("Oh looks like you don't have a " + PARTYHAT_SPECS.partAItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(PARTYHAT_SPECS.partBItemID, 1)) {
										player.sendMessage("Oh look like you don't have a " + PARTYHAT_SPECS.partBItemName + ".");
										return;
									}
									if (!player.getInventory().hasItem(995, 100000)) {
										player.sendMessage("Matey, looks like you don't have the coin for me to do that.");
										return;
									}
									player.getInventory().remove(995, 100000);
									player.getInventory().remove(PARTYHAT_SPECS.partAItemID, 1);
									player.getInventory().remove(PARTYHAT_SPECS.partBItemID, 1);
									player.getInventory().add(PARTYHAT_SPECS.sewedItemID, 1);
								}),
								new Option("First Page", () -> player.dialogue(SewableObjects.getDialogue(player, npc)))
							));
						})
					));
				})
			)
		};
	}
}
