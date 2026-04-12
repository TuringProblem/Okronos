package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class SkilledRanger extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public float getTickReduction() {
		float reductionAmount = 1;
		float x = 0.1f + (perkLevel * 0.175f);
		reductionAmount -= x;
		return reductionAmount;
	}

	public int getAttackRange() {
		if (perkLevel > 4)
			return 3;
		else if (perkLevel > 2) return 3;
		else return 2;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Skilled Ranger";
	}

	@Override
	public String getPerkDescription() {
		return "Attack faster with ranged.";
	}

	@Override
	public String getPerkEffect() {
		return "You will attack faster based on the perk level and you'll also be able to attack further away based on your perk level.";
	}

	private double getTickReduction(int level) {
		float reduction = 0;
		reduction += 0.1f + (level * 0.175f);
		return reduction;
	}

	private int getAttackRange(int level) {
		if (level > 4)
			return 3;
		else if (level > 2) return 3;
		else return 2;
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will attack faster with ranged and you will also be able to attack your targets from further away.<br><br>" +
			"At level 1 you will be able to attack from " + getAttackRange(1) + " tiles further away than normal and you will attack " + getTickReduction(1) * 100 + "% faster.<br><br>" +
			"At level 2 you will be able to attack from " + getAttackRange(2) + " tiles further away than normal and you will attack " + getTickReduction(2) * 100 + "% faster.<br><br>" +
			"At level 3 you will be able to attack from " + getAttackRange(3) + " tiles further away than normal and you will attack " + getTickReduction(3) * 100 + "% faster.<br><br>" +
			"At level 4 you will be able to attack from " + getAttackRange(4) + " tiles further away than normal and you will attack " + getTickReduction(4) * 100 + "% faster.<br><br>" +
			"At level 5 you will be able to attack from " + getAttackRange(5) + " tiles further away than normal and you will attack " + getTickReduction(5) * 100 + "% faster."
			;
	}
}
