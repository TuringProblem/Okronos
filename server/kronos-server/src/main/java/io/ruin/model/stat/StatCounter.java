package io.ruin.model.stat;

import io.ruin.model.entity.player.Player;

public class StatCounter {
	public int type, startXp, endXp;
	public transient int index;

	public StatCounter(int index) {
		this.index = index;
	}

	public void send(Player player) {
	}

}
