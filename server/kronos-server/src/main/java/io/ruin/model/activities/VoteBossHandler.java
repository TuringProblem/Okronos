package io.ruin.model.activities;

import com.google.gson.annotations.Expose;
import discord.webhooks.notifications.GlobalBroadcastHook;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import org.json.JSONObject;

public class VoteBossHandler {
	public static NPC boss;

	@Expose
	static int currentVotes = 0;

	public static Position teleportPosition;
	public static DynamicMap map;

	public static void init() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(11576, 1).persistent(true);
		teleportPosition = new Position(map.convertX(2900), map.convertY(3616), 0);
	}

	public static void addVote(int amount) {
		currentVotes += amount;
		if (currentVotes % 5 == 0 && currentVotes < 15) {
			broadcastEvent(
					"Another 5 votes have been added to the vote boss, it'll spawn in " + (15 - currentVotes) + " votes!");
		}
		if (currentVotes >= 15) {
			spawnBoss();
			currentVotes -= 15;
		}
	}

	private static void broadcastEvent(String eventMessage) {
		for (Player p : World.players()) {
			p.getPacketSender().sendBroadcast(eventMessage);
		}
	}

	private static void spawnBoss() {
		if (boss != null)
			return;
		broadcastEvent("The Vote boss will spawn in 60 seconds, type ::vb to teleport to it!");

		// TODO: Discord message, that the boss will spawn in 60 seconds
		var dto = new JSONObject()
			.put("boss", "The vote boss")
			.put("description", "will spawn in 60 seconds.");
		GlobalBroadcastHook.sendGlobalSpawnedMessage(dto);

		World.startEvent(e -> {
			e.delay(100);
			boss = new NPC(8262).spawn(new Position(map.convertX(2912), map.convertY(3616)));
//			RareDropEmbedMessage.sendGlobalSpawnedMessage(boss);
			var dto2 = new JSONObject()
				.put("boss", boss.getName())
				.put("description", "has just spawned!");
			GlobalBroadcastHook.sendGlobalSpawnedMessage(dto2);
		});
	}

}
