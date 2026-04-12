package io.ruin.model.item.actions.impl.pet.perk.impl;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.pet.perk.Perk;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;

public class KeepBonusDeathItemsPerk implements Perk {
	@Override
	public PerkType perkType() {
		return PerkType.KEEP_BONUS_ITEMS_ON_DEATH;
	}

	public int getBonusAmountOfItemsKept(Player player) {
		return 0;
	}

}
