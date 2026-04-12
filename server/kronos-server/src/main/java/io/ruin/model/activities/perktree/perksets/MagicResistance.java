package io.ruin.model.activities.perktree.perksets;

import io.ruin.model.activities.perktree.PlayerPerkSet;

public class MagicResistance extends PlayerPerkSet {
	@Override
	public String getPerkSetName() {
		return "Magic Resistance";
	}

	@Override
	public String getPerkSetDescription() {
		return "Magic Damage Reduction";
	}

	@Override
	public String getPerkSetEffect() {
		return "When this perk set is active you will take less damage from magic attacks.<br><br>" +
			"At level 1 you will take " + getMagicDamageReduction(1) + "% less damage from magic attacks.<br><br>" +
			"At level 2 you will take " + getMagicDamageReduction(2) + "% less damage from magic attacks.<br><br>" +
			"At level 3 you will take " + getMagicDamageReduction(3) + "% less damage from magic attacks.<br><br>" +
			"At level 4 you will take " + getMagicDamageReduction(4) + "% less damage from magic attacks.<br><br>" +
			"At level 5 you will take " + getMagicDamageReduction(5) + "% less damage from magic attacks.<br><br>"
			;
	}

	private double getMagicDamageReduction(int level) {
		return 7.5 + (level * 3.5);
	}

	public double getMagicDamageReduction() {
		double calc = (7.5 + (getLevel() * 3.5)) / 100;
		return 1 - calc;
	}
}
