package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.inter.dialogue.*;

public class Aubury {

	private static final int AUBURY = 2886;

	public static void register() {
		NPCAction.register(AUBURY, "teleport", ((player, npc) -> {
			player.sendMessage("Aubury teleports you to the essence mine.");
			player.getMovement().teleport(2910, 4830, 0);
		}));
	}

}
