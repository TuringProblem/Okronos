package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;
import lombok.Getter;

public class MovementModeUpdate extends UpdateMask {

	private boolean update;

	@Getter
	private int mode;

	public void set(int mode) {
		if (this.mode == mode)
			return;
		this.mode = mode;
		this.update = true;
	}

	@Override
	public void reset() {
		update = false;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return update || added;
	}

	@Override
	public void send(Player player) {
		player.avatarExtended().setMoveSpeed(this.mode);
	}

	@Override
	public int get(boolean playerUpdate) {
		return 16384;
	}

}
