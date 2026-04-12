package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class AnimationUpdate extends UpdateMask {

	public int id = -2, delay;

	public void set(int id, int delay) {
		if (this.id == -1) // trying out something here - animation cancels should always have priority
			return;
		this.id = id;
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
	public void send(boolean playerUpdate) {
	}

	@Override
	public void send(Player player) {
		var avatar = player.avatarExtended();
		avatar.setSequence(this.id, this.delay);
	}

	@Override
	public void send(NPC npc) {
		npc.avatar.getExtendedInfo().setSequence(this.id, this.delay);
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 8 : 8;
	}

}
