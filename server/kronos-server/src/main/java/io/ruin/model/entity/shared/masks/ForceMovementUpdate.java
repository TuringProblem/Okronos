package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class ForceMovementUpdate extends UpdateMask {

	private int diffX1, diffY1;
	private int diffX2, diffY2;
	private int speed1;
	private int speed2;
	private int direction;
	public boolean updated = false;
	private boolean legacy = false;

	public void set(int diffX1, int diffY1, int diffX2, int diffY2, int speed1, int speed2, int direction) {
		set(diffX1, diffY1, diffX2, diffY2, speed1, speed2, direction, false);
	}

	public void set(int diffX1, int diffY1, int diffX2, int diffY2, int speed1, int speed2, int direction,
	                boolean legacy) {
		this.diffX1 = diffX1;
		this.diffY1 = diffY1;
		this.diffX2 = diffX2;
		this.diffY2 = diffY2;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		this.updated = true;
		this.legacy = legacy;
	}

	@Override
	public void reset() {
		updated = false;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return updated;
	}

	@Override
	public void send(NPC npc) {
		npc.avatarExtended().setExactMove(this.diffX1, this.diffY1, this.speed1, this.diffX2, this.diffY2,
			this.speed2, this.direction);
	}

	@Override
	public void send(Player player) {
		player.avatarExtended().setExactMove(this.diffX1, this.diffY1, this.speed1, this.diffX2, this.diffY2,
			this.speed2, this.direction);
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 512 : (legacy ? 256 : 64);
	}

}
