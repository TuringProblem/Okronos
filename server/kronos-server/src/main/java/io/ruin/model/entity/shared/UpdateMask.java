package io.ruin.model.entity.shared;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.npc.NPC;

public abstract class UpdateMask {

	private transient boolean sent;

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public boolean isSent() {
		return sent;
	}

	public abstract void reset();

	public abstract boolean hasUpdate(boolean justAdded);

	public void send(boolean playerUpdate) {
	}

	public void send(Player player) {
	}

	public void send(NPC npc) {
	}

	public abstract int get(boolean playerUpdate);

}
