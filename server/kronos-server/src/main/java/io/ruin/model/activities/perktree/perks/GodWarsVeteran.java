package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class GodWarsVeteran extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getAccuracyBonus() {
		return (2.5 + (perkLevel * 5)) / 100;
	}

	public double getDamageBonus() {
		return (2.5 + (perkLevel * 2.5)) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "God Wars Veteran";
	}

	@Override
	public String getPerkDescription() {
		return "God Wars Dungeon boost.";
	}

	@Override
	public String getPerkEffect() {
		return "You will be able to enter any god wars room without kill count.<br>" +
			"You will also receive a " + getDamageBonus() * 100 + "% damage boost and a "
			+ ((int) (getAccuracyBonus() * 100)) + "% accuracy boost against the bosses.";
	}

	private double getAccuracyBonus(int level) {
		return 2.5 + (level * 5);
	}

	private double getDamageBonus(int level) {
		return 2.5 + (level * 2.5);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will be able to enter any god wars room without having to get kill count, you will also receive and accuracy and damage buff against the bosses.<br><br>" +
			"At level 1 you will be able to enter any god wars room without getting kill count and you will receive an accuracy boost of "
			+ getAccuracyBonus(1) + "% and a damage boost of " + getDamageBonus(1) + "% against the god wars bosses.<br><br>" +
			"At level 2 you will be able to enter any god wars room without getting kill count and you will receive an accuracy boost of "
			+ getAccuracyBonus(2) + "% and a damage boost of " + getDamageBonus(2) + "% against the god wars bosses.<br><br>" +
			"At level 3 you will be able to enter any god wars room without getting kill count and you will receive an accuracy boost of "
			+ getAccuracyBonus(3) + "% and a damage boost of " + getDamageBonus(3) + "% against the god wars bosses.<br><br>" +
			"At level 4 you will be able to enter any god wars room without getting kill count and you will receive an accuracy boost of "
			+ getAccuracyBonus(4) + "% and a damage boost of " + getDamageBonus(4) + "% against the god wars bosses.<br><br>" +
			"At level 5 you will be able to enter any god wars room without getting kill count and you will receive an accuracy boost of "
			+ getAccuracyBonus(5) + "% and a damage boost of " + getDamageBonus(5) + "% against the god wars bosses."
			;
	}
}
