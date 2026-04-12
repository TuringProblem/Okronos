package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

import java.text.DecimalFormat;

public class SecuringTheBag extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	public double getAnotherRollChance() {
		return (2.5 + (5 * (perkLevel * 1.75))) / 100;
	}

	@Override
	public String getPerkName() {
		return "Securing The Bag";
	}

	@Override
	public String getPerkDescription() {
		return "Additional loot.";
	}

	@Override
	public String getPerkEffect() {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return "You have a " + Double.parseDouble(decimalFormat.format(getAnotherRollChance() * 100)) + "% chance to roll another drop when killing a NPC.";
	}

	private double getAnotherRollChance(int level) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(2.5 + (5 * (level * 1.75))));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will have a chance to roll another drop when killing any NPC.<br><br>" +
			"At level 1 you will have a " + getAnotherRollChance(1) + "% chance to roll another drop.<br><br>" +
			"At level 2 you will have a " + getAnotherRollChance(2) + "% chance to roll another drop.<br><br>" +
			"At level 3 you will have a " + getAnotherRollChance(3) + "% chance to roll another drop.<br><br>" +
			"At level 4 you will have a " + getAnotherRollChance(4) + "% chance to roll another drop.<br><br>" +
			"At level 5 you will have a " + getAnotherRollChance(5) + "% chance to roll another drop."
			;
	}
}
