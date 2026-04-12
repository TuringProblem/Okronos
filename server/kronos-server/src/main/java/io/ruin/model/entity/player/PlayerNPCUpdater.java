package io.ruin.model.entity.player;

public final class PlayerNPCUpdater {

	private final Player player;

	private int maxDistance = 14;

	public PlayerNPCUpdater(final Player player) {
		this.player = player;
	}

	/**
	 * Process
	 */

	public void process() {
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(final int maxDistance) {
		this.maxDistance = maxDistance;
	}

	public boolean isLargeView() {
		return maxDistance > 14;
	}

}
