package io.ruin.model.item.actions.impl.pet.perk.impl;

import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.perk.Perk;
import io.ruin.model.item.actions.impl.pet.perk.PerkType;

public class InventoryAutoLootPerk implements Perk {
	@Override
	public PerkType perkType() {
		return PerkType.INVENTORY_AUTO_LOOT;
	}

	public boolean pickUp(Player player, NPC victim, Item drop) {
		return false;
	}

}
