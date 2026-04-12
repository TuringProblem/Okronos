package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class PlayerOpsUpdate extends UpdateMask {

	private String[] ops;

	public void set(String[] ops) {
		if (ops.length != 4) {
			throw new IndexOutOfBoundsException("Update mask only supports 4 ops!");
		}
		this.ops = ops;
	}

	@Override
	public void reset() {
		ops = null;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return ops != null;
	}

	@Override
	public void send(Player player) {
	}

	@Override
	public int get(boolean playerUpdate) {
		return 2048;
	}

}
