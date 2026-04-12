package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class BloodSacrifice extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getHealAmount() {
		return (10 + (7.5 * perkLevel)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Blood Sacrifice";
	}

	@Override
	public String getPerkDescription() {
		return "Blood spells enhanced";
	}

	@Override
	public String getPerkEffect() {
		return "Blood spells will heal " + getHealAmount() * 100 + "% more.";
	}

	private double getHealAmount(int level) {
		return 10 + (7.5 * level);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active blood spells will heal more.<br><br>" +
			"At level 1 blood spells will heal " + getHealAmount(1) + "% more.<br><br>" +
			"At level 2 blood spells will heal " + getHealAmount(2) + "% more.<br><br>" +
			"At level 3 blood spells will heal " + getHealAmount(3) + "% more.<br><br>" +
			"At level 4 blood spells will heal " + getHealAmount(4) + "% more.<br><br>" +
			"At level 5 blood spells will heal " + getHealAmount(5) + "% more."
			;
	}
}
