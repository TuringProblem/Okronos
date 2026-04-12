package io.ruin.model.entity.shared.masks;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class ForceTextUpdate extends UpdateMask {

	private String forceText;

	public void set(String forceText) {
		this.forceText = forceText;
		if (this.forceText.length() > 80) {
			this.forceText = this.forceText.substring(0, 80);
		}
	}

	@Override
	public void reset() {
		forceText = null;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return forceText != null;
	}

	@Override
	public void send(NPC npc) {
		npc.avatarExtended().setSay(this.forceText);
	}

	@Override
	public void send(Player player) {
		player.avatarExtended().setSay(this.forceText);
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 2 : 2;
	}

}
