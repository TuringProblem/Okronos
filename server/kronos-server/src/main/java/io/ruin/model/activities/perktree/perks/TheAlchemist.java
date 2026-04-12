package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

public class TheAlchemist extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double goldDroppedMultiplier() {
		return 1.5 * perkLevel;
	}

	public double alchemyMultiplier() {
		return (1.75 + (perkLevel * 0.25));
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "The Alchemist";
	}

	@Override
	public String getPerkDescription() {
		return "Get more gold.";
	}

	@Override
	public String getPerkEffect() {
		return "You will receive " + goldDroppedMultiplier() + "% more gold when it is dropped.<br>" +
			"You will also receive " + (alchemyMultiplier() * 100) + "% more gold when alching.";
	}

	private double goldDroppedMultiplier(int level) {
		return 1.5 * level;
	}

	private double alchemyMultiplier(int level) {
		return 1.75 + (level * 0.25);
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will receive more gold from alching and monster drops.<br><br>" +
			"At level 1 you will receive " + goldDroppedMultiplier(1) + "x the coins dropped by NPCs, you will also receive " + alchemyMultiplier(1) + "x the coins when alching.<br><br>" +
			"At level 2 you will receive " + goldDroppedMultiplier(2) + "x the coins dropped by NPCs, you will also receive " + alchemyMultiplier(2) + "x the coins when alching.<br><br>" +
			"At level 3 you will receive " + goldDroppedMultiplier(3) + "x the coins dropped by NPCs, you will also receive " + alchemyMultiplier(3) + "x the coins when alching.<br><br>" +
			"At level 4 you will receive " + goldDroppedMultiplier(4) + "x the coins dropped by NPCs, you will also receive " + alchemyMultiplier(4) + "x the coins when alching.<br><br>" +
			"At level 5 you will receive " + goldDroppedMultiplier(5) + "x the coins dropped by NPCs, you will also receive " + alchemyMultiplier(5) + "x the coins when alching."
			;
	}
}
