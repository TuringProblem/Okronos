package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class GraphicsUpdate extends UpdateMask {

	private int id = -2;

	private int height, delay;

	public void set(int id, int height, int delay) {
		this.id = id;
		this.height = height;
		this.delay = delay;
	}

	@Override
	public void reset() {
		id = -2;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return id != -2;
	}

	@Override
	public void send(NPC npc) {
		npc.avatarExtended().setSpotAnim(0, this.id, this.delay, this.height);
	}

	@Override
	public void send(Player player) {
		player.avatarExtended().setSpotAnim(0, this.id, Math.max(0, this.delay), this.height);
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 4096 : 16;
	}

}
