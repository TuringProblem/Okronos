package io.ruin.model.activities.perktree.perksets;

import io.ruin.model.activities.perktree.PlayerPerkSet;

public class MeleeResistance extends PlayerPerkSet {
	@Override
	public String getPerkSetName() {
		return "Melee Resistance";
	}

	public double getMeleeDamageReduction() {
		double calc = (7.5 + (getLevel() * 3.5)) / 100;
		return 1 - calc;
	}

	@Override
	public String getPerkSetDescription() {
		return "Melee Damage Reduction";
	}

	private double getMeleeDamageReduction(int level) {
		return 7.5 + (level * 3.5);
	}

	@Override
	public String getPerkSetEffect() {
		return "When this perk set is active you will take less damage from melee attacks.<br><br>" +
			"At level 1 you will take " + getMeleeDamageReduction(1) + "% less damage from melee attacks.<br><br>" +
			"At level 2 you will take " + getMeleeDamageReduction(2) + "% less damage from melee attacks.<br><br>" +
			"At level 3 you will take " + getMeleeDamageReduction(3) + "% less damage from melee attacks.<br><br>" +
			"At level 4 you will take " + getMeleeDamageReduction(4) + "% less damage from melee attacks.<br><br>" +
			"At level 5 you will take " + getMeleeDamageReduction(5) + "% less damage from melee attacks.<br><br>"
			;
	}
}
