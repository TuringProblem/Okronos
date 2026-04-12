package io.ruin.model.item.actions.impl.pet.perk.impl;

import io.ruin.model.item.actions.impl.pet.perk.Perk;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;

public class DragonFireImmunityPerk implements Perk {
	@Override
	public PerkType perkType() {
		return PerkType.DRAGON_FIRE_IMMUNITY;

	}
}
