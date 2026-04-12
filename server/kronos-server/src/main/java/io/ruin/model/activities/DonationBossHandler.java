package io.ruin.model.activities;

import com.google.gson.annotations.Expose;
import discord.webhooks.notifications.GlobalBroadcastHook;
import io.ruin.model.World;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

public class DonationBossHandler {
	private static int DELAY_5 = 5;
	private static int DELAY_10 = 10;
	private static int DELAY_50 = 50;
	private static int DELAY_100 = 100;

	public static NPC boss;
	static boolean spawning = false;
	static boolean hasAnnouncedOverflow = false;

	@Expose
	public static int currentDonationAmount = 0;
	static int lastUpdate = 0;
	static int overflowDonations = 0;

	public static Position teleportPosition;
	public static Position malakarTeleportPosition;
	public static DynamicMap map;
	public static DynamicMap malakarMap;
	static Position spawnPosition;
	static Bounds mapBounds;

	private static final int DONATION_FOR_BOSS = 200;
	private static final int BROADCAST_THRESHOLD = 50;
	private static Queue<Runnable> bossSpawnQueue = new LinkedList<>();

	public static void init() throws DynamicMap.DynamicMapBuildException {
		map = new DynamicMap().build(11576, 1).persistent(true);
		SummerEvent.map = new DynamicMap().build(11576, 1).persistent(true);
		malakarMap = new DynamicMap().build(11576, 1).persistent(true);
		teleportPosition = new Position(map.convertX(2900), map.convertY(3616), 0);
		malakarTeleportPosition = new Position(malakarMap.convertX(2900), malakarMap.convertY(3616), 0);
		new NPC(12336).spawn(new Position(malakarMap.convertX(2912), malakarMap.convertY(3616)));
		spawnPosition = new Position(map.convertX(2912), map.convertY(3616), 0);
	}

	public static synchronized void addDonationAmount(int amount) {
		currentDonationAmount += amount;

		// Handle regular threshold broadcasts for first boss
		if (currentDonationAmount - lastUpdate >= BROADCAST_THRESHOLD && currentDonationAmount < DONATION_FOR_BOSS) {
			broadcastEvent("Another $" + BROADCAST_THRESHOLD + " was donated, the donator boss now stands at $" +
					currentDonationAmount + "/$" + DONATION_FOR_BOSS);
			lastUpdate = currentDonationAmount;
		}

		// First, check if we have enough for initial boss spawn
		if (currentDonationAmount >= DONATION_FOR_BOSS) {
			if (!spawning && boss == null) {
				spawnBoss(false);
				int remaining = currentDonationAmount - DONATION_FOR_BOSS;
				currentDonationAmount = 0;
				lastUpdate = 0;

				// If there are remaining donations, process them after a delay
				if (remaining > 0) {
					World.startEvent(e -> {
						e.delay(DELAY_5); // Small delay to ensure spawn message is seen
						addDonationAmount(remaining);
					});
				}
			} else {
				// Calculate how many future bosses we can spawn
				int totalBossesQueued = overflowDonations / DONATION_FOR_BOSS;
				int newBossesQueued = (currentDonationAmount + overflowDonations) / DONATION_FOR_BOSS - totalBossesQueued;

				// Update overflow storage
				overflowDonations = (currentDonationAmount + overflowDonations);
				currentDonationAmount = 0;
				lastUpdate = 0;

				// Queue up additional boss spawns
				for (int i = 0; i < newBossesQueued; i++) {
					final int bossIndex = totalBossesQueued + i + 1;
					bossSpawnQueue.offer(() -> {
						spawnBoss(true);
						overflowDonations -= DONATION_FOR_BOSS;
					});
				}

				// Only show overflow message if we haven't already for this boss
				if (!hasAnnouncedOverflow) {
					World.startEvent(e -> {
						e.delay(DELAY_10); // Delay to ensure spawn message is seen first
						int totalQueued = bossSpawnQueue.size();
						if (totalQueued > 0) {
							broadcastEvent("Donations are overflowing! " + totalQueued + " more boss" +
									(totalQueued > 1 ? "es" : "") + " will spawn after the current one is defeated!");
							hasAnnouncedOverflow = true;
						}
					});
				}
			}
		}
	}

	private static void broadcastEvent(String eventMessage) {
		for (Player p : World.players()) {
			p.getPacketSender().sendBroadcast(eventMessage);
		}
	}

	private static void spawnBoss(boolean overflow) {
		if (boss != null && boss.getHp() > 0)
			return;

		spawning = true;
		if (!overflow)
			broadcastEvent("The Donation boss will spawn in 3 minutes, type ::db to teleport to it!");

		World.startEvent(e -> {
			try {
				if (!overflow) {
					// Wait 1 minute (100 ticks)
					e.delay(DELAY_100);
					broadcastEvent("The Donation boss will spawn in 2 minutes, type ::db to teleport to it!");

					// Wait another minute
					e.delay(DELAY_100);
				}
				broadcastEvent("The Donation boss will spawn in 1 minute, type ::db to teleport to it!");
				// TODO: Discord message, that the boss will spawn in 60 seconds
				var dto = new JSONObject()
					.put("boss", "The Donation boss")
					.put("description", "will spawn in 60 seconds.");
				GlobalBroadcastHook.sendGlobalSpawnedMessage(dto);
				// Final minute wait
				e.delay(DELAY_100);
				boss = new NPC(1787).spawn(new Position(map.convertX(2912), map.convertY(3616)));
				if (boss == null || boss.isRemoved()) {
					// Spawn failed, return donations
					overflowDonations += DONATION_FOR_BOSS;
					broadcastEvent("Boss spawn failed! Donations have been preserved for next spawn.");
				} else {
//					RareDropEmbedMessage.sendGlobalSpawnedMessage(boss);

					var dto2 = new JSONObject()
						.put("boss", boss.getName())
						.put("description", "has just spawned!");
					GlobalBroadcastHook.sendGlobalSpawnedMessage(dto2);
				}
			} catch (Exception ex) {
				overflowDonations += DONATION_FOR_BOSS;
				broadcastEvent("Boss spawn failed! Donations have been preserved for next spawn.");
			} finally {
				spawning = false;
			}
		});
	}

	public static void onBossDeath() {
		boss = null;
		hasAnnouncedOverflow = false;

		// Check if there's a queued boss spawn
		if (!bossSpawnQueue.isEmpty()) {
			broadcastEvent("The Donation Boss has been defeated! A new boss will spawn shortly...");
			Runnable nextBossSpawn = bossSpawnQueue.poll();
			World.startEvent(e -> {
				e.delay(DELAY_50); // Small delay before starting next boss spawn
				broadcastEvent("Get ready! Another Donation Boss is about to appear!");
				nextBossSpawn.run();
			});
		} else {
			broadcastEvent("The Donation Boss has been defeated! Donate more to summon another boss!");
			if (overflowDonations > 0) {
				// Handle any leftover overflow
				World.startEvent(e -> {
					e.delay(DELAY_10);
					addDonationAmount(overflowDonations);
					overflowDonations = 0;
				});
			}
		}
	}
}
