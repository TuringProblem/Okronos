package io.ruin.model.entity.shared.masks;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class TransformUpdate extends UpdateMask {

	private int id = -1;

	public void set(int id) {
		this.id = id;
	}

	@Override
	public void reset() {
		id = -1;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return id != -1;
	}

	@Override
	public void send(boolean playerUpdate) {
	}

	@Override
	public void send(NPC npc) {
		npc.avatar().setId(this.id);
		npc.avatarExtended().setTransmogrification(this.id);
	}

	@Override
	public void send(Player player) {
		player.avatarExtended().setTransmogrification(this.id);
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 16 : 32;
	}

}
