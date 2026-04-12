package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;

public class Oziach {

	public static final int OZIACH = 822;

	public static void register() {
		NPCAction.register(OZIACH, "Talk-to", (player, npc) -> player.dialogue(
			new NPCDialogue(npc, "I can craft your Dragon platebody if you give me 1m, Dragon chainbody and a Dragon metal lump"),
			new OptionsDialogue(
				new Option("I have everything you need.", () -> craftPlatebody(player)))));

	}

	private static void craftPlatebody(Player player) {
		if (!player.getInventory().hasItem(995, 1000000) || !player.getInventory().hasItem(3140, 1) || !player.getInventory().hasItem(22103, 1)
			|| !player.getInventory().hasItem(22097, 1)) {
			player.sendMessage("You need 1 million coins, Dragon chainbody, Dragon metal shard and a Dragon metal lump");
			return;
		}
		if (player.getInventory().hasItem(995, 1000000) && player.getInventory().hasItem(3140, 1) && player.getInventory().hasItem(22103, 1)
			&& player.getInventory().hasItem(22097, 1)) {
			player.getInventory().remove(995, 1000000);
			player.getInventory().remove(3140, 1);
			player.getInventory().remove(22103, 1);
			player.getInventory().remove(22097, 1);
			player.getInventory().add(21892, 1);
		}
	}

}
