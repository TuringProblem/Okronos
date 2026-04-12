package io.ruin.model.item.actions.impl.pet.perk.impl;

import io.ruin.model.item.actions.impl.pet.perk.Perk;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;


public class KaruulmImmunityPerk implements Perk {

	@Override
	public PerkType perkType() {
		return PerkType.KARUULM_IMMUNITY;
	}
}
