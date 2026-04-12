package io.ruin.model.entity.shared.masks;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.shared.UpdateMask;

public class NpcCombatUpdate extends UpdateMask {

	int startTick;
	int endTick;
	int hue;
	int saturation;
	int luminosity;
	byte transparency;

	public boolean updated = false;

	public void set(int hue, int saturation, int luminosity, byte transparency, int startTick, int endTick) {
		this.hue = hue;
		this.saturation = saturation;
		this.luminosity = luminosity;
		this.transparency = transparency;
		this.startTick = startTick;
		this.endTick = endTick;
		updated = true;
	}

	@Override
	public void reset() {
		updated = false;
	}

	@Override
	public void send(NPC npc) {
		npc.avatarExtended().setTinting(
			this.startTick,
			this.endTick,
			this.hue,
			this.saturation,
			this.luminosity,
			this.transparency
		);
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return updated;
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 8192 : 512;
	}

}
