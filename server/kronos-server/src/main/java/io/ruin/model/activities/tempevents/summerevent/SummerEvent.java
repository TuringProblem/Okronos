package io.ruin.model.activities.tempevents.summerevent;

import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.utility.Broadcast;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@Slf4j
public class SummerEvent {
	public static DynamicMap map;

	public static boolean disabled = false;

	static List<String> summerBossNames = Arrays.asList(
			"Dagannoth Rex", "Dagannoth Prime",
			"Dagannoth Supreme", "Kalphite Queen",
			"Corporeal Beast", "King Black Dragon", "General Graardor",
			"Commander Zilyana", "Kree'arra", "K'ril Tsutsaroth",
			"Thermonuclear smoke devil", "Zulrah", "Kraken", "Abyssal Sire",
			"Dusk", "Alchemical Hydra", "The Nightmare", "Theatre of Blood",
			"Vorkath", "The Gauntlet", "Chambers of Xeric", "Tombs of Amascut", "Argentavis",
			"Sol Heredit", "Galvek", "Nex", "Sarachnis", "Ophidia", "Phantom Muspah", "The Leviathan", "Duke Sucellus",
			"The Whisperer", "Vardorvis", "Malakar", "Cerberus");

	static List<String> availableBossNames = new ArrayList<>();
	static List<String> cachedBossNames = new ArrayList<>();
	public static Map<String, Integer> activeBossNames = new HashMap<>();
	public static Map<String, Integer> startingAmountOfBosses = new HashMap<>();
	private static int bossSpawnTicks = 0;

	/*
	 * Teleports to the boss area
	 */
	public static void teleportToBoss(Player player) {
		if (disabled) {
			return;
		}
		if (map == null) {
			player.sendMessage("The summer event is not currently active.");
			return;
		}
		if (player.wildernessLevel > 20 || player.pvpAttackZone) {
			if (!(World.isDev() && player.isAdmin())) {
				player.sendMessage("You can't use this command from where you are standing.");
				return;
			}
		}
		if (player.getInventory().contains(25104)) {
			player.sendMessage("The crystal of memories stores your last location as an available teleport.");
			player.crystalMemoryPosition = player.getPosition().copy();
		}
		player.getMovement().startTeleport(event -> {
			event.setCancelCondition(() -> player.teleportListener != null && !player.teleportListener.allow(player));
			if (player.pet != null && player.familiarNPC != null) {
				Pet.pickup(player, player.familiarNPC, player.pet, true);
			}
			player.animate(3864);
			player.graphics(1039);
			player.privateSound(200, 0, 10);
			event.delay(2);
			player.getMovement().teleport(map.convertX(2900), map.convertY(3616), 0);
		});
	}


	/*
	 * Caches the previous bosses that were picked so they can't be repicked picks 8 bosses and sets a random amount for
	 * the server to kill before the summer boss spawns.
	 */
	public static void newEventStart() {
		if (disabled) {
			return;
		}
		World.startEvent(e -> {
			activeBossNames.clear();
			populateList();

			for (String bossName : cachedBossNames) {
				availableBossNames.remove(bossName);
			}
			cachedBossNames.clear();

			for (int i = 0; i < 8; i++) {
				String bossName = Random.get(availableBossNames);
				int amount = Random.get(100, 300);
				activeBossNames.put(bossName, amount);
				startingAmountOfBosses.put(bossName, amount);
				availableBossNames.remove(bossName);
				cachedBossNames.add(bossName);
			}
			broadcastEvent("</col>[<shad=8A0011>Summer Event</shad>] <shad=9B9B9B> " +
					"The new bosses have been selected for the summer event, check them at home!");
		});
	}

	/*
	 * Handles the kill of a boss TODO: Handle raids kills and others that use a diff drop.
	 */
	public static void handleKill(Player player, String bossName) {
		if (disabled) {
			return;
		}

		if (activeBossNames.containsKey(bossName)) {
			player.summerPoints += 1;
			player.sendFilteredMessage(
					"You now have <col=000000><shad=29F1FE>" + player.summerPoints + " Summer points<col=000000></shad>.");
			decrementRemainingKills(bossName, 1);
		}
	}

	private static void decrementRemainingKills(String bossName, int amount) {
		if (!activeBossNames.containsKey(bossName)) {
			return;
		}

		int remaining = activeBossNames.get(bossName);
		if (remaining <= 0) {
			return;
		}

		var newRemaining = Math.max(0, remaining - amount);
		if (newRemaining == 0) {
			activeBossNames.remove(bossName);
		} else {
			activeBossNames.put(bossName, newRemaining);
		}

		if (activeBossNames.isEmpty()) {
			onAllBossesKilled();
		}
	}

	// happens whenever all bosses get killed from the current active set,
	// meaning the set is empty.
	private static void onAllBossesKilled() {
		startBossEvent();
	}

	public static void skipBossKilling() {
		if (activeBossNames.isEmpty()) {
			log.warn("There are no bosses to kill.");
			return;
		}
		Map.copyOf(activeBossNames).forEach(SummerEvent::decrementRemainingKills);
	}

	public static void skipActivationTimer() {
		if (bossSpawnTicks > 1) {
			bossSpawnTicks = 1;
		}
	}

	public static void startBossEvent() {
		if (bossSpawnTicks > 0) {
			log.warn("There is currently boss npc spawn pending: " + bossSpawnTicks);
			return;
		}

		bossSpawnTicks = 100;
		World.startEvent(e -> {
			broadcastEvent("The Summer Boss will spawn in 1 minute!" +
					" Type ::sb to teleport to the boss area!");
			while (--bossSpawnTicks > 0) {
				e.delay(1);
			}
			NPC boss = new NPC(17020).spawn(new Position(map.convertX(2912), map.convertY(3616), 0));
			boss.multi = true;
			broadcastEvent("</col>[<shad=8A0011>Summer Event</shad>] <shad=9B9B9B> " +
					"The summer boss has spawned, type ::sb to get there!");
		});
	}

	private static void broadcastEvent(String eventMessage) {
		for (Player p : World.players()) {
			p.getPacketSender().sendBroadcast(eventMessage);
		}
	}

	private static void populateList() {
		availableBossNames.clear();
		availableBossNames.addAll(summerBossNames);
	}
}
