package io.ruin.model.entity.npc.actions.edgeville;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.var.VarPlayerRepository;

public class DeironNPC {
	public static void deiron(Player player) {
		player.dialogue(new NPCDialogue(311, "Are you sure you want to de-iron your account? This is permanent."),
			new OptionsDialogue(
				new Option("Yes, de-iron my account.", () -> deironAccount(player)),
				new Option("No, I want to stay an iron.", player::closeDialogue)
			));
	}

	private static void deironAccount(Player player) {
		VarPlayerRepository.IRONMAN_MODE.set(player, 0);
		VarPlayerRepository.CHAT_ICONS.set(player, 0);
		GameMode.changeForumsGroup(player, GameMode.STANDARD.groupId);
		player.dialogue(new NPCDialogue(311, "Your account has been de-ironed. You are now a standard player."));
	}

}
