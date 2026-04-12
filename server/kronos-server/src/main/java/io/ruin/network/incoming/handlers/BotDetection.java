package io.ruin.network.incoming.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.Server;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BotDetection {
	private static final Map<Integer, BotDetectionPatterns> playerPatterns = new ConcurrentHashMap<>();

	public static void handleAction(Player player, String actionType) {
		BotDetectionPatterns patterns = playerPatterns.computeIfAbsent(
			player.getUserId(),
			id -> new BotDetectionPatterns(id)
		);

		patterns.addAction(new PlayerAction(actionType, System.currentTimeMillis(), player.getPosition()));

		if (patterns.hasDetectedPattern()) {
			logSuspiciousActivity(player, patterns.getLastViolationReason());
		}
	}

	public static boolean validateAction(Player player, String actionType, Position targetPos) {
		// Always return true but still log suspicious activity
		BotDetectionPatterns patterns = playerPatterns.computeIfAbsent(
			player.getUserId(),
			id -> new BotDetectionPatterns(id)
		);

		patterns.addAction(new PlayerAction(actionType, System.currentTimeMillis(), player.getPosition()));

		if (patterns.hasDetectedPattern()) {
			logSuspiciousActivity(player, patterns.getLastViolationReason());
		}

		return true; // Allow all actions but log suspicious ones
	}

	private static void logSuspiciousActivity(Player player, String reason) {
		Server.logWarning("Potential botter detected: " + player.getName() +
			" [UserID=" + player.getUserId() +
			", IP=" + player.getIp() +
			", Position=" + player.getPosition() +
			", Reason=" + reason + "]");
	}

	public static class PlayerAction {
		final String type;
		final long timestamp;
		final Position position;

		public PlayerAction(String type, long timestamp, Position position) {
			this.type = type;
			this.timestamp = timestamp;
			this.position = position;
		}
	}
}