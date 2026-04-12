package io.ruin.model.entity.npc.actions.canifis;


import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;

public class DragontoothBoat {

	public static void register() {
		NPCAction.register(3005, "talk-to", (player, npc) -> talkCrewmember(player));
		NPCAction.register(3005, "travel", (player, npc) -> travelDragontooth(player));
	}

	public static void talkCrewmember(Player player) {
		player.dialogue(
			new NPCDialogue(1334, "Hello. How can I help you today?"),
			new OptionsDialogue("Select an option",
				new Option("Travel to Dragontooth Island"),
				new Option("Nevermind")
			)
		);
	}

	public static void travelDragontooth(Player player) {
		player.getMovement().startTeleport(event -> {
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(3792, 3560, 0);
			player.getPacketSender().clearFade();
			player.dialogue(new MessageDialogue("You board the ship, and arrive on Dragontooth Island."));
		});
	}


}
