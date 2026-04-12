package io.ruin.model.item.actions.impl;

import io.ruin.cache.NPCType;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.var.VarPlayerRepository;

public class SlayerPickTaskScroll {

	private static boolean hasSlayerTask(Player player) {
		if (player.bossSlayerName != null) {
			player.sendMessage("You already have a slayer task.");
			return true;
		}
		return false;
	}

	private static int getDonatorBoostAmount(Player player, int amount) {
		double boost = 1.0;
		switch (player.getSecondaryGroup()) {
			case DONATOR -> boost = 1.05;
			case SUPER_DONATOR -> boost = 1.1;
			case ELITE_DONATOR -> boost = 1.15;
			case NOBLE_DONATOR -> boost = 1.2;
			case GOLD_DONATOR -> boost = 1.25;
			case PLATINUM_DONATOR -> boost = 1.3;
			case LEGENDARY_DONATOR -> boost = 1.35;
			case SUPREME_DONATOR -> boost = 1.4;
		}
		return (int) (amount * boost);
	}

	private static NPC getNPCFromName(String name) {
		String searchName = name.toLowerCase();
		for (int i = 0; i < 14194; i++) {
			NPC npc = new NPC(i);
			npc.setCombat();
			var npcDef = npc.getDef();
			if (npcDef == null) {
				continue;
			}

			String npcName = npcDef.name.toLowerCase();
			if (searchName.equalsIgnoreCase("kraken") && npcName.contains("cave"))
				continue;
			if (npcName.equals(searchName) && npc.getCombat() != null) {
				return npc;
			}
			if (npcName.startsWith(searchName) && npc.getCombat() != null) {
				return npc;
			}
			if (npcName.contains(searchName) && npc.getCombat() != null) {
				return npc;
			}
		}
		return null;
	}

	public static void register() {
		ItemAction.registerInventory(30629, "read", (player, item) -> {
			if (hasSlayerTask(player)) {
				return;
			}
			if (player.previousBossSlayerName != null && !player.previousBossSlayerName.isEmpty()) {
				player.dialogue(
					new OptionsDialogue(
						"Would you like to repeat your last task?",
						new Option("Yes", () -> {
							var previousSlayerNPC = getNPCFromName(player.previousBossSlayerName);
							assert previousSlayerNPC != null: "Previous Slayer NPC is null";
							assignTask(player, previousSlayerNPC, previousSlayerNPC.getName(), player.previousBossSlayerStartAmount);
						}),
						new Option("No", () -> promptPlayerChoice(player)))
				);
			}
			else
				promptPlayerChoice(player);
		});
	}

	private static void promptPlayerChoice(Player player) {
		player.stringInput("Type the name of the slayer task you desire.", s -> {
			NPC npc = getNPCFromName(s);
			if (npc == null) {
				player.sendMessage("That NPC does not exist.");
				return;
			}
			if (npc.getCombat() == null) {
				player.sendMessage("That NPC cannot be assigned as a slayer task.");
				return;
			}
			player.dialogue(
				new OptionsDialogue("Are you sure you want to pick " + npc.getDef().name + " as your slayer task?",
					new Option("Yes, I want to pick this slayer task!", () -> {
						player.integerInput("How many " + npc.getDef().name + " would you like to kill?", amount -> {
							assignTask(player, npc, npc.getName(), amount);
							// set the previous selected task
							player.previousBossSlayerStartAmount = amount;
							player.previousBossSlayerName = npc.getName();
						});
					}),
					new Option("No, I'm not ready yet!")));
		});
	}

	private static void assignTask(Player player, NPC npc, String taskName, int amount) {
		player.bossSlayerName = taskName;
		player.bossSlayerStartAmount = amount;
		if (npc.getMaxHp() > 240 && amount > getDonatorBoostAmount(player, 50))
			amount = getDonatorBoostAmount(player, 50);
		else if (amount > getDonatorBoostAmount(player, 150))
			amount = getDonatorBoostAmount(player, 150);
		if (amount < 25)
			amount = 25;
		player.currentBossSlayerAmount = amount;
		player.dialogue(new NPCDialogue(10529, "Your new task is to kill %d %s.".formatted(amount, taskName)));
		player.getInventory().remove(30629, 1);
	}

}
