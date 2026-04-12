package io.ruin.model.activities.bosses.nightmare;

import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap;


import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class NightmareEvent {
	private static final Logger logger = Logger.getLogger(NightmareEvent.class.getName());

	// Constants
	private static final int MAX_PLAYERS_PER_INSTANCE = 80;
	private static final int INSTANCE_TIMEOUT_MINUTES = 30;
	private static final int CLEANUP_DELAY = 10; // 10 ticks
	private static final Position SAFE_TELEPORT = new Position(3808, 9755, 1);


	ActivityTimer timer;


	// Instance tracking
	private static final Map<String, NightmareEvent> INSTANCES = new ConcurrentHashMap<>();

	// Instance-specific fields
	private final DynamicMap map;
	private final ArrayList<Player> players;
	private final String instanceOwner;
	private final long creationTime;
	private final Object cleanupLock = new Object();

	private boolean inited = false;
	private boolean destroyed = false;
	private boolean active = true;
	private Nightmare nightmare;
	private Position base;


	private static void registerStaffCombinations() {
		int[] orbs = {24511, 24514, 24517};
		int staff = 24422;
		int[] staffs = {24423, 24424, 24425};

		for (int i = 0; i < orbs.length; i++) {
			final int idx = i;
			ItemItemAction.register(orbs[idx], staffs[idx], (player, orbItem, staffItem) -> {
				player.getInventory().remove(orbs[idx], 1);
				player.getInventory().remove(staff, 1);
				player.getInventory().add(staffs[idx], 1);
			});
		}
	}

	private NightmareEvent(ArrayList<Player> players) {
		if (players.size() > MAX_PLAYERS_PER_INSTANCE) {
			throw new IllegalArgumentException("Too many players for instance (max: " + MAX_PLAYERS_PER_INSTANCE + ")");
		}

		this.players = players;
		this.instanceOwner = players.get(0).getName().toLowerCase();
		this.creationTime = System.currentTimeMillis();

		// Initialize map with error handling
		try {
			this.map = new DynamicMap().build(15515, 3);
			if (this.map == null) {
				throw new RuntimeException("Failed to create dynamic map");
			}
		} catch (Exception e) {
			logger.severe("Failed to create NightmareEvent: " + e.getMessage());
			throw new RuntimeException(e);
		}

		// Setup base position
		this.base = new Position(map.convertX(3840), map.convertY(9936), 3);

		// Initialize systems
		startTimeoutMonitor();
		initNightmare();
	}

	private void setupCleanupHandlers(Player player) {
		map.assignListener(player).onExit((p, logout) -> {
			synchronized (cleanupLock) {
				handlePlayerExit(player, logout);
			}
		});
	}

	private void startTimeoutMonitor() {
		World.startEvent(e -> {
			while (active && !destroyed) {
				// Check for timeout
				if (System.currentTimeMillis() - creationTime > INSTANCE_TIMEOUT_MINUTES * 60 * 1000) {
					logger.info("Instance timed out: " + instanceOwner);
					destroy();
					break;
				}

				// Check for abandoned instance
				if (players.isEmpty()) {
					logger.info("Instance abandoned: " + instanceOwner);
					destroy();
					break;
				}

				e.delay(100); // Check every minute
			}
		});
	}

	private void initNightmare() {
		timer = new ActivityTimer();
		nightmare = new Nightmare(9432, base);
		nightmare.transform(9432);
		nightmare.deathEndListener = (entity, killer, killHit) -> {

			for (Player p : players) {
				if (p == null)
					continue;
				if (timer != null) {
					if (entity.getPosition().getRegion().players.size() > 1) {
						p.nightmareBestTime = timer.stop(p, p.nightmareBestTime);
					} else {
						p.soloNightmareBestTime = timer.stop(p, p.soloNightmareBestTime);
						if (ActivityTimer.timeInSeconds(p.soloNightmareBestTime) < 180) {
							Objects.requireNonNull(killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_SOLO_SPEED_TRIALIST.ordinal())).
								getCombatAchievement()).check(killer.player);
						}
						if (ActivityTimer.timeInSeconds(p.soloNightmareBestTime) < 120) {
							Objects.requireNonNull(killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_SOLO_SPEED_CHASER.ordinal())).
								getCombatAchievement()).check(killer.player);
						}
						if (ActivityTimer.timeInSeconds(p.soloNightmareBestTime) < 90) {
							Objects.requireNonNull(killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_SOLO_SPEED_RUNNER.ordinal())).
								getCombatAchievement()).check(killer.player);
						}
					}
				}
			}

			nightmare.npc.remove();
			killer.player.sendMessage("You have defeated the Nightmare!");
			restartInstance();
		};


		// Initialize totems
		TotemPlugin[] totems = new TotemPlugin[]{

			new TotemPlugin(9434, new Position(base.getRegion().baseX + 23, base.getRegion().baseY + 22, 3)),
			new TotemPlugin(9437, new Position(base.getRegion().baseX + 39, base.getRegion().baseY + 22, 3)),
			new TotemPlugin(9440, new Position(base.getRegion().baseX + 23, base.getRegion().baseY + 38, 3)),
			new TotemPlugin(9443, new Position(base.getRegion().baseX + 39, base.getRegion().baseY + 38, 3))

		};

		nightmare.setTotems(totems);
		for (TotemPlugin totem : totems) {
			totem.setNightmare(nightmare);
		}

		nightmare.spawn(new Position(base.getRegion().baseX + 30, base.getRegion().baseY + 29, 3));
	}

	private void restartInstance() {
		World.startEvent(e -> {
			e.delay(15);//Time to respawn
			inited = false;
			destroyed = false;
			initNightmare();
			nightmare.getMasks()[0].reset();
			nightmare.toggleShield();
			e.delay(8);
			nightmare.transform(9430);
			nightmare.animate(8611);
			broadcastMessage("<col=ff0000>The nightmare has awoken!");
			inited = true;
			nightmare.setStage(0);
			e.delay(8);
			nightmare.transform(9425);
			nightmare.animate(-1);
		});
	}

	private void startInstance(Player player) {
		World.startEvent(e -> {
			if (inited || destroyed) {
				return;
			}

			try {
				// Initial setup
				nightmare.getMasks()[0].reset();
				nightmare.toggleShield();

				e.delay(1);

				// Teleport players
				teleportPlayerToInstance(player);

				e.delay(7);

				// Wake up nightmare
				nightmare.transform(9430);
				nightmare.animate(8611);
				broadcastMessage("<col=ff0000>The nightmare has awoken!");

				inited = true;
				nightmare.setStage(0);

				e.delay(8);
				nightmare.transform(9425);
				nightmare.animate(-1);

			} catch (Exception ex) {
				logger.severe("Error starting instance: " + ex.getMessage());
				destroy();
			}
		});
	}

	private void teleportPlayerToInstance(Player p) {
		setupCleanupHandlers(p);
		p.currentDynamicMap = map;
		p.inDynamicMap = true;
		addPlayer(p);
		p.getMovement().teleport(base.getRegion().baseX + 32, base.getRegion().baseY + 28, 3);
	}

	private void handlePlayerExit(Player player, boolean logout) {
		synchronized (cleanupLock) {
			player.currentDynamicMap = null;
			player.inDynamicMap = false;
			players.remove(player);

			if (players.isEmpty()) {
				scheduleDestroy();
			}
		}
	}

	private void scheduleDestroy() {
		World.startEvent(e -> {
			e.delay(CLEANUP_DELAY);
			destroy();
		});
	}

	public void destroy() {
		synchronized (cleanupLock) {
			if (destroyed) {
				return;
			}

			try {
				destroyed = true;
				active = false;

				// Remove instance from tracking
				INSTANCES.remove(instanceOwner);

				// Clean up NPCs
				if (nightmare != null) {
					try {
						nightmare.getPosition().getRegion().players.forEach(p -> p.getMovement().teleport(SAFE_TELEPORT));
						nightmare.remove();
						if (nightmare.getTotems() != null) {
							for (TotemPlugin totem : nightmare.getTotems()) {
								if (totem != null) {
									totem.remove();
								}
							}
						}
					} catch (Exception e) {
						logger.severe("Error cleaning up Nightmare: " + e.getMessage());
					}
				}

				players.clear();

				// Destroy map
				if (map != null) {
					try {
						map.destroy();
					} catch (Exception e) {
						logger.severe("Error destroying map: " + e.getMessage());
					}
				}

			} catch (Exception e) {
				logger.severe("Error in destroy(): " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	// Static factory methods
	public static NightmareEvent createInstance(ArrayList<Player> players) {
		if (players == null || players.isEmpty()) {
			return null;
		}

		String ownerName = players.get(0).getName().toLowerCase();

		// Check if owner already has an instance
		if (INSTANCES.containsKey(ownerName)) {
			players.get(0).sendMessage("You already have an active Nightmare instance.");
			return null;
		}

		try {
			NightmareEvent event = new NightmareEvent(players);
			INSTANCES.put(ownerName, event);
			Player p = World.getPlayer(ownerName);
			event.startInstance(p);
			return event;
		} catch (Exception e) {
			logger.severe("Error creating instance: " + e.getMessage());
			players.get(0).sendMessage("Failed to create instance. Please try again.");
			return null;
		}
	}

	public static NightmareEvent joinInstance(String key, Player player) {
		NightmareEvent event = INSTANCES.get(key.toLowerCase());

		if (event == null) {
			player.sendMessage(key + " has no active Nightmare instance.");
			return null;
		}

		if (event.isDestroyed()) {
			player.sendMessage("This instance is being destroyed and cannot be joined.");
			return null;
		}

		if (event.players.size() >= MAX_PLAYERS_PER_INSTANCE) {
			player.sendMessage("This instance is full.");
			return null;
		}

		event.addPlayer(player);
		player.currentDynamicMap = event.map;
		player.inDynamicMap = true;
		player.getMovement().teleport(event.base.getRegion().baseX + 32, event.base.getRegion().baseY + 28, event.getBase().getZ());


		return event;
	}

	// Utility methods
	private void addPlayer(Player player) {
		if (!players.contains(player)) {
			players.add(player);
		}
	}

	private void broadcastMessage(String message) {
		for (Player p : players) {
			p.sendMessage(message);
		}
	}

	// Getters
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public DynamicMap getMap() {
		return map;
	}

	public Position getBase() {
		return base;
	}

	// Static utility methods
	public static void clearAllInstances() {
		Iterator<NightmareEvent> it = INSTANCES.values().iterator();
		while (it.hasNext()) {
			NightmareEvent event = it.next();
			event.destroy();
			it.remove();
		}
	}

	public static int getActiveInstanceCount() {
		return INSTANCES.size();
	}

	// Monitoring methods
	public static class InstanceMonitor {
		public static void checkInstanceHealth() {
			INSTANCES.forEach((owner, instance) -> {
				long uptime = System.currentTimeMillis() - instance.creationTime;
				logger.info(String.format("Instance %s: uptime=%d mins, players=%d",

					owner, uptime / 1000 / 60, instance.players.size()));
			});
		}

		public static Map<String, Object> getInstanceStats() {
			Map<String, Object> stats = new HashMap<>();
			stats.put("activeInstances", INSTANCES.size());
			stats.put("totalPlayers", INSTANCES.values().stream()

				.mapToInt(i -> i.players.size())
				.sum());

			return stats;
		}
	}
}
