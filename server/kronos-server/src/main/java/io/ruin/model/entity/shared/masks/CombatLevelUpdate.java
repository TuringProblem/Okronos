package io.ruin.model.entity.shared.masks;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;

public class CombatLevelUpdate extends UpdateMask {

	private int combatLevel = -1;

	public void set(int level) {
		this.combatLevel = level;
		setSent(false);
	}

	@Override
	public void reset() {
		combatLevel = -1;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return combatLevel != -1;
	}

	@Override
	public void send(Player player) {
		player.avatarExtended().setCombatLevel(this.combatLevel);
	}

	@Override
	public int get(boolean playerUpdate) {
		return 1024;
	}

}
