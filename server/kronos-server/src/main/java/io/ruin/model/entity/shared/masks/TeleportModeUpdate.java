package io.ruin.model.entity.shared.masks;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class TeleportModeUpdate extends UpdateMask {

	private boolean update;

	private int mode;

	public void set(int mode) {
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
		player.avatarExtended().setTempMoveSpeed(this.mode);
	}

	@Override
	public int get(boolean playerUpdate) {
		return 256;
	}

}
