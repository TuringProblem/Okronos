package io.ruin.model.activities.perktree;

import io.ruin.model.entity.player.Player;

public abstract class PlayerPerkSet {

	private int level = 1;

	public abstract String getPerkSetName();

	public void setLevel(int level) {
		this.level = level;
	}

	public abstract String getPerkSetDescription();

	public abstract String getPerkSetEffect();

	public int getLevel() {
		return this.level;
	}
}
