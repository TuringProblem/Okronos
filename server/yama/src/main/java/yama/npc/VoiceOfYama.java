package yama.npc;

import io.ruin.model.activities.bosses.instancetoken.InstanceHandler;
import io.ruin.model.activities.bosses.instancetoken.InstanceManager;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import yama.combat.util.YamaMapHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceOfYama {

	public static final Map<String, YamaMapHandler> instances = new ConcurrentHashMap<>();
	private static void talkTo(Player player, NPC npc) {
		//TODO:
	}

	private static void travel(Player player) {
		YamaMapHandler yamaMapHandler = new YamaMapHandler();
		yamaMapHandler.movePlayerToInstance(player);
		instances.put(player.getName().toLowerCase(), yamaMapHandler);
	}

	private static void requestToJoinInstance(Player player) {
		if (player.teleportListener != null && !player.teleportListener.allow(player)) {
			return;
		}
		player.stringInput("Enter the players instance you wish to join", s -> {
			String name = s.toLowerCase();
			var instance = instances.get(name);
			if (instance == null) {
				player.sendMessage("An instance hosted by that player couldn't be found.");
				return;
			}
			player.dialogue(
				new OptionsDialogue("Are you sure you want to join " + name + "'s instance?",
					new Option("Yes.", () -> instance.movePlayerToInstance(player)),
					new Option("No.", player::closeDialogue)
				)
			);
		});
	}

	public static void register() {
		NPCAction.register(14185, "talk-to", VoiceOfYama::talkTo);
		NPCAction.register(14185, "join", (player, npc) -> requestToJoinInstance(player));
		NPCAction.register(14185, "travel", (player, npc) -> travel(player));

	}
}
