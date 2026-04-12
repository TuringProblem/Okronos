package io.ruin.model.entity.npc.actions;

import io.ruin.Server;
import io.ruin.api.utils.TimeUtils;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ActionDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;

public class Hans {

	public static void register() {
		NPCAction.register(3105, "talk-to", ((player, npc) -> {
			player.dialogue(
				new NPCDialogue(3105, "Hello. What are you doing here?"),
				new OptionsDialogue("Select an option",
					new Option("I'm looking for whoever is in charge of this place.", () -> inCharge(player)),
					new Option("I have come to kill everyone in this castle!", () -> killEveryone(player, npc)),
					new Option("I don't know. I'm lost. Where am I?", () -> imLost(player)),
					new Option("Can you tell me how long I've been here?", () -> askAge(player)),
					new Option("Nothing.")
				)
			);
		}));
		NPCAction.register(3105, "age", (player, npc) -> askAge(player));
	}

	public static void inCharge(Player player) {
		player.dialogue(
			new PlayerDialogue("I'm looking for whoever is in charge of this place."),
			new NPCDialogue(3105, "Who, the Duke? He's in his study, on the first floor.")
		);
	}

	public static void killEveryone(Player player, NPC npc) {
		player.dialogue(
			new PlayerDialogue("I have come to kill everyone in this castle!"),
			new ActionDialogue(() -> {
				npc.forceText("Help! Help!");
			})
		);
	}

	public static void imLost(Player player) {
		player.dialogue(
			new PlayerDialogue("I don't know. I'm lost. Where am I?"),
			new NPCDialogue(3105, "You are in Lumbridge Castle, in the Kingdom of" +
				" Misthalin. Across the river, the road leads north to" +
				" Varrock, and to the west lies Draynor Village.")
		);
	}

	public static void askAge(Player player) {
		player.dialogue(
			new PlayerDialogue("Can you tell me how long I've been here?"),
			new NPCDialogue(3105, "Ahh, I see all the newcomers arriving in Lumbridge, " +
				"fresh-faced and eager for adventure. I remember you..."),
			new NPCDialogue(3105, "You've spent " + toTime(player) + " in Reason.")
		);
	}

	private static String toTime(Player player) {
		return TimeUtils.fromMs(player.playTime * Server.tickMs(), false);
	}

}
