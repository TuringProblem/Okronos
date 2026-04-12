package io.ruin.model.entity.player;

import io.ruin.api.buffer.OutBuffer;

public class PlayerUpdater {

	private Player player;
	public boolean updateRegion = false;
	private boolean justAdded = true;

	public PlayerUpdater(Player player) {
		this.player = player;
	}

	public void init(OutBuffer out) {
	}

	public void process() {
		this.processMasks();
		if (updateRegion || player.updateRegion()) {
			player.getPacketSender().sendRegion(false);
			updateRegion = false;
		}
	}

	private void processMasks() {
		for (var mask : player.getMasks()) {
			if (!mask.hasUpdate(this.justAdded)) {
				continue;
			}
			mask.send(this.player);
			mask.setSent(true);
		}
		this.justAdded = false;
	}
}
