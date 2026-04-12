package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class ModelTintUpdate extends UpdateMask {

	int startTick;
	int endTick;
	int hue;
	int saturation;
	int luminosity;
	int amount;

	public boolean updated = false;

	public void set(int start, int end, int hue, int saturation, int luminance, int amount) {
		this.startTick = start;
		this.endTick = end;
		this.hue = hue;
		this.saturation = saturation;
		this.luminosity = luminance;
		this.amount = amount;
		updated = true;
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
		npc.avatarExtended().setTinting(this.startTick, this.endTick, this.hue, this.saturation, this.luminosity, this.amount);
	}

	@Override
	public void send(Player player) {
		player.avatarExtended().setTinting(this.startTick, this.endTick, this.hue, this.saturation, this.luminosity, this.amount);
	}

	@Override
	public void send(boolean playerUpdate) {
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 8192 : 512;
	}

}
