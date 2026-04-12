package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class SpeedyStrikes extends PlayerPerk {

	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public int getTickReductionChance() {
		return 15 + (this.perkLevel * 17);
	}

	public double getTickReductionAmount() {
		double toReturn = this.perkLevel * 0.25;
		if (this.perkLevel >= 5)
			toReturn += 0.5;
		return toReturn;
	}

	public double getTickReductionAmount(int level) {
		return ((level * 0.25) * 100);
	}


	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Speedy Strikes";
	}

	@Override
	public String getPerkDescription() {
		return "Attack faster with melee.";
	}

	@Override
	public String getPerkEffect() {
		return "Your melee attacks will be " + getTickReductionAmount() * 100 + "% ticks faster than usual.";
	}

	private int getTickReductionChance(int level) {
		return 15 + (level * 17);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to attack faster than usual with melee.<br><br>" +
			"At level 1 you will attack " + getTickReductionAmount(1) + "% faster with melee.<br><br>" +
			"At level 2 you will attack " + getTickReductionAmount(2) + "% faster with melee.<br><br>" +
			"At level 3 you will attack " + getTickReductionAmount(3) + "% faster with melee.<br><br>" +
			"At level 4 you will attack " + getTickReductionAmount(4) + "% faster with melee.<br><br>" +
			"At level 5 you will attack " + getTickReductionAmount(5) + "% faster with melee.<br><br>"

			;
	}
}
