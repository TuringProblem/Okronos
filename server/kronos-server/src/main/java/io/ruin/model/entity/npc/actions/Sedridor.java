package io.ruin.model.entity.npc.actions;

import io.ruin.model.entity.npc.NPCAction;

public class Sedridor {

	public static void register() {
		NPCAction.register(5034, "teleport", ((player, npc) -> {
			player.sendMessage("Sedridor teleports you to the essence mine.");
			player.getMovement().teleport(2910, 4830, 0);
		}));
	}

}
