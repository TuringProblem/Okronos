package io.ruin.model.activities.perktree.perksets;

import io.ruin.model.activities.perktree.PlayerPerkSet;

public class GoldDigger extends PlayerPerkSet {

	public double getDropRateBoost() {
		return 4 + (2.5 * (1.5 * getLevel()));
	}

	@Override
	public String getPerkSetName() {
		return "Gold Digger";
	}

	@Override
	public String getPerkSetDescription() {
		return "Boosted Drop Rates";
	}

	private double getDropRateBoost(int level) {
		return 4 + (2.5 * (1.5 * level));
	}

	@Override
	public String getPerkSetEffect() {
		return "When this perk set is active you will have better drop rates for rare items.<br><br>" +
			"At level 1 your drop rates will be boosted by " + getDropRateBoost(1) + "%.<br><br>" +
			"At level 2 your drop rates will be boosted by " + getDropRateBoost(2) + "%.<br><br>" +
			"At level 3 your drop rates will be boosted by " + getDropRateBoost(3) + "%.<br><br>" +
			"At level 4 your drop rates will be boosted by " + getDropRateBoost(4) + "%.<br><br>" +
			"At level 5 your drop rates will be boosted by " + getDropRateBoost(5) + "%.<br><br>"
			;
	}
}
