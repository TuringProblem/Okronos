package io.ruin.model.activities.bosses.globalboss;

import discord.webhooks.notifications.GlobalBroadcastHook;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import org.json.JSONObject;

public class GlobalBossHandler {
	static NPC currentBoss;
	static int currentIndex = 0;

	public static void spawnBoss() {
		if (currentBoss != null)
			currentBoss.remove();
		if (currentIndex >= GlobalBosses.values().length - 1) {
			currentIndex = 0;
		} else
			currentIndex++;
		int commandType = currentIndex + 1;
		broadcastEvent("The " + GlobalBosses.values()[currentIndex].boss.getDef().name
				+ " will spawn in 60 seconds, type ::gb to teleport to it!");

		World.startEvent(e -> {

			var nextBoss = new NPC(GlobalBosses.values()[currentIndex].boss.getId());
			onBossPrepared(nextBoss);
			e.delay(100);
			currentBoss = nextBoss.spawn(GlobalBosses.values()[currentIndex].spawnPosition);
			onBossSpawned(currentBoss);
		});
	}

	private static void onBossPrepared(NPC npc) {
		var dto = new JSONObject()
				.put("boss", npc.getName())
				.put("description", "will spawn in 60 seconds.");
		GlobalBroadcastHook.sendGlobalSpawnedMessage(dto);
	}

	private static void onBossSpawned(NPC npc) {
		// RareDropEmbedMessage.sendGlobalSpawnedMessage(currentBoss);
		var dto = new JSONObject()
				.put("boss", npc.getName())
				.put("description", "has just spawned!");
		GlobalBroadcastHook.sendGlobalSpawnedMessage(dto);
	}

	public static void register() {
		World.startEvent(e -> {
			while (true) {
				e.delay(4900);
				spawnBoss();
			}
		});
	}

	private static void broadcastEvent(String eventMessage) {
		for (Player p : World.players()) {
			p.getPacketSender().sendBroadcast(eventMessage);
		}
	}
}

