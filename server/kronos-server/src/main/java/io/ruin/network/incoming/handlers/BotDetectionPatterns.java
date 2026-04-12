package io.ruin.network.incoming.handlers;

import java.util.*;

public class BotDetectionPatterns {

	private final int userId;
	private final Queue<BotDetection.PlayerAction> recentActions = new LinkedList<>();
	private final Map<String, Integer> actionCounts = new HashMap<>();

	private long lastActionTime = 0;
	private int actionsThisTick = 0;
	private String lastViolationReason = "";
	private boolean patternDetected = false;

	public BotDetectionPatterns(int userId) {
		this.userId = userId;
	}

	public void addAction(BotDetection.PlayerAction action) {
		long now = System.currentTimeMillis();

		// Store recent actions
		recentActions.offer(action);
		if (recentActions.size() > 100) {
			recentActions.poll();
		}

		// Simple pattern detection
		if (detectSimplePattern()) {
			lastViolationReason = "Repetitive actions detected";
			patternDetected = true;
		}

		lastActionTime = now;
		actionsThisTick++;
	}

	private boolean detectSimplePattern() {
		if (recentActions.size() < 20) return false;

		List<BotDetection.PlayerAction> recent = new ArrayList<>(recentActions);
		int sameActionCount = 1;
		String lastType = recent.get(0).type;

		for (int i = 1; i < recent.size(); i++) {
			if (recent.get(i).type.equals(lastType)) {
				sameActionCount++;
				if (sameActionCount >= 15) { // 15 same actions in a row
					return true;
				}
			} else {
				sameActionCount = 1;
				lastType = recent.get(i).type;
			}
		}

		return false;
	}

	public boolean hasDetectedPattern() {
		return patternDetected;
	}

	public String getLastViolationReason() {
		return lastViolationReason;
	}
}