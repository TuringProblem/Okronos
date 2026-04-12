package io.ruin.network.central;

import io.ruin.Server;
import io.ruin.api.utils.IPBans;
import io.ruin.api.utils.JsonUtils;
import io.ruin.api.utils.MACBan;
import io.ruin.api.utils.UUIDBan;
import io.ruin.central.utility.CentralSaves;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.npc.actions.edgeville.Probita;
import io.ruin.model.var.VarPlayerRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Slf4j
public class CentralSender {

	private static final Logger logger = Logger.getLogger(CentralSender.class.getName());
	private static final CentralSender instance = new CentralSender();
	private static volatile boolean running = true;

	private final SaveSystem saveSystem;
	private final MessageSystem messageSystem;
	private final BanSystem banSystem;
	private final SystemMonitor systemMonitor;

	protected CentralSender() {
		this.saveSystem = new SaveSystem();
		this.messageSystem = new MessageSystem();
		this.banSystem = new BanSystem();
		this.systemMonitor = new SystemMonitor();

		initializeSystems();
		startMonitoring();
	}

	private void initializeSystems() {
		try {
			saveSystem.initialize();
			messageSystem.initialize();
			banSystem.initialize();
		} catch (Exception e) {
			logger.severe("Failed to initialize systems: " + e.getMessage());
		}
	}

	private void startMonitoring() {
		new Thread(() -> {
			while (running) {
				try {
					systemMonitor.checkSystems();
					TimeUnit.MINUTES.sleep(1);
				} catch (Exception e) {
					logger.severe("Error in system monitoring: " + e.getMessage());
				}
			}

		}, "central-sender-monitor");
	}

	// System management methods
	public void restartSystem(String systemType) {
		try {
			switch (systemType.toLowerCase()) {
				case "save":
					saveSystem.reinitialize();
					break;
				case "message":
					messageSystem.reinitialize();
					break;
				case "ban":
					banSystem.reinitialize();
					break;
				case "all":
					saveSystem.reinitialize();
					messageSystem.reinitialize();
					banSystem.reinitialize();
					break;
				default:
					throw new IllegalArgumentException("Unknown system type: " + systemType);
			}
			logger.info("Successfully restarted " + systemType + " system");
		} catch (Exception e) {
			logger.severe("Failed to restart " + systemType + " system: " + e.getMessage());
			throw e;
		}
	}

	public boolean isSystemHealthy(String systemType) {
		try {
			switch (systemType.toLowerCase()) {
				case "save":
					return saveSystem.isHealthy();
				case "message":
					return messageSystem.isHealthy();
				case "ban":
					return banSystem.isHealthy();
				default:
					throw new IllegalArgumentException("Unknown system type: " + systemType);
			}
		} catch (Exception e) {
			logger.severe("Failed to check health of " + systemType + " system: " + e.getMessage());
			return false;
		}
	}

	public static void sendGlobalMessage(int userId, String message) {
		try {
			instance.messageSystem.sendGlobalMessage(userId, message);
		} catch (Exception e) {
			logger.severe("Error in message system: " + e.getMessage());
			instance.systemMonitor.reportFailure(SystemType.MESSAGE);
		}
	}


	public static void reloadBans() {
		try {
			instance.banSystem.reloadBans();
		} catch (Exception e) {
			logger.severe("Error in ban system: " + e.getMessage());
			instance.systemMonitor.reportFailure(SystemType.BAN);
		}
	}

	public static void requestUUIDBan(String uuid) {
		try {
			UUIDBan.requestBan(uuid);
		} catch (Exception e) {
			logger.severe("Error in UUID ban system: " + e.getMessage());
			instance.systemMonitor.reportFailure(SystemType.BAN);
		}
	}

	public static void requestIPBan(Player player, String ip) {
		try {
			IPBans.requestBan(player.getName(), ip);
		} catch (Exception e) {
			logger.severe("Error in IP ban system: " + e.getMessage());
			instance.systemMonitor.reportFailure(SystemType.BAN);
		}
	}

	public static void requestMACBan(Player player, String mac) {
		try {
			MACBan.requestBan(player.getName(), mac);
		} catch (Exception e) {
			logger.severe("Error in MAC ban system: " + e.getMessage());
			instance.systemMonitor.reportFailure(SystemType.BAN);
		}
	}

	public static boolean disabled = false;

	// System implementations remain unchanged
	private static class SaveSystem {
		private final AtomicBoolean operational = new AtomicBoolean(true);
		private final AtomicInteger failureCount = new AtomicInteger(0);

		void initialize() {
			operational.set(true);
			failureCount.set(0);
		}

		void reinitialize() {
			initialize();
		}

		boolean isHealthy() {
			return operational.get() && failureCount.get() < 3;
		}

		void handleSaveFailure(int userId, int attempt, String json, Exception error) {
			logger.severe("Save failure for user " + userId + ": " + error.getMessage());
			failureCount.incrementAndGet();
			if (failureCount.get() >= 3) {
				operational.set(false);
			}
		}
	}

	private static class MessageSystem {
		private final AtomicBoolean operational = new AtomicBoolean(true);

		void initialize() {
			operational.set(true);
		}

		void reinitialize() {
			initialize();
		}

		boolean isHealthy() {
			return operational.get();
		}

		void sendGlobalMessage(int userId, String message) {
			if (!operational.get())
				return;

			if (userId == -1) {
				for (Player p : World.players()) {
					p.sendMessage(message);
				}
			} else {
				Player player = World.getPlayer(userId, true);
				if (player == null)
					return;

				player.sendMessage(message);
				for (Player p : World.players()) {
					p.sendMessage(message);
				}
			}
		}
	}

	private static class BanSystem {
		private final AtomicBoolean operational = new AtomicBoolean(true);

		void initialize() {
			operational.set(true);
		}

		void reinitialize() {
			initialize();
		}

		boolean isHealthy() {
			return operational.get();
		}

		void reloadBans() {
			if (!operational.get())
				return;

			IPBans.refreshBans();
			MACBan.refreshBans();
			UUIDBan.refreshBans();
		}
	}

	private enum SystemType {
		SAVE, MESSAGE, BAN;

		public static final SystemType[] VALUES = values();
	}

	private static class SystemMonitor {
		private static final int FAILURE_THRESHOLD = 3;
		private final AtomicInteger[] failureCounts = new AtomicInteger[SystemType.VALUES.length];

		SystemMonitor() {
			for (int i = 0; i < failureCounts.length; i++) {
				failureCounts[i] = new AtomicInteger(0);
			}
		}

		void reportFailure(SystemType system) {
			int failures = failureCounts[system.ordinal()].incrementAndGet();
			if (failures >= FAILURE_THRESHOLD) {
				logger.severe("System " + system + " has failed " + FAILURE_THRESHOLD
						+ " times, initiating recovery");
				try {
					instance.restartSystem(system.name().toLowerCase());
					failureCounts[system.ordinal()].set(0);
				} catch (Exception e) {
					logger.severe("Failed to recover system " + system + ": " + e.getMessage());
				}
			}
		}

		void checkSystems() {
			for (SystemType type : SystemType.VALUES) {
				if (!instance.isSystemHealthy(type.name().toLowerCase())) {
					reportFailure(type);
				}
			}
		}
	}

	public static void shutdown() {
		running = false;
	}
}
