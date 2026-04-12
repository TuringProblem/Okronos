package io.ruin.model.activities.perktree.perks;

import io.ruin.model.activities.perktree.PlayerPerk;

import java.text.DecimalFormat;

public class VanquishedFoe extends PlayerPerk {
	@Override
	public int getPerkMaxLevel() {
		return 5;
	}

	public double getHealAmount() {
		return (2.5 + (3.5 * (perkLevel * 1.5))) / 100;
	}

	@Override
	public void activatePerk() {
		this.isActive = true;
	}

	@Override
	public String getPerkName() {
		return "Vanquished Foe";
	}

	@Override
	public String getPerkDescription() {
		return "Heal off dead NPCs.";
	}

	@Override
	public String getPerkEffect() {
		return "You will heal " + getHealAmount() * 100 + "% of the NPC's maximum health you just killed.";
	}

	private double getHealAmount(int level) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(2.5 + (3.5 * (level * 1.5))));
	}

	@Override
	public String getRepositoryDescription() {
		return "When this perk is active you will heal a percentage of the NPC you killed health.<br><br>" +
			"At level 1 " + getHealAmount(1) + "% of the NPC you just killed health will be added to your health.<br><br>" +
			"At level 2 " + getHealAmount(2) + "% of the NPC you just killed health will be added to your health.<br><br>" +
			"At level 3 " + getHealAmount(3) + "% of the NPC you just killed health will be added to your health.<br><br>" +
			"At level 4 " + getHealAmount(4) + "% of the NPC you just killed health will be added to your health.<br><br>" +
			"At level 5 " + getHealAmount(5) + "% of the NPC you just killed health will be added to your health."
			;
	}
}
