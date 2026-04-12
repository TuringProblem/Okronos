package io.ruin.model.activities.perktree.perksets;

import io.ruin.model.activities.perktree.PlayerPerkSet;

public class RangeResistance extends PlayerPerkSet {

	public double getRangeDamageReduction() {
		double calc = (7.5 + (getLevel() * 3.5)) / 100;
		return 1 - calc;
	}

	@Override
	public String getPerkSetName() {
		return "Range Resistance";
	}

	@Override
	public String getPerkSetDescription() {
		return "Range Damage Reduction";
	}

	private double getRangeDamageReduction(int level) {
		return 7.5 + (level * 3.5);
	}

	@Override
	public String getPerkSetEffect() {
		return "When this perk set is active you will take less damage from ranged attacks.<br><br>" +
			"At level 1 you will take " + getRangeDamageReduction(1) + "% less damage from ranged attacks.<br><br>" +
			"At level 2 you will take " + getRangeDamageReduction(2) + "% less damage from ranged attacks.<br><br>" +
			"At level 3 you will take " + getRangeDamageReduction(3) + "% less damage from ranged attacks.<br><br>" +
			"At level 4 you will take " + getRangeDamageReduction(4) + "% less damage from ranged attacks.<br><br>" +
			"At level 5 you will take " + getRangeDamageReduction(5) + "% less damage from ranged attacks.<br><br>"
			;
	}
}
