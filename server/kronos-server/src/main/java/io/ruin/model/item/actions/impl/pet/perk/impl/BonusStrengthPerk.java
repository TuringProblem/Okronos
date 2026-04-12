package io.ruin.model.item.actions.impl.pet.perk.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.pet.perk.Perk;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;
import io.ruin.model.stat.StatType;

public class BonusStrengthPerk implements Perk {
	@Override
	public PerkType perkType() {
		return PerkType.BONUS_STRENGTH;
	}

	public int getBonusStrength(Player player, StatType statType) {
		return 0;
	}

}
