package io.ruin.model.item.actions.impl.pet.perk.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.pet.perk.Perk;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;

public class BonusPointsPerk implements Perk {

	@Override
	public PerkType perkType() {
		return PerkType.BONUS_POINTS;
	}

	public int getBonusPoints(Player player, BonusPoints bonusPoints, Object... object) {
		return 0;
	}

	public static enum BonusPoints {
		SLAYER
	}

}
