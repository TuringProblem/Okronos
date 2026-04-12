package io.ruin.model.content.drop_rate;

import io.ruin.model.entity.player.Player;

public class DropRateBonusManager {

	private static DropRateBonusManager instance = null;

	public static DropRateBonusManager getInstance() {
		if (instance == null)
			instance = new DropRateBonusManager();
		return instance;
	}

	private DropRateBonusManager() {
	}

	public static final float MAX_BONUS_DROP_RATE = 95;

	public float getTotalBonusDropRate(Player player) {

		float totalDropRateBonus = 0;

		for (DropRateBonusData d :
			DropRateBonusData.VALUES) {
			totalDropRateBonus += d.getDropRateBonus().accept(player);
		}

		if (totalDropRateBonus > MAX_BONUS_DROP_RATE)
			totalDropRateBonus = MAX_BONUS_DROP_RATE;

		return totalDropRateBonus;

	}

}
