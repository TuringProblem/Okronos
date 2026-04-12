package io.ruin.model.item.actions.impl.pet.perk.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.pet.perk.Perk;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;

public class BonusSmitePerk implements Perk {
	@Override
	public PerkType perkType() {
		return PerkType.BONUS_SMITE;
	}

	public double getBonusSmitePercentage(Player player) {
		return 0;
	}

}
