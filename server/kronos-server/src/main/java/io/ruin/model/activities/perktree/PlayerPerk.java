package io.ruin.model.activities.perktree;

public abstract class PlayerPerk {
	protected transient boolean isActive = false;
	protected transient int perkLevel = 1;

	public void updatePerkLevel(int change) {
		this.perkLevel += change;
	}

	public void setPerkLevel(int newLevel) {
		this.perkLevel = newLevel;
	}

	public abstract int getPerkMaxLevel();

	public int getPerkLevel() {
		return this.perkLevel;
	}

	public abstract void activatePerk();

	public abstract String getPerkName();

	public abstract String getPerkDescription();

	public abstract String getPerkEffect();

	public abstract String getRepositoryDescription();
}
